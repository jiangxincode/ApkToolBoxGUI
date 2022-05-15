#!/bin/sh
set -x
CURRENT_DIR=`pwd`
TARGET_DIR=$CURRENT_DIR/target
RELEASE_JAR_FILE=`ls -al target/APKToolBoxGUI-*.jar | awk '{print $9}'`

RELEASE_DIR="$TARGET_DIR/release"

if [ -d $RELEASE_DIR ];
then
    rm -rf $RELEASE_DIR
fi

if [ ! -d $RELEASE_DIR ];
then
    mkdir -p $RELEASE_DIR
fi

cp $RELEASE_JAR_FILE $RELEASE_DIR/APKToolBoxGUI.jar

VERSION=${RELEASE_JAR_FILE#*APKToolBoxGUI-}
VERSION=${VERSION%*.jar}

cp $CURRENT_DIR/ApkToolBoxGUI.bat $RELEASE_DIR
cp $CURRENT_DIR/ApkToolBoxGUI.sh $RELEASE_DIR
cp $CURRENT_DIR/apktoolboxgui.properties $RELEASE_DIR
cp -r $CURRENT_DIR/tools $RELEASE_DIR

cd $RELEASE_DIR
zip -r APKToolBoxGUI-$VERSION.zip *
cd $CURRENT_DIR
mv $RELEASE_DIR/APKToolBoxGUI-$VERSION.zip $TARGET_DIR
