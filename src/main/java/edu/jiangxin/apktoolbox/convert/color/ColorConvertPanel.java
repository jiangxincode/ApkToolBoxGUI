package edu.jiangxin.apktoolbox.convert.color;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//https://www.sioe.cn/yingyong/yanse-rgb-16/
public class ColorConvertPanel  extends EasyPanel {
    String hexValue;
    String rgbValue;

    public static JTextField textFieldHex;
    public static JTextField textFieldRgb;
    private JButton btnConvert;
    private JLabel lblResultOfConversion;
    private JTextField colorBox;

    public ColorConvertPanel() throws HeadlessException {
        super();
        initUI();
    }

    private void initUI() {
        JLabel lblHex = new JLabel("Hexadecimal: ");
        JLabel lblRgb = new JLabel("RGB: ");
        lblResultOfConversion = new JLabel("");

        setBorder(new EmptyBorder(5, 5, 5, 5));

        //hexadecimal input field
        textFieldHex = new JTextField();
        textFieldHex.setToolTipText("Enter a hexadecimal value(#FFB6C1)");
        textFieldHex.setEditable(true);
        textFieldHex.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                textFieldRgb.setText("");
            }
        });

        //convert button; when action performed,
        //between color types
        btnConvert = new JButton("Convert");
        btnConvert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hexValue = textFieldHex.getText();
                rgbValue = textFieldRgb.getText();

                try {
                    processConversion(hexValue, rgbValue);
                } catch (Exception e1) {
                    System.out.println("Exception: " + e1);
                    e1.printStackTrace();
                }
            }
        });

        //RGB input field
        textFieldRgb = new JTextField();
        textFieldRgb.setToolTipText("Enter an RGB value(255,182,193)");
        textFieldRgb.setEditable(true);
        textFieldRgb.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                textFieldHex.setText("");
            }
        });

        //color box
        colorBox = new JTextField();
        colorBox.setEditable(false);
        colorBox.setOpaque(false);

        GroupLayout gl_contentPane = new GroupLayout(this);
        gl_contentPane.setHorizontalGroup(
                //set horizontal gaps between elements
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblHex)
                                        .addComponent(lblRgb))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(textFieldRgb, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                                        .addComponent(textFieldHex, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                        .addComponent(colorBox)
                                        .addComponent(lblResultOfConversion))
                                .addGap(19)
                                .addComponent(btnConvert)
                                .addGap(19))
        );
        gl_contentPane.setVerticalGroup(
                //set vertical gaps between elements and set height of each element
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(29)
                                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblRgb)
                                                        .addComponent(textFieldRgb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(11)
                                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblHex)
                                                        .addComponent(textFieldHex, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
                                                .addGap(11)
                                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblResultOfConversion))
                                                .addGap(11)
                                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(colorBox, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addGap(41)
                                                .addComponent(btnConvert, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
                                .addGap(28))
        );
        setLayout(gl_contentPane);

    }

    public void processConversion(String inputHexValue, String inputRgbValue) {
        String outputRgb, outputHex;
        RgbColor inputRgbColor = new RgbColor(inputRgbValue);
        outputHex = inputRgbColor.convert();

        HexColor inputHexColor = new HexColor(inputHexValue);
        outputRgb = inputHexColor.convert();

        //if the user input a hex value
        if(!outputRgb.equals("")) {

            lblResultOfConversion.setText(inputHexValue + " is " + outputRgb);
            colorBox.setBackground(new Color(inputHexColor.getRed(), inputHexColor.getGreen(), inputHexColor.getBlue()));
        }

        //if the user input an RGB value
        if(!outputHex.equals("")) {
            lblResultOfConversion.setText(inputRgbValue + " is " + outputHex);
            colorBox.setBackground(new Color(inputRgbColor.getRed(), inputRgbColor.getGreen(), inputRgbColor.getBlue()));
        }

        //reset the text fields to prepare for another entry
        textFieldRgb.setText("");
        textFieldHex.setText("");

        //paint the color box with the converted output color
        colorBox.setOpaque(true);
    }
}
