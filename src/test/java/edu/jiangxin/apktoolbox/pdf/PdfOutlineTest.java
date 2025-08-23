package edu.jiangxin.apktoolbox.pdf;

import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.action.PDAction;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfOutlineTest {
    String path = "target/test-classes/pdf/";

    public void alterOutline(PDOutlineNode pdOutlineNode, String indentation) throws IOException {

        PDOutlineItem currentPdOutlineItem = pdOutlineNode.getFirstChild();
        while (currentPdOutlineItem != null) {
            if (currentPdOutlineItem.getDestination() instanceof PDPageDestination) {
                PDPageDestination pdOutlineItemDestination = (PDPageDestination) currentPdOutlineItem.getDestination();
                pdOutlineItemDestination.setPageNumber(pdOutlineItemDestination.retrievePageNumber() + 25);
            }
            if (currentPdOutlineItem.getAction() instanceof PDActionGoTo) {
                PDActionGoTo pdActionGoTo = (PDActionGoTo) currentPdOutlineItem.getAction();
                if (pdActionGoTo.getDestination() instanceof PDPageDestination) {
                    PDPageDestination pd = (PDPageDestination) pdActionGoTo.getDestination();
                    pd.setPageNumber(pd.retrievePageNumber() + 25);
                }
            }
            alterOutline(currentPdOutlineItem, indentation + "    ");
            currentPdOutlineItem = currentPdOutlineItem.getNextSibling();
        }
    }

    public void printOutline(PDOutlineNode pdOutlineNode, String indentation) throws IOException {
        PDOutlineItem currentPdOutlineItem = pdOutlineNode.getFirstChild();
        while (currentPdOutlineItem != null) {
            int pages = 0;
            StringBuilder extraInfo = new StringBuilder();
            PDDestination pdDestination = currentPdOutlineItem.getDestination();
            extraInfo.append(indentation + currentPdOutlineItem.getTitle());
            if (pdDestination instanceof PDPageDestination) {
                PDPageDestination pdOutlineItemDestination = (PDPageDestination) pdDestination;
                pages = (pdOutlineItemDestination.retrievePageNumber() + 1);
                extraInfo.append(":").append(pdDestination.getClass().getSimpleName()).append(":").append(pages);
            } else {
                PDAction pdAction = currentPdOutlineItem.getAction();
                if (pdAction instanceof PDActionGoTo) {
                    PDActionGoTo pdActionGoTo = (PDActionGoTo) pdAction;
                    pdDestination = pdActionGoTo.getDestination();
                    if (pdDestination instanceof PDPageDestination) {
                        PDPageDestination pd = (PDPageDestination) pdDestination;
                        pages = (pd.retrievePageNumber() + 1);
                        extraInfo.append(":PDActionGoTo:").append(pdDestination.getClass().getSimpleName()).append(":").append(pages);
                    }
                }
            }

            System.out.println(extraInfo);
            printOutline(currentPdOutlineItem, indentation + "    ");
            currentPdOutlineItem = currentPdOutlineItem.getNextSibling();
        }
    }

    @Test
    public void testPrintOutline() {
        File file = new File(path, "R常用函数整理.pdf");
        if (!file.exists()) {
            System.err.println(" file is not exists ");
            return;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            PDFParser parser = new PDFParser(new RandomAccessReadBuffer(fis));
            try (PDDocument document = parser.parse()) {
                PdfOutlineTest pdfOutlineTest = new PdfOutlineTest();
                PDDocumentOutline pdDocumentOutline = document.getDocumentCatalog().getDocumentOutline();
                if (pdDocumentOutline != null) {
                    pdfOutlineTest.printOutline(pdDocumentOutline, "");
                } else {
                    System.out.println("This document does not contain any outline");
                }
            }
        } catch (IOException ex) {
            System.err.println("IOException");
        }
    }

    @Test
    public void testAlterOutline() {
        File file = new File(path, "R常用函数整理.pdf");
        if (!file.exists()) {
            System.err.println(" file is not exists ");
            return;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            PDFParser parser = new PDFParser(new RandomAccessReadBuffer(fis));
            try (PDDocument document = parser.parse()) {
                PdfOutlineTest pdfOutlineTest = new PdfOutlineTest();
                PDDocumentOutline pdDocumentOutline = document.getDocumentCatalog().getDocumentOutline();
                if (pdDocumentOutline != null) {
                    pdfOutlineTest.alterOutline(pdDocumentOutline, "");
                } else {
                    System.out.println("This document does not contain any outline");
                }
                document.save(new File(path, "R常用函数整理_altered.pdf"));
            }
        } catch (IOException ex) {
            System.err.println("IOException");
        }
    }
}