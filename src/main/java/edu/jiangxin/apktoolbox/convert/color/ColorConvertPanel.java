package edu.jiangxin.apktoolbox.convert.color;

import edu.jiangxin.apktoolbox.convert.color.colortable.IColorTable;
import edu.jiangxin.apktoolbox.convert.color.colortable.OrdinaryColorTable;
import edu.jiangxin.apktoolbox.convert.color.colortable.RalColorTable;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;

public class ColorConvertPanel extends EasyPanel {
    private Color color;

    private JSpinner redSpinner;

    private JSpinner greenSpinner;

    private JSpinner blueSpinner;

    private JTextField hexTextField;

    private JTextField hueTextField;

    private JTextField saturationTextField;

    private JTextField brightnessTextField;

    private JTextField cyanTextField;

    private JTextField magentaTextField;

    private JTextField yellowTextField;

    private JTextField blackTextField;

    private JTextField colorBoxTextField;

    private JComboBox<IColorTable> colorTableTypeComboBox;

    private JTable colorTable;

    private boolean isChangedByUser;

    public ColorConvertPanel() throws HeadlessException {
        super();
        initUI();
        isChangedByUser = true;
    }

    private void initUI() {
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createColorBoxPanel();

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createRgbPanel();

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createHsbPanel();

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createCmykPanel();

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createColorTablePanel();
    }

