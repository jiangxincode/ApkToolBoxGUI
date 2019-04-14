package edu.jiangxin.apktoolbox.text.core;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;

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

    ArrayList<File> arrayList = new ArrayList<File>();

    /**
     * 实现对指定文件或者目录的过滤
     * 
     * @param name   文件或者目录名称
     * @param suffix 指定后缀
     * @return 返回一个文件列表
     */
    public ArrayList<File> list(String name, String suffix) {

        try {
            File file = new File(name);
            if (!file.exists()) {
                System.out.println("Can't find the file!");
                return null;
            }

            if (file.isDirectory()) {
                // 如果是目录的话，将该目录下符合条件的文件加入ArrayList
                File[] files = file.listFiles(getFileExtensionFilter(suffix));
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isFile()) {
                            arrayList.add(files[i]);
                        }

                    }
                }
                // 过滤出所有的目录
                files = file.listFiles(getDirectoryFilter());
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        list(files[i].toString(), suffix);
                    }
                }
            } else {
                // 如果是文件的话，直接将该文件加入ArrayList
                arrayList.add(file);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return arrayList;

    }

    private FilenameFilter getFileExtensionFilter(final String extension) {
        if (extension == null) {
            // 没有指定后缀，则返回该目录下所有的文件
            return new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    return true;
                }
            };
        } else {
            // 指定后缀，则返回该目录下拥有这些后缀的文件
            return new FilenameFilter() {
                @Override
                public boolean accept(File file, String name) {
                    boolean ret = name.endsWith(extension);
                    return ret;
                }
            };
        }

    }

    private FileFilter getDirectoryFilter() {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
    }
}
