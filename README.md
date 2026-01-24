# Auto Stress Calm 🧘

<div align="center">
  <img src="assets/icon.svg" width="128" height="128" alt="Auto Stress Calm Icon">
</div>

<p align="center"><strong>Near-Zero Battery Impact • Automatic Stress Detection & Relief</strong></p>

<div align="center">

[![Download Latest Release](https://img.shields.io/badge/Download-Latest%20Release-blue?style=for-the-badge)](https://github.com/Ethan-Ming/Auto-Stress-Calm/releases/latest)
[![Wear OS](https://img.shields.io/badge/Wear%20OS-5%2F6-4285F4?style=for-the-badge&logo=wear-os)](https://wearos.google.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](LICENSE)

English | [简体中文](README.zh-CN.md) | [日本語](README.ja.md)

</div>

---

## 💆 What is Auto Stress Calm?

**Auto Stress Calm** is a free, open-source app for your Wear OS smartwatch that helps you manage stress in the moment. When your Fitbit detects a stress response (using Body Response/EDA sensors), Auto Stress Calm automatically starts a gentle 8-second guided breathing session using haptic vibrations on your watch.

Think of it as having a mindfulness coach on your wrist, ready to help you breathe and relax whenever stress strikes.

- **🎯 Fully Automatic**: Detects and reminds you when you need it most
- **🔋 Zero Battery Impact**: Inactive until a stress event occurs - zero idle power drain
- **💆 Gentle Guidance**: Feel the breathing rhythm through subtle, localized vibrations
- **🆓 100% Free**: Open source, no ads, no subscriptions, no tracking
- **🔒 Private**: Fully offline - your biometric data never leaves your watch

---

## 📲 How to Install

### Step 1: Download the App

1. Go to the [**Releases Page**](https://github.com/Ethan-Ming/Auto-Stress-Calm/releases/latest)
2. Download the latest **`Auto-Stress-Calm-v1.0.1.apk`** file to your computer

### Step 2: Install on Your Watch

You'll need to use ADB (Android Debug Bridge) to install the app. Don't worry - it's easier than it sounds!

#### 🪟 For Windows Users:

1. **Enable Developer Mode on your watch**:
   - On your watch: Settings → System → About → Tap "Build number" 7 times
   - You'll see "You are now a developer!"

2. **Enable ADB Debugging**:
   - Settings → Developer options → Turn on "ADB debugging"
   - Turn on "Debug over Wi-Fi"
   - Note the IP address shown (e.g., `192.168.1.100:5555`)

3. **Install ADB on your computer**:
   - Download [Platform Tools](https://developer.android.com/tools/releases/platform-tools) from Google
   - Extract the zip file to a folder (e.g., `C:\platform-tools`)

4. **Connect and Install**:
   ```powershell
   # Open PowerShell in the platform-tools folder
   # Connect to your watch (replace with your watch's IP)
   .\adb connect 192.168.1.100:5555
   
   # Install the app (replace path with where you downloaded the APK)
   .\adb install Auto-Stress-Calm-v1.0.1.apk
   ```

#### 🍎 For Mac/Linux Users:

```bash
# Install ADB (Mac with Homebrew)
brew install android-platform-tools

# Or on Linux
sudo apt-get install adb

# Connect to your watch
adb connect 192.168.1.100:5555

# Install the app
adb install Auto-Stress-Calm-v1.0.1.apk
```

### Step 3: Grant Permissions

This is the most important step! The app needs permission to read notifications.

1. **Open Auto Stress Calm** on your watch
2. You'll see a checklist - tap **"Open Settings"**
3. Find **"Auto Stress Calm"** in the list and toggle it **ON**
4. Tap **"Allow"** when prompted
5. Go back to the app - you should see a green checkmark ✅

**That's it!** Auto Stress Calm is now monitoring for stress notifications.

---

## 🎯 How to Use

### Automatic Mode (Recommended)

Once installed and permissions are granted, Auto Stress Calm works automatically:

1. **Wear your Fitbit** and go about your day
2. When Fitbit detects a stress response, it sends a notification to your watch
3. **Auto Stress Calm intercepts this notification** and immediately starts a gentle breathing session
4. **Feel the vibration pattern**:
   - 🌬️ **Breathe IN** (2.5 seconds) - vibration gradually increases
   - 🫁 **Hold** (3 seconds) - gentle pause
   - 😌 **Breathe OUT** (2.5 seconds) - vibration gradually fades
5. Repeat as needed throughout the day

### Manual Test

Want to try it without waiting for stress?

1. Open the **Auto Stress Calm** app on your watch
2. Tap the **"Test Haptics"** button
3. Feel the 8-second breathing pattern

---

## ❓ Frequently Asked Questions

### Does this work with other fitness trackers?

Currently, Auto Stress Calm is designed for **Fitbit** devices that have Body Response/EDA stress detection. However, it can be configured to work with other apps that send stress notifications.

### Will this drain my battery?

No! Auto Stress Calm uses very little battery because it only activates when you receive a stress notification. It's designed to be efficient and runs quietly in the background.

### Is my data being collected?

**Absolutely not.** Auto Stress Calm is 100% open source and runs entirely on your watch. No data is sent to any servers, and there's no internet connection required.

### What if I don't feel the vibration?

Make sure:
- ✅ You granted notification access permission
- ✅ Your watch isn't in "Do Not Disturb" mode
- ✅ Your watch vibration is turned on in Settings
- ✅ Try the "Test Haptics" button in the app

### Can I customize the breathing pattern?

Not yet, but this is a planned feature! The current 8-second pattern (2.5s in, 3s hold, 2.5s out) is based on proven breathing techniques for stress relief.

### Which watches are supported?

Auto Stress Calm works on:
- ✅ Pixel Watch 2 (tested)
- ✅ Pixel Watch 3
- ✅ Any Wear OS 5 or 6 device (API 31+)

---

## 🆘 Troubleshooting

### "Notification Access Not Granted" Error

1. Go to your watch: **Settings → Apps → Special app access → Notification access**
2. Find **Auto Stress Calm** and toggle it **ON**
3. Restart the Auto Stress Calm app

### App Not Responding to Stress Notifications

1. Make sure your **Fitbit app** is installed and working on your watch
2. Check that Fitbit is sending notifications (test by triggering a stress response)
3. Open Auto Stress Calm and verify the green checkmark is showing
4. Try restarting your watch

### Can't Install via ADB

- Make sure **ADB debugging** is enabled on your watch
- Verify you're connected: `adb devices` should show your watch
- Try disconnecting and reconnecting: `adb disconnect` then `adb connect <IP>`

---

## 🤝 Contributing & Support

### Found a Bug?

Please [open an issue](https://github.com/Ethan-Ming/Auto-Stress-Calm/issues) with:
- Your watch model
- Wear OS version
- What happened vs. what you expected

### Want to Help?

This is an open-source project! Contributions are welcome:
- 🐛 Report bugs
- 💡 Suggest features
- 🔧 Submit pull requests
- ⭐ Star the repo if you find it useful!

---

## 📄 License

Auto Stress Calm is free and open source, licensed under the [MIT License](LICENSE).

You're free to use, modify, and share this app. See the license file for details.

---

## 🙏 Credits

- Built with ❤️ for anyone dealing with stress
- Designed to work seamlessly with Fitbit's Body Response/EDA features
- Inspired by mindfulness and evidence-based breathing techniques

---

<div align="center">

**Take a deep breath. You've got this.** 🌬️

[⬇️ Download Now](https://github.com/Ethan-Ming/Auto-Stress-Calm/releases/latest) • [📖 Report Issue](https://github.com/Ethan-Ming/Auto-Stress-Calm/issues) • [⭐ Star on GitHub](https://github.com/Ethan-Ming/Auto-Stress-Calm)

</div>
