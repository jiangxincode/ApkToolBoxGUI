package edu.jiangxin.apktoolbox.file.password.recovery.checker;

import edu.jiangxin.apktoolbox.file.password.recovery.exception.UnknownException;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
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
        return "ZIP Checker";
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
        try (ZipFile zFile = new ZipFile(file)) {
            zFile.setCharset(StandardCharsets.UTF_8);
            String dest = file.getAbsolutePath().replace(".zip", "Tmp" + File.separator + Thread.currentThread().getId());
            File destDir = new File(dest);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            if (zFile.isEncrypted()) {
                zFile.setPassword(password.toCharArray());
            }
            zFile.extractAll(dest);
            result = true;
        } catch (ZipException e) {
        } catch (IOException e) {
            throw new UnknownException(e);
        }
        return result;
    }
}
