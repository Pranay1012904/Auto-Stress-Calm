//This revised Product Requirements Document (PRD) incorporates the critical technical findings for **Wear OS 5 & 6 (API Level 35)**. The original "Ghost" architecture has been replaced with a "Resilient Zombie" model to ensure the service remains functional despite aggressive system power management and privacy redactions.

---

# Product Requirements Document (PRD): Auto Stress Calm (v2.0)

## Product Name

**Auto Stress Calm** (Internal: **StressViber**)

## Target Platform

*
**Wear OS**: 3.0 – 6.0


*
**Target API**: 35 (Android 15)


*
**Primary Test Device**: Pixel Watch 2



---

## 1. Project Overview

Auto Stress Calm is a resilient Wear OS service designed to intercept Fitbit **Body Response** (stress detection) notifications and trigger a perceptible, user-defined haptic response. Because Wear OS 5+ and Android 15 introduce aggressive background process termination and notification redaction, this version pivots from a passive "ghost" to a hardened, system-persistent service.

### Core Objective

Reliably detect Fitbit stress alerts and provide a "Breathing Exercise" haptic pattern that overrides the default, weak system vibration.

### Design Philosophy: "The Resilient Zombie"

*
**Persistence**: Uses explicit battery exemptions to survive Deep Doze and memory pressure.


*
**Hardware Directivity**: Bypasses the unreliable `NotificationChannel` vibration API in favor of direct `Vibrator` HAL control.


*
**Blind Logic**: Operates based on Channel ID heuristics, assuming notification content will be redacted by Android 15 privacy features.



---

## 2. Problem Statement


On Pixel Watch, the Fitbit app emits **Body Response** notifications when stress is detected. However:

- These notifications are silent by design
- The haptic feedback is weak and non-configurable
- Vibration strength and pattern are locked by system-protected channels
- Users cannot adjust or override these haptics through system settings

General-purpose automation tools (e.g., Tasker, MacroDroid) can react to notifications, but their background services introduce unacceptable power overhead for users targeting a **~1%/h total discharge rate**.

A specialized, passive, and battery-neutral solution is required.

The goals are also blocked by three platform realities:

1.
**Lifecycle Deletion**: Wear OS kills cached background processes that lack "Unrestricted" battery status.


2.
**Haptic Suppression**: Wear OS routinely ignores custom vibration patterns set on `NotificationChannels` to enforce a unified system "theme".


3.
**Privacy Redaction**: Android 15 masks health notification text as "Sensitive notification content hidden," breaking keyword-based filters.



---

## 3. Functional Requirements

### FR-1: Resilient Notification Interception

The app must remain bound to the system to catch Fitbit alerts even after long periods of inactivity.

*
**Mechanism**: `NotificationListenerService`.


*
**Survival Requirement**: Must prompt the user for `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`.


*
**Trigger**: `onNotificationPosted` callback.



### FR-2: Direct Hardware Haptics (System Override)

The app must bypass standard notification haptics, which are often suppressed or shortened by Wear OS.

*
**Mechanism**: Use `VibratorManager` or `Vibrator` service directly.


*
**Pattern ("Breathing")**: 3s ON, 3s OFF, 3s ON.


*
**Intensity**: Defined via `VibrationEffect.createWaveform` with explicit amplitude arrays to ensure maximum perceptibility.



### FR-3: Blind Filtering Logic (Android 15 Ready)

Because notification text (e.g., "Stress level high") is likely to be redacted, the app must filter based on immutable metadata.

*
**Primary Filter**: Regex match on `channelId`.
*
*Targets*: `(?i).*(body|response|stress|eda).*`. 


* **Secondary Filter**: `packageName == "com.fitbit.FitbitMobile"`. or similer com.fitbit.* regex


*
**Fallback**: Treat all notifications from the identified "Stress" channel as actionable, ignoring redacted text fields.



### FR-4: Mock Testing

in virtual mechine, is hard to verify that the haptics are working. 

*
**Test Action**:monitor the `Vibrator` service for activity. use wear notifaction app to send test notification with custom channel id that matches the fitbit stress channel id.


*
**Validation**: Confirms the `Vibrator` service triggers the correct 12-second waveform.



---

## 4. Technical Specifications & "Red Lines"

| Metric | Target | Rationale |
| --- | --- | --- |
| **Battery Overhead** | < 0.2% / hour | Slightly higher than original to accommodate "Unrestricted" status.

|
| **Permissions** | `VIBRATE`, `BIND_NLS`, `IGNORE_BATTERY_OPTIMIZATIONS` | Required for hardware control and survival.

|
| **Code Size** | ≤ 500 LOC | Remains auditable while adding permission handling.

|
| **Execution** | Event-Driven | No polling; CPU wakes only on notification or haptic trigger.

|

---

## 5. Risk Mitigation

*
**Fitbit Channel Volatility**: Fitbit may change internal Channel IDs at any time.


*
*Mitigation*: Use broad regex matching rather than exact string matches.




*
**Foreground Service (FGS) Restrictions**: API 35 restricts background FGS starts.


*
*Mitigation*: The `NotificationListenerService` is a system-bound service, which typically provides the necessary context to trigger haptics without a separate FGS.




*
**User Setup Friction**: Direct vibrator use and battery exemptions require manual user steps.


*
*Mitigation*: The "Test Haptics" UI will double as an onboarding checklist to ensure all permissions are granted.





---

## 6. Success Criteria

1.
**Functional**: A physical Fitbit stress alert triggers a 12-second custom vibration.


2.
**Stability**: The service remains active and responsive after 24 hours of device idle time.


3.
**Privacy**: Filtering remains successful on Android 15 even when notification text is redacted.

