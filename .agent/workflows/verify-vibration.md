# Vibration Verification Workflow

This workflow automates the testing of vibration waveforms triggered by stress notifications.

// turbo-all
1. **Prepare Device**
   - Reset logcat: `adb shell logcat -c`
   - Ensure app is open: `adb shell am start -n com.example.autocalm/.MainActivity`

2. **Configure Settings via UI**
   - Navigate to Settings: `adb shell input keyevent KEYCODE_DPAD_DOWN; adb shell input keyevent KEYCODE_DPAD_DOWN; adb shell input keyevent KEYCODE_DPAD_DOWN; adb shell input keyevent KEYCODE_ENTER`
   - Adjust first slider (Repeats) +1: `adb shell input keyevent KEYCODE_DPAD_DOWN; adb shell input keyevent KEYCODE_DPAD_RIGHT`
   - Save: `adb shell input keyevent 61; adb shell input keyevent 61; adb shell input keyevent KEYCODE_ENTER` (Tab to save button)
   - *Note: Interaction is approximate, diagnostic logs will tell the truth.*

3. **Trigger Notification**
   - Click "Test via Service": `adb shell input keyevent KEYCODE_DPAD_UP; adb shell input keyevent KEYCODE_DPAD_UP; adb shell input keyevent KEYCODE_ENTER`

4. **Capture and Analyze**
   - Dump logs: `adb logcat -d VibrationVerifier:I *:S`
   - Verify: Analyze if "Generated timings" in logs matches the expected duration and repeats.
