package edu.jiangxin.apktoolbox.swing.extend.filepanel;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FilePanel extends JPanel {
    private static final Logger LOGGER = LogManager.getLogger(FilePanel.class.getSimpleName());
    private JTextField fileTextField;

    private String buttonText;

    private String currentDirectoryPath = ".";

    private int fileSelectionMode = JFileChooser.FILES_ONLY;

    private String description;

    private String[] extensions;

    private JFileChooser fileChooser;

    private IFileReadyCallback callback;

    public FilePanel(String buttonText) {
        super();
        fileChooser = new JFileChooser();
        this.buttonText = buttonText;
        initUI();
    }

    public void setCurrentDirectoryPath(String currentDirectoryPath) {
        this.currentDirectoryPath = currentDirectoryPath;
    }

    public void setFileReadyCallback(IFileReadyCallback callback) {
        this.callback = callback;
    }

    public void setFileSelectionMode(int fileSelectionMode) {
        this.fileSelectionMode = fileSelectionMode;
    }

    public void setDescriptionAndFileExtensions(String description, String[] extensions) {
        this.description = description;
        this.extensions = extensions;
        if (StringUtils.isNotEmpty(description) || (ArrayUtils.isNotEmpty(extensions))) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
            if (fileChooser != null) {
                fileChooser.resetChoosableFileFilters();
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(false);
            }
        } else {
            if (fileChooser != null) {
                fileChooser.resetChoosableFileFilters();
                fileChooser.setAcceptAllFileFilterUsed(true);
            }
        }
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        fileTextField = new JTextField();
        fileTextField.setName(Constants.KEY_PREFIX + "FilePanel.TextField");
        fileTextField.setPreferredSize(new Dimension(600, 30));
        fileTextField.setMaximumSize(new Dimension(1200, 30));
        fileTextField.setTransferHandler(new FileTransferHandler());

        JButton chooseButton = new JButton(buttonText);
        chooseButton.addActionListener(new OpenDictionaryFileActionListener());

        add(fileTextField);
        add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        add(chooseButton);
    }

    public File getFile() {
        return new File(fileTextField.getText());
    }

    class OpenDictionaryFileActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fileChooser.setCurrentDirectory(new File(currentDirectoryPath));
            fileChooser.setFileSelectionMode(fileSelectionMode);
            fileChooser.setMultiSelectionEnabled(false);
            if (StringUtils.isNotEmpty(description) || (ArrayUtils.isNotEmpty(extensions))) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(false);
            } else {
                fileChooser.resetChoosableFileFilters();
                fileChooser.setAcceptAllFileFilterUsed(true);
            }
            int returnVal = fileChooser.showOpenDialog(FilePanel.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                fileTextField.setText(FileUtils.getCanonicalPathQuiet(selectedFile));
                if (callback != null) {
                    callback.onFileReady(selectedFile);
                }
            }
        }
    }

    class FileTransferHandler extends TransferHandler {
        public FileTransferHandler() {
        }

        @Override
        public boolean importData(JComponent comp, Transferable t) {
            List<File> files = null;
            try {
                files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
            } catch (IOException e) {
                LOGGER.error("importData failed: IOException");
            } catch (UnsupportedFlavorException e) {
                LOGGER.error("importData failed: UnsupportedFlavorException");
            }
            if (files == null || files.size() != 1) {
                LOGGER.error("importData failed: getTransferData failed");
                return false;
            }
            File file = files.get(0);
            if ((!file.isFile() && fileSelectionMode == JFileChooser.FILES_ONLY)
                    ||(!file.isDirectory() && fileSelectionMode == JFileChooser.DIRECTORIES_ONLY)) {
                LOGGER.error("importData failed: fileSelectionMode is not match");
                return false;
            }
            fileTextField.setText(FileUtils.getCanonicalPathQuiet(file));
            if (callback != null) {
                callback.onFileReady(file);
            }
            return true;
        }

        @Override
        public boolean canImport(JComponent jComponent, DataFlavor[] dataFlavors) {
            for (DataFlavor dataFlavor : dataFlavors) {
                if (DataFlavor.javaFileListFlavor.equals(dataFlavor)) {
                    return true;
                }
            }
            return false;
        }
    }
}
