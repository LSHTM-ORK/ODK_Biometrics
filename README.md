# Keppel

## Creating an Android release

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
