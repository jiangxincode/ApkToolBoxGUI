package edu.jiangxin.apktoolbox.pdf;

import edu.jiangxin.apktoolbox.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PdfUtils {
    private static final Logger LOGGER = LogManager.getLogger(DateUtils.class.getSimpleName());
    public static boolean isScannedPdf(File file, int threshold) {
        int length = 0;

        try (PDDocument document = Loader.loadPDF(file)) {
            boolean isEncrypted = document.isEncrypted();
            if (isEncrypted) {
                document.setAllSecurityToBeRemoved(true);
            }

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document).trim();
            length = text.length();
        } catch (IOException e) {
            LOGGER.error("Error reading PDF file: {}", e.getMessage());
            return false;
        }
        LOGGER.info("Processing file: {}, text size: {}", file.getPath(), length);
        return length < threshold;
    }

    public static boolean isEncryptedPdf(File file) {
        boolean isEncrypted;

        try (PDDocument document = Loader.loadPDF(file)) {
            isEncrypted = document.isEncrypted();
        } catch (IOException e) {
            LOGGER.error("Error reading PDF file: {}", e.getMessage());
            return false;
        }
        LOGGER.info("Processing file: {}, is encrypted: {}", file.getPath(), isEncrypted);
        return isEncrypted;
    }
}
