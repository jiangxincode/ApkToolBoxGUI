package edu.jiangxin.apktoolbox.file.password.recovery.checker;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.BufferedInputStream;
import java.io.IOException;

public final class SevenZipChecker extends FileChecker {
    private static final boolean DEBUG = false;

    public SevenZipChecker() {
        super();
    }

    @Override
    public String[] getFileExtensions() {
        return new String[]{"7z"};
    }

    @Override
    public String getFileDescription() {
        return "*.7z";
    }

    @Override
    public String getDescription() {
        return "7Zip Checker";
    }

    @Override
    public boolean prepareChecker() {
        return true;
    }

    @Override
    public boolean checkPassword(String password) {
        if (DEBUG) {
            logger.info("checkPassword: " + password);
        }
        boolean result = false;

        try (SevenZFile sevenZFile = SevenZFile.builder().setFile(file).setPassword(password.toCharArray()).get()) {
            SevenZArchiveEntry entry = sevenZFile.getNextEntry();
            while (entry != null) {
                try (BufferedInputStream bis = new BufferedInputStream(sevenZFile.getInputStream(entry))) {
                    byte[] buffer = new byte[1024];
                    int size = bis.read(buffer);
                    while (size != -1) {
                        size = bis.read(buffer);
                    }
                }
                entry = sevenZFile.getNextEntry();
            }
            result = true;
        } catch (IOException e) {
            if (DEBUG) {
                logger.error("IOException: ", e);
            }
        } catch (Exception e) {
            if (DEBUG) {
                logger.error("Exception: ", e);
            }
        }
        return result;
    }
}
