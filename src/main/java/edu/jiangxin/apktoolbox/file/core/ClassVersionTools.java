package edu.jiangxin.apktoolbox.file.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author jiangxin
 * @author 2018-09-09
 *
 */
public class ClassVersionTools {
    private static final Logger logger = LogManager.getLogger(ClassVersionTools.class);

    public static void getClassVersion(String pathname) {
        if (pathname == null) {
            logger.debug("filePath is null.");
            return;
        }
        File srcDirFile = new File(pathname);
        if (!srcDirFile.exists()) {
            logger.warn("Can't find the target file or directory: " + srcDirFile.getAbsolutePath());
            return;
        }
        Collection<File> arrayList = FileUtils.listFiles(srcDirFile, new String[]{"class"}, true);
        if (arrayList.isEmpty()) {
            logger.warn("Can't find class file.");
            return;
        }
        Iterator<File> it = arrayList.iterator();
        while (it.hasNext()) {
            File classFile = it.next();

            byte[] bVersion = new byte[8];
            try (FileInputStream fis = new FileInputStream(classFile);) {
                int ret = fis.read(bVersion, 0, 8);
                if (ret <= 0) {
                    logger.error("read version byte failed");
                }
            } catch (IOException e) {
                logger.error("Some io errors happened.");
            }

            String majorVersion = String.valueOf((bVersion[6] << 8 & 0xff00) | bVersion[7] & 0x00ff);
            String minorVersion = String.valueOf((bVersion[4] << 8 & 0xff00) | bVersion[5] & 0x00ff);

            logger.debug("majorVersion: " + majorVersion + "; minorVersion: " + minorVersion);

            logger.info("The version of " + classFile.getAbsolutePath() + " is: " + majorVersion + "." + minorVersion);
        }
    }

    /**
     *
     * @param srcDirString The directory of source
     * @param desDirString The directory of target
     */
    public static void modifyClassVersion(String srcDirString, String desDirString, byte[] newVersion) {
        File srcDirFile = new File(srcDirString);
        if (!srcDirFile.exists()) {
            logger.error("srcDirString isn't exist: " + srcDirFile.getAbsolutePath());
            return;
        }

        if (!srcDirFile.isDirectory()) {
            logger.error("srcDirString isn't a directory: " + srcDirFile.getAbsolutePath());
            return;
        }

        if (newVersion == null || newVersion.length != 4) {
            logger.error("The newVersion is error");
            return;
        }

        FileProcess.copyDirectory(srcDirString, desDirString);

        Collection<File> arrayList = FileUtils.listFiles(new File(desDirString), new String[]{"class"}, true);
        Iterator<File> it = arrayList.iterator();
        while (it.hasNext()) {

            File file = it.next();

            byte[] content = new byte[1024 * 1024];
            int len = 0;

            try (FileInputStream fis = new FileInputStream(file);) {
                len = fis.read(content);
            } catch (FileNotFoundException e) {
                logger.error("Can't find the file: " + file.getName());
            } catch (IOException e) {
                logger.error("Some io errors happened.");
            }

            for (int i = 0; i < 4; i++) {
                content[i + 4] = newVersion[i];
            }

            try (FileOutputStream fos = new FileOutputStream(file);) {
                fos.write(content, 0, len);
            } catch (FileNotFoundException e) {
                logger.error("Can't find the file: " + file.getName());
            } catch (IOException e) {
                logger.error("Some io errors happened.");
            }

            logger.info("Process file success." + file.getAbsolutePath());
        }
    }

}
