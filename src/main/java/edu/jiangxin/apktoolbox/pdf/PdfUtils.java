package edu.jiangxin.apktoolbox.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.HorizontalAlignment;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

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
        String targetFilePath = targetDir.getAbsolutePath() + File.separator + encryptedFile.getName();
        PdfReader reader = null;
        PdfDocument pdfDoc = null;
        PdfWriter writer = null;
        try {
            reader = new PdfReader(encryptedFile);
            reader.setUnethicalReading(true);
            writer = new PdfWriter(targetFilePath);
            pdfDoc = new PdfDocument(reader, writer);
        } catch (IOException e) {
            LOGGER.error("Error processing PDF file: {}", e.getMessage());
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(pdfDoc);
            IOUtils.closeQuietly(reader);
        }
    }

    public static int getPageCount(File file) {
        int pageCount = 0;

        try (PDDocument document = Loader.loadPDF(file)) {
            boolean isEncrypted = document.isEncrypted();
            if (isEncrypted) {
                document.setAllSecurityToBeRemoved(true);
            }
            pageCount = document.getNumberOfPages();
        } catch (IOException e) {
            LOGGER.error("Error reading PDF file: {}", e.getMessage());
            return 0;
        }
        LOGGER.info("Processing file: {}, page count: {}", file.getPath(), pageCount);
        return pageCount;
    }

    public static void imagesToPdf(Set<File> images, File targetFile) {
        PdfDocument pdfDoc = null;
        PdfWriter writer = null;
        Document doc = null;
        try {
            writer = new PdfWriter(new FileOutputStream(targetFile));
            pdfDoc = new PdfDocument(writer);
            doc = new Document(pdfDoc);

            for (File img : images) {
                BufferedImage bufferedImage = ImageIO.read(img);
                float width = bufferedImage.getWidth();
                float height = bufferedImage.getHeight();

                PageSize pageSize = new PageSize(width, height);
                pdfDoc.addNewPage(pageSize);

                Image image = new Image(ImageDataFactory.create(img.getAbsolutePath()));
                image.setFixedPosition(pdfDoc.getNumberOfPages(), 0, 0, width);

                doc.setMargins(0, 0, 0, 0);
                doc.add(image);
            }
        } catch (IOException e) {
            LOGGER.error("Error processing PDF file: {}", e.getMessage());
        } finally {
            IOUtils.closeQuietly(doc);
            IOUtils.closeQuietly(pdfDoc);
            IOUtils.closeQuietly(writer);
        }
    }
}
