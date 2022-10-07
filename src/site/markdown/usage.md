## How to use

Download the latest Release:

[![Release Version](https://img.shields.io/github/v/release/jiangxincode/ApkToolBoxGUI?include_prereleases&sort=semver)](https://github.com/jiangxincode/ApkToolBoxGUI/releases/latest)

There are two types of package:

* `APKToolBoxGUI-${VERSION}-with-JRE.zip` This package contains JRE already, you do not need to download JRE individually. This package can only work on Windows.
* `APKToolBoxGUI-${VERSION}-without-JRE.zip` This package does not contain JRE, if your PC has installed JRE(9+), this package is recommended. This package can work on all platforms(Windows/Linux/Mac).

### Recover password

**Breadcrumb Navigation**

`File->Recover File Password`

### Convert between different character encodings

**Breadcrumb Navigation**

`File->Convert Encoding`

### Convert between different OS types

**Breadcrumb Navigation**

`File->Convert OS Pattern`

### Convert between Simplified Chinese and Traditional Chinese

![SimpleTraditional](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/SimpleTraditional_01.png)

### Calculate and Compare File Hash

**Breadcrumb Navigation**

`File->Check Summary`

![CheckSum](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/Checksum_01.png)

### Find duplicated files(Not Finished)

![DuplicateFile](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/DuplicateFile_01.png)
![DuplicateFile](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/DuplicateFile_02.png)

### Convert between time formats

**Breadcrumb Navigation**

`Convert->Time`

![TimeConvert](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/TimeConvert_en.gif)

### Convert between color formats

![ColorConvert](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/ColorConvert_01.png)

### Color picker

![ColorPicker](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/ColorPicker_01.png)

### Convert between different base

**Breadcrumb Navigation**

`Convert->Base`

![BaseConvert](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/BaseConvert_en.gif)

### Convert between Chinese character and Unicode character

### Reverse

Using third-party tools to decompile package like jar, aar, war, apk, dex and so on.

**Dependencies**

| Name         | Version        | Website                                        | License    |
|--------------|----------------|------------------------------------------------|------------|
| Apktool      | v2.5.0         | <https://github.com/iBotPeaches/Apktool>       | Apache 2.0 |
| GD-GUI       | 1.6.6          | <http://jd.benow.ca>                           | GNU GPL v3 |
| JADX-GUI     | v1.2.0         | <https://github.com/skylot/jadx>               | Apache 2.0 |
| ApkSigner    | 1.3            | <http://apk.aq.163.com/apkpack.do#download>    | Apache 2.0 |
| AXMLPrinter3 | 0.0.1-SNAPSHOT | <https://github.com/jiangxincode/AXMLPrinter3> | Apache 2.0 |

### SnapShot(Android Develop)

### Dumpsys Alarm Info(Android Develop)

* adb shell dumpsys alarm

### Monkey Test(Android Develop)

### Copy internationalization items(Android Develop)

Copy some `<string />` in strings.xml in the `value[.*]` directory of the [A directory] to the strings.xml in the `value[.*]` directory of the [B directory]. It is mainly used to merge translations into several code branches.

### Replace internationalization items(Android Develop)

Replace some `<string />` in strings.xml in the `value[.*]` directory of the [A directory] to the strings.xml in the `value[.*]` directory of the [B directory]. It is mainly used to merge translations into several code branches.

### Delete internationalization items(Android Develop)

Delete some `<string />` in strings.xml in the `value[.*]` directory of the [A directory]

### Find the longest in internationalization items(Android Develop)

Find the longest translation of a `<string />`. Primarily for finding cuts or line breaks.
