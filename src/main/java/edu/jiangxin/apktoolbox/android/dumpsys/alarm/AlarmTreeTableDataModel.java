package edu.jiangxin.apktoolbox.android.dumpsys.alarm;

import edu.jiangxin.apktoolbox.swing.treetable.MyAbstractTreeTableModel;
import edu.jiangxin.apktoolbox.swing.treetable.MyTreeTableModel;

public class AlarmTreeTableDataModel extends MyAbstractTreeTableModel {
    static protected String[] columnNames = { "Object ID", "Package", "Alarm Type", "When", "Fire DateTime" };

    static protected Class<?>[] columnTypes = { MyTreeTableModel.class, String.class, String.class, String.class, String.class };

    public AlarmTreeTableDataModel(AlarmTreeTableDataNode rootNode) {
        super(rootNode);
        root = rootNode;
    }

    public Object getChild(Object parent, int index) {
        return ((AlarmTreeTableDataNode) parent).getChildren().get(index);
    }


    public int getChildCount(Object parent) {
        return ((AlarmTreeTableDataNode) parent).getChildren().size();
    }


    public int getColumnCount() {
        return columnNames.length;
    }


    public String getColumnName(int column) {
        return columnNames[column];
    }


    public Class<?> getColumnClass(int column) {
        return columnTypes[column];
    }

    public Object getValueAt(Object node, int column) {
        switch (column) {
            case 0:
                return ((AlarmTreeTableDataNode) node).getObjectId();
            case 1:
                return ((AlarmTreeTableDataNode) node).getAppPackage();
            case 2:
                return ((AlarmTreeTableDataNode) node).getAlarmType();
            case 3:
                return ((AlarmTreeTableDataNode) node).getWhen();
            case 4:
                return ((AlarmTreeTableDataNode) node).getFireDateTime();
            default:
                break;
        }
        return null;
    }

    public boolean isCellEditable(Object node, int column) {
        return true; // Important to activate TreeExpandListener
    }

    public void setValueAt(Object aValue, Object node, int column) {
    }
}
