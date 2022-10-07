## Features

### Recover password

```
You can use this function only when you have the express authorization of opening and operating the encrypted file.

I am only providing a generic tool, you are the one who decides what to do with it and you should take the responsability of using it!
```

**Introduce**

* Support password recovery from `PDF/ZIP/RAR/7Z...` files
* Support password recovery from Office files like: `doc/docx/ppt/pptx/xls/xlsx`
* Multi-thread processing, high recovery efficiency
* Support progress showing
* Support brute force recovery and dictionary recovery (more methods will be supported in the future)
* Brute force recovery supports customized character set and password length
* Dictionary recovery supports customized dictionaries. The default dictionary currently covers various common download sites, gentlemen's sites, and other network resource passwords (if you find a password that is not covered in the dictionary, please provide feedback to help more people)

### Convert between different character encodings

**Introduce**

* Support conversion between hundreds of encoding formats such as `UTF-8`, `GB2312`, `GBK`, `Big5`
* Support batch conversion of multiple folders and files
* Support automatic identification of source file encoding, automatic identification adopts multi-engine scheme, high identification rate

### Convert between different OS types

**Introduce**

* Used to convert plain text files to and from DOS/Mac/Unix format

### Convert between Simplified Chinese and Traditional Chinese

### Calculate and Compare File Hash

**Introduce**

* Support calculate file hash with MD5/SHA1/SHA256/SHA384/SHA512/CRC32
* Support calculate string hash with MD5/SHA1/SHA256/SHA384/SHA512/CRC32
* Support compare two hash values
* Support compare two file
* Support file drag and drop

### Find duplicated files(Not Finished)

### Convert between time formats

**Introduce**

* Transform between timestamp and time
* Transform between different time zones
* Get current time

### Convert between color formats

**Introduce**

Convert between common color formats: `RGB`/`HEX`/`CMYK`/`HSB`(`HSV`)

### Color picker

**Introduce**

A useful little color picker that grabs the pixel under your mouse and transforms it into a number of different color formats. You can use the built-in magnifier to zoom in on your screen, click on a color value to copy it directly to the clipboard.

### Convert between different base

**Introduce**

* A base converter between Binary/Octal/Decimal/Hex
* Show result as long as you input
* Support calculate big number

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
