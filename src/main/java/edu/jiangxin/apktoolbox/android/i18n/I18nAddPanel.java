/**
 * 
 */
package edu.jiangxin.apktoolbox.android.i18n;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.jiangxin.apktoolbox.swing.extend.listener.SelectDirectoryListener;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class I18nAddPanel extends EasyPanel {
    
    private static final long serialVersionUID = 1L;
    
    private static final String CHARSET = "UTF-8";

    private static final boolean REMOVE_LAST_LF_OPEN = true;

    private static Map<String, String> replace = new HashedMap<String, String>();

    private JTextField srcTextField;

    private JTextField targetTextField;

    private JTextField itemTextField;

    static {
        replace.put("&quot;", "jiangxin001");
        replace.put("&#160;", "jiangxin002");
    }
    
    public I18nAddPanel() throws HeadlessException {
        super();
    }

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createSourcePanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createTargetPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createItemPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createOperationPanel();
    }

    private void createOperationPanel() {
        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        add(operationPanel);

        JButton addButton = new JButton(bundle.getString("android.i18n.add.title"));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String srcPath = checkAndGetDirContent(srcTextField, "android.i18n.add.src.dir", "Source directory is invalid");
                if (srcPath == null) {
                    return;
                }

                String targetPath = checkAndGetDirContent(targetTextField, "android.i18n.add.target.dir", "Target directory is invalid");
                if (targetPath == null) {
                    return;
                }

                String itemStr = checkAndGetStringContent(itemTextField, "android.i18n.add.items", "Items is empty");
                if (itemStr == null) {
                    return;
                }

                List<String> items = new ArrayList<>(Arrays.asList(itemStr.split(";")));

                for (String item : items) {
                    int ret = innerProcessor(srcPath, targetPath, item);
                    if (ret != 0) {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(I18nAddPanel.this, "Failed, please see the log", "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        });

        operationPanel.add(addButton);
    }

    private void createItemPanel() {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
        add(itemPanel);
        
        itemTextField = new JTextField();
        itemTextField.setText(conf.getString("android.i18n.add.items"));

        JLabel itemLabel = new JLabel("Items");

        itemPanel.add(itemTextField);
        itemPanel.add(Box.createHorizontalGlue());
        itemPanel.add(itemLabel);
    }

    private void createTargetPanel() {
        JPanel targetPanel = new JPanel();
        targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.X_AXIS));
        add(targetPanel);
        
        targetTextField = new JTextField();
        targetTextField.setText(conf.getString("android.i18n.add.target.dir"));

        JButton targetButton = new JButton("Save Directory");
        targetButton.addActionListener(new SelectDirectoryListener("save to", targetTextField));

        targetPanel.add(targetTextField);
        targetPanel.add(Box.createHorizontalGlue());
        targetPanel.add(targetButton);
    }

    private void createSourcePanel() {
        JPanel sourcePanel = new JPanel();
        sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.X_AXIS));
        add(sourcePanel);
        
        srcTextField = new JTextField();
        srcTextField.setText(conf.getString("android.i18n.add.src.dir"));

        JButton srcButton = new JButton("Source Directory");
        srcButton.addActionListener(new SelectDirectoryListener("select a directory", srcTextField));

        sourcePanel.add(srcTextField);
        sourcePanel.add(Box.createHorizontalGlue());
        sourcePanel.add(srcButton);
    }

    private int innerProcessor(String sourceBaseStr, String targetBaseStr, String itemName) {
        if (StringUtils.isAnyEmpty(sourceBaseStr, targetBaseStr, itemName)) {
            logger.error("params are invalid: sourceBaseStr: " + sourceBaseStr + ", targetBaseStr: " + targetBaseStr
                    + ", itemName: " + itemName);
            return -1;
        }
        File sourceBaseFile = new File(sourceBaseStr);
        File targetBaseFile = new File(targetBaseStr);
        int count = 0;

        File[] sourceParentFiles = sourceBaseFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith("values");
            }
        });
        if (sourceParentFiles == null) {
            logger.error("sourceParentFiles is null");
            return -1;
        }
        for (File sourceParentFile : sourceParentFiles) {
            File sourceFile = new File(sourceParentFile, "strings.xml");

            Element sourceElement = getSourceElement(sourceFile, itemName);
            if (sourceElement == null) {
                logger.warn("sourceElement is null: " + sourceFile);
                continue;
            }

            File targetFile = new File(new File(targetBaseFile, sourceParentFile.getName()), "strings.xml");
            if (!targetFile.exists()) {
                logger.warn("targetFile does not exist: " + sourceFile);
                continue;
            }
            try {
                prePocess(targetFile);
            } catch (IOException e) {
                logger.error("prePocess failed.", e);
                return -1;
            }
            boolean res = setTargetElement(targetFile, sourceElement, itemName);
            if (!res) {
                logger.error("setTargetElement failed.");
                return -1;
            }
            try {
                postProcess(targetFile);
            } catch (IOException e) {
                logger.error("postProcess failed.", e);
                return -1;
            }
            logger.info("count: " + (++count) + ", in path: " + sourceFile + ", out path: " + targetFile);
        }
        logger.info("finish one cycle");
        return 0;
    }

    private Element getSourceElement(File sourceFile, String itemName) {
        if (!sourceFile.exists()) {
            logger.warn("sourceFile does not exist: " + sourceFile);
            return null;
        }
        SAXBuilder builder = new SAXBuilder();
        Document sourceDoc = null;
        try (InputStream in = new FileInputStream(sourceFile)) {
            sourceDoc = builder.build(in);
            logger.info("build source document: " + sourceFile);
        } catch (JDOMException | IOException e) {
            logger.error("build source document failed: " + sourceFile, e);
            return null;
        }
        if (sourceDoc == null) {
            logger.error("sourceDoc is null");
            return null;
        }
        Element sourceElement = null;
        for (Element sourceChild : sourceDoc.getRootElement().getChildren()) {
            String sourceValue = sourceChild.getAttributeValue("name");
            if (sourceValue != null && sourceValue.equals(itemName)) {
                sourceElement = sourceChild.clone();
                break;
            }
        }
        return sourceElement;
    }

    private boolean setTargetElement(File targetFile, Element sourceElement, String itemName) {
        SAXBuilder builder = new SAXBuilder();
        Document targetDoc;
        try {
            targetDoc = builder.build(targetFile);
            logger.info("build target document: " + targetFile);
        } catch (JDOMException | IOException e) {
            logger.error("build target document failed: " + targetFile, e);
            return false;
        }
        Element targetRoot = targetDoc.getRootElement();
        boolean isFinished = false;
        for (Element targetChild : targetRoot.getChildren()) {
            String targetValue = targetChild.getAttributeValue("name");
            if (targetValue != null && targetValue.equals(itemName)) {
                targetChild.setText(sourceElement.getText());
                isFinished = true;
                break;
            }
        }
        if (!isFinished) {
            targetRoot.addContent("    ");
            targetRoot.addContent(sourceElement);
            targetRoot.addContent("\n");
        }
        XMLOutputter out = new XMLOutputter();
        Format format = Format.getRawFormat();
        format.setEncoding("UTF-8");
        format.setLineSeparator("\n");
        out.setFormat(format);
        OutputStream os = null;
        try {
            os = new FileOutputStream(targetFile);
            out.output(targetDoc, os);
        } catch (IOException e) {
            logger.error("output fail", e);
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("close output stream exception", e);
                }
            }
        }
        return true;
    }

    private static void prePocess(File file) throws IOException {
        String content = FileUtils.readFileToString(file, CHARSET);
        for (Map.Entry<String, String> entry : replace.entrySet()) {
            content = content.replaceAll(entry.getKey(), entry.getValue());
        }
        FileUtils.writeStringToFile(file, content, CHARSET);
    }

    private static void postProcess(File file) throws IOException {
        String content = FileUtils.readFileToString(file, CHARSET);
        for (Map.Entry<String, String> entry : replace.entrySet()) {
            content = content.replaceAll(entry.getValue(), entry.getKey());
        }
        if (REMOVE_LAST_LF_OPEN) {
            content = StringUtils.removeEnd(content, "\n");
        }
        FileUtils.writeStringToFile(file, content, CHARSET);
    }

}
