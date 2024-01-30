##!/usr/bin/env bash

set -e

mkdir -p sdks
rm -rf sdks/*

# Copy MFS100 dependencies
if [ ! -f MFS100-Android-SDK-9.0.3.2.zip ]; then
  echo ""
  echo "You need MFS100-Android-SDK-9.0.3.2.zip in the project! Download it at https://download.mantratecapp.com/Forms/UserDownload."
  echo ""
  exit 1
fi

unzip MFS100-Android-SDK-9.0.3.2.zip -d sdks/mfs100

cp sdks/mfs100/MFS100-Android-SDK-9.0.3.2/libs/mantra.mfs100.jar \
    Android/mantramfs100/libs
cp -r sdks/mfs100/MFS100-Android-SDK-9.0.3.2/jniLibs \
    Android/mantramfs100/src/main/

# Copy BioMiniSDK dependencies
if [ ! -f "BioMiniSDK for Android_v2.1.0.380.zip" ]; then
  echo ""
  echo "You need BioMiniSDK for Android_v2.1.0.380.zip in the project! Obtain it from Suprema or your biometric partner."
  echo ""
  exit 1
fi

unzip "BioMiniSDK for Android_v2.1.0.380.zip" -d sdks/biomini
cp "sdks/biomini/BioMiniSDK for Android_v2.1.0.380/sample/Suprema380/libBioMini/libBioMini.aar" Android/biomini-aar/
