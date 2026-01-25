package com.example.autocalm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class TestSettingsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.autocalm.SET_VIBRATION") {
            val settingsManager = SettingsManager(context)
            val current = settingsManager.getSettings()
            
            val newSettings = VibrationSettings(
                repeats = intent.getIntExtra("repeats", current.repeats),
                maxIntensity = intent.getIntExtra("max_intensity", current.maxIntensity),
                minIntensity = intent.getIntExtra("min_intensity", current.minIntensity),
                segmentDuration = intent.getIntExtra("duration", current.segmentDuration)
            )
            
            settingsManager.saveSettings(newSettings)
            Log.i("VibrationVerifier", "Settings updated via ADB: $newSettings")
        } else if (intent.action == "com.example.autocalm.TRIGGER_VIBRATION") {
            Log.i("VibrationVerifier", "Manual Trigger via ADB Broadcast")
            val settings = SettingsManager(context).getSettings()
            val (timings, amplitudes) = SettingsManager.generateWaveform(settings)
            
            val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as android.os.VibratorManager
                vm.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
            }
            
            val effect = android.os.VibrationEffect.createWaveform(timings, amplitudes, -1)
            vibrator.vibrate(effect)
        }
    }
}
