package edu.jiangxin.apktoolbox.word;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WordUtils {

    private static final Logger LOGGER = LogManager.getLogger(WordUtils.class.getSimpleName());

    private WordUtils() {
        // utility class
    }

    /**
     * 读取 Word 文档的页数。
     * - .doc  使用 HWPFDocument（poi-scratchpad）
     * - .docx 使用 XWPFDocument（poi-ooxml）
     * 若读取失败或页数 <= 0，返回 0。
     *
     * @param file Word 文档文件
     * @return 文档页数，若无法读取则返回 0
     */
    public static int getPageCount(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            LOGGER.warn("Invalid file: {}", file);
            return 0;
        }

        String name = file.getName().toLowerCase();
        if (name.endsWith(".doc")) {
            return getDocPageCount(file);
        } else if (name.endsWith(".docx")) {
            return getDocxPageCount(file);
        } else {
            LOGGER.warn("Unsupported file type: {}", file.getPath());
            return 0;
        }
    }

    private static int getDocPageCount(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             HWPFDocument document = new HWPFDocument(fis)) {
            SummaryInformation si = document.getSummaryInformation();
            int pageCount = (si != null) ? si.getPageCount() : 0;
            if (pageCount <= 0) {
                LOGGER.info("Page count <= 0 for .doc file: {}, returning 0", file.getPath());
                return 0;
            }
            LOGGER.info("Processing .doc file: {}, page count: {}", file.getPath(), pageCount);
            return pageCount;
        } catch (IOException e) {
            LOGGER.error("Error reading .doc file: {}, message: {}", file.getPath(), e.getMessage());
            return 0;
        }
    }

    private static int getDocxPageCount(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {
            // 优先从 ExtendedProperties 读取页数
            int pageCount = 0;
            if (document.getProperties() != null
                    && document.getProperties().getExtendedProperties() != null
                    && document.getProperties().getExtendedProperties().getUnderlyingProperties() != null) {
                pageCount = document.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
            }
            // 若 ExtendedProperties 无效，尝试从 CoreProperties 读取 revision 作为备选
            if (pageCount <= 0) {
                String revision = null;
                if (document.getProperties() != null
                        && document.getProperties().getCoreProperties() != null) {
                    revision = document.getProperties().getCoreProperties().getRevision();
                }
                if (revision != null && !revision.isEmpty()) {
                    try {
                        pageCount = Integer.parseInt(revision);
                    } catch (NumberFormatException e) {
                        LOGGER.warn("Cannot parse revision as page count for .docx file: {}", file.getPath());
                        pageCount = 0;
                    }
                }
            }
            if (pageCount <= 0) {
                LOGGER.info("Page count <= 0 for .docx file: {}, returning 0", file.getPath());
                return 0;
            }
            LOGGER.info("Processing .docx file: {}, page count: {}", file.getPath(), pageCount);
            return pageCount;
        } catch (IOException e) {
            LOGGER.error("Error reading .docx file: {}, message: {}", file.getPath(), e.getMessage());
            return 0;
        }
    }
}
