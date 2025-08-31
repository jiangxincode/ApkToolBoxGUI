package edu.jiangxin.apktoolbox.android.dumpsys;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.filepanel.FilePanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DumpsysPanel extends EasyPanel {
    private static final long serialVersionUID = 1L;

    private FilePanel targetFilePanel;

    private JPanel optionPanel;
    private JPanel dumpsysTypeChoosePanel;

    private JPanel operationPanel;
    private JButton startButton;


    public DumpsysPanel() {
        super();
    }

    @Override
    public void initUI() {
        setPreferredSize(new Dimension(Constants.DEFAULT_PANEL_WIDTH, Constants.DEFAULT_PANEL_HEIGHT));

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        createTargetPanel();
        add(targetFilePanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOptionPanel();
        add(optionPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);

    }

    private void createTargetPanel() {
        targetFilePanel = new FilePanel("Target Directory");
        targetFilePanel.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(optionPanel, BoxLayout.Y_AXIS);
        optionPanel.setLayout(boxLayout);

        dumpsysTypeChoosePanel = new JPanel();
        dumpsysTypeChoosePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));

        JCheckBox inputCheckBox = new JCheckBox("input");
        inputCheckBox.setSelected(true);
        JCheckBox windowCheckBox = new JCheckBox("window");
        dumpsysTypeChoosePanel.add(windowCheckBox);
        JCheckBox SurfaceFlingerCheckBox = new JCheckBox("SurfaceFlinger");
        dumpsysTypeChoosePanel.add(SurfaceFlingerCheckBox);
        JCheckBox activityCheckBox = new JCheckBox("activity");
        dumpsysTypeChoosePanel.add(activityCheckBox);
        JCheckBox accessibilityCheckBox = new JCheckBox("accessibility");
        dumpsysTypeChoosePanel.add(accessibilityCheckBox);
        JCheckBox packageCheckBox = new JCheckBox("package");
        dumpsysTypeChoosePanel.add(packageCheckBox);
        JCheckBox alarmCheckBox = new JCheckBox("alarm");
        dumpsysTypeChoosePanel.add(alarmCheckBox);
        JCheckBox inputMethodCheckBox = new JCheckBox("input_method");
        dumpsysTypeChoosePanel.add(inputMethodCheckBox);
        JCheckBox sensorServiceCheckBox = new JCheckBox("sensorservice");
        dumpsysTypeChoosePanel.add(sensorServiceCheckBox);
        JCheckBox accountCheckBox = new JCheckBox("account");
        dumpsysTypeChoosePanel.add(accountCheckBox);
        JCheckBox displayCheckBox = new JCheckBox("display");
        dumpsysTypeChoosePanel.add(displayCheckBox);
        JCheckBox powerCheckBox = new JCheckBox("power");
        dumpsysTypeChoosePanel.add(powerCheckBox);
        JCheckBox batteryCheckBox = new JCheckBox("battery");
        dumpsysTypeChoosePanel.add(batteryCheckBox);

        optionPanel.add(dumpsysTypeChoosePanel);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(operationPanel, BoxLayout.X_AXIS);
        operationPanel.setLayout(boxLayout);

        startButton = new JButton("Start");
        startButton.setEnabled(true);
        startButton.addActionListener(new OperationButtonActionListener());
        operationPanel.add(startButton);
    }

    class OperationButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            startButton.setEnabled(false);
            File targetDirFile = targetFilePanel.getFile();
            if (!targetDirFile.exists()) {
                JOptionPane.showMessageDialog(getFrame(), "Target directory does not exist!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis()));
            File targetDirWithTimestamp = new File(targetDirFile, timeStamp);

            if (!targetDirWithTimestamp.exists()) {
                boolean success = targetDirWithTimestamp.mkdirs();
                if (!success) {
                    JOptionPane.showMessageDialog(getFrame(), "Create target directory failed!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Component[] components = dumpsysTypeChoosePanel.getComponents();
            for (Component component : components) {
                if (component instanceof JCheckBox checkBox) {
                    String dumpsysType = checkBox.getText();
                    if (checkBox.isSelected()) {
                        dumpsysAndSaveToFile(dumpsysType, targetDirWithTimestamp);
                    }
                }
            }
            startButton.setEnabled(true);
        }

        private void dumpsysAndSaveToFile(String dumpsysType, File targetDir) {
            String command = "adb shell dumpsys " + dumpsysType;
            CommandLine cmdLine = CommandLine.parse(command);
            DefaultExecutor executor = new DefaultExecutor();
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                 ByteArrayOutputStream errorStream = new ByteArrayOutputStream();) {
                PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
                executor.setStreamHandler(streamHandler);
                int exitValue = executor.execute(cmdLine);
                if (exitValue == 0) {
                    String result = outputStream.toString().trim();
                    if (StringUtils.isNotEmpty(result)) {
                        File outputFile = new File(targetDir, dumpsysType + ".dumpsys");
                        FileUtils.writeStringToFile(outputFile, result, "UTF-8");
                    }
                } else {
                    String errorResult = errorStream.toString().trim();
                    if (StringUtils.isNotEmpty(errorResult)) {
                        File errorFile = new File(targetDir, dumpsysType + ".error");
                        FileUtils.writeStringToFile(errorFile, errorResult, "UTF-8");
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(getFrame(), "Execute command failed:\n" + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
