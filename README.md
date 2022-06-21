![](Android/app/src/main/res/mipmap-xhdpi/ic_launcher.png)

# Keppel
[![CLI](https://github.com/chrissyhroberts/ODK_Fingerprints_Mantra/workflows/CLI/badge.svg)](https://github.com/chrissyhroberts/ODK_Fingerprints_Mantra/actions?query=workflow%3ACLI)

Allows fingerprints to be scanned as part of an [ODK Collect](https://opendatakit.org/software/odk/#odk-collect) form.


## System design

The novel biometrics system consists of two components. The first component is “Keppel”, a smartphone app designed to run on Google Android operating systems. This app provides an I/O interface between the ODK Collect app and an ANSI INCITS 378-2004 compliant electronic fingerprint reader/sensor device. The app has to be sideloaded (it isn't on play store yet).

A really important point here is that the system is not simply taking photographs of fingerprints. The data are stored as concise code which has a very 'lite' impact on the size of the data stored in ODK and also requires no use of attachments. The fingerprint data are captured as plain text that is stored and encrypted along with other ODK data.

The Keppel Smartphone app was designed using [Android Studio and Software Development Kit (SDK)](https://developer.android.com/studio). The initial version of the app works only with the low cost (<£50) Mantra MFS100 Biometric C-Type Fingerprint Scanner from [Mantra Softech Inc](www.mantratec.com), functionality for which was based on code templates provided within the [Mantra MFS100 Software Development Kit](https://download.mantratecapp.com/).

![photo of the Mantra MFS100 device](imgs/mantra_img.jpeg)

## Security and Privacy

It's important to realize that fingerprints (or any form of biometric data) are very sensitive. Collecting them may help your programme or study but make sure to fully consider privacy and security concerns when doing so.

If you're collecting any personal information using ODK it would be a good idea to look into [encrypting forms](https://docs.opendatakit.org/encrypted-forms) and also to read ODK's [general documentation on security](https://docs.opendatakit.org/security-privacy/).

## Usage

### Scanning fingerprints in forms

To setup a form to scan fingerprints the devices used for data collection will all need the app installed. It can be downloaded [here](https://github.com/chrissyhroberts/ODK_Fingerprints_Mantra/releases). The app integrates with ODK Collect's [External app widget](https://docs.opendatakit.org/form-question-types/#external-app-widget) using the `uk.ac.lshtm.keppel.android.SCAN` intent. An example XML form can be found [here](docs/form.xml) and an XLS Form version can be found [here](docs/form.xlsx).

### Matching fingerprints

You can install the command line interface from matching fingerprints [here](https://github.com/chrissyhroberts/ODK_Fingerprints_Mantra/releases). 

To install the CLI, download the latest release zip file, unzip and then run

```./install.sh```

This will copy all required applications and libraries to your /usr/local/bin folder

To match two (hex encoded) fingerprint templates run:

```bash
keppel match /path/to/first_template /path/to/second_template
```
The core function requires that each template is stored in a single line of its own text file. 


#### Other commands

To see available commands type 

```keppel match -h```

From version 0.3, the following options are available


**-p**         
Treats TEMPLATE_ONE and TEMPLATE_TWO as plain text rather than file
This option is very useful for scripted analysis

Example [templates truncated] 

```keppel match -p 464d520020323000000001080000013c016200c500c5... 464d520020323000000000f00000013c016200c500c...```
  
**-ms**     
Return whether templates match along with score like "match_210.124"

**-m**   
Return whether templates match (either "match" or "mismatch")

**-t FLOAT**   
Threshold (score) to be used to determine whether templates are a match or mismatch

**-h, --help** 
Show this message and exit`




  

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
