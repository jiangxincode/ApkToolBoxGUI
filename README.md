**NAME:** TextTools

**VERAION:** 0.18

**AUTHOR:** jiangxin

**WebSite:**

*binary:* https://sourceforge.net/projects/TestTools

*source:* https://github.com/jiangxincode/TestTools

*Blog:* http://blog.csdn.net/jiangxinnju

*Email:* jiangxinnju@163.com

**FUNCTION:**
文本处理工具包，您可以有两种方式使用：

	1. 作为一个文本处理小工具，以实现以下功能：

	+ 文本文件的编码格式批量转换（可以自行指定编码亦可以由本工具自行探测）；
	+ Unix、Mac、Dos等文件格式的批量互转；
	+ 批量文件重命名，批量文件备份和恢复；
	+ 批量源代码文件注释删除；
	+ 批量代码文件行数统计等等。

	2. 作为一个文本工具处理的JAVA封装包，由于上述功能都很好的以包的方式进行封装，您可以直接调用相关功能，另外您还可能使用以下功能：
	+ 目录的递归复制、移动、删除；
	+ 文件过滤（通过该功能您可以将某个目录下符合特定条件的文件进行操作，比如仅针对后缀名为.java的文件进行复制）；
	+ 随机数据生成，可看做是Java官方Random类的扩充。目前已经实现：
		+ 随机英文输出（自定义大小写，自定义固定长度还是随机长度）；
		+ 随机ASCII字符，随机扩展ASCII字符，随机可打印ASCII字符输出；
		+ 随机中文输出（自定义固定长度还是随机长度）；
		+ 随机Eamil地址输出，随机手机号码输出，随机日期输出等。

说明：由于使用的是Java进行编程，所以具有平台通用性，您可以同时在Mac,Windows,Linux/Unix等平台上以上述两种方式使用。如果您想以第一种方式对文件进行处理，可以参考下面的用法说明；如果您想以第二种方式使用，您所下载的压缩包里应该有该工具的example和详细的API文档，如果没有请您到下面提供的网站中重新下载。

License:
---
	GPLV3 http://www.gnu.org/licenses/gpl-3.0.txt
---

Usage:
---
	java -jar TestTools [option] filename1 [filename2...]
	-f The encoder of your file.If you don't know the econder,we will try to detect auto.However we can't ensure the validity!
	-t The encoder you want to convert
	-os pattern Convert your file from one os patter to another.pattern includes:dos2mac dos2unix dos2linux mac2dos mac2unix, etc.
	-b Backup your file when Convert.It's recommended!
	-r Recovey your file..Can't use with other options!
	-suffix 文件的后缀名 过滤特定后缀的文件进行处理，比如-suffix .txt
	-sum 统计代码的行数
	-h Display this usage.
	filename1[,filename2...] The file that you want to convert,at most one file.
---

For example:
---
	java -jar TestTools -f GBK -t UTF-8 test.txt
	java -jar TestTools  -dos2unix test.txt
	java -jar TestTools -r test.txt
---

日志：
---
	+ Version 0.01 搭建起基本框架，实现基本功能，包括：备份、恢复文件，基本编码转换，显示帮助文档，文本编码检测，DOS/Unix文件格式互转
	+ Version 0.02 为每个源文件添加了说明信息，去除了部分冗余代码，提高了程序的可读性。
	+ Version 0.03 为FileProcess类添加了删除目录的函数，便于以后扩展。
	+ Version 0.04 实现代码统计功能。
	+ Version 0.05 为每个源文件添加了说明信息，去除了部分冗余代码，提高了程序的可读性。
	+ Version 0.06 实现简单的文件重命名功能。
	+ Version 0.07 Apache commons-io包的依赖，使用java自带的类库代替，同时统一了函数接口。提高了系统效率。优化了程序结构。
	+ Version 0.08 这是一次较为重大的升级，主要变动为：将不同操作系统之间文件格式进行转变的逻辑判断部分由主类转入OSPatternConvert；OSPatternConvert类实现了更好地封装，提供了更多函数功能，实现批量文件转换，鲁棒性更好，系统结构更加优良；同时修复了fileFilter类中的部分bug。
	+ Version 0.09 将备份功能从Mail类中分离，单列为一类，以实现结构优化和之后的程序扩展；进一步优化了Main的结构。
	+ Version 0.10 将恢复功能从Mail类中分离，单列为一类，以实现结构优化和之后的程序扩展；进一步优化了Main的结构。
	+ Version 0.11 优化了Main类和OSPatternConvert类。
	+ Version 0.12 优化了Main类和检码转码部分。至此Main部分优化大体完成，实现了程序的可读性。
	+ Version 0.13 实现了消除注释功能。但是此部分编码识别等部分还存在一些bug。此部分代码参考了一些网友的代码。
	+ Version 0.14 实现了提示信息的文档化。
	+ Version 0.15 添加随机数据生成功能，能够实现基本的随机数据生成，包括随机英文输出（自定义大小写，自定义固定长度还是随机长度），随机ASCII字符，随机扩展ASCII字符，随机可打印ASCII字符输出，随机中文输出（自定义固定长度还是随机长度），随机Eamil地址输出，随机手机号码输出，随机日期输出等，可看做是Java官方Random类的扩充。
	+ Version 0.16 更新的文档说明。
	+ Version 0.16 将所有代码文件添加简单注释。优化了程序结构。
	+ Version 0.17 对文件结构进行了大调整，进一步增加了说明注释，修改了OSPatternConvert类的部分方法。
	+ Version 0.18 利用Markdown重写了README.MD
	+ Version 0.19 修改了此README.md文件
	+ Version 0.20 使用junit替换原手工测试
---

将要实现的功能：
---
	+ 增强系统鲁棒性
	+ 随机身份证号码输出
	+ 随机姓氏输出
	+ 去除不必要的文档信息输出，如果必要，该用xml方式输出，便于扩展
	+ 使用junit重新进行单元测试
---