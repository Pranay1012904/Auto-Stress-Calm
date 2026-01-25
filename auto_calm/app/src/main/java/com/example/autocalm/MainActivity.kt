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
import android.view.MotionEvent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.material.*
import kotlinx.coroutines.launch
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.autocalm.ui.theme.AutoCalmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutoCalmTheme {
                var currentScreen by remember { mutableStateOf("home") }
                
                if (currentScreen == "settings") {
                    SettingsScreen(onBack = { currentScreen = "home" })
                } else {
                    MainScreen(onGoToSettings = { currentScreen = "settings" })
                }
            }
        }
    }
}

@Composable
fun MainScreen(onGoToSettings: () -> Unit) {
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

        Button(
            onClick = onGoToSettings,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
        ) {
            Text("⚙ Settings")
        }
    }
}

@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val initialSettings = remember { settingsManager.getSettings() }
    
    var repeats by remember { mutableFloatStateOf(initialSettings.repeats.toFloat()) }
    var maxIntensity by remember { mutableFloatStateOf(initialSettings.maxIntensity.toFloat()) }
    var minIntensity by remember { mutableFloatStateOf(initialSettings.minIntensity.toFloat()) }
    var duration by remember { mutableFloatStateOf(initialSettings.segmentDuration.toFloat()) }

    val scrollState = rememberScalingLazyListState()
    val focusRequester = rememberActiveFocusRequester()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        positionIndicator = { PositionIndicator(scalingLazyListState = scrollState) }
    ) {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        scrollState.scrollBy(it.verticalScrollPixels)
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable(),
            state = scrollState,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp, start = 8.dp, end = 8.dp)
        ) {
            item {
                Text("Waveform Settings", style = MaterialTheme.typography.title3)
            }

            item {
                SettingSlider(
                    label = "Repeats: ${repeats.toInt()}",
                    value = repeats,
                    onValueChange = { repeats = it },
                    valueRange = 1f..10f,
                    steps = 8
                )
            }

            item {
                SettingSlider(
                    label = "Max Intent: ${maxIntensity.toInt()}",
                    value = maxIntensity,
                    onValueChange = { maxIntensity = it },
                    valueRange = 1f..10f,
                    steps = 8
                )
            }

            item {
                SettingSlider(
                    label = "Min Intent: ${minIntensity.toInt()}",
                    value = minIntensity,
                    onValueChange = { minIntensity = it },
                    valueRange = 1f..10f,
                    steps = 8
                )
            }

            item {
                SettingSlider(
                    label = "Haptic Dur: ${duration.toInt()}s",
                    value = duration,
                    onValueChange = { duration = it },
                    valueRange = 1f..10f,
                    steps = 8
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(
                        onClick = {
                            settingsManager.saveSettings(
                                VibrationSettings(
                                    repeats = repeats.toInt(),
                                    maxIntensity = maxIntensity.toInt(),
                                    minIntensity = minIntensity.toInt(),
                                    segmentDuration = duration.toInt()
                                )
                            )
                            onBack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save")
                    }
                    
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.secondaryButtonColors(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
fun SettingSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.caption2)
        InlineSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            modifier = Modifier.fillMaxWidth(),
            decreaseIcon = { Icon(androidx.wear.compose.material.InlineSliderDefaults.Decrease, "Decrease") },
            increaseIcon = { Icon(androidx.wear.compose.material.InlineSliderDefaults.Increase, "Increase") }
        )
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

    val settings = SettingsManager(context).getSettings()
    val (timings, amplitudes) = SettingsManager.generateWaveform(settings)
    val effect = android.os.VibrationEffect.createWaveform(timings, amplitudes, -1)
    
    if (vibrator.hasVibrator()) {
        vibrator.vibrate(effect)
    }
}

fun sendMockNotification(context: Context) {
    val channelId = "body_response"
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
