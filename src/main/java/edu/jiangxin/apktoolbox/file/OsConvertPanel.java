package edu.jiangxin.apktoolbox.file;

import edu.jiangxin.apktoolbox.file.core.OsPatternConvert;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.FileListPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class OsConvertPanel extends EasyPanel {
    private static final long serialVersionUID = 1L;

    private FileListPanel srcPanel;

    private JPanel optionPanel;

    private JTextField suffixTextField;

    private JCheckBox recursiveCheckBox;

    private JComboBox<String> typeComboBox;

    private JPanel operationPanel;

    public OsConvertPanel() throws HeadlessException {
        super();
    }

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createSrcPanel();
        add(srcPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOptionPanel();
        add(optionPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));

        JButton convertButton = new JButton("Convert");
        convertButton.addActionListener(new ConvertButtonActionListener());

        operationPanel.add(convertButton);
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));

        JLabel suffixLabel = new JLabel("Suffix:");
        suffixTextField = new JTextField();
        suffixTextField.setToolTipText("an array of extensions, ex. {\"java\",\"xml\"}. If this parameter is empty, all files are returned.");
        suffixTextField.setText(conf.getString("osconvert.suffix"));
        optionPanel.add(suffixLabel);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(suffixTextField);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        recursiveCheckBox = new JCheckBox("Recursive");
        recursiveCheckBox.setSelected(true);
        optionPanel.add(recursiveCheckBox);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        JLabel typeLabel = new JLabel("Type:");
        typeComboBox = new JComboBox<>();
        typeComboBox.addItem("Convert to UNIX(LF Only)");
        typeComboBox.addItem("Convert to Macintosh(CR Only)");
        typeComboBox.addItem("Convert to Windows(CR+LF)");

        optionPanel.add(typeLabel);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(typeComboBox);
    }

    private void createSrcPanel() {
        srcPanel = new FileListPanel();
    }

    private final class ConvertButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            conf.setProperty("osconvert.suffix", suffixTextField.getText());
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
            OsPatternConvert.osConvertFiles(fileList, convertShowNameToPattern(typeComboBox.getSelectedItem().toString()));
            logger.info("convert finish");
        }
    }

    private String convertShowNameToPattern(String showName) {
        switch (showName) {
            case "Convert to UNIX(LF Only)":
                return "tounix";
            case "Convert to Macintosh(CR Only)":
                return "tomac";
            case "Convert to Windows(CR+LF)":
                return "towindows";
        }
        return "towindows";
    }
}
