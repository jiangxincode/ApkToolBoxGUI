# ApkToolBoxGUI

![Java CI with Maven](https://github.com/jiangxincode/ApkToolBoxGUI/workflows/Java%20CI%20with%20Maven/badge.svg)
[![Project Documentation](https://img.shields.io/badge/Aloys-Project%20Documentation-green.svg)](https://jiangxincode.github.io/ApkToolBoxGUI)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jiangxincode_ApkToolBoxGUI&metric=alert_status)](https://sonarcloud.io/dashboard?id=jiangxincode_ApkToolBoxGUI)
[![Code Climate Maintainability](https://api.codeclimate.com/v1/badges/fb8d289a4b0ee14f9d8b/maintainability)](https://codeclimate.com/github/jiangxincode/ApkToolBoxGUI/maintainability)
[![Coverage Status](https://coveralls.io/repos/github/jiangxincode/ApkToolBoxGUI/badge.svg?branch=master)](https://coveralls.io/github/jiangxincode/ApkToolBoxGUI?branch=master)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/19016/badge.svg)](https://scan.coverity.com/projects/jiangxincode-apktoolboxgui)
[![Release Version](https://img.shields.io/github/v/release/jiangxincode/ApkToolBoxGUI?include_prereleases&sort=semver)](https://github.com/jiangxincode/ApkToolBoxGUI/releases/latest)


APKToolBoxGUI is a handy tool for programmer with user-friendly GUI. 

It's a collection of common tools, encoding convert, timestamp convert, color picker and so on, furthermore there are some specialized tools for Android development(That is why it is named `ApkToolBoxGUI`).

## Why you should try

* Open source forever
* More powerful features
* Easier to use
* Update more frequently

## Features

### File

#### Convert between different character encodings

* 支持`UTF-8`,`GB2312`,`GBK`,`Big5`等上百种编码格式间的互相转换
* 支持多文件夹、多文件批量转换
* 支持源文件编码自动识别，自动识别采用多引擎方案，识别率高

Similar Tools:

* 元宝文件编码转换器: <https://www.cnblogs.com/yuanbao/archive/2008/01/30/1059065.html>
* UltraCodingSwitch: <http://www.ultrasofts.cn/>

![FileEncoding](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/FileEncoding_01.png)

#### Convert between different OS types

![OSType](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/OSType_01.png)

#### Convert between Simplified Chinese and Traditional Chinese

![SimpleTraditional](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/SimpleTraditional_01.png)

#### Check files digest

![CheckDigest](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/CheckDigest_01.png)

#### Find duplicated files(Not Finished)

![DuplicateFile](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/DuplicateFile_01.png)
![DuplicateFile](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/DuplicateFile_02.png)

Similar Tools:

RenameIt: <http://www.comicer.com/stronghorse/>

### Convert

#### Convert between timestamp and formatted time

![Timestamp](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/Timestamp_01.png)

#### Convert between color formats

Convert between common color formats: `RGB`/`HEX`/`CMYK`/`HSB`(`HSV`)

![ColorConvert](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/ColorConvert_01.png)

Similar Tools:

* <https://www.sioe.cn/yingyong/yanse-rgb-16/>
* <https://c.runoob.com/front-end/868/>
* <https://www.qtccolor.com/tool/hsv.aspx>
* <http://colorizer.org/>

#### Color picker

A useful little color picker that grabs the pixel under your mouse and transforms it into a number of different color formats. You can use the built-in magnifier to zoom in on your screen, click on a color value to copy it directly to the clipboard.

Similar Tools:

* ColorPix: <https://colorpix.en.softonic.com/>

![ColorPicker](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/ColorPicker_01.png)

#### Convert between different base

![BaseConvert](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/BaseConvert_01.png)

#### Convert between Chinese character and Unicode character

### Reverse

Using third-party tools to decompile package like jar, aar, war, apk, dex and so on.

Similar Tools:

* I18NTools: <https://github.com/jiangxincode/I18NTools>
* TextTools: <https://github.com/jiangxincode/TextTools>
* ApkToolBox(C#): <https://github.com/qtfreet00/ApkToolBox>
* ApkToolBox(PowerShell): <https://github.com/jiangxincode/ApkToolBox>
* APKDB(安卓逆向助手): <https://bitbucket.org/idoog/apkdb/downloads/>
* APKIDE(改之理): <http://hrtsea.com/15759.html>
* ApkToolkit: <https://www.52pojie.cn/thread-263925-1-1.html>
* Android Killer: <https://www.52pojie.cn/thread-319641-1-1.html>

| Name | Version | Website | License
| ------ | ------ | ------ | ------ |
| Apktool | v2.5.0 | <https://github.com/iBotPeaches/Apktool> | Apache 2.0 |
| GD-GUI | 1.6.6 | <http://jd.benow.ca> | GNU GPL v3 |
| JADX-GUI | v1.2.0 | <https://github.com/skylot/jadx> | Apache 2.0 |
| ApkSigner | 1.3 | <http://apk.aq.163.com/apkpack.do#download> | Apache 2.0 |
| AXMLPrinter3 | 0.0.1-SNAPSHOT | <https://github.com/jiangxincode/AXMLPrinter3> | Apache 2.0 |

### SnapShot

### Dumpsys

* adb shell dumpsys alarm

### Test

### I18N

#### Copy Items

Copy some `<string />` in strings.xml in the `value[.*]` directory of the [A directory] to the strings.xml in the `value[.*]` directory of the [B directory]. It is mainly used to merge translations into several code branches.

#### Replace Items

Replace some `<string />` in strings.xml in the `value[.*]` directory of the [A directory] to the strings.xml in the `value[.*]` directory of the [B directory]. It is mainly used to merge translations into several code branches.

#### Delete Items

Delete some `<string />` in strings.xml in the `value[.*]` directory of the [A directory]

#### Find the longest

Find the longest translation of a `<string />`. Primarily for finding cuts or line breaks.

## License

* Apache License V2.0 <http://www.apache.org/licenses/LICENSE-2.0>

## Contributing

Any contributing is welcomed.

1. Fork repository
2. Make changes
3. Ensure tests pass (or hopefully adding tests!)
4. Submit pull request/issue

* build binary: mvn clean package
* build project documentation: mvn clean package site

## Contributors

All contributors will be listed here.

* Jiangxin <jiangxinnju@gmail.com>
