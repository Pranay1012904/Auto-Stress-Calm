# 自動状態異常回復 🧘

<div align="center">
  <img src="assets/icon.svg" width="128" height="128" alt="自動状態異常回復 图标">
</div>

<p align="center"><strong>零电量消耗 • 自动压力检测与状态回复</strong></p>

<div align="center">

[![下载最新版本](https://img.shields.io/badge/下载-最新版本-blue?style=for-the-badge)](https://github.com/Ethan-Ming/Auto-Stress-Calm/releases/latest)
[![Wear OS](https://img.shields.io/badge/Wear%20OS-5%2F6-4285F4?style=for-the-badge&logo=wear-os)](https://wearos.google.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](LICENSE)

[English](README.md) | 简体中文 | [日本語](README.ja.md)

</div>

---

## 💆 什么是 自動状態異常回復？

**自動状態異常回復** 是一款免费的开源 Wear OS 智能手表应用，可帮助您即时管理压力。当您的 Fitbit 检测到压力反应（使用身体反应/EDA 传感器）时，**自動状態異常回復** 会自动在您的手表上使用触觉振动启动一个温和的 8 秒引导呼吸课程。

将它想象成您手腕上的正念教练，随时准备在压力来袭时提醒您呼吸和放松。

### ✨ 您会喜欢它的原因

- **🎯 全自动运行**：静默监测，在您最需要时自动介入
- **🔋 零电池负担**：仅在检测到压力时激活，平时处于零功耗状态
- **💆 温和引导**：通过精细的触觉振动指引呼吸节奏
- **🆓 100% 免费**：开源、无广告、无订阅、不收集数据
- **🔒 隐私保护**：完全离线运行 - 您的生物识别数据永不离开手表

---

## 📲 如何安装

### 步骤 1：下载应用

1. 访问 [**发布页面**](https://github.com/Ethan-Ming/Auto-Stress-Calm/releases/latest)
2. 将最新的 **`Auto-Stress-Calm-v1.0.1.apk`** 文件下载到您的计算机

### 步骤 2：在手表上安装

您需要使用ADB来安装应用。别担心 - 这比听起来容易！

#### 🪟 Windows 用户：

1. **在手表上启用开发者模式**：
   - 在手表上：设置 → 系统 → 关于 → 点击"版本号"7 次
   - 您会看到"您现在是开发者！"

2. **启用 ADB 调试**：
   - 设置 → 开发者选项 → 打开"ADB 调试"
   - 打开"通过 Wi-Fi 调试"
   - 记下显示的 IP 地址（例如 `192.168.1.100:5555`）

3. **在计算机上安装 ADB**：
   - 从 Google 下载 [Platform Tools](https://developer.android.com/tools/releases/platform-tools)
   - 将 zip 文件解压到文件夹（例如 `C:\platform-tools`）

4. **连接并安装**：
   ```powershell
   # 在 platform-tools 文件夹中打开 PowerShell
   # 连接到您的手表（替换为您手表的 IP）
   .\adb connect 192.168.1.100:5555
   
   # 安装应用（替换为您下载 APK 的路径）
   .\adb install Auto-Stress-Calm-v1.0.1.apk
   ```

#### 🍎 Mac/Linux 用户：

```bash
# 安装 ADB（Mac 使用 Homebrew）
brew install android-platform-tools

# 或在 Linux 上
sudo apt-get install adb

# 连接到您的手表
adb connect 192.168.1.100:5555

# 安装应用
adb install Auto-Stress-Calm-v1.0.1.apk
```

### 步骤 3：授予权限

这是最重要的步骤！应用需要读取通知的权限。

1. **在手表上打开 自動状態異常回復**
2. 您会看到一个检查清单 - 点击**"打开设置"**
3. 在列表中找到 **"自動状態異常回復"** 并将其切换为**打开**
4. 出现提示时点击**"允许"**
5. 返回应用 - 您应该会看到绿色对勾 ✅

**就是这样！** 自動状態異常回復 现在正在监控压力通知。

---

## 🎯 如何使用

### 自动模式（推荐）

安装并授予权限后，**自動状態異常回復** 会自动运行：

1. **佩戴您的 Fitbit** 并继续您的日常活动
2. 当 Fitbit 检测到压力反应时，它会向您的手表发送通知
3. **自動状態異常回復 拦截此通知**并立即开始温和的呼吸课程
4. **感受振动模式**：
   - 🌬️ **吸气**（2.5 秒）- 振动逐渐增强
   - 🫁 **屏住**（3 秒）- 温和暂停
   - 😌 **呼气**（2.5 秒）- 振动逐渐减弱
5. 全天根据需要重复

### 手动测试

想在不等待压力的情况下尝试吗？

1. 在手表上打开 **自動状態異常回復** 应用
2. 点击**"测试触觉"**按钮
3. 感受 8 秒呼吸模式

---

## ❓ 常见问题

### 这适用于其他健身追踪器吗？

目前，**自動状態異常回復** 专为具有身体反应/EDA 压力检测功能的 **Fitbit** 设备设计。但是，它可以配置为与发送压力通知的其他应用一起使用。

### 这会耗尽我的电池吗？

不会！Auto Stress Calm 使用的电池很少，因为它只在您收到压力通知时才激活。它设计为高效运行并在后台静默运行。

### 我的数据会被收集吗？

**绝对不会。** **自動状態異常回復** 是 100% 开源的，完全在您的手表上运行。不会向任何服务器发送数据，也不需要互联网连接。

### 如果我感觉不到振动怎么办？

请确保：
- ✅ 您已授予通知访问权限
- ✅ 您的手表未处于"勿扰"模式
- ✅ 您的手表振动已在设置中打开
- ✅ 尝试应用中的"测试触觉"按钮

### 我可以自定义呼吸模式吗？

目前还不行，但这是一个计划中的功能！当前的 8 秒模式（2.5 秒吸气，3 秒屏住，2.5 秒呼气）基于经过验证的压力缓解呼吸技术。

### 支持哪些手表？

Auto Stress Calm 适用于：
- ✅ Pixel Watch 2（已测试）
- ✅ Pixel Watch 3
- ✅ 任何 Wear OS 5 或 6 设备（API 31+）

---

## 🆘 故障排除

### "未授予通知访问权限"错误

1. 在手表上：**设置 → 应用 → 特殊应用访问 → 通知访问**
2. 找到 **Auto Stress Calm** 并将其切换为**打开**
3. 重启 **自動状態異常回復** 应用

### 应用未响应压力通知

1. 确保您的 **Fitbit 应用**已安装并在手表上正常工作
2. 检查 Fitbit 是否正在发送通知（通过触发压力反应进行测试）
3. 打开 **自動状態異常回復** 并验证是否显示绿色对勾
4. 尝试重启手表

### 无法通过 ADB 安装

- 确保在手表上启用了 **ADB 调试**
- 验证您已连接：`adb devices` 应显示您的手表
- 尝试断开连接并重新连接：`adb disconnect` 然后 `adb connect <IP>`

---

## 🤝 贡献与支持

### 发现错误？

请[提交问题](https://github.com/Ethan-Ming/Auto-Stress-Calm/issues)并提供：
- 您的手表型号
- Wear OS 版本
- 发生了什么与您期望的对比

### 想要帮忙？

这是一个开源项目！欢迎贡献：
- 🐛 报告错误
- 💡 建议功能
- 🔧 提交拉取请求
- ⭐ 如果您觉得有用，请给仓库加星！

---

## 📄 许可证

Auto Stress Calm 是免费和开源的，根据 [MIT 许可证](LICENSE)授权。

您可以自由使用、修改和分享此应用。有关详细信息，请参阅许可证文件。

---

## 🙏 致谢

- 为所有应对压力的人用 ❤️ 构建
- 设计为与 Fitbit 的身体反应/EDA 功能无缝配合
- 受正念和循证呼吸技术的启发

---

<div align="center">

**深呼吸。你能做到。** 🌬️

[⬇️ 立即下载](https://github.com/Ethan-Ming/Auto-Stress-Calm/releases/latest) • [📖 报告问题](https://github.com/Ethan-Ming/Auto-Stress-Calm/issues) • [⭐ 在 GitHub 上加星](https://github.com/Ethan-Ming/Auto-Stress-Calm)

</div>
