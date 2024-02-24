package edu.jiangxin.apktoolbox.convert.relationship;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class RelationShipConvertPanel extends EasyPanel {
    private static final Map<String, Map<String, String>> dataMap = new HashMap<>();

    private String now = "你";

    private JTextField inputTextField;
    private JTextField outputTextField;

    private final Stack<String> stack = new Stack<>();

    static {
        dataMap.put("不存在", Map.of("父", "不存在", "母", "不存在", "兄", "不存在", "弟", "不存在", "姐", "不存在", "妹", "不存在", "夫", "不存在", "妻", "不存在", "儿", "不存在", "女", "不存在"));
        dataMap.put("你", Map.of("父", "爸爸", "母", "妈妈", "兄", "哥哥", "弟", "弟弟", "姐", "姐姐", "妹", "妹妹", "夫", "老公", "妻", "老婆", "儿", "儿子", "女", "女儿"));
        dataMap.put("爸爸", Map.of("父", "爷爷", "母", "奶奶", "兄", "伯伯", "弟", "叔叔", "姐", "姑姑", "妹", "姑姑", "夫", "不存在", "妻", "妈妈", "儿", "<哥哥><弟弟>", "女", "<姐姐><妹妹>"));
        dataMap.put("妈妈", Map.of("父", "姥爷", "母", "姥姥", "兄", "舅舅", "弟", "舅舅", "姐", "大姨", "妹", "小姨", "夫", "爸爸", "妻", "不存在", "儿", "<哥哥><弟弟>", "女", "<姐姐><妹妹>"));
        dataMap.put("哥哥", Map.of("父", "爸爸", "母", "妈妈", "兄", "哥哥", "弟", "<哥哥><弟弟>", "姐", "姐姐", "妹", "<姐姐><妹妹>", "夫", "不存在", "妻", "嫂子", "儿", "侄子", "女", "侄女"));
        dataMap.put("弟弟", Map.of("父", "爸爸", "母", "妈妈", "兄", "<哥哥><弟弟>", "弟", "弟弟", "姐", "<姐姐><妹妹>", "妹", "妹妹", "夫", "不存在", "妻", "弟媳", "儿", "侄子", "女", "侄女"));
        dataMap.put("姐姐", Map.of("父", "爸爸", "母", "妈妈", "兄", "哥哥", "弟", "<哥哥><弟弟>", "姐", "姐姐", "妹", "<姐姐><妹妹>", "夫", "姐夫", "妻", "不存在", "儿", "外甥", "女", "外甥女"));
        dataMap.put("妹妹", Map.of("父", "爸爸", "母", "妈妈", "兄", "<哥哥><弟弟>", "弟", "弟弟", "姐", "<姐姐><妹妹>", "妹", "妹妹", "夫", "妹夫", "妻", "不存在", "儿", "外甥", "女", "外甥女"));
        dataMap.put("老婆", Map.of("父", "岳父", "母", "岳母", "兄", "大舅", "弟", "小舅", "姐", "大姨", "妹", "小姨", "夫", "你", "妻", "不存在", "儿", "儿子", "女", "女儿"));
        dataMap.put("儿子", Map.of("父", "你", "母", "老婆", "兄", "儿子", "弟", "儿子", "姐", "女儿", "妹", "女儿", "夫", "不存在", "妻", "儿媳", "儿", "孙子", "女", "孙女"));
        dataMap.put("女儿", Map.of("父", "你", "母", "老婆", "兄", "儿子", "弟", "儿子", "姐", "女儿", "妹", "女儿", "夫", "女婿", "妻", "不存在", "儿", "外孙", "女", "外孙女"));
        dataMap.put("伯伯", Map.of("父", "爷爷", "母", "奶奶", "兄", "伯伯", "弟", "<伯伯><叔叔>", "姐", "姑姑", "妹", "姑姑", "夫", "不存在", "妻", "婶婶", "儿", "<哥哥><弟弟>", "女", "<姐姐><妹妹>"));
        dataMap.put("叔叔", Map.of("父", "爷爷", "母", "奶奶", "兄", "<伯伯><叔叔>", "弟", "叔叔", "姐", "姑姑", "妹", "姑姑", "夫", "不存在", "妻", "婶婶", "儿", "<哥哥><弟弟>", "女", "<姐姐><妹妹>"));
        dataMap.put("姑姑", Map.of("父", "爷爷", "母", "奶奶", "兄", "<伯伯><叔叔>", "弟", "<伯伯><叔叔>", "姐", "姑姑", "妹", "姑姑", "夫", "姑父", "妻", "不存在", "儿", "<哥哥><弟弟>", "女", "<姐姐><妹妹>"));
        dataMap.put("岳父", Map.of("父", "爷爷", "母", "奶奶", "兄", "伯伯", "弟", "叔叔", "姐", "姑姑", "妹", "姑姑", "夫", "不存在", "妻", "岳母", "儿", "<哥哥><弟弟>", "女", "<姐姐><妹妹>"));
        dataMap.put("岳母", Map.of("父", "姥爷", "母", "姥姥", "兄", "舅舅", "弟", "舅舅", "姐", "大姨", "妹", "小姨", "夫", "岳父", "妻", "不存在", "儿", "<哥哥><弟弟>", "女", "<姐姐><妹妹>"));
    }

    public RelationShipConvertPanel() {
        super();
    }

    @Override
    public void initUI() {
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

        JButton obligateButton = new MyButton(" ");
        obligateButton.setEnabled(false);

        gbc.gridx = 3;
        add(obligateButton, gbc);

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
    }

    static class MyTextField extends JTextField {
        public MyTextField() {
            super();
            Font font = new Font("宋体", Font.BOLD, 15);
            setFont(font);
            setEditable(false);
        }
    }

    static class MyButton extends JButton {
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
                case "AC" -> {
                    now = "你";
                    inputTextField.setText("");
                    outputTextField.setText("");
                }
                case "=" -> outputTextField.setText(now);
                case "←" -> {
                    String inputText = inputTextField.getText();
                    String removedText = null;
                    if (inputText != null && inputText.length() >= 1) {
                        inputTextField.setText(inputText.substring(0, inputText.length() - 1));
                        removedText = inputText.substring(inputText.length() - 1);
                    }
                    if (!stack.isEmpty() && isRelationString(removedText)) {
                        now = stack.pop();
                        outputTextField.setText(now);
                    }
                }
                case "的" -> inputTextField.setText(inputTextField.getText() + which);
                default -> {
                    inputTextField.setText(inputTextField.getText() + which);
                    stack.push(now);
                    String result = getRelation(now, which);
                    if (isPossibleRelation(result)) {
                        result = getExactRelation(result);
                    }
                    if (result != null) {
                        now = result;
                        outputTextField.setText(now);
                    } else {
                        outputTextField.setText("超出计算范围");
                    }
                }
            }
        }
    }

    private static boolean isRelationString(String relationString) {
        return relationString != null && "父母兄弟姐妹夫妻儿女".contains(relationString);
    }

    private static String getRelation(String now, String what) {
        Map<String, String> relationsMap = dataMap.get(now);
        if (relationsMap == null) {
            return null;
        }
        return relationsMap.get(what);
    }

    private static boolean isPossibleRelation(String possibleRelation) {
        return possibleRelation != null && possibleRelation.charAt(0) == '<';
    }

    private static String getExactRelation(String possibleRelation) {
        String[] tmp = StringUtils.substringsBetween(possibleRelation, "<", ">");
        if (tmp != null && tmp.length == 2) {
            String message = "是否比";
            if ("伯伯".equals(tmp[0])) {
                message += "爸爸";
            } else if ("姐姐".equals(tmp[0]) || "哥哥".equals(tmp[0])) {
                message += "你";
            }
            message += "大？";
            int userChoose = JOptionPane.showConfirmDialog(null, message, "提示", JOptionPane.YES_NO_OPTION);
            boolean isOlder = userChoose == JOptionPane.YES_OPTION;
            return isOlder ? tmp[0] : tmp[1];
        } else {
            return null;
        }
    }
}
