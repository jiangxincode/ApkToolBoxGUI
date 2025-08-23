package edu.jiangxin.apktoolbox.pdf.finder;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PdfFilesTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int groupNo = (Integer) table.getValueAt(row, 0);
        setBackground(PdfFilesConstants.BACKGROUND.get(groupNo % PdfFilesConstants.BACKGROUND.size()));
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
