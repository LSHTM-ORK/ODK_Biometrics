#!/usr/bin/env sh

set -e

pushd CLI
    ./gradlew clean distZip
    mkdir -p keppel-cli
    unzip -d keppel-cli build/distributions/keppel-cli.zip
    cp assets/install.sh keppel-cli
    zip -r keppel-cli.zip keppel-cli
    rm -r keppel-cli
popd

echo ""
echo "Package at CLI/keppel-cli.zip"
