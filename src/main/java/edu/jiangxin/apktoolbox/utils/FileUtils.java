package edu.jiangxin.apktoolbox.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import java.util.TreeSet;

public class FileUtils {

    private static final Logger LOGGER = LogManager.getLogger(FileUtils.class.getSimpleName());

    public static final long ONE_KB = 1024;

    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(ONE_KB);

    public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);

    public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);

    public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);

    public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);

    public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);

    public static String sizeOfInHumanFormat(final File file) {
        BigInteger size = BigInteger.valueOf(file.length());
        String displaySize;

        if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = divide(size, ONE_EB_BI) + " EB";
        } else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = divide(size, ONE_PB_BI) + " PB";
        } else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = divide(size, ONE_TB_BI) + " TB";
        } else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = divide(size, ONE_GB_BI) + " GB";
        } else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = divide(size, ONE_MB_BI) + " MB";
        } else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = divide(size, ONE_KB_BI) + " KB";
        } else {
            displaySize = size + " bytes";
        }
        return displaySize;
    }

    public static String lastModifiedInHumanFormat(File file) {
        long lastModified = file.lastModified();
        return DateUtils.millisecondToHumanFormat(lastModified);
    }

    public static double divide(BigInteger size, BigInteger one_bi) {
        BigDecimal decimalSize = BigDecimal.valueOf(size.doubleValue())
                .divide(BigDecimal.valueOf(one_bi.doubleValue()), 2, BigDecimal.ROUND_HALF_UP);
        return decimalSize.doubleValue();
    }

    public static String getCanonicalPathQuiet(File file) {
        if (file == null) {
            LOGGER.warn("getCanonicalPathQuiet failed: file is null");
            return null;
        }
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            LOGGER.error("getCanonicalPathQuiet failed: IOException");
            return null;
        }
    }


    public static Set<File> listFiles(final File file, final String[] extensions, final boolean recursive) {
        Set<File> files = new TreeSet<>();
        if (!file.exists()) {
            LOGGER.error("file does not exist: {}", file.getAbsolutePath());
            return files;
        }

        if (file.isDirectory()) {
            files.addAll(org.apache.commons.io.FileUtils.listFiles(file, extensions, recursive));
            return files;
        }
        if (file.isFile()) {
            String fileName = file.getName();
            if (ArrayUtils.isEmpty(extensions)) {
                files.add(file);
                return files;
            }
            for (String extension : extensions) {
                if (fileName.endsWith(extension)) {
                    files.add(file);
                    return files;
                }
            }
        }
        LOGGER.warn("file is not directory or file: {}", file.getAbsolutePath());
        return files;
    }
}


