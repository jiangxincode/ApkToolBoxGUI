package edu.jiangxin.apktoolbox.convert.relationship;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;

/*
 * 介绍：关于“小”这个功能，在称呼存在年龄大小不同时，按钮会改变颜色，默认为比你大，点击后选择比你小。
 * 退格功能，就是返回上一步功能还没做，也不想做了。有兴趣自己看着加。
 * 当你能够确定关系输入过程中不存在年龄比较问题时，可以连续输入，不需要点“的”按钮。
 * 例如直接按“兄”->“姐”->“=”就可以得到答案 【姐姐】
 * 在这个函数initData()里面修改你的数据文件的位置。
 * 数据文件为一行一条数据（注意使用utf-8编码）。
 * 格式如下：[]{(父-)(母-)(兄-)(弟-)(姐-)(妹-)(夫-)(妻-)(儿-)(女-)}
 * 存在年龄比较时<称谓1><称谓2>，比你大的称谓在前面。
 */
public class RelationShipConvertPanel extends EasyPanel {
    static Hashtable<String, String> dataHashtable = new Hashtable<>();
    static String now = "你";
    static JTextField inputTextField;
    static JTextField outputTextField;
    static JButton olderButton;
    static boolean isOlder = true;

    public RelationShipConvertPanel() {
        super();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        inputTextField = new MyTextField();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(inputTextField, gbc);

        outputTextField = new MyTextField();
        outputTextField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        gbc.gridy = 1;
        add(outputTextField, gbc);

        ButtonListener btnListener = new ButtonListener();

        JButton fatherButton = new MyButton("父");
        fatherButton.addActionListener(btnListener);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(fatherButton, gbc);

        JButton motherButton = new MyButton("母");
        motherButton.addActionListener(btnListener);

        gbc.gridx = 1;
        add(motherButton, gbc);

        JButton backButton = new MyButton("←");
        backButton.addActionListener(btnListener);

        gbc.gridx = 2;
        add(backButton, gbc);

        JButton acButton = new MyButton("AC");
        acButton.addActionListener(btnListener);

        gbc.gridx = 3;
        add(acButton, gbc);

        JButton brotherButton = new MyButton("兄");
        brotherButton.addActionListener(btnListener);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(brotherButton, gbc);

        JButton littleBrotherButton = new MyButton("弟");
        littleBrotherButton.addActionListener(btnListener);

        gbc.gridx = 1;
        add(littleBrotherButton, gbc);

        JButton husbandButton = new MyButton("夫");
        husbandButton.addActionListener(btnListener);

        gbc.gridx = 2;
        add(husbandButton, gbc);

        olderButton = new MyButton("小");
        olderButton.addActionListener(btnListener);

        gbc.gridx = 3;
        add(olderButton, gbc);

        JButton sisterButton = new MyButton("姐");
        sisterButton.addActionListener(btnListener);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(sisterButton, gbc);

        JButton littleSisterButton = new MyButton("妹");
        littleSisterButton.addActionListener(btnListener);

        gbc.gridx = 1;
        add(littleSisterButton, gbc);

        JButton wifeButton = new MyButton("妻");
        wifeButton.addActionListener(btnListener);

        gbc.gridx = 2;
        add(wifeButton, gbc);

        JButton deButton = new MyButton("的");
        deButton.addActionListener(btnListener);

        gbc.gridx = 3;
        add(deButton, gbc);

        JButton sonButton = new MyButton("儿");
        sonButton.addActionListener(btnListener);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(sonButton, gbc);

        JButton daughterbButton = new MyButton("女");
        daughterbButton.addActionListener(btnListener);

        gbc.gridx = 1;
        add(daughterbButton, gbc);

        JButton resultButton = new MyButton("=");
        resultButton.addActionListener(btnListener);

        gbc.gridx = 2;
        gbc.gridwidth = 2;
        add(resultButton, gbc);

        initData();
    }

    private void initData() {
        try (BufferedReader bufferedReader = new BufferedReader(new StringReader(Constants.data))) {
            String tempString;
            while ((tempString = bufferedReader.readLine()) != null && tempString.length() > 3) {
                String key = StringUtils.substringBetween(tempString, "[", "]");
                String value = StringUtils.substringBetween(tempString, "{", "}");
                if (key != null && value != null) {
                    dataHashtable.put(key, value);
                }
            }
            logger.info("load data finished. count: " + dataHashtable.size());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "数据载入失败!");
        }
    }

    class MyTextField extends JTextField {
        public MyTextField() {
            super();
            Font font = new Font("宋体", Font.BOLD, 15);
            setFont(font);
            setEditable(false);
        }
    }

    class MyButton extends JButton {
        public MyButton(String text) {
            super(text);
            Font font = new Font("宋体", Font.BOLD, 30);
            setFont(font);
        }
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String which = ((JButton) e.getSource()).getText();
            switch (which) {
                case "AC":
                    now = "你";
                    isOlder = true;
                    inputTextField.setText("");
                    outputTextField.setText("");
                    break;
                case "=":
                    if (now.charAt(0) == '<') {
                        String[] oldChooseString = StringUtils.substringsBetween(now, "<", ">");
                        now = isOlder ? oldChooseString[0] : oldChooseString[1];
                        inputTextField.setText(inputTextField.getText() + (isOlder ? "(比你年长)" : "(比你年轻)"));
                    }
                    isOlder = true;
                    outputTextField.setText(now);
                    //now="你";
                    break;
                case "小":
                    isOlder = false;
                    break;
                case "←":

                    break;
                case "的":
                    String temp = "";
                    if (now.charAt(0) == '<') {
                        String[] oldChooseString = StringUtils.substringsBetween(now, "<", ">");
                        now = isOlder ? oldChooseString[0] : oldChooseString[1];
                        temp = isOlder ? "(比你年长)" : "(比你年轻)";
                    }
                    inputTextField.setText(inputTextField.getText() + temp + which);
                    isOlder = true;
                    break;
                default: //按下关系按钮时
                    inputTextField.setText(inputTextField.getText() + which);
                    now = getNext(which).substring(2);
                    if (now.charAt(0) == '<') {
                    }
                    isOlder = true;
                    break;
            }
        }

        String getNext(String what) {
            String relationsString = dataHashtable.get(now);
            String[] relation;
            if (relationsString != null) {
                relation = StringUtils.substringsBetween(relationsString, "(", ")");
            } else {
                outputTextField.setText("超出计算范围");
                return now;
            }
            return switch (what) {
                case "父" -> relation[0];
                case "母" -> relation[1];
                case "兄" -> relation[2];
                case "弟" -> relation[3];
                case "姐" -> relation[4];
                case "妹" -> relation[5];
                case "夫" -> relation[6];
                case "妻" -> relation[7];
                case "儿" -> relation[8];
                case "女" -> relation[9];
                default -> now;
            };

        }
    }
}
