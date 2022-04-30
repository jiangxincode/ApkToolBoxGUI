package edu.jiangxin.apktoolbox.file.crack.cracker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import java.io.File;
import java.io.IOException;

public class PdfCracker extends FileCracker {
    private static final boolean DEBUG = false;
    private Logger logger;

    public PdfCracker(File file) {
        super(file);
        logger = LogManager.getLogger(this.getClass().getSimpleName());
    }

    @Override
    public boolean prepareCracker() {
        return true;
    }

    @Override
    public boolean checkPwd(String pwd) {
        if (DEBUG) {
            logger.info("checkPwd: " + pwd);
        }
        boolean result = false;
        try {
            PDDocument.load(file, pwd);
            result = true;
        } catch (InvalidPasswordException e) {
            result = false;
        } catch (IOException e) {
        }
        return result;
    }
}
