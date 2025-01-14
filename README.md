![](Android/app/src/main/res/mipmap-xhdpi/ic_launcher.png)


# Keppel

## Biometrics Solutions for ODK Ecosystem Tools

[![Android](https://github.com/LSHTM-ORK/ODK_Biometrics/workflows/Android/badge.svg)](https://github.com/LSHTM-ORK/ODK_Biometrics/actions?query=workflow%3AAndroid)

[![CLI](https://github.com/LSHTM-ORK/ODK_Biometrics/workflows/CLI/badge.svg)](https://github.com/LSHTM-ORK/ODK_Biometrics/actions?query=workflow%3ACLI)

This project provides an Android app `Keppel` that interfaces with mobile data collection software of the ODK ecosystem and which allows ISO 19794-2 fingerprint templates to be scanned and/or validated as part of an [XLSForm](https://xlsform.org/). We also provide a second app, the `Keppel CLI`, a command line interface which is designed to be run on a computer workstation. Keppel CLI can compare two fingerprint templates and return a matching score and is primarily useful for _post-hoc_ quality assurance and audit. 




### Demo : Registration & Verification/Validation 

Please start by watching this video demonstration of registration and verification/validation of fingerprint templates.
[![Watch the video](https://img.youtube.com/vi/2q3iZiACFN4/hqdefault.jpg)](https://youtu.be/2q3iZiACFN4)  
Click the image to begin

### Demo : Core Functions in Detail

A more in-depth demonstration of the core functions of the software is also available on YouTube
[![Watch the video](https://img.youtube.com/vi/wTRbKZzGTLI/hqdefault.jpg)](https://youtu.be/wTRbKZzGTLI)  
Click the image to begin

## Validation 

This software has been evaluated in a formal scientific context and the results are published in the following study.

**Biometric linkage of longitudinally collected electronic case report forms and confirmation of subject identity: an open framework for ODK and related tools**  
<sub>Chrissy h Roberts, Callum Stott, Marianne Shawe-Taylor, Zain Chaudhry, Sham Lal 1 & Michael Marks  
_Front Digit Health_. 2023 Aug 4:5:1072331. eCollection 2023.</sub>
  
<sub>PMID: 37600479  PMCID: PMC10436742  DOI: [10.3389/fdgth.2023.1072331](https://doi.org/10.3389/fdgth.2023.1072331)</sub>




## Features

* Capture to XLSForm one or more `enrollment templates`<sup>A</sup> in an XLSForm
* Capture to XLSForm a National Institute of Standards and Technology Fingerprint Image Quality (NFIQ) value, a measure of fingerprint quality
* Scan a `verification template`<sup>B</sup> and compare template to one or more previously saved templates for a known individual
* Return a match probability score indicating likelihood of their being a match between a verification template and one or more enrollment template.
* Using XLSform design, constrain form progress, content or actions on basis of both NFIQ and match probability.
* Depending on form design, capture an `identification template`<sup>C</sup> to scan a modestly sized database of people, in order to identify the current person.
* Capture to XLSForm the verification or identification templates and verification NFIQ scores

  
<sup>A</sup> Enrollment Templates: Created when a user is initially registered in the system. These templates need to be of high quality to ensure reliable future matching.
<sup>B</sup> Verification Templates: Generated when a user attempts to verify their identity. These are compared against enrollment templates for a match.
<sup>C</sup> Identification Templates: Used in systems where one-to-many matching is required, such as law enforcement databases.

## Disclaimer

While we are confident that Keppel provides a robust framework for the integration of biometric data in the ODK ecosystem, please be aware that you use the software entirely at your own risk. We make no representations, warranties, or guarantees—express or implied—regarding the suitability, reliability, security, or accuracy of Keppel for your specific use case.

By using Keppel, you acknowledge and agree that:

* Data Protection: It is your responsibility to ensure compliance with all applicable data protection laws, including but not limited to the UK GDPR and the Data Protection Act 2018, when collecting, storing, or processing biometric data using this software.  
* No Liability: To the fullest extent permitted by law, we disclaim any and all liability for any loss, damage, or legal claims that may arise from the use, misuse, or inability to use Keppel in connection with biometric data collection or processing.  
* User Responsibility: You are solely responsible for conducting due diligence to assess the suitability of Keppel for your particular requirements, including evaluating the potential risks associated with handling sensitive biometric information.  
* No Endorsement: The availability of Keppel does not imply any endorsement of its fitness for the collection or processing of biometric data, particularly in jurisdictions where additional regulatory or ethical considerations may apply.  
* By continuing to use Keppel for biometric purposes, you accept these terms and conditions in full.  

## Security and Privacy

Please be aware that fingerprints (or any form of biometric data) are very sensitive personal data. Collecting them may help your programme or study but make sure to fully consider privacy and security concerns when doing so. Please consider carrying out a data protection impact assessment prior to data collection.

The data collected by Keppel are stored as concise textual code which has a very 'lite' impact on the size of the data stored in ODK and also requires no use of attachments. 

Whilst Keppel does not directly capture an image of the fingerprint, [recent research](https://arxiv.org/pdf/2201.06164) has shown that generative adversarial networks are able to accurately reconstruct an image of a fingerprint from the type of template data that are collected by Keppel and other ISO 19794-2 templates. This new development and the inherent risk that the system could be used for malicious purpose, underlines the need for extreme caution when handling and storing template data.

When validating fingerprints, a large number of templates, person identifiers and other data will be stored on Android handsets. It is of critical importance that you take steps to partition your work and minimise risk of data being exposed if one or more devices became compromised by malicious actors.

As a bare minimum...

* All Android devices should be **encrypted** and **password protected** with a long password or numeric pass-code that is **unique to each device**.
* **Android devices should be managed** by an organisation. Enumerators should not use their personal devices. 
* You should **partition your work** in to multiple forms. This minimises the risk that might emerge from having all study data on all devices. For instance, if you were working with ten schools, you should consider creating a separate version of your form for each school. This way, if a device being used in school A were to become compromised, the data being exposed would be constrained to school A.
* Use ODK central's **app user** controls to limit access to each partitioned form, allowing only specified users to download the template data.
  
## Compatibility with ODK Ecosystem Platforms

This platform should work with all platforms that are based on ODK. 

| <sub>Platform</sub>  | <sub>App & version</sub>         | <sub>Compatible with Releases <0.4.0</sub> |  <sub>Compatible with Releases >=0.4.0</sub>|
|----------------------|----------------------------------|--------------------------------------------|---------------------------------------------|
|<sub>ODK</sub>        |<sub>ODK Collect v2022.3</sub>    |<sub>YES</sub>                              | <sub>YES</sub>   
|<sub>KoBoToolbox</sub>|<sub>KoboCollect v2022.1.2</sub>  |<sub>YES</sub>                              | <sub>YES - No Method for select_one from existing rows</sub>   
|<sub>SurveyCTO</sub>  |<sub>SurveyCTO Collect v2.72</sub>|<sub>YES</sub>                              | <sub>UNTESTED</sub>   
|<sub>CommCare</sub>   |<sub>CommCare v8</sub>            |<sub>Only with Advanced Plan or higher</sub>| <sub>UNTESTED</sub>   
|<sub>Ona</sub>        |<sub>ODK COllect v2022.3</sub>    |<sub>YES</sub>                              | <sub>UNTESTED</sub>   

Versions 0.3 and lower worked only with the Mantra MFS100 Biometric C-Type Fingerprint Scanner from [Mantra Softech Inc](www.mantratec.com), functionality for which was based on code templates provided within the [Mantra MFS100 Software Development Kit](https://download.mantratecapp.com/).

## Biometric Scanner Device Compatibility

Keppel works with two devices at present, but full functionality is only possible with the Biomini Slim 3 and hardware modules based on this. 
We strongly recommend using the most recent release and a BioMini 3. Legacy users working with Mantra MDS100 should use release v0.3

| <sub>Scanner</sub> | <sub>Releases</sub> |<sub>Compatible Android Versions</sub> |<sub>Register Fingerprint Templates</sub>| <sub>Match Templates on PC </sub> | <sub>Match Templates on Android Device </sub> 
|--------------------|---------------------|-----------|-----------------------------------------|-----------------------------------|----------------------------------------------|
|Biomini Slim 3      | 0.4.2 and above     |12 - 15 | YES | YES | YES |
|Biomini Slim 3      | 0.4.0 - 0.4.1       |12 - 13 | YES | YES | YES |
|Mantra MFS100       | 0.3                 |12 - 15 | YES | YES | NO|

## Suppliers

* **Biomini Slim 3**
  * This device is available from a variety of stockists
  * We bought our test devices from Xperix - https://www.xperix.com/en/contents/detail.php?code=010109
  * Also any device using the Biomini Slim 3 Module (for instance products similar to this https://www.siasa.com/Biopad/pdf/250821_REV3.pdf)
  * Total cost for two devices including shipping and customs taxes was ~£250
 
  
* **Mantra MFS100**
  * We bought our test devices from Mantratec - https://www.mantratec.com/products/Fingerprint-Sensors/MFS100-Fingerprint-Scanner
  * There's also plenty of generic biometric readers that look suspiciously like the MFS100 on Amazon, but we haven't tested these.
  * We paid around £150 for two devices including shipping and customs taxes


## System design

The biometrics system consists of two components. The first component is “Keppel”, a smartphone app designed to run on Google Android operating systems. This app provides an I/O interface between the ODK Collect app and an ISO19794-2 compliant electronic fingerprint reader/sensor device. The app has to be sideloaded (it isn't on play store yet). The second component is a java script which allows the comparison of templates collected with Keppel on a PC. 

The Keppel Smartphone app was designed using [Android Studio and Software Development Kit (SDK)](https://developer.android.com/studio). 

The app was designed with a view to making the addition of further biometric sensors relatively simple. A software ‘demo’ scanner is also included, and this allows users to test their fingerprint supported ODK forms without having a scanner connected.

* As of v0.4.0, Keppel is able to perform registration and validation of templates
* Keppel is controlled exclusively ODK Collect via XLSForm design


## Usage

### Install the Keppel App on your Android phone or tablet

Download the [latest release](https://github.com/LSHTM-ORK/ODK_Biometrics/releases/latest) and sideload the APK file on to your Android device. If you are unsure of how to do this please ask for help from an information technology expert. Instructions for how to sideload on specific Android devices is widely available online, but differs between devices so we can't summarise here. Please note that sideloading any app is a potential security risk. Please refer to our open source in order to carry out your own risk assessment for sideloading. We strongly recommend that if asked, you allow your system to perform a Google Play Protect scan on our app before installing. 

### Scanning fingerprints in ODK XLSform format

The anticipated workflow is as follows

* Timepoint 1  
  * Register participants by scanning several templates
  * Store several templates and information about the person's identity and location to an ODK Entities dataset.
    
* Timepoint 2 and further timepoints  
  * Encounter participant, search for their name, age and location on list of entities.
  * Confirm their identity by validating one new fingerprint template against the several stored templates.
  * Collect further data ± updating entities


Example [XLS Forms](Example_ODK_form/) are provided for purposes of both [Registration](Example_ODK_form/person_registration.xlsx) and [Validation](Example_ODK_form/Person_follow_up.xlsx).


For a full list of actions that Keppel supports, see [here](ACTIONS.md).
An example XLSform providing a complete list of functions is provided [here](Example_ODK_form/All_Functions.xlsx)


### Quality Assurance and Threshold Values

**NFIQ** is a quality metric. It is a score between 1 (best) and 5 (worst). You should generally aim to accept templates which have NFIQ scores of 1 or 2. When using Keppel, you should generally constrain your forms to progress only when NFIQ <=3. You may need to scan an individual's finger several times in order to get a high quality template.   

Known Issue : Some individuals have poor quality fingerprints and it may be difficult to obtain NFIQ < 4. Sometimes it can help to scantly moisten the skin before scanning. This increases the surface contact between the scanner and skin.   

**Matching Score** is a unitless metric that describes how well two templates compare to one another. It is likely to be much higher when two high quality (NFIQ = 1) templates are compared. Your ability to validate an indentity on the basis of the matching score is therefore predicated on the quality of the templates that are stored during registration and read during validation.   

In order to confirm or reject the validation of an individual, you will need to constrain the matching score to an arbitrary **Threshold value**. In our validation study we found that it was very highly unlikely to obtain a matching score above 100 if the templates being validated were not obtained from the same finger. Whilst values above 40 are also likely to be true matches, we strongly encourage you to rescan any validation which obtains a score between 40 and 100. By rescanning, it is likely that in the case of a true match, you will eventually obtain a better matching score. We have constrained the example forms to matching scores >= 100. 

![fdgth-05-1072331-g002](https://github.com/user-attachments/assets/997f6b46-7806-46a2-8181-2f3d106ee6af)
_Figure (A). ROC analysis (Single Finger). The area under the ROC curve for any pair of fingerprint templates was 0.99. This analysis was based on 1,010 true positive and 1,010 true negative template pairs. (B) Assay performance variations by finger. There was a progressive (from thumb to pinkie) decrease in the average score [S] of a true positive template pair according to which finger was used. (C) ROC analysis (score summed across thumb, index and middle finger). The area under the ROC curve was 1.00, indicating a perfect delineation between true positives and true negatives. (D) Summed scores [∑S] of three-finger scanning among true positive and true negative groups._


### ODK data downloads

When you download your data CSV file from ODK Central, your fingerprint templates will be stored as plain text in line with other data from the form. From here you can either test them one at a time, or use a script to automate batch processing. 

| <sub>SubmissionDate</sub>         | <sub>ID</sub> | <sub>scan1.-R.THUMB</sub>                     |  <sub>UUID</sub>                                  |
|------------------------|----|------------------------------------|-----------------------------------------|  
|<sub>2021-12-21T18:19:36.622Z</sub>|<sub>001</sub> |<sub>464d520020323000000000f60000013c016200c500c5010000006424808b00ca7664...</sub>|<sub>uuid:4f1f19e2-846a-42d7-aef2-0574474a993b</sub>|
|<sub>2021-12-21T19:25:32.123Z</sub>|<sub>002</sub> |<sub>464d5200203230000000009c0000013c016200c500c5010000003415408c00e0874c...</sub>|<sub>uuid:2382975e-52bb-4a6b-880f-49bc5c27e787</sub>|
|<sub>2021-12-22T09:14:09.431Z</sub>|<sub>003</sub> |<sub>464d520020323000000000ae0000013c016200c500c5010000006418409200cb7b...</sub>|<sub>uuid:f7a0ab80-9d46-423a-9e64-28a0b3fb7076</sub>|
|<sub>2021-12-22T12:00:11.682Z</sub>|<sub>004</sub> |<sub>464d520020323000000000d80000013c016200c500c501000000461f807f00eed71...</sub>|<sub>uuid:9d55c0a9-901a-4908-b553-8a43a0b4037e</sub>|


## Keppel CLI
The Keppel CLI is a command line tool that allows you to validate any pair of templates on a PC. This may be useful for _post-hoc_ quality assurance processes. 

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


### Matching fingerprints on Keppel CLI

To match two (hex encoded) fingerprint templates run:

```console
foo@bar:~$ keppel match /path/to/first_template /path/to/second_template

15.386568130470566
```
The core function requires that each template is stored in a single line of its own text file. The default behaviour is to return the matching score for the two templates


#### Other commands in Keppel CLI

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


### Controlling the Keppel CLI with R

[R](https://www.r-project.org/) is our favourite application for data munging, analysis and statistics. R plays nicely with system tools and can be used to control the Keppel API. .

In R, we created a very simple wrapper function that runs the Keppel CLI and returns a match score. As with any R function, this can be applied to lists, arrays and tibbles (data frames) to perform batch actions. 


```r
##########################################################################
#Define a function that gets the match score between scans.
##########################################################################

fingerprint.score<-function(print1,print2)
{

  if(!is.na(print1) & !is.na(print2)){
    c<-system(
      command = str_c("keppel match -p ",print1," ",print2),
      intern = TRUE
    )}

  if(is.na(print1) | is.na(print2)){ c<-NA}
  message(c)
  c
}
```

On an off-the-shelf MacBook Pro with 2.3 GHz 8-Core Intel Core i9 and 32 GB RAM, it took approximately 71 seconds to process 200 template matching calls sequentially. 

```R
library(furrr)
library(future)

plan(sequential)

system.time(future_map2(
  .x = df$scan1,
  .y = df$scan2,
  .f = fingerprint.score))

user     system  elapsed 
101.276  16.056  71.427 
```

>   

Called in parallel using the _**furrr**_ and _**future**_ packages, the process is much faster

```R
library(furrr)
library(future)

plan(multisession, workers = 16)
system.time(future_map2(.x = df$scan1..R.THUMB,.y = df$scan2..R.THUMB.2,.f = fingerprint.score))

user   system  elapsed 
1.261  0.059   16.467 

```
Processing n templates using the parallel approach (16 cores) took

| <sub>n</sub>   |<sub>Cores</sub>| <sub>time (s)</sub> | <sub>time/call (s)</sub> |
|----------------|----------------|---------------------|--------------------------|
|<sub>200</sub>  | <sub>1</sub>   | <sub>68.6</sub>     | <sub>0.343</sub>         |
|<sub>400</sub>  | <sub>1</sub>   | <sub>140.2</sub>    | <sub>0.350</sub>         |
|<sub>200</sub>  | <sub>16</sub>  | <sub>16.5</sub>     | <sub>0.083</sub>         |
|<sub>400</sub>  | <sub>16</sub>  | <sub>30.86</sub>    | <sub>0.077</sub>         |
|<sub>1000</sub> | <sub>16</sub>  | <sub>25.0</sub>     | <sub>0.025</sub>         | 
|<sub>2000</sub> | <sub>16</sub>  | <sub>69.7</sub>     | <sub>0.034</sub>         |
|<sub>10000</sub>| <sub>16</sub>  | <sub>773.4</sub>    | <sub>0.077</sub>         |


## Why the name? 

This project was created by researchers at the [London School of Hygiene & Tropical Medicine](https://www.lshtm.ac.uk/) in collaboration with [getODK](https://getodk.org/). LSHTM's mission is to improve health and health equity in the UK and worldwide; working in partnership to achieve excellence in public and global health research, education and translation of knowledge into policy and practice. LSHTM's main building is situated on [Keppel Street](https://www.google.com/maps/place/Keppel+St,+London+WC1E+6DP/@51.5205239,-0.1325345,966m/data=!3m2!1e3!4b1!4m6!3m5!1s0x48761b2e05fb6be3:0xaf76c86ede9df86d!8m2!3d51.5205206!4d-0.1299596!16s%2Fg%2F11fy7f88f4!5m1!1e1?entry=ttu&g_ep=EgoyMDI0MTExMC4wIKXMDSoASAFQAw%3D%3D) in London's Bloomsbury district. The Keppel App and CLI are named after the street where you will find us, and not after the street's own namesake [Lord Augustus Keppel (1725-1786)](https://en.wikipedia.org/wiki/Augustus_Keppel,_1st_Viscount_Keppel).


## Funding & Ethics

Initial work on Keppel was funded by the UK Department of Health and Social Care using UK Aid funding managed by the NIHR (PR-OD-1017-20001). Ethical permission for elements of the validation work that handled fingerprint templates from living humans was granted by the London School of Hygiene & Tropical Medicine Observational Research Ethics Committee (Ref. 22562). Continuing development of Keppel has been funded by the LSHTM ODK Pay What You Can Scheme, and by anonymous donors. 

<p float="left">
  <img src="imgs/NIHR.png" width="250" /> 
  <img src="/imgs/DHSC.png" width="70" />
</p>

## Development

See [DEVELOPMENT.md].
