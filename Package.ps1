$CURRENT_DIR = Split-Path -Parent $MyInvocation.MyCommand.Definition
$TARGET_DIR = "$CURRENT_DIR/target"
$RELEASE_JAR_FILE_Filter = 'APKToolBoxGUI-*.jar'
$RELEASE_JAR_FILE = Get-ChildItem -Path "$TARGET_DIR" -Filter $RELEASE_JAR_FILE_Filter

$RELEASE_DIR = "$TARGET_DIR/release"
if (Test-Path $RELEASE_DIR) {
    Remove-Item $RELEASE_DIR -recurse
}
If (!(Test-Path $RELEASE_DIR)) {
    New-Item -ItemType Directory -Force -Path $RELEASE_DIR
}

Copy-Item $RELEASE_JAR_FILE.FullName "$RELEASE_DIR/APKToolBoxGUI.jar"

$VERSION = $RELEASE_JAR_FILE.BaseName.Replace("APKToolBoxGUI-", "").Replace(".jar", "")

Copy-Item "$CURRENT_DIR/ApkToolBoxGUI.bat" $RELEASE_DIR
Copy-Item "$CURRENT_DIR/ApkToolBoxGUI.sh" $RELEASE_DIR

Compress-Archive -Path "$RELEASE_DIR\*" -DestinationPath "$TARGET_DIR/APKToolBoxGUI-$VERSION.zip" -Force