package edu.jiangxin.apktoolbox.convert.color;

import edu.jiangxin.apktoolbox.convert.color.colortable.IColorTable;
import edu.jiangxin.apktoolbox.convert.color.colortable.OrdinaryColorTable;
import edu.jiangxin.apktoolbox.convert.color.colortable.RalColorTable;
import edu.jiangxin.apktoolbox.convert.color.utils.ColorUtils;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.Serial;

public class ColorTablePanel extends EasyPanel {
    @Serial
    private static final long serialVersionUID = 1L;

    private JComboBox<IColorTable> colorTableTypeComboBox;

    private JTable colorTable;

    public ColorTablePanel() throws HeadlessException {
        super();
    }

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createColorTablePanel();
    }

    private void createColorTablePanel() {
        JPanel colorTablePanel = new JPanel();
        add(colorTablePanel);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Color Table"));

        JPanel yPanel = new JPanel();
        add(yPanel);
        yPanel.setLayout(new BoxLayout(yPanel, BoxLayout.Y_AXIS));

        colorTableTypeComboBox = new JComboBox<>();
        IColorTable ordinaryColorTable = new OrdinaryColorTable();
        IColorTable ralColorTable = new RalColorTable();
        colorTableTypeComboBox.addItem(ordinaryColorTable);
        colorTableTypeComboBox.addItem(ralColorTable);
        colorTableTypeComboBox.setSelectedItem(ordinaryColorTable);
        colorTableTypeComboBox.addItemListener(itemEvent -> {
            ColorDefaultTableModel model = (ColorDefaultTableModel) colorTable.getModel();
            IColorTable selectedColorTable = (IColorTable) itemEvent.getItem();
            model.setDataVector(selectedColorTable.getTableRowData(), selectedColorTable.getColumnNames());
            onColorTableChanged();
        });

        IColorTable selectedColorTable = (IColorTable) colorTableTypeComboBox.getSelectedItem();
        colorTable = new JTable(new ColorDefaultTableModel(selectedColorTable.getTableRowData(), selectedColorTable.getColumnNames()));
        JScrollPane colorTableScrollPane = new JScrollPane(colorTable);
        onColorTableChanged();

        yPanel.add(colorTableTypeComboBox);
        yPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        yPanel.add(colorTableScrollPane);
    }

    private void onColorTableChanged() {
        IColorTable selectedColorTable = (IColorTable) colorTableTypeComboBox.getSelectedItem();
        int labelIndex = selectedColorTable.getLabelIndex();
        String labelName = colorTable.getColumnName(labelIndex);
        colorTable.getColumn(labelName).setCellRenderer(new ColorTableCellRenderer());
    }

    private class ColorDefaultTableModel extends DefaultTableModel {
        @Serial
        private static final long serialVersionUID = 1L;

        public ColorDefaultTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }
    }

    private class ColorTableCellRenderer extends DefaultTableCellRenderer {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            IColorTable selectedColorTable = (IColorTable) colorTableTypeComboBox.getSelectedItem();
            if (selectedColorTable == null) {
                return renderer;
            }
            String hex = (String) colorTable.getValueAt(row, selectedColorTable.getHexIndex());
            Color color = ColorUtils.hex2Color(hex);
            renderer.setBackground(color);
            return renderer;
        }
    }
}
