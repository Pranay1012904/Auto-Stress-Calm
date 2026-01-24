package com.example.autocalm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.text.TextUtils
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.wear.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.autocalm.ui.theme.AutoCalmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutoCalmTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isNlsEnabled by remember { mutableStateOf(isNotificationServiceEnabled(context)) }

    // Re-check status when returning to foreground
    DisposableEffect(Unit) {
        onDispose { }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Stress Viber Setup",
            style = MaterialTheme.typography.title3,
            textAlign = TextAlign.Center
        )

        StatusItem(
            label = "Notification Access",
            isDone = isNlsEnabled,
            onClick = {
                try {
                    context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                } catch (e: Exception) {
                    context.startActivity(Intent(Settings.ACTION_SETTINGS))
                }
            }
        )

        // Android 13+ Notification Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasNotifyPermission = ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            
            StatusItem(
                label = "Notify Permission",
                isDone = hasNotifyPermission,
                onClick = {
                    // This is simple for a debug tool, in production we'd use a launcher
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                sendMockNotification(context)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("1. Test via Service")
        }

        Button(
            onClick = {
                triggerDirectHaptic(context)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
        ) {
            Text("2. Direct Vibrate")
        }
    }
}

@Composable
fun StatusItem(label: String, isDone: Boolean, onClick: () -> Unit) {
    Chip(
        onClick = onClick,
        label = { Text(label) },
        secondaryLabel = { Text(if (isDone) "✓ Enabled" else "⚠️ Action Required") },
        colors = ChipDefaults.primaryChipColors(
            backgroundColor = if (isDone) MaterialTheme.colors.surface else MaterialTheme.colors.error
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

fun isNotificationServiceEnabled(context: Context): Boolean {
    val packageNames = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
    if (TextUtils.isEmpty(packageNames)) return false
    val listeners = packageNames.split(":")
    val expectedListener = ComponentName(context, StressNotificationListener::class.java).flattenToString()
    return listeners.any { it == expectedListener }
}



fun triggerDirectHaptic(context: Context) {
    android.util.Log.i("StressViber", "Manual Trigger from UI...")
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as android.os.VibratorManager
        vm.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
    }

    val timings = longArrayOf(250, 250, 250, 250, 250)
    val amplitudes = intArrayOf(25, 50, 100, 50, 25)
    val effect = android.os.VibrationEffect.createWaveform(timings, amplitudes, -1)
    
    if (vibrator.hasVibrator()) {
        vibrator.vibrate(effect)
    }
}

fun sendMockNotification(context: Context) {
    val channelId = "eda_stress_test"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

    android.util.Log.i("StressViber", "Sending Mock Notification...")

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = android.app.NotificationChannel(
            channelId,
            "Mock Stress Alert",
            android.app.NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for body response testing"
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Fitbit Simulation")
        .setContentText("Body Response Detected (Mock)")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setAutoCancel(true)

    try {
        notificationManager.notify(1001, builder.build())
        android.util.Log.i("StressViber", "Notification Sent to System")
    } catch (e: Exception) {
        android.util.Log.e("StressViber", "Notify Error: ${e.message}")
    }
}
