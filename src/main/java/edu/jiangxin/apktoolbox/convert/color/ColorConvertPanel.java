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

public class ColorConvertPanel extends EasyPanel {
    private Color color;

    private JSpinner redSpinner;

    private JSpinner greenSpinner;

    private JSpinner blueSpinner;

    private JTextField hexTextField;

    private JSpinner hueInHsbSpinner;

    private JSpinner saturationInHsbSpinner;

    private JSpinner brightnessInHsbSpinner;

    private JSpinner hueInHslSpinner;

    private JSpinner saturationInHslSpinner;

    private JSpinner lightnessInHslSpinner;

    private JSpinner cyanSpinner;

    private JSpinner magentaSpinner;

    private JSpinner yellowSpinner;

    private JSpinner blackSpinner;

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
        createHslPanel();

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
        hsbPanel.setBorder(BorderFactory.createTitledBorder("HSB/HSV"));

        JPanel xPanel = new JPanel();
        hsbPanel.add(xPanel);
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.X_AXIS));

        JLabel hueLabel = new JLabel("H(Hue, [0-360])");
        hueInHsbSpinner = new JSpinner();
        hueInHsbSpinner.setModel(new SpinnerNumberModel(0, 0, 360, 1));
        hueInHsbSpinner.addChangeListener(new HsbChangeListener());

        JLabel saturationLabel = new JLabel("S(Saturation, [0-100])");
        saturationInHsbSpinner = new JSpinner();
        saturationInHsbSpinner.setModel(new SpinnerNumberModel(100, 0, 100, 1));
        saturationInHsbSpinner.addChangeListener(new HsbChangeListener());


        JLabel brightnessLabel = new JLabel("B/V(Brightness/Value, [0-100])");
        brightnessInHsbSpinner = new JSpinner();
        brightnessInHsbSpinner.setModel(new SpinnerNumberModel(100, 0, 100, 1));
        brightnessInHsbSpinner.addChangeListener(new HsbChangeListener());

        xPanel.add(hueLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(hueInHsbSpinner);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(saturationLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(saturationInHsbSpinner);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(brightnessLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(brightnessInHsbSpinner);
    }

    private void createHslPanel() {
        JPanel hslPanel = new JPanel();
        add(hslPanel);
        hslPanel.setLayout(new BorderLayout());
        hslPanel.setBorder(BorderFactory.createTitledBorder("HSL"));

        JPanel xPanel = new JPanel();
        hslPanel.add(xPanel);
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.X_AXIS));

        JLabel hueLabel = new JLabel("H(Hue, [0-360])");
        hueInHslSpinner = new JSpinner();
        hueInHslSpinner.setModel(new SpinnerNumberModel(0, 0, 360, 1));
        hueInHslSpinner.addChangeListener(new HslChangeListener());

        JLabel saturationLabel = new JLabel("S(Saturation, [0-100])");
        saturationInHslSpinner = new JSpinner();
        saturationInHslSpinner.setModel(new SpinnerNumberModel(100, 0, 100, 1));
        saturationInHslSpinner.addChangeListener(new HslChangeListener());


        JLabel lightnessLabel = new JLabel("L(Lightness, [0-100])");
        lightnessInHslSpinner = new JSpinner();
        lightnessInHslSpinner.setModel(new SpinnerNumberModel(50, 0, 100, 1));
        lightnessInHslSpinner.addChangeListener(new HslChangeListener());

        xPanel.add(hueLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(hueInHslSpinner);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(saturationLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(saturationInHslSpinner);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(lightnessLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(lightnessInHslSpinner);
    }

    private void createCmykPanel() {
        JPanel cmykPanel = new JPanel();
        add(cmykPanel);
        cmykPanel.setLayout(new BorderLayout());
        cmykPanel.setBorder(BorderFactory.createTitledBorder("CMYK"));

        JPanel xPanel = new JPanel();
        cmykPanel.add(xPanel);
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.X_AXIS));

        JLabel cyanLabel = new JLabel("C(Cyan, [0-100])");
        cyanSpinner = new JSpinner();
        cyanSpinner.setModel(new SpinnerNumberModel(100, 0, 100, 1));
        cyanSpinner.addChangeListener(new CmykChangeListener());

        JLabel magentaLabel = new JLabel("M(Magenta, [0-100])");
        magentaSpinner = new JSpinner();
        magentaSpinner.setModel(new SpinnerNumberModel(100, 0, 100, 1));
        magentaSpinner.addChangeListener(new CmykChangeListener());


        JLabel yellowLabel = new JLabel("Y(Yellow, [0-100])");
        yellowSpinner = new JSpinner();
        yellowSpinner.setModel(new SpinnerNumberModel(100, 0, 100, 1));
        yellowSpinner.addChangeListener(new CmykChangeListener());

        JLabel blackLabel = new JLabel("K(Black, [0-100])");
        blackSpinner = new JSpinner();
        blackSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        blackSpinner.addChangeListener(new CmykChangeListener());

        xPanel.add(cyanLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(cyanSpinner);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(magentaLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(magentaSpinner);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(yellowLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(yellowSpinner);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(blackLabel);
        xPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        xPanel.add(blackSpinner);
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
            int[] hsbArray = ColorUtils.color2Hsb(color);
            hueInHsbSpinner.setValue(hsbArray[0]);
            saturationInHsbSpinner.setValue(hsbArray[1]);
            brightnessInHsbSpinner.setValue(hsbArray[2]);
        }
        if (!colorMode.equalsIgnoreCase("HSL")) {
            int[] hsbArray = ColorUtils.color2Hsl(color);
            hueInHslSpinner.setValue(hsbArray[0]);
            saturationInHslSpinner.setValue(hsbArray[1]);
            lightnessInHslSpinner.setValue(hsbArray[2]);
        }
        if (!colorMode.equalsIgnoreCase("CMYK")) {
            int[] cmykArray = ColorUtils.color2Cmyk(color);
            cyanSpinner.setValue(cmykArray[0]);
            magentaSpinner.setValue(cmykArray[1]);
            yellowSpinner.setValue(cmykArray[2]);
            blackSpinner.setValue(cmykArray[3]);
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

    class HsbChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (!isChangedByUser) {
                return;
            }
            isChangedByUser = false;
            int hue = (Integer) hueInHsbSpinner.getValue();
            int saturation = (Integer) saturationInHsbSpinner.getValue();
            int brightness = (Integer) brightnessInHsbSpinner.getValue();
            color = ColorUtils.hsb2Color(hue, saturation, brightness);
            syncToOthersFormat("HSB");
            isChangedByUser = true;
        }
    }

    class HslChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (!isChangedByUser) {
                return;
            }
            isChangedByUser = false;
            int hue = (Integer) hueInHslSpinner.getValue();
            int saturation = (Integer) saturationInHslSpinner.getValue();
            int lightness = (Integer) lightnessInHslSpinner.getValue();
            color = ColorUtils.hsl2Color(hue, saturation, lightness);
            syncToOthersFormat("HSL");
            isChangedByUser = true;
        }
    }

    class CmykChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (!isChangedByUser) {
                return;
            }
            isChangedByUser = false;
            int cyan = (Integer) cyanSpinner.getValue();
            int magenta = (Integer) magentaSpinner.getValue();
            int yellow = (Integer) yellowSpinner.getValue();
            int black = (Integer) blackSpinner.getValue();
            color = ColorUtils.cmyk2Color(cyan, magenta, yellow, black);
            syncToOthersFormat("CMYK");
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
}
