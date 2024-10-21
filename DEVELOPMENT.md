# Development

## Prerequisites

For Android:

- Android Studio
- SDK files (if not included)
- JDK 17

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
1. [Build a release APK](#buiding-a-signed-apk)
1. Navigate to https://github.com/LSHTM-ORK/ODK_Biometrics/tags, click your new tag, click "Edit tag"
1. Set the `versionName` as the "Release title", attach the signed release APK and hit "Publish release" 🚢

### Builing a signed APK

**Prerequisites**: You will need to install both the Android SDK to build a release. The easiest way to install Android is to download [Android Studio](https://developer.android.com/studio/) and import the project in `Android` into it. This takes care of downloading the correct Android dependencies for you.

1. Make sure you have the project keystore and a `secrets.properties` set up. If you need to create new ones:
   1. Open Android Studio and click "Build" > "Generate Signed Bundle / APK"
   1. Choose APK and click "Next"
   1. Click "Create new..."
   1. Enter the details for the keystore making sure you take a note of the password, the key alias and the key password
   1. Click "OK" and then "Cancel" the signing process (this flow is just the easiest way to generate a keystore)
   1. Create a `secrets.properties` in `Android` with the keystore details:
      ```
      KEYSTORE=<path to keystore relative to `Android/app`>
      KEYSTORE_PASSWORD=<keystore password>
      KEY_ALIAS=<key alias>
      KEY_PASSWORD=<key password>
      ```
1. Run `./import-device-sdks.sh` and follow any instructions to make sure device SDKs are setup correctly
1. Run `./build-android-release.sh` to build the signed release APK

### Adding analytics

The app can optionally log analytics events if needed using Firebase Analytics. By default, the build does not include this integratin however. To set it up, create a new Firebase project and download the `google-services.json` file to `Android/app`. The `firebase-analytics` dependency will then be included in release builds.

You can log an event by calling `Analyitcs.log` from anywhere in the code.

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