    public void createColorBoxPanel() {
        JPanel colorBoxPanel = new JPanel();
        add(colorBoxPanel);
        colorBoxPanel.setLayout(new BorderLayout());
        colorBoxPanel.setBorder(BorderFactory.createTitledBorder("Color Box"));

        JPanel xPanel = new JPanel();
        colorBoxPanel.add(xPanel);
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.X_AXIS));

        colorBoxTextField = new JTextField();
        colorBoxTextField.setEditable(false);
        colorBoxTextField.setOpaque(false);

        xPanel.add(colorBoxTextField);
    }

    private void createRgbPanel() {
        JPanel rgbPanel = new JPanel();
        add(rgbPanel);
        rgbPanel.setLayout(new BorderLayout());
        rgbPanel.setBorder(BorderFactory.createTitledBorder("RGB"));

        JPanel xPanel = new JPanel();
        rgbPanel.add(xPanel);
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.X_AXIS));

        JLabel rebLabel = new JLabel("R(Red, [0-255])");
        redSpinner = new JSpinner();
        redSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
        redSpinner.addChangeListener(new RgbChangeListener());

        JLabel greenLabel = new JLabel("G(Green, [0-255])");
        greenSpinner = new JSpinner();
        greenSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
        greenSpinner.addChangeListener(new RgbChangeListener());

        JLabel blueLabel = new JLabel("B(Blue, [0-255])");
        blueSpinner = new JSpinner();
        blueSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
        blueSpinner.addChangeListener(new RgbChangeListener());

        JLabel hexLabel = new JLabel("Hex");
        hexTextField = new JTextField();
        hexTextField.setToolTipText("0xFFFFFF格式常用在通用语言中(Java/C++等), #FFFFFF格式常用在标记语言中(XML/HTML等)");
        hexTextField.setEditable(true);
        hexTextField.getDocument().addDocumentListener(new HexDocumentListener());

        xPanel.add(rebLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(redSpinner);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(greenLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(greenSpinner);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(blueLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(blueSpinner);
        xPanel.add(Box.createHorizontalBox().createHorizontalStrut(3 * Constants.DEFAULT_X_BORDER));
        xPanel.add(hexLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(hexTextField);
    }

    private void createHsbPanel() {
        JPanel hsbPanel = new JPanel();
        add(hsbPanel);
        hsbPanel.setLayout(new BorderLayout());
        hsbPanel.setBorder(BorderFactory.createTitledBorder("HSB"));

        JPanel xPanel = new JPanel();
        hsbPanel.add(xPanel);
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.X_AXIS));

        JLabel hueLabel = new JLabel("H(Hue, [0-1])");
        hueTextField = new JTextField();
        hueTextField.setText(new DecimalFormat("0.0000").format(0.0f));
        hueTextField.getDocument().addDocumentListener(new HsbDocumentListener());


        JLabel saturationLabel = new JLabel("S(Saturation, [0-1])");
        saturationTextField = new JTextField();
        saturationTextField.setText(new DecimalFormat("0.0000").format(0.0f));
        saturationTextField.getDocument().addDocumentListener(new HsbDocumentListener());


        JLabel brightnessLabel = new JLabel("B/V(Brightness or Value, [0-1])");
        brightnessTextField = new JTextField();
        brightnessTextField.setText(new DecimalFormat("0.0000").format(0.0f));
        brightnessTextField.getDocument().addDocumentListener(new HsbDocumentListener());

        xPanel.add(hueLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(hueTextField);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(saturationLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(saturationTextField);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(brightnessLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(brightnessTextField);
    }

    private void createCmykPanel() {
        JPanel cmykPanel = new JPanel();
        add(cmykPanel);
        cmykPanel.setLayout(new BorderLayout());
        cmykPanel.setBorder(BorderFactory.createTitledBorder("CMYK"));

        JPanel xPanel = new JPanel();
        cmykPanel.add(xPanel);
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.X_AXIS));

        JLabel cyanLabel = new JLabel("C(Cyan, [0-1])");
        cyanTextField = new JTextField();
        cyanTextField.setText(new DecimalFormat("0.0000").format(0.0f));
        cyanTextField.getDocument().addDocumentListener(new CmykDocumentListener());


        JLabel magentaLabel = new JLabel("M(Magenta, [0-1])");
        magentaTextField = new JTextField();
        magentaTextField.setText(new DecimalFormat("0.0000").format(0.0f));
        magentaTextField.getDocument().addDocumentListener(new CmykDocumentListener());


        JLabel yellowLabel = new JLabel("Y(Yellow, [0-1])");
        yellowTextField = new JTextField();
        yellowTextField.setText(new DecimalFormat("0.0000").format(0.0f));
        yellowTextField.getDocument().addDocumentListener(new CmykDocumentListener());

        JLabel blackLabel = new JLabel("K(Black, [0-1])");
        blackTextField = new JTextField();
        blackTextField.setText(new DecimalFormat("0.0000").format(0.0f));
        blackTextField.getDocument().addDocumentListener(new CmykDocumentListener());

        xPanel.add(cyanLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(cyanTextField);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(magentaLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(magentaTextField);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(yellowLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(yellowTextField);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(blackLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(blackTextField);
    }

    private void createColorTablePanel() {
        JPanel colorTablePanel = new JPanel();
        add(colorTablePanel);
        colorTablePanel.setLayout(new BorderLayout());
        colorTablePanel.setBorder(BorderFactory.createTitledBorder("Color Table"));

        JPanel yPanel = new JPanel();
        colorTablePanel.add(yPanel);
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
        public ColorDefaultTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }
    }

    private class ColorTableCellRenderer extends DefaultTableCellRenderer {
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

    private void syncToOthersFormat(String colorMode) {
        if (!colorMode.equalsIgnoreCase("RGB")) {
            redSpinner.setValue(color.getRed());
            greenSpinner.setValue(color.getGreen());
            blueSpinner.setValue(color.getBlue());
        }
        if (!colorMode.equalsIgnoreCase("HEX")) {
            hexTextField.setText(ColorUtils.color2Hex(color));
        }
        if (!colorMode.equalsIgnoreCase("HSB")) {
            float[] hsbArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            hueTextField.setText(new DecimalFormat("0.0000").format(hsbArray[0]));
            saturationTextField.setText(new DecimalFormat("0.0000").format(hsbArray[1]));
            brightnessTextField.setText(new DecimalFormat("0.0000").format(hsbArray[2]));
        }
        if (!colorMode.equalsIgnoreCase("CMYK")) {
            float[] cmykArray = ColorUtils.color2Cmyk(color);
            cyanTextField.setText(new DecimalFormat("0.0000").format(cmykArray[0]));
            magentaTextField.setText(new DecimalFormat("0.0000").format(cmykArray[1]));
            yellowTextField.setText(new DecimalFormat("0.0000").format(cmykArray[2]));
            blackTextField.setText(new DecimalFormat("0.0000").format(cmykArray[3]));
        }
        colorBoxTextField.setBackground(color);
        //paint the color box with the converted output color
        colorBoxTextField.setOpaque(true);
    }

    class RgbChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (!isChangedByUser) {
                return;
            }
            isChangedByUser = false;
            int red = (Integer) redSpinner.getValue();
            int green = (Integer) greenSpinner.getValue();
            int blue = (Integer) blueSpinner.getValue();
            color = new Color(red, green, blue);
            syncToOthersFormat("RGB");
            isChangedByUser = true;
        }
    }

    class HexDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            onValueUpdate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onValueUpdate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            onValueUpdate();
        }

        private void onValueUpdate() {
            if (!isChangedByUser) {
                return;
            }
            isChangedByUser = false;
            String hex = hexTextField.getText();
            try {
                color = ColorUtils.hex2Color(hex);
                syncToOthersFormat("HEX");
            } catch (NumberFormatException e) {
            }
            isChangedByUser = true;
        }
    }

    class HsbDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            onValueUpdate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onValueUpdate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            onValueUpdate();
        }

        private void onValueUpdate() {
            if (!isChangedByUser) {
                return;
            }
            isChangedByUser = false;
            try {
                float hue = Float.parseFloat(hueTextField.getText());
                float saturation = Float.parseFloat(saturationTextField.getText());
                float brightness = Float.parseFloat(brightnessTextField.getText());
                color = Color.getHSBColor(hue, saturation, brightness);
                syncToOthersFormat("HSB");
            } catch (NumberFormatException e) {
            }
            isChangedByUser = true;
        }
    }

    class CmykDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            onValueUpdate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onValueUpdate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            onValueUpdate();
        }

        private void onValueUpdate() {
            if (!isChangedByUser) {
                return;
            }
            isChangedByUser = false;
            try {
                float cyan = Float.parseFloat(cyanTextField.getText());
                float magenta = Float.parseFloat(magentaTextField.getText());
                float yellow = Float.parseFloat(yellowTextField.getText());
                float black = Float.parseFloat(blackTextField.getText());
                color = ColorUtils.cmyk2Color(cyan, magenta, yellow, black);
                syncToOthersFormat("CMYK");
            } catch (NumberFormatException e) {
            }
            isChangedByUser = true;
        }
    }
}
