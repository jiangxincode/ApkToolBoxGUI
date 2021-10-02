package edu.jiangxin.apktoolbox.reverse;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.*;

import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.StreamHandler;
import edu.jiangxin.apktoolbox.utils.Utils;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class AxmlPrinterPanel extends EasyPanel {
    private static final long serialVersionUID = 1L;

    private JPanel srcPanel;

    private JTextField srcTextField;

    private JButton srcButton;

    private JPanel targetPanel;

    private JTextField targetTextField;

    private JButton targetButton;

    private JPanel operationPanel;

    private JButton getFileButton;

    public AxmlPrinterPanel() throws HeadlessException {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createSrcPanel();
        add(srcPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createTargetPanel();
        add(targetPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        getFileButton = new JButton("Get File");
        getFileButton.addMouseListener(new GetFileButtonMouseAdapter());

        operationPanel.add(getFileButton);
    }

    private void createTargetPanel() {
        targetPanel = new JPanel();
        targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.X_AXIS));
        targetTextField = new JTextField();
        targetTextField.setText(conf.getString("axmlprinter.target.dir"));

        targetButton = new JButton("Save Dir");
        targetButton.addMouseListener(new TargetButtonMouseAdapter());

        targetPanel.add(targetTextField);
        targetPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        targetPanel.add(targetButton);
    }

    private void createSrcPanel() {
        srcPanel = new JPanel();
        srcPanel.setLayout(new BoxLayout(srcPanel, BoxLayout.X_AXIS));
        srcTextField = new JTextField();
        srcTextField.setText(conf.getString("axmlprinter.src.file"));

        srcButton = new JButton("Source File");
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
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setDialogTitle("select a file");
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

    private final class TargetButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.setDialogTitle("save to");
            int ret = jfc.showDialog(new JLabel(), null);
            switch (ret) {
                case JFileChooser.APPROVE_OPTION:
                    File file = jfc.getSelectedFile();
                    targetTextField.setText(file.getAbsolutePath());
                    break;

                default:
                    break;
            }

        }
    }

    private final class GetFileButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            File srcFile = new File(srcTextField.getText());
            if (!srcFile.exists() || !srcFile.isFile()) {
                logger.error("srcFile is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(AxmlPrinterPanel.this, "Source file is invalid", "ERROR",
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
            conf.setProperty("axmlprinter.src.file", srcPath);
            File targetFile = new File(targetTextField.getText());
            if (!targetFile.exists() || !targetFile.isDirectory()) {
                logger.error("targetFile is invalid");
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(AxmlPrinterPanel.this, "Target dir is invalid", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                targetTextField.requestFocus();
                return;
            }
            String targetPath;
            try {
                targetPath = targetFile.getCanonicalPath();
            } catch (IOException e2) {
                logger.error("getCanonicalPath fail");
                return;
            }
            conf.setProperty("axmlprinter.target.dir", targetPath);
            try (ZipFile zip = new ZipFile(srcFile)) {
                Enumeration<?> entries = zip.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    if ("AndroidManifest.xml".equals(entry.getName())) {
                        try (InputStream inputStream = zip.getInputStream(entry);
                             OutputStream outputSteam = new FileOutputStream(new File(targetPath, "AndroidManifest.xml.orig"))) {
                            IOUtils.copy(inputStream,outputSteam);
                        } catch (IOException e2) {
                            logger.error("axmlprinter fail", e2);
                        }
                        break;
                    }
                }

                StringBuilder sb = new StringBuilder();
                sb.append("java -jar \"-Duser.language=en\" \"-Dfile.encoding=UTF8\"").append(" \"")
                        .append(Utils.getToolsPath()).append(File.separator).append("AXMLPrinter3.jar\"")
                        .append(" ").append(new File(targetPath, "AndroidManifest.xml.orig").getCanonicalPath());
                String cmd = sb.toString();
                logger.info(cmd);
                Process process = Runtime.getRuntime().exec(cmd);
                String content = Utils.loadStream(process.getInputStream());
                FileUtils.writeStringToFile(new File(targetFile, "AndroidManifest.xml"), content, "UTF-8", true);
                new StreamHandler(process.getErrorStream(), 1).start();
                process.waitFor();
                logger.info("axmlprinter finish");
            } catch (IOException e1) {
                logger.error("axmlprinter fail", e1);
            } catch (InterruptedException e1) {
                logger.error("axmlprinter fail", e1);
                Thread.currentThread().interrupt();
            }
        }
    }

}
