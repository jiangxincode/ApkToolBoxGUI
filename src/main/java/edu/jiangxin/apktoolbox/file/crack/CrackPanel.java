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

    private JCheckBox numberCheckBox;
    private JCheckBox lowercaseLetterCheckBox;
    private JCheckBox uppercaseLetterCheckBox;

    private JSpinner minSpinner;
    private JSpinner maxSpinner;

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

        JPanel optionY1Panel = new JPanel();
        optionY1Panel.setLayout(new BoxLayout(optionY1Panel, BoxLayout.X_AXIS));

        JPanel optionY2Panel = new JPanel();
        optionY2Panel.setLayout(new BoxLayout(optionY2Panel, BoxLayout.X_AXIS));

        JPanel optionY3Panel = new JPanel();
        optionY3Panel.setLayout(new BoxLayout(optionY3Panel, BoxLayout.X_AXIS));

        optionPanel.add(optionY1Panel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(optionY2Panel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(optionY3Panel);

        JRadioButton bruteForceRadioButton = new JRadioButton("Brute Force");
        JRadioButton dictionaryRadioButton = new JRadioButton("Dictionary");
        ButtonGroup crackTypeButtonGroup = new ButtonGroup();
        crackTypeButtonGroup.add(bruteForceRadioButton);
        crackTypeButtonGroup.add(dictionaryRadioButton);
        bruteForceRadioButton.setSelected(true);

        optionY1Panel.add(bruteForceRadioButton);
        optionY1Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionY1Panel.add(dictionaryRadioButton);

        numberCheckBox = new JCheckBox("Number");
        numberCheckBox.setSelected(true);
        lowercaseLetterCheckBox = new JCheckBox("Lowercase Letter");
        uppercaseLetterCheckBox = new JCheckBox("Uppercase Letter");

        optionY2Panel.add(numberCheckBox);
        optionY2Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionY2Panel.add(lowercaseLetterCheckBox);
        optionY2Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionY2Panel.add(uppercaseLetterCheckBox);

        JLabel minLabel = new JLabel("Minimum Length: ");
        JLabel maxLabel = new JLabel("Maximum Length: ");

        minSpinner = new JSpinner();
        minSpinner.setModel(new SpinnerNumberModel(1, 1, 9, 1));
        minSpinner.setToolTipText("Minimum Length");

        maxSpinner = new JSpinner();
        maxSpinner.setModel(new SpinnerNumberModel(6, 1, 9, 1));
        maxSpinner.setToolTipText("Maximum Length");

        optionY3Panel.add(minLabel);
        optionY3Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionY3Panel.add(minSpinner);
        optionY3Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionY3Panel.add(maxLabel);
        optionY3Panel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionY3Panel.add(maxSpinner);

    }

    private void createOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));

        JButton buttonCrackRar = new JButton("Crack RAR File");
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

        JButton buttonCrackZip = new JButton("Crack ZIP File");
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

        JButton buttonCrackPdf = new JButton("Crack PDF File");
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
            JOptionPane.showMessageDialog(this, "Crack condition is not ready! Check the condition");
            return;
        }
        StringBuffer tmpSb = new StringBuffer();
        if (numberCheckBox.isSelected()) {
            tmpSb.append("0123456789");
        }
        if (lowercaseLetterCheckBox.isSelected()) {
            tmpSb.append("abcdefghijklmnopqrstuvwxyz");
        }
        if (uppercaseLetterCheckBox.isSelected()) {
            tmpSb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        if (tmpSb.length() <= 0) {
            JOptionPane.showMessageDialog(this, "Character set is empty!");
            return;
        }
        final String charSet = tmpSb.toString();

        int minLength = (Integer) minSpinner.getValue();
        int maxLength = (Integer) maxSpinner.getValue();
        if (minLength > maxLength) {
            JOptionPane.showMessageDialog(this, "Minimum length is bigger than maximum length!");
            return;
        }
        String password = null;
        try {
            for (int length = minLength; length <= maxLength; length++) {
                long startTime = System.currentTimeMillis();
                int numThreads = getThreadCount(charSet.length(), length);
                logger.info("Current attempt length: " + length + ", thread number: " + numThreads);
                boolean isEarlyTermination = true;

                ExecutorService workerPool = Executors.newFixedThreadPool(numThreads);
                PasswordFuture passwordFuture = new PasswordFuture(numThreads);
                PasswordCrackerConsts consts = new PasswordCrackerConsts(numThreads, length, cracker, charSet);

                for (int taskId = 0; taskId < numThreads; taskId++) {
                    workerPool.execute(new PasswordCrackerTask(taskId, isEarlyTermination, consts, passwordFuture));
                }
                try {
                    logger.info("before");
                    password = passwordFuture.get();
                    logger.info("after password: " + password);
                } catch (Exception e) {
                    logger.info("Exception test: ", e);
                } finally {
                    workerPool.shutdown();
                }
                long endTime = System.currentTimeMillis();
                logger.info("Current attempt length: " + length + ", Cost time: " + (endTime - startTime) + "ms");
                if (password != null) {
                    break;
                }
            }
            if (password == null) {
                JOptionPane.showMessageDialog(this, "指定的密码无法解开文件!");
            } else {
                JOptionPane.showMessageDialog(this, password);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "破解过程中出错!");
        }
    }

    private int getThreadCount(int charSetSize, int length) {
        int result = 1;
        for (int i = 1; i <= length; i++) {
            result *= charSetSize;
        }
        if (result > 1000) {
            result = 1000;
        }
        return result;
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

