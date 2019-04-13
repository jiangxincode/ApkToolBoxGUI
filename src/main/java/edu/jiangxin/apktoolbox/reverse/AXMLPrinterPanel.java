package edu.jiangxin.apktoolbox.reverse;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import edu.jiangxin.apktoolbox.swing.extend.JEasyPanel;
import edu.jiangxin.apktoolbox.utils.StreamHandler;
import edu.jiangxin.apktoolbox.utils.Utils;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class AXMLPrinterPanel extends JEasyPanel {
    private static final long serialVersionUID = 1L;

    public AXMLPrinterPanel() throws HeadlessException {
        super();
        setPreferredSize(new Dimension(600, 120));
        setMaximumSize(new Dimension(600, 120));

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        JPanel sourcePanel = new JPanel();
        sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.X_AXIS));
        add(sourcePanel);

        JTextField srcTextField = new JTextField();
        srcTextField.setText(conf.getString("axmlprinter.src.file"));

        JButton srcButton = new JButton("Source File");
        srcButton.addMouseListener(new MouseAdapter() {
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
        });

        sourcePanel.add(srcTextField);
        sourcePanel.add(srcButton);

        JPanel targetPanel = new JPanel();
        targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.X_AXIS));
        add(targetPanel);

        JTextField targetTextField = new JTextField();
        targetTextField.setText(conf.getString("axmlprinter.target.dir"));

        JButton targetButton = new JButton("Save Dir");
        targetButton.addMouseListener(new MouseAdapter() {
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
        });

        targetPanel.add(targetTextField);
        targetPanel.add(targetButton);

        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        add(operationPanel);

        JButton sceenshotButton = new JButton("Get File");
        sceenshotButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                File srcFile = new File(srcTextField.getText());
                if (!srcFile.exists() || !srcFile.isFile()) {
                    logger.error("srcFile is invalid");
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(AXMLPrinterPanel.this, "Source file is invalid", "ERROR",
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
                    JOptionPane.showMessageDialog(AXMLPrinterPanel.this, "Target dir is invalid", "ERROR",
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
                try {
                    ZipFile zip = new ZipFile(srcFile);
                    Enumeration<?> entries = zip.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) entries.nextElement();
                        if (entry.getName().equals("AndroidManifest.xml")) {
                            IOUtils.copy(zip.getInputStream(entry),
                                    new FileOutputStream(new File(targetPath, "AndroidManifest.xml.orig")));
                            break;
                        }

                    }

                    zip.close();
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
                } catch (IOException | InterruptedException e1) {
                    logger.error("axmlprinter fail", e);
                }
            }
        });

        operationPanel.add(sceenshotButton);
    }

}
