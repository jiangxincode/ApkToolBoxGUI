package edu.jiangxin.apktoolbox.file.crack.cracker;

import edu.jiangxin.apktoolbox.file.crack.exception.UnknownException;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import java.io.IOException;

/**
 * Brief introduction to PDF password
 * The standard security provided by PDF has two different passwords: user passwords and owner passwords.
 * A PDF document may be protected by a password for opening (user password)
 * and the document may also specify operations that should be restricted even when the document is decrypted:
 * printing; copying text and graphics out of the document; modifying the document;
 * and adding or modifying text notes (using owner password).
 *
 */
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
        boolean result = false;
        PDDocument pdDocument = null;
        try {
            pdDocument = PDDocument.load(file, pwd);
            result = true;
        } catch (InvalidPasswordException e) {
            if (DEBUG) {
                logger.error("[InvalidPasswordException]password is incorrect: " + pwd);
            }
        } catch (IOException e) {
            throw new UnknownException(e);
        } finally {
            if (pdDocument != null) {
                IOUtils.closeQuietly(pdDocument);
            }
        }
        return result;
    }
}
