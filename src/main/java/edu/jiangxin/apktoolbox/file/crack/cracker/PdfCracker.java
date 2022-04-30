package edu.jiangxin.apktoolbox.file.crack.cracker;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import java.io.IOException;

public class PdfCracker extends FileCracker {
    private static final boolean DEBUG = false;

    public PdfCracker() {
        super();
    }

    @Override
    public String[] getFileExtensions() {
        return new String[]{"pdf"};
    }

    @Override
    public String getFileDescription() {
        return "*.pdf";
    }

    @Override
    public String getDescription() {
        return "PDF Cracker";
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
        PDDocument pdDocument = null;
        try {
            pdDocument = PDDocument.load(file, pwd);
            result = true;
        } catch (InvalidPasswordException e) {
        } catch (IOException e) {
            logger.error("load failed: " + e.getMessage());
        } finally {
            if (pdDocument != null) {
                IOUtils.closeQuietly(pdDocument);
            }
        }
        return result;
    }
}
