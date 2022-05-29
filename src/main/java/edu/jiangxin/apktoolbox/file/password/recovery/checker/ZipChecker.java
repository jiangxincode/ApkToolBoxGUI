package edu.jiangxin.apktoolbox.file.password.recovery.checker;

import edu.jiangxin.apktoolbox.file.password.recovery.exception.UnknownException;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.inputstream.ZipInputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class ZipChecker extends FileChecker {
    private static final boolean DEBUG = false;

    public ZipChecker() {
        super();
    }

    @Override
    public String[] getFileExtensions() {
        return new String[]{"zip"};
    }

    @Override
    public String getFileDescription() {
        return "*.zip";
    }

    @Override
    public String getDescription() {
        return "ZIP Checker(Not support Non-ASCII password)";
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
        byte[] readBuffer = new byte[4096];
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file), password.toCharArray(), StandardCharsets.UTF_8)) {
            while (zipInputStream.getNextEntry() != null) {
                while (zipInputStream.read(readBuffer) != -1) {
                }
            }
            result = true;
        } catch (ZipException e) {
            if (DEBUG) {
                logger.error("checkPassword", e);
            }
        } catch (IOException e) {
            throw new UnknownException(e);
        }
        return result;
    }
}
