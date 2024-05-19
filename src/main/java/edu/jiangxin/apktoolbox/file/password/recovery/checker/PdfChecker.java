package edu.jiangxin.apktoolbox.file.password.recovery.checker;

import edu.jiangxin.apktoolbox.file.password.recovery.exception.UnknownException;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
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
 */
public class PdfChecker extends FileChecker {
    private static final boolean DEBUG = false;

    public PdfChecker() {
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
        return "PDF Checker";
    }

    @Override
    public boolean prepareChecker() {
        return true;
    }

    @Override
    public boolean checkPassword(String password) {
        boolean result = false;
        PDDocument pdDocument = null;
        try {
            pdDocument = Loader.loadPDF(new RandomAccessReadBufferedFile(file), password);
            result = true;
        } catch (InvalidPasswordException e) {
            logger.debug("[InvalidPasswordException]password is incorrect: {}", password);
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
