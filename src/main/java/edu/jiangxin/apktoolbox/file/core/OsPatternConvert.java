package edu.jiangxin.apktoolbox.file.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 不同操作系统文件格式转换
 * 
 * @author jiangxin
 * @author 2018-09-09
 */
public class OsPatternConvert {

    private static final Logger logger = LogManager.getLogger(OsPatternConvert.class.getSimpleName());

    /**
     * 不同操作系统文件格式之间的转换.
     * <p style="text-indent:2em">
     * 转换函数的真正实现函数，其它转换函数必须调用此函数。
     * </p>
     * 
     * @param srcFileString             源文件的文件名
     * @param desFileString             目标文件的文件名
     * @param options:换行符，比如:\n,\r,\r\n
     */
    private static void convert(String srcFileString, String desFileString, String options) {

        // 临时文件的后缀名，尽量保证不会含有同名文件
        String special = ".OSPattenConvert.temp";
        // 如果源文件和目标文件相同（包括路径），则使用临时文件进行转换
        if (srcFileString.equals(desFileString)) {
            srcFileString = srcFileString + special;
            FileProcess.copyFile(desFileString, srcFileString);
        }
        File srcFileFile = new File(srcFileString);
        File desFileFile = new File(desFileString);

        // 判断源文件是否存在
        if (!srcFileFile.exists()) {
            System.out.println("源文件不存在:" + srcFileFile.getAbsolutePath());
            return;
        }
        // 判断目标文件是否存在
        if (!desFileFile.getParentFile().exists()) {
            boolean ret = desFileFile.getParentFile().mkdirs();
            if (!ret) {
                logger.error("mkdirs failed: " + desFileFile.getParentFile());
            }
        }

        // 仅支持UTF-8编码
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(srcFileFile), StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(desFileFile), StandardCharsets.UTF_8))) {
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                writer.write(temp);
                writer.write(options);
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        }

        // 如果存在临时文件，则删除
        if (srcFileString.equals(desFileString + special)) {
            boolean ret = srcFileFile.delete();
            if (!ret) {
                logger.error("delete tmp file failed: " + srcFileFile);
            }
        }
    }

    /**
     * 其它操作系统文件格式转换成Unix文件格式.
     * 
     * @param srcFileString 转换前的文件
     * @param desFileString 转换后的文件
     */
    private static void toUnix(String srcFileString, String desFileString) {
        convert(srcFileString, desFileString, "\n");
        logger.info("Success to convert {} to unix", srcFileString);
    }

    /**
     * 其它操作系统文件格式转换成Dos文件格式.
     * 
     * @param srcFileString 转换前的文件
     * @param desFileString 转换后的文件
     */
    private static void toDos(String srcFileString, String desFileString) {
        convert(srcFileString, desFileString, "\r\n");
        logger.info("Success to convert {} to dos", srcFileString);
    }

    /**
     * 其它操作系统文件格式转换成Mac文件格式.
     * 
     * @param srcFileString 转换前的文件
     * @param desFileString 转换后的文件
     */
    private static void toMac(String srcFileString, String desFileString) {
        convert(srcFileString, desFileString, "\r");
        logger.info("Success to convert {} to mac", srcFileString);
    }

    /**
     * Dos文件格式转换成Unix文件格式.
     * 
     * @param srcFileString 转换前的文件
     * @param desFileString 转换后的文件
     */
    public static void dos2Unix(String srcFileString, String desFileString) {
        toUnix(srcFileString, desFileString);
    }

    /**
     * Dos文件格式转换成Mac文件格式.
     * 
     * @param srcFileString 转换前的文件
     * @param desFileString 转换后的文件
     */
    public static void dos2Mac(String srcFileString, String desFileString) {
        toMac(srcFileString, desFileString);
    }

    /**
     * Unix文件格式转换成Dos文件格式.
     * 
     * @param srcFileString 转换前的文件
     * @param desFileString 转换后的文件
     */
    public static void unix2Dos(String srcFileString, String desFileString) {
        toDos(srcFileString, desFileString);
    }

    /**
     * Unix文件格式转换成Mac文件格式.
     * 
     * @param srcFileString 转换前的文件
     * @param desFileString 转换后的文件
     */
    public static void unix2Mac(String srcFileString, String desFileString) {
        toMac(srcFileString, desFileString);
    }

    /**
     * Mac文件格式转换成Unix文件格式.
     * 
     * @param srcFileString 转换前的文件
     * @param desFileString 转换后的文件
     */
    public static void mac2Unix(String srcFileString, String desFileString) {
        toUnix(srcFileString, desFileString);
    }

    /**
     * Mac文件格式转换成Dos文件格式.
     * 
     * @param srcFileString 转换前的文件
     * @param desFileString 转换后的文件
     */
    public static void mac2Dos(String srcFileString, String desFileString) {
        toDos(srcFileString, desFileString);
    }

    /**
     * 不同操作系统文件格式之间的转换.
     * <p style="text-indent:2em">
     * 从一个操作系统文件格式转向另一个，转换模式有patter指定，pattern的格式为：
     * </p>
     * <p style="text-indent:4em">
     * os_a2os_b os_a和os_b的可能取值为linux/dos/windows/mac/unix/bsd等
     * </p>
     * 
     * @param srcFileString 转换前的文件
     * @param desFileString 转换后的文件
     * @param pattern       转换模式
     */
    public static void osFileConvert(String srcFileString, String desFileString, String pattern) {

        // 允许输入大写字母格式的转换模式
        pattern = pattern.toLowerCase();
        // 替换pattern中的to，防止误输入
        pattern = pattern.replace("to", "2");
        // 由于linux和unix文件格式相同，所以直接用unix替换linux，但会产生unix2unix类型
        pattern = pattern.replace("linux", "unix");
        // 由于bsd和unix文件格式相同，所以直接用unix替换linux，但会产生unix2unix类型
        pattern = pattern.replace("bsd", "unix");
        // 由于windows和dos文件格式相同，所以直接用unix替换linux，但会产生dos2dos类型
        pattern = pattern.replace("windows", "dos");
        boolean isToUnix = "2unix".equals(pattern) || "mac2unix".equals(pattern) || "dos2unix".equals(pattern)
                || "unix2unix".equals(pattern);
        boolean isToMac = "2mac".equals(pattern) || "dos2mac".equals(pattern) || "unix2mac".equals(pattern);
        boolean isToDos = "2dos".equals(pattern) || "mac2dos".equals(pattern) || "unix2dos".equals(pattern)
                || "dos2dos".equals(pattern);

        if (isToUnix) {
            toUnix(srcFileString, desFileString);
        } else if (isToDos) {
            toDos(srcFileString, desFileString);
        } else if (isToMac) {
            toMac(srcFileString, desFileString);
        } else {
            System.err.println("Error input,can't convert!");
        }
    }

    /**
     * 不同操作系统文件格式之间的转换.
     * <p style="text-indent:2em">
     * 从一个操作系统文件格式转向另一个，转换模式有patter指定，pattern的格式为：
     * </p>
     * <p style="text-indent:4em">
     * os_a2os_b os_a和os_b的可能取值为linux/dos/windows/mac/unix/bsd等
     * </p>
     * 
     * @param fileString 需要转换的文件
     * @param pattern    转换模式
     * @see #osFileConvert(String, String, String)
     */
    public static void osFileConvert(String fileString, String pattern) {
        osFileConvert(fileString, fileString, pattern);
    }

    /**
     * 不同操作系统文件格式之间的转换.
     * <p style="text-indent:2em">
     * 转换的对象为指定目录中的所有以后缀suffix指定的文件
     * </p>
     * 
     * @param srcDirString 要求转换的目录
     * @param desDirString 转化后要存放的目录
     * @param pattern      转换模式
     * @see #osFileConvert(String, String, String)
     * @param suffix 过滤特定文件后缀
     */
    public static void osDirConvert(String srcDirString, String desDirString, String pattern, String suffix) {
        File srcDirFile = new File(srcDirString);
        File desDirFile = new File(desDirString);
        if (!srcDirFile.exists()) {
            System.out.println("源目录不存在" + srcDirFile.getAbsolutePath());
        }
        String[] extensions = null;
        if (StringUtils.isNotEmpty(suffix)) {
            extensions = suffix.split(",");
        }
        Collection<File> arrayList = FileUtils.listFiles(srcDirFile, extensions, true);
        Iterator<File> it = arrayList.iterator();
        while (it.hasNext()) {
            File srcFileFile = it.next();
            // 得到源文件绝对地址
            String srcFileString = srcFileFile.getAbsolutePath();
            String temp = srcFileFile.getAbsolutePath().substring(srcDirFile.getAbsolutePath().toString().length());
            // 得到目标文件绝对地址
            String desFileString = desDirFile.getAbsolutePath() + temp;
            osFileConvert(srcFileString, desFileString, pattern);
        }

    }

    /**
     * 不同操作系统文件格式之间的转换.
     * <p style="text-indent:2em">
     * 转换的对象为指定目录中的所有文件
     * </p>
     * 
     * @param srcDirString 要求转换的目录
     * @param desDirString 转化后要存放的目录
     * @param pattern      转换模式
     * @see #osFileConvert(String, String, String)
     */
    public static void osDirConvert(String srcDirString, String desDirString, String pattern) {
        osDirConvert(srcDirString, desDirString, pattern, null);
    }

    /**
     * 不同操作系统文件格式之间的转换.
     * <p style="text-indent:2em">
     * 转换的对象为文件列表
     * </p>
     * 
     * @param files   要转换是文件列表
     * @param pattern 转换模式
     * @see #osFileConvert(String, String, String)
     */
    public static void osConvertFiles(List<File> files, String pattern) {
        Iterator<File> it = files.iterator();
        while (it.hasNext()) {
            osFileConvert(it.next().getAbsolutePath(), pattern);
        }
    }

}
