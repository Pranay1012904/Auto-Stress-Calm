# ADB Vibration Verification Workflow

This workflow uses direct broadcasts to configure vibration settings, ensuring 100% reliable automated testing.

// turbo-all
1. **Prepare Device**
   - Reset logcat: `adb shell logcat -c`
   - Ensure app is installed and service is ready.

2. **Configure Settings via ADB**
   - Set specific parameters: `adb shell am broadcast -a com.example.autocalm.SET_VIBRATION --ei repeats 3 --ei duration 2 --ei max_intensity 10 --ei min_intensity 1`
   - Verify update in logs: `adb logcat -d VibrationVerifier:I *:S`

3. **Trigger Notification**
   - Use mock trigger: `adb shell am start -n com.example.autocalm/.MainActivity; Start-Sleep -s 1; adb shell input tap 192 100` (Tap "Test via Service")
   - *Alternative: Direct trigger if we had a broadcast for that, but UI tap on the first button is reliable enough since it's the top item.*

4. **Verify Waveform**
   - Analyze logs for "Generated timings" and "Generated amplitudes".
   - Repeats=3 means the arrays should be 3x longer than a single cycle.
   - Duration=2 means 2000ms phases.
