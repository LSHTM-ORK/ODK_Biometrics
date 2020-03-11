#!/usr/bin/env sh

./gradlew clean distZip
unzip build/distributions/keppel-cli.zip
assets/install.sh
rm -r keppel-cli