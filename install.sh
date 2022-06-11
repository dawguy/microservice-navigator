#!/usr/bin/env bash

curDir=`pwd`
FILE="msn"
SCRIPT="microservice_navigator.clj"

if test -f "$FILE"; then
    echo "$FILE already exists. Using existing $FILE"
else
    echo "Creating $FILE at $curDir/src/$SCRIPT"
    touch msn
    chmod 755 msn
    echo "bb $curDir/src/$SCRIPT" >> msn
fi

if test -f "/usr/local/bin/$FILE"; then
    echo "Replacing $FILE in /usr/local/bin"
else
    echo "Moving newly created $FILE to /usr/local/bin/$FILE"
fi

sudo mv $FILE /usr/local/bin/$FILE
