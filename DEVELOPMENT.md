# Development

## Prerequisites

For Android:

- Android Studio
- SDK files (if not included)
- JDK 1.8 or 11 (for gradle builds)

For CLI development:

- JDK 11

## Android Setup

1. Install the SDK files:

   ```
   cd ODK_Biometrics/
   sh ./import-device-sdks.sh
   ```

   The script will raise an error if the necessary SDK(s) cannot be found. Place the SDK zip files in the root of the repository, if needed.

2. Open the `Android/` directory in Android Studio.

3. To run the project on an external test device, see Hardware Testing (below) and then select Run -> Run in Android Studio.

4. To build an APK via the command line (same process as CI):

   ```
   cd Android/
   java -version # verify 1.8 or 11
   ./gradlew clean
   ./gradlew build
   find . -name '*.apk'
   ```

## Hardware Testing

Before attempting to enable debugging, you may need to [enable Developer Options](https://www.digitaltrends.com/mobile/how-to-get-developer-options-on-android/) in the Android system settings.

For testing USB devices on real hardware, it's helpful to get Wi-Fi debugging connection via `adb`.

For Android 10 and below, this can be done [via the CLI](https://developer.android.com/tools/adb#wireless).

For Android 11 and above, there's a [pairing interface](https://developer.android.com/tools/adb#connect-to-a-device-over-wi-fi) in Android Studio.

## CLI Setup

1. To force a particular version of Java to be used, it can be helpful to set `JAVA_HOME` directly:

```
export JAVA_HOME=$(/usr/libexec/java_home -v11)
```

2. Run the `dev-install.sh` script to build and install the CLI:

```
cd CLI/
sh dev-install.sh
```

Depending on your setup, you may need to customize the script to wrap `assets/install.sh` with `sudo`.
