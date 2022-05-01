package edu.jiangxin.apktoolbox.file.crack.cracker;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.nio.charset.StandardCharsets;

public final class ZipCracker extends FileCracker {
    private static final boolean DEBUG = false;

    public ZipCracker() {
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
        return "ZIP Cracker";
    }

    @Override
    public boolean prepareCracker() {
        return true;
    }

    @Override
    public boolean checkPassword(String password) {
        if (DEBUG) {
            logger.info("checkPassword: " + password);
        }
        boolean result = false;
        try {
            ZipFile zFile = new ZipFile(file);
            zFile.setFileNameCharset(StandardCharsets.UTF_8.name());
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
        }
        return result;
    }
}
