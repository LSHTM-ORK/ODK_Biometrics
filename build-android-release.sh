##!/usr/bin/env bash

set -e

pushd Android
    if [ ! -f Keystore ]; then
      echo ""
      echo "You need the Keystore file in Android/ to build a release!"
      echo ""
      exit 1
    fi

    if [ ! -f secrets.properties ]; then
      echo ""
      echo "You need the secrets.properties file in Android/ to build a release!"
      echo ""
      exit 1
    fi

    ./gradlew assembleRelease

    echo ""
    echo "APK at Android/app/build/outputs/apk/release/app-release.apk"
popd
