package edu.jiangxin.apktoolbox.swing.extend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileTransferHandler extends TransferHandler {
    private static final Logger LOGGER = LogManager.getLogger(FileListPanel.class);

    private JTextField textField;

    public FileTransferHandler(JTextField textField) {
        this.textField = textField;
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        try {
            Object object = t.getTransferData(DataFlavor.javaFileListFlavor);
            for (File file : (List<File>)object) {
                textField.setText(file.getCanonicalPath());
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