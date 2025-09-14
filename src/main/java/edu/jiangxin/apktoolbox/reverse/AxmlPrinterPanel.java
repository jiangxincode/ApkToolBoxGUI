package edu.jiangxin.apktoolbox.reverse;

import edu.jiangxin.apktoolbox.swing.extend.listener.SelectDirectoryListener;
import edu.jiangxin.apktoolbox.swing.extend.plugin.PluginPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.ProcessLogOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.IOUtils;
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
public class AxmlPrinterPanel extends PluginPanel {
    private static final long serialVersionUID = 1L;

    private JTextField srcTextField;

    private JTextField targetTextField;

    public AxmlPrinterPanel() throws HeadlessException {
        super();
    }

    @Override
    public String getPluginFilename() {
        return "AXMLPrinter3.jar";
    }

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createSrcPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createTargetPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
    }

    private void createOperationPanel() {
        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        add(operationPanel);

        JButton getFileButton = new JButton("Get File");
        getFileButton.addActionListener(new GetFileButtonActionListener());

        operationPanel.add(getFileButton);
    }

    private void createTargetPanel() {
        JPanel targetPanel = new JPanel();
        targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.X_AXIS));
        add(targetPanel);

        targetTextField = new JTextField();
        targetTextField.setText(conf.getString("axmlprinter.target.dir"));

        JButton targetButton = new JButton("Save Dir");
        targetButton.addActionListener(new SelectDirectoryListener("Save To", targetTextField));

        targetPanel.add(targetTextField);
        targetPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        targetPanel.add(targetButton);
    }

    private void createSrcPanel() {
        JPanel srcPanel = new JPanel();
        srcPanel.setLayout(new BoxLayout(srcPanel, BoxLayout.X_AXIS));
        add(srcPanel);

        srcTextField = new JTextField();
        srcTextField.setText(conf.getString("axmlprinter.src.file"));

        JButton srcButton = new JButton("Source File");
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
            String srcPath = checkAndGetFileContent(srcTextField, "axmlprinter.src.file", "Source file is invalid");
            if (srcPath == null) {
                return;
            }

            String targetPath = checkAndGetDirContent(targetTextField, "axmlprinter.target.dir", "Target dir is invalid");
            if (targetPath == null) {
                return;
            }

            try (ZipFile zip = new ZipFile(srcPath)) {
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
                sb.append(getPluginStartupCmd()).append(" ").append(new File(targetPath, "AndroidManifest.xml.orig").getCanonicalPath());
                String cmd = sb.toString();
                logger.info(cmd);
                File outputFile = new File(new File(targetPath), "AndroidManifest.xml");
                try (FileOutputStream outStream = new FileOutputStream(outputFile);
                     ProcessLogOutputStream errStream = new ProcessLogOutputStream(logger, Level.ERROR)
                ) {
                    CommandLine commandLine = CommandLine.parse(cmd);
                    DefaultExecutor exec = DefaultExecutor.builder().get();
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
