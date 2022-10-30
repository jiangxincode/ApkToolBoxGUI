#!/bin/sh
set -x
CURRENT_DIR=`pwd`
TARGET_DIR=$CURRENT_DIR/target
RELEASE_JAR_FILE=`ls -al target/APKToolBoxGUI-*.jar | awk '{print $9}'`
VERSION=${RELEASE_JAR_FILE#*APKToolBoxGUI-}
VERSION=${VERSION%*.jar}
TMP_DIR="$TARGET_DIR/release"

# Builed $TARGET_DIR/APKToolBoxGUI-$VERSION-without-JRE.zip
if [ -d $TMP_DIR ];
then
    rm -rf $TMP_DIR
fi

if [ ! -d $TMP_DIR ];
then
    mkdir -p $TMP_DIR
fi

cp $RELEASE_JAR_FILE $TMP_DIR/APKToolBoxGUI.jar

cp $CURRENT_DIR/ApkToolBoxGUI.bat $TMP_DIR
cp $CURRENT_DIR/ApkToolBoxGUI.sh $TMP_DIR

cd $TMP_DIR
zip -r APKToolBoxGUI-$VERSION-without-JRE.zip *
cd $CURRENT_DIR
mv $TMP_DIR/APKToolBoxGUI-$VERSION-without-JRE.zip $TARGET_DIR


# Builed $TARGET_DIR/APKToolBoxGUI-$VERSION-with-JRE.zip
if [ -d $TMP_DIR ];
then
    rm -rf $TMP_DIR
fi

if [ ! -d $TMP_DIR ];
then
    mkdir -p $TMP_DIR
fi

if [ -d $TARGET_DIR/ApkToolBoxGUI-$VERSION-with-JRE ];
then
    rm -rf $TARGET_DIR/ApkToolBoxGUI-$VERSION-with-JRE.app
fi

cp $RELEASE_JAR_FILE $TMP_DIR/APKToolBoxGUI.jar

jpackage --input $TMP_DIR --type app-image --name ApkToolBoxGUI-$VERSION-with-JRE --main-jar APKToolBoxGUI.jar --dest $TARGET_DIR --verbose

cd $TARGET_DIR/ApkToolBoxGUI-$VERSION-with-JRE.app
zip -r $TARGET_DIR/APKToolBoxGUI-$VERSION-with-JRE.zip ./*
cd $CURRENT_DIR

if [ -d $TMP_DIR ];
then
    rm -rf $TMP_DIR
fi

if [ -d $TARGET_DIR/ApkToolBoxGUI-$VERSION-with-JRE.app ];
then
    rm -rf $TARGET_DIR/ApkToolBoxGUI-$VERSION-with-JRE.app
fi
