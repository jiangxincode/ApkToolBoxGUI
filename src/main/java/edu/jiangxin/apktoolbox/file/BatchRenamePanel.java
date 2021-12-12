package edu.jiangxin.apktoolbox.file;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import java.awt.*;

public class BatchRenamePanel extends EasyPanel {
    private JPanel sourceFilePanel;

    private JLabel sourceFileLabel;

    private JButton sourceFileButton;

    private JTextField sourceFileTextField;


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
    }

    private void createTargetFilePanel() {
        targetFilePanel = new JPanel();
        targetFilePanel.setBorder(BorderFactory.createTitledBorder("2. 目标文件"));
        targetFilePanel.setLayout(new BorderLayout());
    }

    private void createRenameRulePanel() {
        renameRulePanel = new JPanel();
        renameRulePanel.setBorder(BorderFactory.createTitledBorder("3. 更名规则"));
        renameRulePanel.setLayout(new BorderLayout());
    }

    private void createNewFileConstructorPanel() {
        newFileConstructorPanel = new JPanel();
        newFileConstructorPanel.setBorder(BorderFactory.createTitledBorder("4. 新文件的目录结构"));
        newFileConstructorPanel.setLayout(new BorderLayout());
    }

    private void createOthersPanel() {
        othersPanel = new JPanel();
        othersPanel.setBorder(BorderFactory.createTitledBorder("5. 其它"));
        othersPanel.setLayout(new BorderLayout());
    }

    private void createStatusPanel() {
        statusPanel = new JPanel();
        statusPanel.setBorder(BorderFactory.createTitledBorder("正在处理"));
        statusPanel.setLayout(new BorderLayout());
    }
}
