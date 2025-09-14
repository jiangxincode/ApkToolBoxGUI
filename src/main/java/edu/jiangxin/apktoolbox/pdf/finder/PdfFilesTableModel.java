package edu.jiangxin.apktoolbox.pdf.finder;

import javax.swing.table.DefaultTableModel;
import java.io.Serial;
import java.util.Vector;

public class PdfFilesTableModel extends DefaultTableModel {
    @Serial
    private static final long serialVersionUID = 1L;

    public PdfFilesTableModel(Vector<? extends Vector<Object>> data, Vector<String> columnNames) {
        super(data, columnNames);
    }
}
