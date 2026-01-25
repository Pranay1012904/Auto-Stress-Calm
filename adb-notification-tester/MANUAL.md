# ADB Notification Tester 📲

This utility allows you to simulate incoming notifications on Wear OS via ADB broadcasts. It is primarily used to test the **Auto Stress Calm** haptic response system.

## 🚀 How to use

### 1. Install the app
```bash
adb install -r adb-notification-tester.apk
```

### 2. Post a Mock Notification
Use the following ADB command to simulate a stress event:

```bash
adb shell "am broadcast -a com.example.adbnotificationtester.POST_NOTIFICATION \
  --es title 'Stress Alert' \
  --es message 'Body Response Detected' \
  --es channel_id 'body_response' \
  com.example.adbnotificationtester/.NotificationReceiver"
```

### 🛠️ Customization
You can change the parameters in the command:
- `--es title`: The notification heading.
- `--es message`: The notification body text.
- `--es channel_id`: The Android notification channel (Auto-Calm listens for `body_response`, `stress`, `eda`, etc.).
