# ApkToolBoxGUI

![Java CI with Maven](https://github.com/jiangxincode/ApkToolBoxGUI/workflows/Java%20CI%20with%20Maven/badge.svg)
[![Project Documentation](https://img.shields.io/badge/Aloys-Project%20Documentation-green.svg)](https://jiangxincode.github.io/ApkToolBoxGUI)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/dab038ef9ed04bf1b73bd0031422b814)](https://www.codacy.com/gh/jiangxincode/ApkToolBoxGUI/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jiangxincode/ApkToolBoxGUI&amp;utm_campaign=Badge_Grade)
[![Infer Check](https://img.shields.io/badge/Facebook-InferCheck-green)](https://github.com/jiangxincode/ApkToolBoxGUI/blob/infer-out/report.txt)
[![CodeQL Check](https://img.shields.io/badge/Github-CodeQL-yellow)](https://github.com/jiangxincode/ApkToolBoxGUI/security/code-scanning)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jiangxincode_ApkToolBoxGUI&metric=alert_status)](https://sonarcloud.io/dashboard?id=jiangxincode_ApkToolBoxGUI)
[![Code Climate Maintainability](https://api.codeclimate.com/v1/badges/fb8d289a4b0ee14f9d8b/maintainability)](https://codeclimate.com/github/jiangxincode/ApkToolBoxGUI/maintainability)
[![Coverage Status](https://coveralls.io/repos/github/jiangxincode/ApkToolBoxGUI/badge.svg?branch=master)](https://coveralls.io/github/jiangxincode/ApkToolBoxGUI?branch=master)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/19016/badge.svg)](https://scan.coverity.com/projects/jiangxincode-apktoolboxgui)
[![墨菲安全](https://img.shields.io/badge/%E5%A2%A8%E8%8F%B2%E5%AE%89%E5%85%A8-%E6%8A%A5%E5%91%8A-blue)](https://www.murphysec.com/pd/1523240128993079296/1523240128636563456)


APKToolBoxGUI is a handy tool for programmer with user-friendly GUI. 

It's a collection of common tools, encoding convert, timestamp convert, color picker and so on, furthermore there are some specialized tools for Android development(That is why it is named `ApkToolBoxGUI`).

## How to use

Download the latest Release: 

[![Release Version](https://img.shields.io/github/v/release/jiangxincode/ApkToolBoxGUI?include_prereleases&sort=semver)](https://github.com/jiangxincode/ApkToolBoxGUI/releases/latest)

There are two types of package:

* `APKToolBoxGUI-${VERSION}-with-JRE.zip` This package contains JRE already, you do not need to download JRE individually. This package can only work on Windows.
* `APKToolBoxGUI-${VERSION}-without-JRE.zip` This package does not contain JRE, if your PC has install JRE(9+), this package is recommended. This package can work on all platforms(Windows/Linux/Mac).

## Features

### Recover password

```
You can use this function only when you have the express authorization of opening and operating the encrypted file.

I am only providing a generic tool, you are the one who decides what to do with it and you should take the responsability of using it!
```

**Breadcrumb Navigation**

`File->Recover File Password`

**Introduce**

* Support password recovery from `PDF/ZIP/RAR/7Z...` files
* Support password recovery from Office files like: `doc/docx/ppt/pptx/xls/xlsx`
* Multi-thread processing, high recovery efficiency
* Support progress showing
* Support brute force recovery and dictionary recovery (more methods will be supported in the future)
* Brute force recovery supports customized character set and password length
* Dictionary recovery supports customized dictionaries. The default dictionary currently covers various common download sites, gentlemen's sites, and other network resource passwords (if you find a password that is not covered in the dictionary, please provide feedback to help more people)

### Convert between different character encodings

**Breadcrumb Navigation**

`File->Convert Encoding`

**Introduce**

* Support conversion between hundreds of encoding formats such as `UTF-8`, `GB2312`, `GBK`, `Big5`
* Support batch conversion of multiple folders and files
* Support automatic identification of source file encoding, automatic identification adopts multi-engine scheme, high identification rate

### Convert between different OS types

**Breadcrumb Navigation**

`File->Convert OS Pattern`

**Introduce**

* Used to convert plain text files to and from DOS/Mac/Unix format

### Convert between Simplified Chinese and Traditional Chinese

![SimpleTraditional](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/SimpleTraditional_01.png)

### Calculate and Compare File Hash

`File->Check Summary`

* Support calculate file hash with MD5/SHA1/SHA256/SHA384/SHA512
* Support file drag and drop

### Find duplicated files(Not Finished)

![DuplicateFile](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/DuplicateFile_01.png)
![DuplicateFile](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/DuplicateFile_02.png)

### Convert between timestamp and formatted time

![Timestamp](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/Timestamp_01.png)

### Convert between color formats

Convert between common color formats: `RGB`/`HEX`/`CMYK`/`HSB`(`HSV`)

![ColorConvert](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/ColorConvert_01.png)

### Color picker

A useful little color picker that grabs the pixel under your mouse and transforms it into a number of different color formats. You can use the built-in magnifier to zoom in on your screen, click on a color value to copy it directly to the clipboard.

![ColorPicker](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/ColorPicker_01.png)

### Convert between different base

![BaseConvert](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/BaseConvert_01.png)

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

## Communication Group

![QQ Group](https://raw.githubusercontent.com/wiki/jiangxincode/ApkToolBoxGUI/QQ_Group.png)
