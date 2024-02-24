package edu.jiangxin.apktoolbox.android.i18n;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.jiangxin.apktoolbox.swing.extend.listener.SelectDirectoryListener;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class I18nFindLongestPanel extends EasyPanel {
    private static final long serialVersionUID = 1L;

    List<I18nInfo> infos = new ArrayList<I18nInfo>();

    private JTextField srcTextField;

    private JTextField itemTextField;

    public I18nFindLongestPanel() throws HeadlessException {
        super();
    }

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createSourcePanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createItemPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createOperationPanel();
    }

    private void createOperationPanel() {
        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        add(operationPanel);

        JButton findButton = new JButton(bundle.getString("android.i18n.longest.find"));
        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infos.clear();

                String srcPath = checkAndGetDirContent(srcTextField, "android.i18n.longest.src.dir", "Source directory is invalid");
                if (StringUtils.isEmpty(srcPath)) {
                    return;
                }

                String item = checkAndGetStringContent(itemTextField, "android.i18n.longest.items", "Item is empty");
                if (StringUtils.isEmpty(item)) {
                    return;
                }

                sort(srcPath, itemTextField.getText());
                if (CollectionUtils.isEmpty(infos)) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(I18nFindLongestPanel.this, "Failed, please see the log", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    I18nInfo info = infos.get(0);
                    StringBuilder sb = new StringBuilder();
                    sb.append("length: ").append(info.length).append(System.getProperty("line.separator"))
                            .append("text: ").append(info.text).append(System.getProperty("line.separator"))
                            .append("path: ").append(info.path);
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(I18nFindLongestPanel.this, sb.toString(), "INFO",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        operationPanel.add(findButton);
    }

    private void createItemPanel() {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
        add(itemPanel);
        
        itemTextField = new JTextField();
        itemTextField.setText(conf.getString("android.i18n.longest.items"));

        JLabel itemLabel = new JLabel("Items");

        itemPanel.add(itemTextField);
        itemPanel.add(Box.createHorizontalGlue());
        itemPanel.add(itemLabel);
    }

    private void createSourcePanel() {
        JPanel sourcePanel = new JPanel();
        sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.X_AXIS));
        add(sourcePanel);
        
        srcTextField = new JTextField();
        srcTextField.setText(conf.getString("android.i18n.longest.src.dir"));

        JButton srcButton = new JButton("Source Directory");
        srcButton.addActionListener(new SelectDirectoryListener("select a directory", srcTextField));

        sourcePanel.add(srcTextField);
        sourcePanel.add(Box.createHorizontalGlue());
        sourcePanel.add(srcButton);
    }

    private String getCanonicalPath(File file) {
        if (file == null) {
            logger.error("file is null");
            return null;
        }
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            logger.error("getCanonicalPath failed: " + file.getAbsolutePath(), e);
            return null;
        }
    }

    private void sort(String sourceBaseStr, String itemName) {
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
        for (File sourceParentFile : sourceParentFiles) {
            File sourceFile = new File(sourceParentFile, "strings.xml");
            if (sourceFile.exists()) {
                SAXBuilder builder = new SAXBuilder();
                Document sourceDoc;
                try {
                    sourceDoc = builder.build(sourceFile);
                } catch (JDOMException | IOException e) {
                    logger.error("build failed: " + sourceFile, e);
                    continue;
                }
                Element sourceRoot = sourceDoc.getRootElement();
                for (Element child : sourceRoot.getChildren()) {
                    String value = child.getAttributeValue("name");
                    if (value != null && value.equals(itemName)) {
                        String text = child.getText();
                        if (text != null) {
                            I18nInfo info = new I18nInfo(getCanonicalPath(sourceFile), text, text.length());
                            infos.add(info);
                            break;
                        }
                    }
                }

            }

        }
        Collections.sort(infos, new Comparator<I18nInfo>() {
            @Override
            public int compare(I18nInfo o1, I18nInfo o2) {
                return o2.length - o1.length;
            }
        });

        logger.info(infos);
    }

    class I18nInfo {
        String path;
        String text;
        int length;

        public I18nInfo(String path, String text, int length) {
            this.path = path;
            this.text = text;
            this.length = length;
        }

        @Override
        public String toString() {
            return "I18NInfo [path=" + path + ", text=" + text + ", length=" + length + "]";
        }
    }
}
