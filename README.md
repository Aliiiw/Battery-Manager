# Battery Manager

Battery Manager is an Android app written in Kotlin with XML layouts. It shows live battery information, tracks recent app usage, and can run a foreground service that warns the user when the phone is almost fully charged.

The project is built as a native Android app with ViewBinding, Material components, Lottie animation, and a circular progress indicator for the battery level.

## Features

- Live battery percentage with animated circular progress
- Battery voltage, temperature, technology, and plugged/unplugged status
- Battery health messages for good, cold, overheat, over-voltage, and dead states
- Drawer menu with app usage navigation
- Foreground service that keeps a battery notification active
- Alarm sound and vibration when the battery level goes above 97%
- 24-hour app usage list with app names, icons, usage percentages, and time spent
- Splash screen with Lottie animation and rotating messages
- Service state saved with SharedPreferences

## Tech Stack

- Kotlin
- Android XML layouts
- Gradle / Android Gradle Plugin
- ViewBinding
- AndroidX AppCompat
- ConstraintLayout
- Material Components
- Lottie
- CircularProgressBar by Mikhael Lopez

## Project Structure

```text
.
+-- app/
|   +-- src/main/
|   |   +-- AndroidManifest.xml
|   |   +-- java/com/alirahimi/batterymanager/
|   |   |   +-- activity/
|   |   |   |   +-- SplashActivity.kt
|   |   |   |   +-- MainActivity.kt
|   |   |   |   +-- UsageBatteryActivity.kt
|   |   |   +-- adapter/
|   |   |   |   +-- BatteryUsageAdapter.kt
|   |   |   +-- model/
|   |   |   |   +-- BatteryModel.kt
|   |   |   +-- services/
|   |   |   |   +-- BatteryAlarmService.kt
|   |   |   +-- sharedPreferences/
|   |   |   |   +-- SharedPreferencedManager.kt
|   |   |   +-- utils/
|   |   |       +-- BatteryUsage.kt
|   |   +-- res/
|   |       +-- layout/
|   |       +-- drawable/
|   |       +-- raw/
|   |       +-- values/
+-- build.gradle
+-- settings.gradle
+-- gradle.properties
+-- README.md
```

## Main Components

| Component | Purpose |
| --- | --- |
| `SplashActivity` | Displays the splash screen and opens the main screen after a short delay. |
| `MainActivity` | Shows live battery status and controls the navigation drawer. |
| `UsageBatteryActivity` | Displays recent app usage in a RecyclerView. |
| `BatteryUsage` | Reads app usage stats for the last 24 hours. |
| `BatteryUsageAdapter` | Groups usage entries by package and renders app name, icon, time, and percentage. |
| `BatteryAlarmService` | Foreground service that updates a persistent battery notification and triggers an alarm near full charge. |
| `SharedPreferencedManager` | Stores whether the battery alarm service is enabled. |
| `BatteryModel` | Simple model for package name, usage percent, and usage time. |

## Permissions

The app declares these permissions in `AndroidManifest.xml`:

| Permission | Used For |
| --- | --- |
| `FOREGROUND_SERVICE` | Running `BatteryAlarmService` as a foreground service. |
| `QUERY_ALL_PACKAGES` | Resolving installed app names and icons for the app usage list. |
| `VIBRATE` | Vibrating the phone when the near-full-charge alarm is triggered. |

The usage screen also relies on Android Usage Access. If usage stats are unavailable, the app opens the system Usage Access settings screen so the user can grant access.

## How It Works

1. `SplashActivity` shows the startup screen, then launches `MainActivity`.
2. `MainActivity` registers an `ACTION_BATTERY_CHANGED` receiver.
3. The receiver updates battery percentage, voltage, temperature, battery technology, plugged status, and health state.
4. The circular progress bar changes color based on charge level: red for low, yellow for medium, and green for high.
5. The drawer switch stores the service state in SharedPreferences.
6. When enabled, `BatteryAlarmService` starts as a foreground service and keeps a notification updated with current battery status.
7. If battery level is above 97%, the service plays the default notification sound and vibrates the device.
8. `UsageBatteryActivity` reads usage stats from the last 24 hours and displays each app with usage time and percentage.

## App Usage Calculation

The app usage page calculates percentages from foreground time during the last 24 hours:

```kotlin
item.totalTimeInForeground / totalForegroundTime * 100
```

This means the list represents relative foreground usage time, not exact battery drain reported by Android's battery settings.

## Requirements

- Android Studio
- JDK 8 or newer
- Android SDK with compile SDK 32
- Android device or emulator running Android 5.0+ because `minSdk` is 21

Project configuration:

| Setting | Value |
| --- | --- |
| Application ID | `com.alirahimi.batterymanager` |
| Min SDK | 21 |
| Target SDK | 32 |
| Compile SDK | 32 |
| Version | 1.0 |
| Android Gradle Plugin | 7.1.3 |
| Kotlin Plugin | 1.5.30 |

## How to Run

Open the project in Android Studio and let Gradle sync. Then run the `app` configuration on an emulator or physical Android device.

You can also build from the terminal:

```bash
./gradlew assembleDebug
```

Install the debug build on a connected device:

```bash
./gradlew installDebug
```

## Notes

- The app uses `ACTION_BATTERY_CHANGED`, which is a sticky broadcast and does not require a custom polling loop.
- The near-full-charge alarm currently triggers when battery level is greater than `97`.
- Usage stats require user approval from Android settings.
- `QUERY_ALL_PACKAGES` is a sensitive permission on newer Android versions and should be reviewed before publishing to Google Play.
- The repository includes IDE and release-output files; they are not required to understand the source code.

## Possible Improvements

- Add a configurable alarm threshold instead of the fixed 97% value
- Add a notification action to stop the foreground service
- Register and unregister battery receivers with lifecycle cleanup
- Use `UsageStatsManager` defensively when total foreground time is zero
- Replace `QUERY_ALL_PACKAGES` with a narrower package visibility strategy when possible
- Add unit tests for usage grouping and time formatting
- Add screenshots or a demo GIF to the README
