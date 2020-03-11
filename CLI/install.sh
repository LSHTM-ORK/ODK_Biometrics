#!/usr/bin/env sh

./gradlew clean distZip
unzip -d build/distributions build/distributions/keppel-cli.zip
cp -r build/distributions/keppel-cli /usr/local/bin
ln -sf /usr/local/bin/keppel-cli/bin/keppel-cli /usr/local/bin/keppel