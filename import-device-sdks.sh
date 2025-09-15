##!/usr/bin/env bash

set -e

mkdir -p sdks
rm -rf sdks/*

# Copy MFS100 dependencies
if [ -f MFS100-Android-SDK-9.0.3.2.zip ]; then
  unzip MFS100-Android-SDK-9.0.3.2.zip -d sdks/mfs100

  cp sdks/mfs100/MFS100-Android-SDK-9.0.3.2/libs/mantra.mfs100.jar \
    Android/mantramfs100/libs
  cp -r sdks/mfs100/MFS100-Android-SDK-9.0.3.2/jniLibs \
    Android/mantramfs100/src/main/
fi

# Copy BioMiniSDK dependencies
if [ -f "BioMiniSDK for Android_v2.1.0.380.zip" ]; then
  unzip "BioMiniSDK for Android_v2.1.0.380.zip" -d sdks/biomini
  cp "sdks/biomini/BioMiniSDK for Android_v2.1.0.380/sample/Suprema380/libBioMini/libBioMini.aar" Android/biomini-aar/
elif [ -f "libBioMini-v2.1.0.380.aar" ]; then
  cp "libBioMini-v2.1.0.380.aar" Android/biomini-aar/libBioMini.aar
fi

# Copy Aratek dependencies
if [ -f "Aratek TrustFinger SDK For Android v3.1.0.4_2023.12.14.zip" ]; then
  unzip "Aratek TrustFinger SDK For Android v3.1.0.4_2023.12.14.zip" -d sdks/aratek
  
  cp "sdks/aratek/Aratek TrustFinger SDK For Android v3.1.0.4_2023.12.14/libs/TrustFinger_v3.1.0.4.jar" \
    Android/aratek/libs
  cp -r "sdks/aratek/Aratek TrustFinger SDK For Android v3.1.0.4_2023.12.14/libs/arm64-v8a" \
    Android/aratek/src/main/jniLibs/arm64-v8a
  cp -r "sdks/aratek/Aratek TrustFinger SDK For Android v3.1.0.4_2023.12.14/libs/armeabi-v7a" \
    Android/aratek/src/main/jniLibs/armeabi-v7a
fi
