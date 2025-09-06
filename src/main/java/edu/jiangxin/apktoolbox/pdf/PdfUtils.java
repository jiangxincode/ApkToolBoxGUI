package edu.jiangxin.apktoolbox.pdf;

import com.itextpdf.kernel.pdf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PdfUtils {
    private static final Logger LOGGER = LogManager.getLogger(PdfUtils.class.getSimpleName());
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

    public static boolean isNonOutlinePdf(File file) {
        boolean hasOutline = false;

        try (PDDocument document = Loader.loadPDF(file)) {
            boolean isEncrypted = document.isEncrypted();
            if (isEncrypted) {
                document.setAllSecurityToBeRemoved(true);
            }

            if (document.getDocumentCatalog() != null && document.getDocumentCatalog().getDocumentOutline() != null) {
                hasOutline = true;
            }
        } catch (IOException e) {
            LOGGER.error("Error reading PDF file: {}", e.getMessage());
            return false;
        }
        LOGGER.info("Processing file: {}, has outline: {}", file.getPath(), hasOutline);
        return !hasOutline;
    }

    public static boolean hasAnnotations(File file) {
        boolean hasAnnotations = false;

        try (PDDocument document = Loader.loadPDF(file)) {
            boolean isEncrypted = document.isEncrypted();
            if (isEncrypted) {
                document.setAllSecurityToBeRemoved(true);
            }
            PDDocumentCatalog catalog = document.getDocumentCatalog();
            if (catalog == null) {
                return false;
            }
            PDPageTree pages = document.getDocumentCatalog().getPages();
            if (pages == null || pages.getCount() == 0) {
                return false;
            }

            for (PDPage page : pages) {
                if (page.getAnnotations() != null && !page.getAnnotations().isEmpty()) {
                    int pageNumber = page.getCOSObject().getInt("PageNumber", 0);
                    String subType = page.getAnnotations().get(0).getSubtype();
                    LOGGER.info("Found annotations on page: {}, subType: {}", pageNumber, subType);
                    if (!subType.equals("Link")) {
                        hasAnnotations = true;
                        break; // No need to check further if we found annotations
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error reading PDF file: {}", e.getMessage());
            return hasAnnotations;
        }
        LOGGER.info("Processing file: {}, has annotations: {}", file.getPath(), hasAnnotations);
        return hasAnnotations;
    }

    public static void removePassword(File encryptedFile, File targetDir) {
        try (PDDocument document = Loader.loadPDF(encryptedFile)) {
            boolean isEncrypted = document.isEncrypted();
            if (isEncrypted) {
                document.setAllSecurityToBeRemoved(true);
            }
            String targetFilePath = targetDir.getAbsolutePath() + File.separator + encryptedFile.getName();
            document.save(targetFilePath);
            LOGGER.info("Remove password success: {}", targetFilePath);
        } catch (IOException e) {
            LOGGER.error("Error processing PDF file: {}", e.getMessage());
        }
    }

    public static void removePasswordWithIText(File encryptedFile, File targetDir) {
        PdfReader reader;
        try {
            reader = new PdfReader(encryptedFile).setUnethicalReading(true);
        } catch (IOException e) {
            LOGGER.error("Error reading PDF file: {}", e.getMessage());
            return;
        }
        String targetFilePath = targetDir.getAbsolutePath() + File.separator + encryptedFile.getName();

        try (PdfDocument pdfDoc = new PdfDocument(reader,
                new PdfWriter(targetFilePath))) {
            LOGGER.info("Remove password success: {}", targetFilePath);
        } catch (IOException e) {
            LOGGER.error("Error writing PDF file: {}", e.getMessage());
        }
    }
}
