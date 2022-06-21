![](Android/app/src/main/res/mipmap-xhdpi/ic_launcher.png)

# Keppel
[![CLI](https://github.com/chrissyhroberts/ODK_Fingerprints_Mantra/workflows/CLI/badge.svg)](https://github.com/chrissyhroberts/ODK_Fingerprints_Mantra/actions?query=workflow%3ACLI)

Allows fingerprints to be scanned as part of an [ODK Collect](https://opendatakit.org/software/odk/#odk-collect) form.



## System design

The novel biometrics system consists of two components. The first component is “Keppel”, a smartphone app designed to run on Google Android operating systems. This app provides an I/O interface between the ODK Collect app and an ANSI INCITS 378-2004 compliant electronic fingerprint reader/sensor device. The app has to be sideloaded (it isn't on play store yet).

A really important point here is that the system is not simply taking photographs of fingerprints. The data are stored as concise code which has a very 'lite' impact on the size of the data stored in ODK and also requires no use of attachments. The fingerprint data are captured as plain text that is stored and encrypted along with other ODK data.

The Keppel Smartphone app was designed using [Android Studio and Software Development Kit (SDK)](https://developer.android.com/studio). The initial version of the app works only with the low cost (<£50) Mantra MFS100 Biometric C-Type Fingerprint Scanner from [Mantra Softech Inc](www.mantratec.com), functionality for which was based on code templates provided within the [Mantra MFS100 Software Development Kit](https://download.mantratecapp.com/).

<p align="center"><img  src="imgs/mantra_img.jpeg" width="400"></p>


The app was designed with a view to making the addition of further biometric sensors relatively simple. A software ‘demo’ scanner is also included, and this allows users to test their fingerprint supported ODK forms without having a scanner connected.


## Security and Privacy

Please be aware that fingerprints (or any form of biometric data) are very sensitive personal data. Collecting them may help your programme or study but make sure to fully consider privacy and security concerns when doing so. Please consider carrying out a data protection impact assessment prior to data collection.

If you're collecting any personal information using ODK it would be a good idea to look into [encrypting forms](https://docs.opendatakit.org/encrypted-forms) and also to read ODK's [general documentation on security](https://docs.opendatakit.org/security-privacy/).



## Usage

### Install the Keppel App on your Android phone or tablet

Download the [latest release](https://github.com/LSHTM-ORK/ODK_Biometrics/releases/latest) and sideload the APK file on to your Android device

### Scanning fingerprints in ODK XLSform format

To setup a form to scan fingerprints the devices used for data collection will all need the app installed. It can be downloaded [here](https://github.com/chrissyhroberts/ODK_Fingerprints_Mantra/releases). The app integrates with ODK Collect's [External app widget](https://docs.opendatakit.org/form-question-types/#external-app-widget) using the `uk.ac.lshtm.keppel.android.SCAN` intent. An example [XML form](Example_ODK_form/form.xml) and [XLS Form](Example_ODK_form/XLS Form) are provided.

Forms should be added to your ODK system as usual, via ODK Central. 

To capture all the fingers of one hand, your XLS form would look like this.

<p align="center"><img  src="imgs/form_five_fingers.png"></p>

The images below show how this looks in ODK Collect. Clicking 'launch' in ODK Collect opens the external app. Pressing 'capture' activates the scanner. Once the template has been captured, the data are returned to ODK Collect as plain text (N.B. Here I'm using the dummy scanner)
<p float="left">
  <img src="/imgs/form_five_fingers_odk_collect.png" width="200" />
  <img src="imgs/keppel_app_ss_1.png" width="200" /> 
  <img src="imgs/keppel_app_ss_2.png" width="200" />
  <img src="imgs/keppel_app_ss_3.png" width="200" /> 
</p>

### ODK data downloads

When you download your data CSV file from ODK Central, your fingerprint templates will be stored as plain text in line with other data from the form. From here you can either test them one at a time, or use a script to automate batch processing. 

| <sub>SubmissionDate</sub>         | <sub>ID</sub> | <sub>scan1.-R.THUMB</sub>                     |  <sub>UUID</sub>                                  |
|------------------------|----|------------------------------------|-----------------------------------------|  
|<sub>2021-12-21T18:19:36.622Z</sub>|<sub>001</sub> |<sub>464d520020323000000000f60000013c016200c500c5010000006424808b00ca7664...</sub>|<sub>uuid:4f1f19e2-846a-42d7-aef2-0574474a993b</sub>|
|<sub>2021-12-21T19:25:32.123Z</sub>|<sub>002</sub> |<sub>464d5200203230000000009c0000013c016200c500c5010000003415408c00e0874c...</sub>|<sub>uuid:2382975e-52bb-4a6b-880f-49bc5c27e787</sub>|
|<sub>2021-12-22T09:14:09.431Z</sub>|<sub>003</sub> |<sub>464d520020323000000000ae0000013c016200c500c5010000006418409200cb7b...</sub>|<sub>uuid:f7a0ab80-9d46-423a-9e64-28a0b3fb7076</sub>|
|<sub>2021-12-22T12:00:11.682Z</sub>|<sub>004</sub> |<sub>464d520020323000000000d80000013c016200c500c501000000461f807f00eed71...</sub>|<sub>uuid:9d55c0a9-901a-4908-b553-8a43a0b4037e</sub>|



### Install the Keppel CLI on your workstation 

Unzip the keppel-cli.zip. Open a terminal and do the following to copy all required applications and libraries to your /usr/local/bin folder


```console
foo@bar:~$ cd keppel-cli
foo@bar:~$ ./install.sh 
```

Test the installation with

```console
foo@bar:~$ keppel
```
you should see the help dialog

```console
foo@bar:~$ keppel

Usage: keppel [OPTIONS] COMMAND [ARGS]...

Options:
  -h, --help  Show this message and exit

Commands:
  match  Match two hex encoded ISO fingerprint templates. Threshold used for
         matching is 40.0.

```


### Matching fingerprints

To match two (hex encoded) fingerprint templates run:

```console
foo@bar:~$ keppel match /path/to/first_template /path/to/second_template

15.386568130470566
```
The core function requires that each template is stored in a single line of its own text file. The default behaviour is to return the matching score for the two templates


#### Other commands

To see available commands type 

```console
foo@bar:~$ keppel match -h
```

From version 0.3, the following options are available


**-p**         
Treats TEMPLATE_ONE and TEMPLATE_TWO as plain text rather than file
This option is very useful for scripted analysis

Example [templates truncated] 

```console
foo@bar:~$ keppel match -p 464d520020323000000001080000013c016200c500c5... 464d520020323000000000f00000013c016200c500c...

15.386568130470566
```
  
**-ms**     
Return whether templates match along with score like "match_210.124"

**-m**   
Return whether templates match (either "match" or "mismatch")

**-t FLOAT**   
Threshold (score) to be used to determine whether templates are a match or mismatch

**-h, --help** 
Show this message and exit`




  

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
