package edu.jiangxin.apktoolbox.file.duplicate;

import javax.swing.table.DefaultTableModel;
import java.io.Serial;
import java.util.Vector;

public class DuplicateFilesTableModel extends DefaultTableModel {
    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateFilesTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == DuplicateFilesConstants.COLUMN_NAMES.indexOf(DuplicateFilesConstants.COLUMN_NAME_GROUP_NO)) {
            return false;
        }
        return super.isCellEditable(row, column);
    }
}
