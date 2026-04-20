package edu.jiangxin.apktoolbox.word.stat;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.Serial;

public class WordFilesTableCellRenderer extends DefaultTableCellRenderer {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int index = (Integer) table.getValueAt(row, 0);
        setBackground(WordFilesConstants.BACKGROUND.get(index % WordFilesConstants.BACKGROUND.size()));
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
