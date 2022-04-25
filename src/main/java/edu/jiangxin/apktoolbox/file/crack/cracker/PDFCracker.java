package edu.jiangxin.apktoolbox.file.crack.cracker;

import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import java.io.File;
import java.io.IOException;

public class PDFCracker extends FileCracker {
    private Logger logger;

    public PDFCracker(File file) {
        super(file);
        logger = LogManager.getLogger(this.getClass().getSimpleName());
    }

    @Override
    public boolean prepareCracker() {
        return true;
    }

    @Override
    public boolean checkPwd(String pwd) {
        logger.info("checkPwd pwd: " + pwd);
        boolean result;
        try {
            PDDocument.load(file, pwd);
            result = true;
        } catch (InvalidPasswordException e) {
            result = false;
        } catch (IOException e) {
            result = false;
        }
        return result;
    }
}
