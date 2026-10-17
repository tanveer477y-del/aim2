# Carrom Floating Aim Bot (ಆರಂಭಿಕ APK ಪ್ರಾಜೆಕ್ಟ್)

ಈ ಪ್ರಾಜೆಕ್ಟ್ Android ನಲ್ಲಿ ಇತರ ಆ್ಯಪ್‌ಗಳ ಮೇಲೆ ತೋರಿಸಬಹುದಾದ floating panel ಮತ್ತು aim line ರಚಿಸುತ್ತದೆ. ಇದು ಆಟವನ್ನು ಸ್ವಯಂಚಾಲಿತವಾಗಿ ಓದುವುದಿಲ್ಲ; ಮೊದಲು Main screen ನಲ್ಲಿ striker/target ನ ಅಂದಾಜು screen pixel coordinates ನಮೂದಿಸಬೇಕು.

## Termux ನಲ್ಲಿ APK build

1. Termux (ARM64) ತೆರೆಯಿರಿ.
2. Android SDK/Gradle ಪರಿಸರ ಸಿದ್ಧಪಡಿಸಲು ಈ community setup ಬಳಸಬಹುದು: https://github.com/Xtratter/termux-android-build
3. ZIP ಅನ್ನು `Download` ಗೆ ಇಟ್ಟು unzip ಮಾಡಿ, ಉದಾಹರಣೆಗೆ:

```sh
pkg install -y unzip
termux-setup-storage
mkdir -p ~/projects
unzip /sdcard/Download/CarromFloatingAimBot.zip -d ~/projects/CarromFloatingAimBot
cd ~/projects/CarromFloatingAimBot
echo "sdk.dir=$HOME/android-sdk" > local.properties
./gradlew --no-daemon assembleDebug
```

`./gradlew` ಇಲ್ಲದಿದ್ದರೆ project wrapper ಸೇರಿಲ್ಲ: ನಿಮ್ಮ Termux Android-build setup ನಲ್ಲಿ Gradle wrapper/Gradle install ಮಾಡಿ ಅಥವಾ Android Studio ನಲ್ಲಿ project ತೆರೆಯಿರಿ. Build ಯಶಸ್ವಿಯಾದರೆ APK ಸಾಮಾನ್ಯವಾಗಿ:
`app/build/outputs/apk/debug/app-debug.apk`

## Install

```sh
termux-open app/build/outputs/apk/debug/app-debug.apk
```

Android installer ನಲ್ಲಿ Install ಒತ್ತಿ. App ತೆರೆಯಿರಿ, coordinates ನಮೂದಿಸಿ, `GRANT OVERLAY + START AIM` ಒತ್ತಿ, ನಂತರ Settings ನಲ್ಲಿ `Display over other apps` ಅನುಮತಿ ನೀಡಿ ಮತ್ತು ಮತ್ತೆ Start ಒತ್ತಿ.

## Coordinates

X = ಪರದೆಯ ಎಡದಿಂದ pixel, Y = ಮೇಲಿನಿಂದ pixel. ಉದಾಹರಣೆಗೆ 1080x2400 ಪರದೆಯಲ್ಲಿ X ಸುಮಾರು 0–1080, Y ಸುಮಾರು 0–2400. ನಿಖರ coordinate ಗಳು ನಿಮ್ಮ display resolution ಮತ್ತು game layout ಮೇಲೆ ಅವಲಂಬಿತ.

## ಮಿತಿಗಳು

- ಇದು starter implementation; live screenshot recognition ಇನ್ನೂ ಸೇರಿಸಿಲ್ಲ.
- Overlay line non-touchable ಆಗಿರುವುದರಿಂದ ಆಟಕ್ಕೆ touch ಹೋಗಬಹುದು.
- Bank point ನೀಡಿದರೆ green two-segment guide ಕಾಣುತ್ತದೆ; ಅದು ಸ್ವಯಂ bank geometry ಲೆಕ್ಕಿಸುವುದಿಲ್ಲ.
- Android overlay ಅನುಮತಿ ಬಳಕೆದಾರರು ಸ್ಪಷ್ಟವಾಗಿ ಒಪ್ಪಬೇಕು.
- ನಿಮ್ಮ ಸಾಧನದಲ್ಲಿ build ಮಾಡಿ ಪರೀಕ್ಷಿಸಬೇಕು; ಇಲ್ಲಿ APK binary build ಅಥವಾ device test ಮಾಡಿಲ್ಲ.


## ಸುಲಭ ವಿಧಾನ: GitHub ಮೂಲಕ APK build (Android SDK ನಿಮ್ಮ ಫೋನ್‌ನಲ್ಲಿ ಬೇಕಿಲ್ಲ)

1. ಈ ZIP ಅನ್ನು ನಿಮ್ಮ GitHub ಖಾತೆಯಲ್ಲಿ ಹೊಸ repository ಗೆ upload ಮಾಡಿ. `.github/workflows/build-apk.yml` ಫೈಲ್ ಸೇರಿರುವುದನ್ನು ಖಚಿತಪಡಿಸಿ.
2. Repository ಯ `Actions` tab ತೆರೆಯಿರಿ. `Build Carrom Aim APK` ಆಯ್ಕೆಮಾಡಿ, `Run workflow` ಒತ್ತಿರಿ (ಅಥವಾ main branch ಗೆ push ಮಾಡಿ).
3. Build ಯಶಸ್ವಿಯಾದ ಮೇಲೆ workflow run ಒಳಗಿನ `Artifacts` ವಿಭಾಗದಿಂದ `carrom-floating-aim-debug-apk` download ಮಾಡಿ. ZIP ಒಳಗೆ `app-debug.apk` ಇರುತ್ತದೆ.
4. APK ಅನ್ನು ಫೋನ್‌ನಲ್ಲಿ ತೆರೆಯಿರಿ ಮತ್ತು install ಮಾಡಿ. ಇದು debug-signed APK; Play Store release ಗೆ ಪ್ರತ್ಯೇಕ release signing ಬೇಕು.

ಈ ಪರಿಸರದಲ್ಲಿ Android SDK/build tools ಇಲ್ಲದ ಕಾರಣ ಇಲ್ಲಿ ನೇರವಾಗಿ APK binary ನಿರ್ಮಿಸಿ ಪರೀಕ್ಷಿಸಲು ಸಾಧ್ಯವಾಗಿಲ್ಲ. Workflow APK ಅನ್ನು GitHub-hosted build machine ನಲ್ಲಿ ತಯಾರಿಸುತ್ತದೆ; ಯಶಸ್ಸು build log ಮೇಲೆ ಅವಲಂಬಿತ.
