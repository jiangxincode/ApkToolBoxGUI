package edu.jiangxin.apktoolbox.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 工具类：将 JTable 的 TableModel 数据导出为 .xlsx 文件。
 */
public class ExcelExporter {

    private static final Logger logger = LogManager.getLogger(ExcelExporter.class);

    private ExcelExporter() {
        // 工具类，禁止实例化
    }

    /**
     * 完整导出流程：空数据检查 → 文件选择 → 写文件 → 结果提示。
     * 必须在 EDT 中调用。
     *
     * @param tableModel      数据来源
     * @param defaultFileName 文件保存对话框的默认文件名，例如 "pdf_stat_export.xlsx"
     * @param parent          父组件，用于对话框定位
     */
    public static void export(TableModel tableModel, String defaultFileName, Component parent) {
        if (tableModel.getRowCount() == 0) {
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(parent, "当前没有可导出的数据", "提示", JOptionPane.INFORMATION_MESSAGE));
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存 Excel 文件");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel 文件 (*.xlsx)", "xlsx"));
        fileChooser.setSelectedFile(new File(defaultFileName));

        int result = fileChooser.showSaveDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        // 自动追加 .xlsx 后缀
        if (!selectedFile.getName().toLowerCase().endsWith(".xlsx")) {
            selectedFile = new File(selectedFile.getAbsolutePath() + ".xlsx");
        }
        final File targetFile = selectedFile;

        // 在后台线程写文件，避免阻塞 EDT
        final TableModel modelSnapshot = tableModel;
        new Thread(() -> {
            try {
                writeToFile(modelSnapshot, targetFile);
                final String absolutePath = targetFile.getAbsolutePath();
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(parent, "导出成功：" + absolutePath, "成功", JOptionPane.INFORMATION_MESSAGE));
            } catch (IOException e) {
                logger.error("导出 Excel 失败", e);
                final String errorMsg = e.getMessage();
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(parent, "导出失败：" + errorMsg, "错误", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    /**
     * 将 TableModel 数据写入指定文件（在后台线程调用）。
     */
    static void writeToFile(TableModel tableModel, File targetFile) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");

            // 写表头
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                headerRow.createCell(col).setCellValue(tableModel.getColumnName(col));
            }

            // 写数据行
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                Row dataRow = sheet.createRow(row + 1);
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    Object value = tableModel.getValueAt(row, col);
                    dataRow.createCell(col).setCellValue(value == null ? "" : value.toString());
                }
            }

            try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                workbook.write(fos);
            }
        }
    }
}
