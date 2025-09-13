package edu.jiangxin.apktoolbox.pdf.stat;

import javax.swing.table.DefaultTableModel;
import java.io.Serial;
import java.util.Vector;

public class PdfFilesTableModel extends DefaultTableModel {
    @Serial
    private static final long serialVersionUID = 1L;

    public PdfFilesTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }
}
