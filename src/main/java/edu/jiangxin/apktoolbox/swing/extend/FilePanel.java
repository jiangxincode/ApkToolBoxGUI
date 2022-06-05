package edu.jiangxin.apktoolbox.swing.extend;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;
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
    private JButton chooseButton;

    private String buttonText;

    private String currentDirectoryPath;

    private boolean acceptAllFileFilterUsed;

    private int fileSelectionMode;

    private boolean multiSelectionEnabled;

    private String description;

    private String[] extensions;

    private JFileChooser fileChooser;

    public FilePanel(String buttonText, String currentDirectoryPath, boolean acceptAllFileFilterUsed, int fileSelectionMode,
                     boolean multiSelectionEnabled, String description, String[] extensions) {
        super();
        fileChooser = new JFileChooser();
        this.buttonText = buttonText;
        this.currentDirectoryPath = currentDirectoryPath;
        this.acceptAllFileFilterUsed = acceptAllFileFilterUsed;
        this.fileSelectionMode = fileSelectionMode;
        this.multiSelectionEnabled = multiSelectionEnabled;
        this.description = description;
        this.extensions = extensions;
        initUI();
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        fileTextField = new JTextField();
        fileTextField.setPreferredSize(new Dimension(600, 0));
        fileTextField.setTransferHandler(new FileTransferHandler());

        chooseButton = new JButton(buttonText);
        chooseButton.addActionListener(new OpenDictionaryFileActionListener());

        add(fileTextField);
        add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        add(chooseButton);
    }

    public File getFile() {
        File file = new File(fileTextField.getText());
        return file;
    }

    public void updateDescriptionAndFileExtensions(String description, String[] extensions) {
        this.description = description;
        this.extensions = extensions;
        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
        fileChooser.addChoosableFileFilter(filter);
    }

    class OpenDictionaryFileActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fileChooser.setCurrentDirectory(new File(currentDirectoryPath));
            fileChooser.setAcceptAllFileFilterUsed(acceptAllFileFilterUsed);
            fileChooser.setFileSelectionMode(fileSelectionMode);
            fileChooser.setMultiSelectionEnabled(multiSelectionEnabled);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extensions);
            fileChooser.addChoosableFileFilter(filter);
            int returnVal = fileChooser.showOpenDialog(FilePanel.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null) {
                    fileTextField.setText(Utils.getCanonicalPathQuiet(selectedFile));
                }
            }
        }
    }

    class FileTransferHandler extends TransferHandler {
        public FileTransferHandler() {
        }

        @Override
        public boolean importData(JComponent comp, Transferable t) {
            try {
                Object object = t.getTransferData(DataFlavor.javaFileListFlavor);
                for (File file : (List<File>)object) {
                    fileTextField.setText(file.getCanonicalPath());
                }
                return true;
            } catch (IOException e) {
                LOGGER.error("importData failed: IOException");
            } catch (UnsupportedFlavorException e) {
                LOGGER.error("importData failed: UnsupportedFlavorException");
            }
            return false;
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
