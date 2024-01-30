# Development

## Prerequisites

For Android:

- Android Studio
- SDK files (if not included)
- JDK 17
   - For Android development, it's often simplest to use the JDK included in Android Studio. The easiest way to do this is to set your `JAVA_HOME` to the JDK location (which is `/Applications/Android\ Studio.app/Contents/jbr/Contents/Home/` on macOS for example).

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

## Creating an Android release

Hopefully the current release will continue to work with future versions of ODK Collect. At present we are passively updating the app as needed. Collaborators are welcome to continue to develop the app and to create new releases.

**Prerequisites**: You will need to install both a JDK and the Android SDK to build a release. The easiest way to install Android is to download [Android Studio](https://developer.android.com/studio/) and import the project in `Android` into it. This takes care of downloading the correct Android dependencies for you.

To create a release:

1. Update the `versionCode` and `versionName` in `Android/app/build.gradle` for the release. `versionCode` should be any number higher than the current value.
1. Commit the changes:
```bash
git add Android/app/build.gradle
git commit -m "Update versionName and versionCode"
```
1. Tag the latest commit and push the changes:
```bash
git tag <versionName>
   git push
   git push --tags
   ```
   1. Run `./import-device-sdks.sh` and follow any instructions to make sure device SDKs are setup correctly
   1. Run `./build-android-release.sh` to build the signed release APK
   1. Navigate to https://github.com/chrissyhroberts/ODK_Fingerprints_Mantra/tags, click your new tag, click "Edit tag"
   1. Set the `versionName` as the "Release title", attach the signed release APK and hit "Publish release" 🚢

   ## Creating a CLI release

   **Prerequisites**: You will need to install a JDK

   To create a release:

   1. Tag the latest commit and push:
   ```bash
   git tag <versionName>
      git push --tags
      ```
      1. Run `./build-cli-release.sh` to package the CLI
      1. Navigate to https://github.com/chrissyhroberts/ODK_Fingerprints_Mantra/tags, click your new tag, click "Edit tag"
      1. Set the "Release title", attach the `.zip` package and hit "Publish release" 🚢
