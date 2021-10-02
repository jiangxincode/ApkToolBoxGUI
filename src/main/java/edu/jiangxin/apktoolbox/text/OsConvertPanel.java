package edu.jiangxin.apktoolbox.text;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.text.core.FileFilterWrapper;
import edu.jiangxin.apktoolbox.text.core.OsPatternConvert;
import edu.jiangxin.apktoolbox.utils.Constants;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class OsConvertPanel extends EasyPanel {
    private static final long serialVersionUID = 1L;

    private JPanel srcPanel;

    private JTextField srcTextField;

    private JButton srcButton;

    private JPanel optionPanel;

    private JLabel suffixLabel;

    private JTextField suffixTextField;

    private JLabel typeLabel;

    private JComboBox<String> typeComboBox;

    private JPanel operationPanel;

    private JButton convertButton;

    public OsConvertPanel() throws HeadlessException {
        super();
        initUI();
    }

    private void initUI() {
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

        convertButton = new JButton("Convert");
        convertButton.addMouseListener(new ConvertButtonMouseAdapter());

        operationPanel.add(convertButton);
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));

        suffixLabel = new JLabel("suffix");
        suffixTextField = new JTextField();
        suffixTextField.setText(conf.getString("osconvert.suffix"));
        optionPanel.add(suffixLabel);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(suffixTextField);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        typeLabel = new JLabel("Type:");
        typeComboBox = new JComboBox<String>();
        typeComboBox.addItem("tounix");
        typeComboBox.addItem("tomac");
        typeComboBox.addItem("towindows");

        optionPanel.add(typeLabel);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(typeComboBox);
    }

    private void createSrcPanel() {
        srcPanel = new JPanel();
        srcPanel.setLayout(new BoxLayout(srcPanel, BoxLayout.X_AXIS));

        srcTextField = new JTextField();
        srcTextField.setText(conf.getString("osconvert.src.dir"));

        srcButton = new JButton("Source Directory");
        srcButton.addMouseListener(new SrcButtonMouseAdapter());

        srcPanel.add(srcTextField);
        srcPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        srcPanel.add(srcButton);
    }

    private final class SrcButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.setDialogTitle("select a directory");
            int ret = jfc.showDialog(new JLabel(), null);
            switch (ret) {
                case JFileChooser.APPROVE_OPTION:
                    File file = jfc.getSelectedFile();
                    srcTextField.setText(file.getAbsolutePath());
                    break;
                default:
                    break;
            }

        }
    }

    private final class ConvertButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            File srcFile = new File(srcTextField.getText());
            if (!srcFile.exists() || !srcFile.isDirectory()) {
                logger.error("srcFile is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(OsConvertPanel.this, "Source file is invalid", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                srcTextField.requestFocus();
                return;
            }
            String srcPath;
            try {
                srcPath = srcFile.getCanonicalPath();
            } catch (IOException e2) {
                logger.error("getCanonicalPath fail");
                return;
            }
            conf.setProperty("osconvert.src.dir", srcPath);
            conf.setProperty("osconvert.suffix", suffixTextField.getText());
            ArrayList<File> files = new ArrayList<File>();
            files.addAll(new FileFilterWrapper().list(srcPath, suffixTextField.getText()));
            OsPatternConvert.osConvertFiles(files, typeComboBox.getSelectedItem().toString());
            logger.info("convert finish");
        }
    }

}
