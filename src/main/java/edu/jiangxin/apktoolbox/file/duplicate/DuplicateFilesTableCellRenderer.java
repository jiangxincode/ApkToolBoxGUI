package edu.jiangxin.apktoolbox.file.duplicate;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class DuplicateFilesTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int groupNo = (Integer) table.getValueAt(row, 0);
        setBackground(DuplicateFilesConstants.BACKGROUND.get(groupNo % DuplicateFilesConstants.BACKGROUND.size()));
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
