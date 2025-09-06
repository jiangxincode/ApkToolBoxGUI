package edu.jiangxin.apktoolbox.pdf.finder;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class PdfFilesTableModel extends DefaultTableModel {
    public PdfFilesTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }
}
