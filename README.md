# I18NTools

[deprecated] All feature has been replaced by ApkToolBoxGUI(https://github.com/jiangxincode/ApkToolBoxGUI)

## 功能描述

I18NTools是一组与Android国际化相关的小工具的集合。目前有如下功能：

* 将A目录下的`value[.*]`目录下的strings.xml中的某个`<string />`，全量拷贝/替换到B目录下的`value[.*]`目录下的strings.xml中。主要适用于将翻译回稿合入到若干个代码分支中。
* 找到某个`<string />`的最长翻译。主要适用于找到测试某些语言的截断或者换行问题。

## 使用说明

* JDK7+
* 使用Eclipse或者IDEA等工具导入此Maven项目，根据实际使用情况进行定制。如果由于某些原因无法使用Maven，请根据pom.xml中的依赖下载对应的jar并在IDE中添加依赖。

## 注意点

* 工具仅提供辅助，操作前需要备份对应文件，操作后需要人工校验修改情况。
* 如有建议可以在Github上提交Issues。