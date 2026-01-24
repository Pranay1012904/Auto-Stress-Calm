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
                    // Fallback to general notification settings if specialized menu fails
                    context.startActivity(Intent(Settings.ACTION_SETTINGS))
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                sendMockNotification(context)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Test Haptics")
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



fun sendMockNotification(context: Context) {
    val channelId = "eda_stress_test"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = android.app.NotificationChannel(
            channelId,
            "Mock Stress Alert",
            android.app.NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Fitbit Simulation")
        .setContentText("Body Response Detected (Mock)")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notify(1001, builder.build())
        } else {
            // Permission missing
        }
    }
}
