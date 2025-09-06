package edu.jiangxin.apktoolbox.file.password.recovery.checker;

import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;

public class XmlBasedOfficeChecker extends FileChecker {
    private static final boolean DEBUG = false;

    public XmlBasedOfficeChecker() {
        super();
    }

    @Override
    public String[] getFileExtensions() {
        return new String[]{"docx", "pptx", "xlsx"};
    }

    @Override
    public String getFileDescription() {
        return "*.docx;*.pptx;*.xlsx";
    }

    @Override
    public String getDescription() {
        return "Office File Checker(XML-based formats)";
    }

    @Override
    public boolean prepareChecker() {
        return true;
    }

    @Override
    public boolean checkPassword(String password) {
        if (DEBUG) {
            logger.info("checkPassword: {}", password);
        }
        boolean result = false;
        try (POIFSFileSystem pfs = new POIFSFileSystem(new FileInputStream(file))) {
            EncryptionInfo info = new EncryptionInfo(pfs);
            Decryptor decryptor = Decryptor.getInstance(info);
            result = decryptor.verifyPassword(password);
        } catch (Exception e) {
            if (DEBUG) {
                logger.error("checkPassword: {}", e.getClass());
            }
        }
        return result;
    }
}
