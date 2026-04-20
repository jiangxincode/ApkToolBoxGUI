package edu.jiangxin.apktoolbox.word.stat;

import javax.swing.table.DefaultTableModel;
import java.io.Serial;
import java.util.Vector;

public class WordFilesTableModel extends DefaultTableModel {
    @Serial
    private static final long serialVersionUID = 1L;

    public WordFilesTableModel(Vector<? extends Vector<Object>> data, Vector<String> columnNames) {
        super(data, columnNames);
    }
}
