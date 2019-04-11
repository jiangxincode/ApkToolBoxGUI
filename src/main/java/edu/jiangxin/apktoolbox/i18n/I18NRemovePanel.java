package edu.jiangxin.apktoolbox.i18n;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import edu.jiangxin.apktoolbox.swing.extend.JEasyPanel;

public class I18NRemovePanel extends JEasyPanel {
    private static final long serialVersionUID = 1L;

    List<I18NInfo> infos = new ArrayList<I18NInfo>();

    private static final String charset = "UTF-8";

    public I18NRemovePanel() throws HeadlessException {
        super();
        setPreferredSize(new Dimension(600, 160));
        setMaximumSize(new Dimension(600, 160));

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        JPanel sourcePanel = new JPanel();
        sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.X_AXIS));
        add(sourcePanel);

        JTextField srcTextField = new JTextField();
        srcTextField.setText(conf.getString("i18n.remove.src.dir"));

        JButton srcButton = new JButton("Source Directory");
        srcButton.addMouseListener(new MouseAdapter() {
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
        });

        sourcePanel.add(srcTextField);
        sourcePanel.add(srcButton);

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
        add(itemPanel);

        JTextField itemTextField = new JTextField();
        itemTextField.setText(conf.getString("i18n.remove.items"));

        JLabel itemLabel = new JLabel("Items");

        itemPanel.add(itemTextField);
        itemPanel.add(itemLabel);

        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        add(operationPanel);

        JButton sceenshotButton = new JButton(bundle.getString("i18n.remove.title"));
        sceenshotButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                infos.clear();
                File srcFile = new File(srcTextField.getText());
                if (!srcFile.exists() || !srcFile.isDirectory()) {
                    logger.error("srcFile is invalid");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(I18NRemovePanel.this, "Source directory is invalid", "ERROR",
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
                conf.setProperty("i18n.remove.src.dir", srcPath);

                String item = itemTextField.getText();
                if (StringUtils.isEmpty(item)) {
                    logger.error("item is empty");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(I18NRemovePanel.this, "item is empty", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    itemTextField.requestFocus();
                    return;
                }

                conf.setProperty("i18n.remove.items", item);
                remove(srcPath, itemTextField.getText());
            }
        });

        operationPanel.add(sceenshotButton);
    }

    private void remove(String sourceBaseStr, String itemName) {
        File[] sourceParentFiles = new File(sourceBaseStr).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith("values");
            }
        });
        if (sourceParentFiles == null) {
            logger.error("None valid directory found");
            return;
        }
        int count = 0;
        for (File sourceParentFile : sourceParentFiles) {
            File sourceFile = new File(sourceParentFile, "strings.xml");
            if (sourceFile.exists()) {
                try {
                    System.out.println("read from: " + sourceFile.getCanonicalPath());
                    String content = FileUtils.readFileToString(sourceFile, charset);
                    Pattern pattern = Pattern.compile("\\s*<string name=\"" + itemName + "\".*>.*</string>");
                    Matcher matcher = pattern.matcher(content);
                    String resultString = matcher.replaceAll("");
                    FileUtils.writeStringToFile(sourceFile, resultString, charset);
                    logger.info("remove success, count: " + (++count) + ", and file: " + sourceFile);
                } catch (IOException e) {
                    logger.error("remove exception: " + sourceFile, e);
                    continue;
                }
            }
        }
    }
}
