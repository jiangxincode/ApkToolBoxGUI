start copyright

NAME:
	TextTools
FUNCTION:
	编码转换工具
VERAION:
	1.0
AUTHOR:
	jiangxin
Blog:
	http://blog.csdn.net/jiangxinnju
Email:
	jiangxinnju@163.com

Copyright (c) 2014, jiangxin


All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
Neither the name of the jiangxin nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

end copyright

start usage
		Usage: convert [option] filename1 [filename2...]
			-f The encoder of your file.If you don't know the econder,we will try to detect auto.However we can't ensure the validity!
			-t The encoder you want to convert
			-dos2unix Convert your file from Dos to Unix.Can't use with -unix2dos!
			-unix2dos Convert your file from Unix to Dos.Can't use with -dos2unix!
			-b Backup your file when Convert.It's recommended!
			-r Recovey your file..Can't use with other options!
			
			-h Display this usage.
			filename1[,filename2...] The file that you want to convert,at most one file.
			 
		For example:
			convert -f GBK -t UTF-8 test.txt
			convert -dos2unix test.txt
			convert -r test.txt
end usage

start log

	Version 0.01 搭建起基本框架，实现基本功能，包括：
		备份、恢复文件
		基本编码转换
		显示帮助文档
		文本编码检测
		DOS/Unix文件格式互转
	Version 0.02 为每个源文件添加了说明信息，去除了部分冗余代码，提高了程序的可读性。
	Version 0.03 为FileProcess类添加了删除目录的函数，便于以后扩展。
	Version 0.04 实现代码统计功能。
	Version 0.05 为每个源文件添加了说明信息，去除了部分冗余代码，提高了程序的可读性。
	Version 0.06 实现简单的文件重命名功能。
	Version 0.07 Apache commons-io包的依赖，使用java自带的类库代替，同时统一了函数接口。提高了系统效率。优化了程序结构。
	Version 0.08 这是一次较为重大的升级，主要变动为：
		将不同操作系统之间文件格式进行转变的逻辑判断部分由主类转入OSPatternConvert；
		OSPatternConvert类实现了更好地封装，提供了更多函数功能，实现批量文件转换，鲁棒性更好，系统结构更加优良；
		同时修复了fileFilter类中的部分bug。
	Version 0.09 将备份功能从Mail类中分离，单列为一类，以实现结构优化和之后的程序扩展；进一步优化了Main的结构。
	Version 0.10 将恢复功能从Mail类中分离，单列为一类，以实现结构优化和之后的程序扩展；进一步优化了Main的结构。
	Version 0.11 优化了Main类和OSPatternConvert类。
	Version 0.12 优化了Main类和检码转码部分。至此Main部分优化大体完成，实现了程序的可读性。
	Version 0.13 实现了消除注释功能。但是此部分编码识别等部分还存在一些bug。此部分代码参考了一些网友的代码。
	Version 0.14 实现了提示信息的文档化。

end log

start future funtions
将要实现的功能：
	增强系统鲁棒性
end future funtions
	