package com.example.autocalm

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import java.util.regex.Pattern
import android.os.Build
import android.os.Vibrator
import android.os.VibrationEffect
import android.os.VibrationAttributes
import android.os.VibratorManager
import android.os.PowerManager
import android.os.CombinedVibration
import android.content.Context

class StressNotificationListener : NotificationListenerService() {

    private val TAG = "StressViber"
    
    // Primary Filter: Regex match on channelId
    // Targets: (?i).*(body|response|stress|eda).*
    private val channelPattern = Pattern.compile("(?i).*(body|response|stress|eda).*")
    
    // Secondary Filter: com.fitbit.FitbitMobile or similar
    private val packagePattern = Pattern.compile("(?i)com\\.fitbit\\..*")

    override fun onCreate() {
        super.onCreate()
        android.util.Log.i(TAG, "StressNotificationListener CREATED")
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        android.util.Log.i(TAG, "StressNotificationListener CONNECTED to system")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val channelId = sbn.notification.channelId
        
        Log.d(TAG, "INBOUND: Pkg=$packageName, Ch=$channelId")

        if (isStressNotification(packageName, channelId)) {
            Log.i(TAG, "MATCH! Stress detected from $packageName ($channelId)")
            triggerHaptics()
        } else {
            Log.d(TAG, "NO MATCH: $packageName | $channelId")
        }
    }

    private fun isStressNotification(packageName: String, channelId: String?): Boolean {
        // Check package name
        val isFitbit = packagePattern.matcher(packageName).matches()
        val isMockApp = packageName == "com.example.wearnotifications" || 
                        packageName == "com.example.adbnotificationtester"
        val isSelf = packageName == applicationContext.packageName

        if (!isFitbit && !isMockApp && !isSelf) return false
        
        // Check channel ID
        if (channelId != null && channelPattern.matcher(channelId).matches()) {
            return true
        }
        
        return false
    }

    private fun triggerHaptics() {
        android.util.Log.i(TAG, "Triggering Refined 8s Ramped Pattern...")
        
        try {
            val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val vm = getSystemService(android.content.Context.VIBRATOR_MANAGER_SERVICE) as android.os.VibratorManager
                vm.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator
            }

            if (vibrator.hasVibrator()) {
                val settings = SettingsManager(applicationContext).getSettings()
                val (timings, amplitudes) = SettingsManager.generateWaveform(settings)
                
                val totalDuration = timings.sum()
                val pm = getSystemService(android.content.Context.POWER_SERVICE) as android.os.PowerManager
                val wl = pm.newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, "StressViber:Haptic")
                wl.acquire(totalDuration + 2000L) // Pattern duration + 2s safety

                val effect = android.os.VibrationEffect.createWaveform(timings, amplitudes, -1)
                val attrs = android.os.VibrationAttributes.Builder()
                    .setUsage(android.os.VibrationAttributes.USAGE_ALARM)
                    .build()

                vibrator.cancel() // Clear any active pattern before starting new one
                vibrator.vibrate(effect, attrs)
            }
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Haptic Error: ${e.message}", e)
        }
    }
}
