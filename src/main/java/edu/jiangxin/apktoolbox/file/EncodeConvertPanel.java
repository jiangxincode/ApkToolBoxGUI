package edu.jiangxin.apktoolbox.file;

import edu.jiangxin.apktoolbox.file.core.EncoderConvert;
import edu.jiangxin.apktoolbox.file.core.EncoderDetector;
import edu.jiangxin.apktoolbox.swing.extend.AutoCompleteComboBox;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.FileListPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class EncodeConvertPanel extends EasyPanel {
    private static final long serialVersionUID = 1L;

    private static final int CONTENT_WIDTH = 500;

    private FileListPanel srcPanel;

    private JPanel optionPanel;

    private JLabel suffixLabel;

    private JTextField suffixTextField;

    private JCheckBox autoDetectCheckBox;

    private JLabel fromLabel;

    private AutoCompleteComboBox<String> fromComboBox;

    private JCheckBox recursiveCheckBox;

    private JLabel toLabel;

    private AutoCompleteComboBox<String> toComboBox;

    private JPanel operationPanel;

    private JButton convertButton;

    public EncodeConvertPanel() throws HeadlessException {
        super();
    }

    @Override
    public void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createSrcPanel();
        add(srcPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOptionPanel();
        add(optionPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);
    }

    private void createSrcPanel() {
        srcPanel = new FileListPanel();
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));

        suffixLabel = new JLabel("Suffix:");
        optionPanel.add(suffixLabel);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        suffixTextField = new JTextField();
        suffixTextField.setToolTipText("an array of extensions, ex. {\"java\",\"xml\"}. If this parameter is empty, all files are returned.");
        suffixTextField.setText(conf.getString("encodeconvert.suffix"));
        optionPanel.add(suffixTextField);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        recursiveCheckBox = new JCheckBox("Recursive");
        recursiveCheckBox.setSelected(true);
        optionPanel.add(recursiveCheckBox);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        fromLabel = new JLabel("From:");
        optionPanel.add(fromLabel);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        fromComboBox = new AutoCompleteComboBox<String>();
        fromComboBox.setEnabled(false);
        optionPanel.add(fromComboBox);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        autoDetectCheckBox = new JCheckBox("Auto Detect");
        autoDetectCheckBox.setSelected(true);
        autoDetectCheckBox.addItemListener(e -> fromComboBox.setEnabled(!(e.getStateChange() == ItemEvent.SELECTED)));
        optionPanel.add(autoDetectCheckBox);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        toLabel = new JLabel("To:");
        optionPanel.add(toLabel);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        toComboBox = new AutoCompleteComboBox<String>();
        toComboBox.setSelectedItem(conf.getString("encodeconvert.to"));
        optionPanel.add(toComboBox);

        for (String charset : Charset.availableCharsets().keySet()) {
            fromComboBox.addItem(charset);
            toComboBox.addItem(charset);
        }
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        convertButton = new JButton("Convert");
        convertButton.addActionListener(new ConvertButtonActionListener());

        operationPanel.add(convertButton);
    }

    private class ConvertButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            conf.setProperty("encodeconvert.suffix", suffixTextField.getText());
            conf.setProperty("encodeconvert.to", toComboBox.getSelectedItem().toString());

            try {
                List<File> fileList = new ArrayList<>();
                for (File file : srcPanel.getFileList()) {
                    String[] extensions = null;
                    if (StringUtils.isNotEmpty(suffixTextField.getText())) {
                        extensions = suffixTextField.getText().split(",");
                    }
                    fileList.addAll(FileUtils.listFiles(file, extensions, recursiveCheckBox.isSelected()));
                }
                Set<File> fileSet = new TreeSet<>(fileList);
                fileList.clear();
                fileList.addAll(fileSet);

                for (File file : fileList) {
                    logger.info("process: " + file.getCanonicalPath());
                    String fromEncoder;
                    if (autoDetectCheckBox.isSelected()) {
                        fromEncoder = EncoderDetector.judgeFile(file.getCanonicalPath());
                    } else {
                        fromEncoder = fromComboBox.getSelectedItem().toString();
                    }
                    logger.info("from: " + fromEncoder);
                    String toEncoder = toComboBox.getSelectedItem().toString();
                    EncoderConvert.encodeFile(file.getCanonicalPath(), fromEncoder, toEncoder);
                }

                logger.info("convert finish");
            } catch (IOException e1) {
                logger.error("convert fail", e1);
            }
        }
    }
}
