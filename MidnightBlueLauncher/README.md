# MIDNIGHT BLUE — Android TV Launcher

A first-pass native Android TV launcher for Xiaomi TV Stick 4K / Android TV 11. It is designed for 1920×1080 landscape screens and remote-control navigation.

## Build without Android Studio

1. Create a new **private** GitHub repository named `MidnightBlueLauncher`.
2. Upload the contents of this folder to the repository root (including the hidden `.github` folder).
3. Open **Actions** → **Build MIDNIGHT BLUE APK** → **Run workflow**.
4. When the run completes, open it and download the `midnight-blue-debug-apk` artifact.
5. Extract the ZIP; the APK is `app-debug.apk`.

The workflow builds on GitHub's cloud runner. No Java, Gradle, Android Studio, or SDK installation is needed on the Mac.

## Install and test

With ADB connected to the TV Stick:

```bash
~/Downloads/platform-tools/adb install -r app-debug.apk
```

Launch it once from the Apps list. To test the Home intent without changing the default launcher:

```bash
~/Downloads/platform-tools/adb shell am start -a android.intent.action.MAIN -c android.intent.category.HOME
```

Only after verifying the screen and remote controls should you choose MIDNIGHT BLUE as the Home app in Android TV settings, if the device exposes that option. The stock launcher and PatchWall are not disabled or removed.

## Notes / first-build limitations

- Prayer times are calculated locally for Al Quseir using approximate Egyptian-method angles (Fajr 19.5°, Isha 17.5°). Verify against your preferred local mosque / official timetable before relying on them. No adhan audio is played.
- App shortcuts resolve installed package names at runtime. MBC and Prime Video can vary by region/device package; missing apps show `NOT FOUND` rather than causing a crash.
- This first build is a visual/functional prototype. The background is drawn locally (not a Red Sea photograph); later we can replace it with a chosen background image and refine prayer-time calculation/adjustments.
