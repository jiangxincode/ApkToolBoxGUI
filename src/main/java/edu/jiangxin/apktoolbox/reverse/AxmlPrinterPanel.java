package edu.jiangxin.apktoolbox.reverse;

import edu.jiangxin.apktoolbox.swing.extend.DirectorySelectButtonActionListener;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.ProcessLogOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
        String toolPath = conf.getString(Constants.AXMLPRINTER_PATH_KEY);
        File toolFile = null;
        if (!StringUtils.isEmpty(toolPath)) {
            toolFile = new File(toolPath);
        }
        if (StringUtils.isEmpty(toolPath) || toolFile == null || !toolFile.exists() || !toolFile.isFile()) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Need Configuration", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

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
        getFileButton.addActionListener(new GetFileButtonActionListener());

        operationPanel.add(getFileButton);
    }

    private void createTargetPanel() {
        targetPanel = new JPanel();
        targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.X_AXIS));
        targetTextField = new JTextField();
        targetTextField.setText(conf.getString("axmlprinter.target.dir"));

        targetButton = new JButton("Save Dir");
        targetButton.addActionListener(new DirectorySelectButtonActionListener("Save To", targetTextField));

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
        srcButton.addActionListener(new SrcButtonActionListener());

        srcPanel.add(srcTextField);
        srcPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        srcPanel.add(srcButton);
    }

    private final class SrcButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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

    private final class GetFileButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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
                        .append(conf.getString(Constants.AXMLPRINTER_PATH_KEY)).append("\"")
                        .append(" ").append(new File(targetPath, "AndroidManifest.xml.orig").getCanonicalPath());
                String cmd = sb.toString();
                logger.info(cmd);
                File outputFile = new File(targetFile, "AndroidManifest.xml");
                try (FileOutputStream outStream = new FileOutputStream(outputFile);
                     ProcessLogOutputStream errStream = new ProcessLogOutputStream(logger, Level.ERROR)
                ) {
                    CommandLine commandLine = CommandLine.parse(cmd);
                    DefaultExecutor exec = new DefaultExecutor();
                    PumpStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);
                    exec.setStreamHandler(streamHandler);
                    int exitValue = exec.execute(commandLine);
                    logger.info("exitValue: [" + exitValue + "]");
                }
            } catch (IOException e1) {
                logger.error("axmlprinter fail", e1);
            }
        }
    }
}
