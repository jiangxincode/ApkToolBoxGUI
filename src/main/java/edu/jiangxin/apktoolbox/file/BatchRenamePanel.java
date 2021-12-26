package edu.jiangxin.apktoolbox.file;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import java.awt.*;

public class BatchRenamePanel extends EasyPanel {
    private JPanel sourceFilePanel;

    private JPanel targetFilePanel;

    private JPanel renameRulePanel;

    private JPanel newFileConstructorPanel;

    private JPanel othersPanel;

    private JPanel statusPanel;

    public BatchRenamePanel() throws HeadlessException {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createSourceFilePanel();
        add(sourceFilePanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createTargetFilePanel();
        add(targetFilePanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createRenameRulePanel();
        add(renameRulePanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createNewFileConstructorPanel();
        add(newFileConstructorPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOthersPanel();
        add(othersPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createStatusPanel();
        add(statusPanel);
    }

    private void createSourceFilePanel() {
        sourceFilePanel = new JPanel();
        sourceFilePanel.setBorder(BorderFactory.createTitledBorder("1. 源文件"));
        sourceFilePanel.setLayout(new BorderLayout());

        JPanel secondLevelPanel = new JPanel();
        secondLevelPanel.setLayout(new BoxLayout(secondLevelPanel, BoxLayout.Y_AXIS));
        sourceFilePanel.add(secondLevelPanel);

        JPanel thirdLevelPanel1 = new JPanel();
        thirdLevelPanel1.setLayout(new BoxLayout(thirdLevelPanel1, BoxLayout.X_AXIS));

        JPanel thirdLevelPanel2 = new JPanel();
        thirdLevelPanel2.setLayout(new BoxLayout(thirdLevelPanel2, BoxLayout.X_AXIS));

        secondLevelPanel.add(thirdLevelPanel1);
        secondLevelPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_Y_BORDER));
        secondLevelPanel.add(thirdLevelPanel2);

        JLabel label = new JLabel("源文件所在目录：");
        JTextField textField = new JTextField();
        JButton button = new JButton("选择");
        thirdLevelPanel1.add(label);
        thirdLevelPanel1.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        thirdLevelPanel1.add(textField);
        thirdLevelPanel1.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        thirdLevelPanel1.add(button);

        JLabel label2 = new JLabel("源文件：");
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("*.JPG");
        comboBox.addItem("*.GIF");
        comboBox.addItem("*.*");
        JCheckBox checkBox = new JCheckBox("包括子目录");

        thirdLevelPanel2.add(label2);
        thirdLevelPanel2.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        thirdLevelPanel2.add(comboBox);
        thirdLevelPanel2.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        thirdLevelPanel2.add(checkBox);
    }

    private void createTargetFilePanel() {
        targetFilePanel = new JPanel();
        targetFilePanel.setBorder(BorderFactory.createTitledBorder("2. 目标文件"));
        targetFilePanel.setLayout(new BorderLayout());

        JPanel secondLevelPanel = new JPanel();
        secondLevelPanel.setLayout(new BoxLayout(secondLevelPanel, BoxLayout.X_AXIS));
        targetFilePanel.add(secondLevelPanel);

        JLabel label = new JLabel("存放新文件的目录：");
        JTextField textField = new JTextField();
        JButton button = new JButton("选择");
        secondLevelPanel.add(label);
        secondLevelPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        secondLevelPanel.add(textField);
        secondLevelPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        secondLevelPanel.add(button);
    }

    private void createRenameRulePanel() {
        renameRulePanel = new JPanel();
        renameRulePanel.setBorder(BorderFactory.createTitledBorder("3. 更名规则"));
        renameRulePanel.setLayout(new BorderLayout());

        JPanel secondLevelPanel = new JPanel();
        secondLevelPanel.setLayout(new BoxLayout(secondLevelPanel, BoxLayout.Y_AXIS));
        renameRulePanel.add(secondLevelPanel);

        JPanel thirdLevelPanel1 = new JPanel();
        thirdLevelPanel1.setLayout(new FlowLayout(FlowLayout.LEFT,10,3));

        JPanel thirdLevelPanel2 = new JPanel();
        thirdLevelPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,10,3));

        JPanel thirdLevelPanel3 = new JPanel();
        thirdLevelPanel3.setLayout(new FlowLayout(FlowLayout.LEFT,10,3));

        JPanel thirdLevelPanel4 = new JPanel();
        thirdLevelPanel4.setLayout(new FlowLayout(FlowLayout.LEFT,10,3));

        JPanel thirdLevelPanel5 = new JPanel();
        thirdLevelPanel5.setLayout(new FlowLayout(FlowLayout.LEFT,10,3));

        JPanel thirdLevelPanel6 = new JPanel();
        thirdLevelPanel6.setLayout(new FlowLayout(FlowLayout.LEFT,10,3));

        secondLevelPanel.add(thirdLevelPanel1);
        secondLevelPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_Y_BORDER));
        secondLevelPanel.add(thirdLevelPanel2);
        secondLevelPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_Y_BORDER));
        secondLevelPanel.add(thirdLevelPanel3);
        secondLevelPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_Y_BORDER));
        secondLevelPanel.add(thirdLevelPanel4);
        secondLevelPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_Y_BORDER));
        secondLevelPanel.add(thirdLevelPanel5);
        secondLevelPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_Y_BORDER));
        secondLevelPanel.add(thirdLevelPanel6);

        ButtonGroup buttonGroup = new ButtonGroup();

        {
            JRadioButton radioButton1 = new JRadioButton("只在文件重名时才更名");
            thirdLevelPanel1.add(radioButton1);
            buttonGroup.add(radioButton1);
        }

        {
            JRadioButton radioButton2 = new JRadioButton("将前缀{String}后的数字改为{Int}位");
            thirdLevelPanel2.add(radioButton2);
            buttonGroup.add(radioButton2);

            JLabel label21 = new JLabel("String");
            JTextField textField21 = new JTextField("");
            JLabel label22 = new JLabel("Int");
            JSpinner spinner21 = new JSpinner();
            spinner21.setValue(new Integer(4));
            thirdLevelPanel2.add(label21);
            thirdLevelPanel2.add(textField21);
            thirdLevelPanel2.add(label22);
            thirdLevelPanel2.add(spinner21);
        }

        {
            JRadioButton radioButton3 = new JRadioButton("重编号所有文件");
            radioButton3.setSelected(true);
            thirdLevelPanel3.add(radioButton3);
            buttonGroup.add(radioButton3);

            JCheckBox checkBox31 = new JCheckBox("编号先按原文件名排序");
            JCheckBox checkBox32 = new JCheckBox("按末尾数字排序");
            JCheckBox checkBox33 = new JCheckBox("用目录名为前缀");
            JLabel label31 = new JLabel("分隔字符");
            JTextField textField31 = new JTextField("_");
            JLabel label32 = new JLabel("前缀");
            JTextField textField32 = new JTextField("File");
            JLabel label33 = new JLabel("起始号码");
            JSpinner spinner31 = new JSpinner();
            spinner31.setValue(new Integer(1));
            JLabel label34 = new JLabel("号码位数");
            JSpinner spinner32 = new JSpinner();
            spinner31.setValue(new Integer(3));
            thirdLevelPanel3.add(checkBox31);
            thirdLevelPanel3.add(checkBox32);
            thirdLevelPanel3.add(checkBox33);
            thirdLevelPanel3.add(label31);
            thirdLevelPanel3.add(textField31);
            thirdLevelPanel3.add(label32);
            thirdLevelPanel3.add(textField32);
            thirdLevelPanel3.add(label33);
            thirdLevelPanel3.add(spinner31);
            thirdLevelPanel3.add(label34);
            thirdLevelPanel3.add(spinner32);
        }

        {
            JRadioButton radioButton4 = new JRadioButton("在第{Int}个字符后添加{String}");
            thirdLevelPanel4.add(radioButton4);
            buttonGroup.add(radioButton4);

            JLabel label41 = new JLabel("Int");
            JSpinner spinner41 = new JSpinner();
            spinner41.setValue(new Integer(0));
            JLabel label42 = new JLabel("String");
            JTextField textField42 = new JTextField("");
            thirdLevelPanel4.add(label41);
            thirdLevelPanel4.add(spinner41);
            thirdLevelPanel4.add(label42);
            thirdLevelPanel4.add(textField42);
        }

        {
            JRadioButton radioButton5 = new JRadioButton("删除自第{Int:1}个字符开始的{Int:2}个字符");
            thirdLevelPanel5.add(radioButton5);
            buttonGroup.add(radioButton5);

            JLabel label51 = new JLabel("Int:1");
            JSpinner spinner51 = new JSpinner();
            spinner51.setValue(new Integer(1));
            JLabel label52 = new JLabel("Int:2");
            JSpinner spinner52 = new JSpinner();
            spinner52.setValue(new Integer(1));
            thirdLevelPanel5.add(label51);
            thirdLevelPanel5.add(spinner51);
            thirdLevelPanel5.add(label52);
            thirdLevelPanel5.add(spinner52);
        }

        {
            JRadioButton radioButton6 = new JRadioButton("将文件名中出现的字符串{String:1}替换为{String:2}");
            thirdLevelPanel6.add(radioButton6);
            buttonGroup.add(radioButton6);

            JLabel label61 = new JLabel("String:1");
            JTextField textField61 = new JTextField("");
            JLabel label62 = new JLabel("String:2");
            JTextField textField62 = new JTextField("");
            thirdLevelPanel6.add(label61);
            thirdLevelPanel6.add(textField61);
            thirdLevelPanel6.add(label62);
            thirdLevelPanel6.add(textField62);
        }
    }

    private void createNewFileConstructorPanel() {
        newFileConstructorPanel = new JPanel();
        newFileConstructorPanel.setBorder(BorderFactory.createTitledBorder("4. 新文件的目录结构"));
        newFileConstructorPanel.setLayout(new BorderLayout());

        JPanel secondLevelPanel = new JPanel();
        secondLevelPanel.setLayout(new BoxLayout(secondLevelPanel, BoxLayout.Y_AXIS));
        newFileConstructorPanel.add(secondLevelPanel);

        ButtonGroup buttonGroup = new ButtonGroup();

        JRadioButton radioButton1 = new JRadioButton("所有文件均放在同一个目录下");
        radioButton1.setSelected(true);
        secondLevelPanel.add(radioButton1);
        buttonGroup.add(radioButton1);

        JRadioButton radioButton2 = new JRadioButton("保持原有目录结构");
        secondLevelPanel.add(radioButton2);
        buttonGroup.add(radioButton2);
    }

    private void createOthersPanel() {
        othersPanel = new JPanel();
        othersPanel.setBorder(BorderFactory.createTitledBorder("5. 其它"));
        othersPanel.setLayout(new BorderLayout());

        JPanel secondLevelPanel = new JPanel();
        secondLevelPanel.setLayout(new BoxLayout(secondLevelPanel, BoxLayout.Y_AXIS));
        othersPanel.add(secondLevelPanel);

        ButtonGroup buttonGroup = new ButtonGroup();

        JRadioButton radioButton1 = new JRadioButton("保留源文件");
        radioButton1.setSelected(true);
        secondLevelPanel.add(radioButton1);
        buttonGroup.add(radioButton1);

        JRadioButton radioButton2 = new JRadioButton("删除源文件");
        secondLevelPanel.add(radioButton2);
        buttonGroup.add(radioButton2);

        JRadioButton radioButton3 = new JRadioButton("删除源目录下所有文件及子目录");
        secondLevelPanel.add(radioButton3);
        buttonGroup.add(radioButton3);
    }

    private void createStatusPanel() {
        statusPanel = new JPanel();
        statusPanel.setBorder(BorderFactory.createTitledBorder("正在处理"));
        statusPanel.setLayout(new BorderLayout());

        JPanel secondLevelPanel = new JPanel();
        secondLevelPanel.setLayout(new BoxLayout(secondLevelPanel, BoxLayout.Y_AXIS));
        statusPanel.add(secondLevelPanel);

        JTextField textField = new JTextField();
        secondLevelPanel.add(textField);
    }
}
