package edu.jiangxin.apktoolbox.pdf;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class PdfFilesTableModel extends DefaultTableModel {
    public PdfFilesTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return super.isCellEditable(row, column);
    }
}
