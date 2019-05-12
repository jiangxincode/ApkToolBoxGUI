package edu.jiangxin.apktoolbox.text.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 文件或文件夹的简单处理
 * 
 * @author jiangxin
 * @author 2018-09-09
 */
public class FileProcess {

    private static final Logger logger = LogManager.getLogger(FileProcess.class);
    /**
     * The size of the buffer
     */
    final static int BUFFERSIZE = 1024 * 5;

    public static void main(String args[]) {
        // copyDirectory("test1", "test2");
        // copyFile("test1/test.txt","test2/test.txt");
        // deleteDir("temp/temp2");
        System.out.println(getString("README.md", "UTF-8", null, null));
    }

    /**
     * 复制文件
     * 
     * @param srcFileString
     * @param desFileString
     */
    public static void copyFile(String srcFileString, String desFileString) {
        File srcFileFile = new File(srcFileString);
        File desFileFile = new File(desFileString);

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFileFile));
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desFileFile))) {

            // 尚未进行覆盖判断 bad
            byte[] buffer = new byte[BUFFERSIZE];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            logger.error("copy file exception", e);
        }
        logger.info(() -> "Success to copy " + srcFileString + " to " + desFileString);
    }

    /**
     * 复制文件夹
     * 
     * @param srcDirString
     * @param desDirString
     * @throws IOException
     */
    public static void copyDirectory(String srcDirString, String desDirString) {
        File srcDirFile = new File(srcDirString);
        File desDirFile = new File(desDirString);
        if (!desDirFile.exists()) {
            boolean ret = desDirFile.mkdirs();
            if (!ret) {
                logger.error("mkidrs failed: " + desDirFile);
                return;
            }
        }

        File[] files = srcDirFile.listFiles();
        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String srcFileTemp = files[i].getAbsolutePath();
                String desFileTemp = desDirFile.getAbsolutePath() + File.separator + files[i].getName();
                copyFile(srcFileTemp, desFileTemp);
            }
            if (files[i].isDirectory()) {
                String srcDirTemp = files[i].getAbsolutePath();
                String desDirTemp = desDirFile.getAbsolutePath() + File.separator + files[i].getName();
                copyDirectory(srcDirTemp, desDirTemp);
            }
        }
        System.out.println("Success to copy " + srcDirString + " to " + desDirString);
    }

    /**
     * 移动文件
     * 
     * @param srcFileString
     * @param desFileString
     * @param isOverride
     * @throws IOException
     */
    public static void moveFile(String srcFileString, String desFileString, boolean isOverride) {
        File srcFileFile = new File(srcFileString);
        File desFileFile = new File(desFileString);
        if (isOverride) {
            if (desFileFile.delete()) {
                System.out.println("Success to delete desFile");
            }
        } else {
            return;
        }
        copyFile(srcFileString, desFileString);
        if (srcFileFile.delete()) {
            System.out.println("Success to delete srcFile");
        }

    }

    /**
     * 递归删除目录
     * 
     * @param dir
     * @return 返回是否删除成功
     */
    public static boolean deleteDir(String dir) {
        File dirFile = new File(dir);
        if (dirFile.isDirectory()) {
            String[] files = dirFile.list();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    String temp = dirFile.getAbsolutePath() + File.separator.toString() + files[i];
                    boolean success = deleteDir(temp);
                    if (!success) {
                        System.out.println("Something error!");
                        return false;
                    }
                }
            }
        }
        boolean ret = dirFile.delete();
        if (ret) {
            System.out.println("Success to delete " + dir);
        } else {
            System.out.println("Something error!");
        }
        return ret;
    }

    /**
     * 删除目录组
     * 
     * @param dirs
     * @return 返回是否删除成功
     */
    public static boolean deleteDirs(String[] dirs) {
        for (int i = 0; i < dirs.length; i++) {
            boolean isSuccessful = deleteDir(dirs[i]);
            if (isSuccessful == true) {
                System.out.println("成功删除目录：" + dirs[i]);
            }
        }
        return false;
    }

    public static String getString(String fileString, String encode, String startString, String endString) {
        StringBuilder content = new StringBuilder();
        String temp = null;
        File fileFile = new File(fileString);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileFile), encode))) {
            if (startString == null || endString == null) {
                while ((temp = reader.readLine()) != null) {
                    content.append(temp);
                    content.append(System.getProperty("line.separator"));
                }
            }
            while ((temp = reader.readLine()) != null) {
                if (!temp.contains(startString)) {
                    continue;
                }
                while ((temp = reader.readLine()) != null) {
                    if (temp.contains(endString)) {
                        continue;
                    }
                    content.append(temp);
                    content.append(System.getProperty("line.separator"));
                }
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        }

        return content.toString();
    }
}
