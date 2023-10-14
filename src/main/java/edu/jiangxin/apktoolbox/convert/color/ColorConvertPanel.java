package edu.jiangxin.apktoolbox.convert.color;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

public class ColorConvertPanel extends EasyPanel {
    private Color color;

    private JPanel rgbPanel;

    private JSpinner redSpinner;

    private JSpinner greenSpinner;

    private JSpinner blueSpinner;

    private JPanel hexPanel;

    private JTextField hexTextField;

    private JPanel hsbPanel;

    private JTextField hueTextField;

    private JTextField saturationTextField;

    private JTextField brightnessTextField;

    private JPanel cmykPanel;

    private JTextField cyanTextField;

    private JTextField magentaTextField;

    private JTextField yellowTextField;

    private JTextField blackTextField;

    private JPanel operationPanel;

    private JScrollPane colorTableScrollPane;

    private JTextField colorBoxTextField;

    private JComboBox<String> colorTableTypeComboBox;

    private JTable colorTable;

    public ColorConvertPanel() throws HeadlessException {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createRgbPanel();
        add(rgbPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createHexPanel();
        add(hexPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createHsbPanel();
        add(hsbPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createCmykPanel();
        add(cmykPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOrdinaryColorTable();
        add(colorTableScrollPane);
    }

    private void createRgbPanel() {
        rgbPanel = new JPanel();
        rgbPanel.setLayout(new BoxLayout(rgbPanel, BoxLayout.X_AXIS));

        JLabel rebLabel = new JLabel("R(Red, [0-255])");
        redSpinner = new JSpinner();
        redSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));

        JLabel greenLabel = new JLabel("G(Green, [0-255])");
        greenSpinner = new JSpinner();
        greenSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));

        JLabel blueLabel = new JLabel("B(Blue, [0-255])");
        blueSpinner = new JSpinner();
        blueSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));

        rgbPanel.add(rebLabel);
        rgbPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        rgbPanel.add(redSpinner);
        rgbPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        rgbPanel.add(greenLabel);
        rgbPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        rgbPanel.add(greenSpinner);
        rgbPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        rgbPanel.add(blueLabel);
        rgbPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        rgbPanel.add(blueSpinner);
    }

    private void createHexPanel() {
        hexPanel = new JPanel();
        hexPanel.setLayout(new BoxLayout(hexPanel, BoxLayout.X_AXIS));

        JLabel hexLabel = new JLabel("Hex");
        hexTextField = new JTextField();
        hexTextField.setToolTipText("0x000000");
        hexTextField.setEditable(true);

        JLabel extraLabel = new JLabel("0xFFFFFF格式常用在通用语言中(Java/C++等), #FFFFFF格式常用在标记语言中(XML/HTML等)");

        hexPanel.add(hexLabel);
        hexPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        hexPanel.add(hexTextField);
        hexPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        hexPanel.add(extraLabel);
    }

    private void createHsbPanel() {
        hsbPanel = new JPanel();
        hsbPanel.setLayout(new BoxLayout(hsbPanel, BoxLayout.X_AXIS));

        JLabel hueLabel = new JLabel("H(Hue, [0-1])");
        hueTextField = new JTextField();
        hueTextField.setText(new DecimalFormat("0.0000").format(0.0f));


        JLabel saturationLabel = new JLabel("S(Saturation, [0-1])");
        saturationTextField = new JTextField();
        saturationTextField.setText(new DecimalFormat("0.0000").format(0.0f));


        JLabel brightnessLabel = new JLabel("B/V(Brightness or Value, [0-1])");
        brightnessTextField = new JTextField();
        brightnessTextField.setText(new DecimalFormat("0.0000").format(0.0f));

        hsbPanel.add(hueLabel);
        hsbPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        hsbPanel.add(hueTextField);
        hsbPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        hsbPanel.add(saturationLabel);
        hsbPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        hsbPanel.add(saturationTextField);
        hsbPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        hsbPanel.add(brightnessLabel);
        hsbPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        hsbPanel.add(brightnessTextField);
    }

    private void createCmykPanel() {
        cmykPanel = new JPanel();
        cmykPanel.setLayout(new BoxLayout(cmykPanel, BoxLayout.X_AXIS));

        JLabel cyanLabel = new JLabel("C(Cyan, [0-1])");
        cyanTextField = new JTextField();
        cyanTextField.setText(new DecimalFormat("0.0000").format(0.0f));


        JLabel magentaLabel = new JLabel("M(Magenta, [0-1])");
        magentaTextField = new JTextField();
        magentaTextField.setText(new DecimalFormat("0.0000").format(0.0f));


        JLabel yellowLabel = new JLabel("Y(Yellow, [0-1])");
        yellowTextField = new JTextField();
        yellowTextField.setText(new DecimalFormat("0.0000").format(0.0f));

        JLabel blaceLabel = new JLabel("K(Black, [0-1])");
        blackTextField = new JTextField();
        blackTextField.setText(new DecimalFormat("0.0000").format(0.0f));

        cmykPanel.add(cyanLabel);
        cmykPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        cmykPanel.add(cyanTextField);
        cmykPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        cmykPanel.add(magentaLabel);
        cmykPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        cmykPanel.add(magentaTextField);
        cmykPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        cmykPanel.add(yellowLabel);
        cmykPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        cmykPanel.add(yellowTextField);
        cmykPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        cmykPanel.add(blaceLabel);
        cmykPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        cmykPanel.add(blackTextField);
    }

    public void createOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));

        JButton rgb2OthersConvertBtn = new JButton("RGB->Others");
        rgb2OthersConvertBtn.addActionListener(e -> {
            int red = (Integer) redSpinner.getValue();
            int green = (Integer) greenSpinner.getValue();
            int blue = (Integer) blueSpinner.getValue();
            color = new Color(red, green, blue);
            syncToOthersFormat();
        });

        JButton hex2OthersConvertBtn = new JButton("Hex->Others");
        hex2OthersConvertBtn.addActionListener(e -> {
            String hex = hexTextField.getText();
            color = ColorUtils.hex2Color(hex);
            syncToOthersFormat();
        });

        JButton hsb2OthersConvertBtn = new JButton("HSB->Others");
        hsb2OthersConvertBtn.addActionListener(e -> {
            float hue = Float.valueOf(hueTextField.getText());
            float saturation = Float.valueOf(saturationTextField.getText());
            float brightness = Float.valueOf(brightnessTextField.getText());
            color = Color.getHSBColor(hue, saturation, brightness);
            syncToOthersFormat();
        });

        JButton cmyk2OthersConvertBtn = new JButton("CMYK->Others");
        cmyk2OthersConvertBtn.addActionListener(e -> {
            float cyan = Float.valueOf(cyanTextField.getText());
            float magenta = Float.valueOf(magentaTextField.getText());
            float yellow = Float.valueOf(yellowTextField.getText());
            float black = Float.valueOf(blackTextField.getText());
            color = ColorUtils.cmyk2Color(cyan, magenta, yellow, black);
            syncToOthersFormat();
        });

        colorTableTypeComboBox = new JComboBox<>();
        colorTableTypeComboBox.addItem("Ordinary Colors");
        colorTableTypeComboBox.addItem("Ral Colors");
        colorTableTypeComboBox.addItem("GSB Colors");
        colorTableTypeComboBox.setSelectedItem("Ordinary Colors");
        colorTableTypeComboBox.addItemListener(itemEvent -> {
            ColorDefaultTableModel model = (ColorDefaultTableModel) colorTable.getModel();
            if (itemEvent.getItem().equals("Ordinary Colors")) {
                model.setDataVector(ColorTableConstant.ORDINARY_COLORS_TABLE_ROW_DATA, ColorTableConstant.ORDINARY_COLORS_COLUMN_NAMES);
            } else if (itemEvent.getItem().equals("Ral Colors")) {
                model.setDataVector(ColorTableConstant.RAL_COLORS_TABLE_ROW_DATA, ColorTableConstant.RAL_COLORS_COLUMN_NAMES);
            } else {
                model.setDataVector(ColorTableConstant.ORDINARY_COLORS_TABLE_ROW_DATA, ColorTableConstant.ORDINARY_COLORS_COLUMN_NAMES);
            }
            onColorTableChanged();
        });

        colorBoxTextField = new JTextField();
        colorBoxTextField.setEditable(false);
        colorBoxTextField.setOpaque(false);

        operationPanel.add(rgb2OthersConvertBtn);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(hex2OthersConvertBtn);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(hsb2OthersConvertBtn);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(cmyk2OthersConvertBtn);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(colorTableTypeComboBox);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(colorBoxTextField);
    }

    private void createOrdinaryColorTable() {
        colorTable = new JTable(new ColorDefaultTableModel(ColorTableConstant.ORDINARY_COLORS_TABLE_ROW_DATA, ColorTableConstant.ORDINARY_COLORS_COLUMN_NAMES));
        colorTableScrollPane = new JScrollPane(colorTable);
        onColorTableChanged();
    }

    private void onColorTableChanged() {
        int columnCount = colorTable.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            colorTable.getColumn(colorTable.getColumnName(0)).setCellRenderer(new ColorTableCellRenderer());
        }
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
            renderer.setBackground(ColorUtils.hex2Color((String) colorTable.getValueAt(row, 3)));
            return renderer;
        }
    }

    private void syncToOthersFormat() {
        redSpinner.setValue(color.getRed());
        greenSpinner.setValue(color.getGreen());
        blueSpinner.setValue(color.getBlue());

        hexTextField.setText(ColorUtils.color2Hex(color));

        float[] hsbArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hueTextField.setText(new DecimalFormat("0.0000").format(hsbArray[0]));
        saturationTextField.setText(new DecimalFormat("0.0000").format(hsbArray[1]));
        brightnessTextField.setText(new DecimalFormat("0.0000").format(hsbArray[2]));

        float[] cmykArray = ColorUtils.color2Cmyk(color);
        cyanTextField.setText(new DecimalFormat("0.0000").format(cmykArray[0]));
        magentaTextField.setText(new DecimalFormat("0.0000").format(cmykArray[1]));
        yellowTextField.setText(new DecimalFormat("0.0000").format(cmykArray[2]));
        blackTextField.setText(new DecimalFormat("0.0000").format(cmykArray[3]));

        colorBoxTextField.setBackground(color);
        //paint the color box with the converted output color
        colorBoxTextField.setOpaque(true);
    }
}
