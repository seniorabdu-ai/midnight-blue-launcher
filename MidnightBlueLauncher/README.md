# MIDNIGHT BLUE — Red Sea Edition

Native Android TV launcher for Xiaomi TV Stick 4K / Android TV 11.

## Version 2.0 Premium redesign
- 1920×1080 landscape-first composition with a layered Red Sea dusk illustration.
- Large live clock and date; Al Quseir prayer-time panel with a live next-prayer countdown (silent).
- Large app cards that use the installed apps' own launcher icons when available.
- D-pad navigation and OK-to-launch behavior.
- All Apps screen discovers Leanback apps, with a regular launcher fallback.
- Stock Xiaomi launcher is not disabled or replaced by the installer.

## Build
Requires JDK 17 and Android SDK platform 35/build-tools 35.0.0.

```bash
gradle assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## Important notes
- This is a native launcher build, not an HTML/TV-browser page.
- Prayer times are calculated locally for Al Quseir using Egyptian-method solar angles. They should be compared with the official local timetable before relying on them for religious observance. No adhan audio is played.
- Package IDs for streaming apps vary by region/device. Missing apps remain visible but show “Not installed”; All Apps provides a fallback list.
- The launcher has not yet been validated on the target Xiaomi TV Stick. Keep the stock launcher as the default until the new build has been visually and functionally tested.
