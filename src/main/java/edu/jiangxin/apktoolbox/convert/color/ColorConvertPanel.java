package edu.jiangxin.apktoolbox.convert.color;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//https://www.sioe.cn/yingyong/yanse-rgb-16/
public class ColorConvertPanel  extends EasyPanel {
    String hexValue;
    String rgbValue;

    private JPanel hexPanel;

    private JLabel hexLabel;

    private JTextField hexTextField;

    private JPanel rgbPanel;

    private JLabel rgbLabel;

    private JTextField rgbTextField;

    private JPanel operationPanel;

    private JButton rgb2HexConvertBtn;

    private JButton hex2RgbConvertBtn;

    private JTextField colorBoxTextField;

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

        createOperationPanel();
        add(operationPanel);
    }

    private void createRgbPanel() {
        rgbPanel = new JPanel();
        rgbPanel.setLayout(new BoxLayout(rgbPanel, BoxLayout.X_AXIS));

        rgbLabel = new JLabel("RGB: ");
        rgbTextField = new JTextField();
        rgbTextField.setToolTipText("Enter an RGB value(255,182,193)");
        rgbTextField.setEditable(true);

        rgbPanel.add(rgbLabel);
        rgbPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        rgbPanel.add(rgbTextField);
    }

    private void createHexPanel() {
        hexPanel = new JPanel();
        hexPanel.setLayout(new BoxLayout(hexPanel, BoxLayout.X_AXIS));

        hexLabel = new JLabel("Hexadecimal: ");
        hexTextField = new JTextField();
        hexTextField.setToolTipText("Enter a hexadecimal value(#FFB6C1)");
        hexTextField.setEditable(true);

        hexPanel.add(hexLabel);
        hexPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        hexPanel.add(hexTextField);
    }

    public void createOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));

        rgb2HexConvertBtn = new JButton("RGB->Hex");
        rgb2HexConvertBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rgbValue = rgbTextField.getText();
                RgbColor rgbColor = new RgbColor(rgbValue);
                String hexValue = rgbColor.convert();
                colorBoxTextField.setBackground(new Color(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue()));
                hexTextField.setText(hexValue);
                //paint the color box with the converted output color
                colorBoxTextField.setOpaque(true);
            }
        });

        hex2RgbConvertBtn = new JButton("Hex->RGB");
        hex2RgbConvertBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hexValue = hexTextField.getText();
                HexColor hexColor = new HexColor(hexValue);
                String rgbValue = hexColor.convert();
                colorBoxTextField.setBackground(new Color(hexColor.getRed(), hexColor.getGreen(), hexColor.getBlue()));
                rgbTextField.setText(rgbValue);
                //paint the color box with the converted output color
                colorBoxTextField.setOpaque(true);
            }
        });

        colorBoxTextField = new JTextField();
        colorBoxTextField.setEditable(false);
        colorBoxTextField.setOpaque(false);

        operationPanel.add(rgb2HexConvertBtn);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(hex2RgbConvertBtn);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(colorBoxTextField);
    }
}
