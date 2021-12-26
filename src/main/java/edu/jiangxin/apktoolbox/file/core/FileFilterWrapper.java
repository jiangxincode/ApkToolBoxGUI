package edu.jiangxin.apktoolbox.file.core;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件过滤器.
 * <p style="text-indent:2em">
 * 过滤出符合要求的目录或者文件夹。
 * </p>
 * 
 * @author jiangxin
 * @author 2018-09-09
 */
public class FileFilterWrapper {
    public static List<File> list(final File file, final String[] extensions, final boolean recursive) {
        List<File> files = new ArrayList<File>();
        if (!file.exists()) {
            System.out.println("Can't find the file!");
            return files;
        }

        if (file.isDirectory()) {
            files.addAll(FileUtils.listFiles(file, extensions, recursive));
        } else if (file.isFile()) {
            String fileName = file.getName();
            if (ArrayUtils.isEmpty(extensions)) {
                files.add(file);
            } else {
                for (String extension : extensions) {
                    if (fileName.endsWith(extension)) {
                        files.add(file);
                        break;
                    }
                }
            }
        }
        return files;
    }
}
