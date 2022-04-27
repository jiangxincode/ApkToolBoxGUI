package edu.jiangxin.apktoolbox.file.crack;

import edu.jiangxin.apktoolbox.file.crack.cracker.ICracker;
import edu.jiangxin.apktoolbox.file.crack.cracker.PDFCracker;
import edu.jiangxin.apktoolbox.file.crack.cracker.RarCracker;
import edu.jiangxin.apktoolbox.file.crack.cracker.ZipCracker;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 压缩解压zip文件的类
 * ref:https://doc.360qnw.com/web/#/p/2ad9e75ae0615dec5e016054cf905581
 * https://www.yunjiemi.net/Passper/index.html
 */
public final class CrackPanel extends EasyPanel {
    private JPanel optionPanel;

    private JPanel operationPanel;

    public CrackPanel() {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createOptionPanel();
        add(optionPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));

        JPanel optionTopPanel = new JPanel();
        optionTopPanel.setLayout(new BoxLayout(optionTopPanel, BoxLayout.X_AXIS));

        JPanel optionBottomPanel = new JPanel();
        optionBottomPanel.setLayout(new BoxLayout(optionBottomPanel, BoxLayout.X_AXIS));

        optionPanel.add(optionTopPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(optionBottomPanel);

        JRadioButton bruteForceRadioButton = new JRadioButton("Brute Force");
        JRadioButton dictionaryRadioButton = new JRadioButton("Dictionary");
        ButtonGroup crackTypeButtonGroup = new ButtonGroup();
        crackTypeButtonGroup.add(bruteForceRadioButton);
        crackTypeButtonGroup.add(dictionaryRadioButton);
        bruteForceRadioButton.setSelected(true);

        optionTopPanel.add(bruteForceRadioButton);
        optionTopPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionTopPanel.add(dictionaryRadioButton);

        JCheckBox numberCheckBox = new JCheckBox("Number");
        JCheckBox lowercaseLetterCheckBox = new JCheckBox("Lowercase Letter");
        JCheckBox uppercaseLetterCheckBox = new JCheckBox("Uppercase Letter");

        JLabel maxDigitLabel = new JLabel("Max Digits");
        JSpinner maxDigitSpinner = new JSpinner();
        maxDigitSpinner.setModel(new SpinnerNumberModel(6, 1, 9, 1));

        optionBottomPanel.add(numberCheckBox);
        optionBottomPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionBottomPanel.add(lowercaseLetterCheckBox);
        optionBottomPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionBottomPanel.add(uppercaseLetterCheckBox);
        optionBottomPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionBottomPanel.add(maxDigitLabel);
        optionBottomPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionBottomPanel.add(maxDigitSpinner);
        optionBottomPanel.add(Box.createHorizontalGlue());

    }

    private void createOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));

        JButton buttonCrackRar = new JButton("破解RAR类型文件");
        buttonCrackRar.addActionListener(new ActionAdapter() {
            public void run() {
                FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter(null, new String[]{"rar"});
                File file = getSelectedArchiverFile(fileNameExtensionFilter);
                if (file == null) {
                    return;
                }
                ICracker cracker = new RarCracker(file);
                onCrackArchiverFile(cracker);
            }
        });
        operationPanel.add(buttonCrackRar);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        JButton buttonCrackZip = new JButton("破解ZIP类型文件");
        buttonCrackZip.addActionListener(new ActionAdapter() {
            public void run() {
                FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter(null, new String[]{"zip"});
                File file = getSelectedArchiverFile(fileNameExtensionFilter);
                if (file == null) {
                    return;
                }
                ICracker cracker = new ZipCracker(file);
                onCrackArchiverFile(cracker);
            }
        });
        operationPanel.add(buttonCrackZip);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        JButton buttonCrackPdf = new JButton("破解PDF类型文件");
        buttonCrackPdf.addActionListener(new ActionAdapter() {
            public void run() {
                FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter(null, new String[]{"pdf"});
                File file = getSelectedArchiverFile(fileNameExtensionFilter);
                if (file == null) {
                    return;
                }
                ICracker cracker = new PDFCracker(file);
                onCrackArchiverFile(cracker);
            }
        });
        operationPanel.add(buttonCrackPdf);
    }

    private File getSelectedArchiverFile(FileNameExtensionFilter filter) {
        JFileChooser o = new JFileChooser(".");
        o.setFileSelectionMode(JFileChooser.FILES_ONLY);
        o.setMultiSelectionEnabled(false);
        o.addChoosableFileFilter(filter);
        int returnVal = o.showOpenDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return o.getSelectedFile();
    }

    private void onCrackArchiverFile(ICracker cracker) {
        if (!cracker.prepareCracker()) {
            JOptionPane.showMessageDialog(this, "没有找到测试程序，无法破解rar文件！");
            return;
        }
        String password = null;
        try {
            long t = System.currentTimeMillis();

            int numThreads = 10;
            int passwordLength = 6;
            boolean isEarlyTermination = true;

            ExecutorService workerPool = Executors.newFixedThreadPool(numThreads);
            PasswordFuture passwordFuture = new PasswordFuture();
            PasswordCrackerConsts consts = new PasswordCrackerConsts(numThreads, passwordLength, cracker);

            for (int i = 0; i < numThreads; i++) {
                workerPool.execute(new PasswordCrackerTask(i, isEarlyTermination, consts, passwordFuture));
            }
            try {
                password = passwordFuture.get();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerPool.shutdown();
            }

            t = System.currentTimeMillis() - t;
            System.out.println(t);

            if (password == null) {
                JOptionPane.showMessageDialog(this, "指定的密码无法解开文件!");
            } else {
                JOptionPane.showMessageDialog(this, password);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "破解过程中出错!");
        }
    }

    private class ActionAdapter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            run();
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        public void run() {
            JOptionPane.showMessageDialog(CrackPanel.this, "暂未实现，敬请期待");
        }
    }

}

