package edu.jiangxin.apktoolbox.file;

import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.collections.ListUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListPanel extends JPanel {

    private JPanel leftPanel;

    private JPanel rightPanel;

    private JList<File> fileList;

    private DefaultListModel<File> fileListModel;

    private JButton addFileButton;

    private JButton addDirectoryButton;

    private JButton removeSelectedButton;

    private JButton clearButton;

    private JButton selectAllButton;

    private JButton inverseSelectedButton;

    public FileListPanel() {
        super();
        initUI();
    }

    public List<File> getFileList() {
        List<File> fileList = new ArrayList<>();
        Object[] objectArray = fileListModel.toArray();
        for (Object obj : objectArray) {
            if (obj instanceof File) {
                fileList.add((File) obj);
            }
        }
        return fileList;
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        createLeftPanel();
        add(leftPanel);

        add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        createRightPanel();
        add(rightPanel);
    }

    private void createLeftPanel() {
        fileList = new JList<>();
        fileListModel = new DefaultListModel<>();
        fileList.setModel(fileListModel);

        JScrollPane scrollPane = new JScrollPane(fileList);
        scrollPane.setPreferredSize(new Dimension(Constants.DEFAULT_SCROLL_PANEL_WIDTH, Constants.DEFAULT_SCROLL_PANEL_HEIGHT));

        leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createTitledBorder("文件列表"));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setTransferHandler(new FileListTransferHandler());
        leftPanel.add(scrollPane);
    }

    private void createRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        addFileButton = new JButton("Add File");
        addFileButton.addMouseListener(new AddFileButtonMouseAdapter());
        rightPanel.add(addFileButton);
        rightPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        addDirectoryButton = new JButton("Add Directory");
        addDirectoryButton.addMouseListener(new AddDirectoryButtonMouseAdapter());
        rightPanel.add(addDirectoryButton);
        rightPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        removeSelectedButton = new JButton("Remove Selected");
        removeSelectedButton.addMouseListener(new RemoveSelectedButtonMouseAdapter());
        rightPanel.add(removeSelectedButton);
        rightPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        clearButton = new JButton("Clear All");
        clearButton.addMouseListener(new ClearButtonMouseAdapter());
        rightPanel.add(clearButton);
        rightPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        selectAllButton = new JButton("Select All");
        selectAllButton.addMouseListener(new SelectAllButtonMouseAdapter());
        rightPanel.add(selectAllButton);
        rightPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        inverseSelectedButton = new JButton("Inverse Selected");
        inverseSelectedButton.addMouseListener(new InverseSelectedButtonMouseAdapter());
        rightPanel.add(inverseSelectedButton);
    }

    private final class FileListTransferHandler extends TransferHandler {
        @Override
        public boolean importData(JComponent comp, Transferable t) {
            try {
                Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
                for (File file : (List<File>)o) {
                    fileListModel.addElement(file);
                }
                return true;
            } catch (Exception e) {
            }
            return false;
        }

        @Override
        public boolean canImport(JComponent jComponent, DataFlavor[] dataFlavors) {
            for (int i = 0; i < dataFlavors.length; i++) {
                if (DataFlavor.javaFileListFlavor.equals(dataFlavors[i])) {
                    return true;
                }
            }
            return false;
        }
    }

    private final class AddFileButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setDialogTitle("Select A File");
            int ret = jfc.showDialog(new JLabel(), null);
            switch (ret) {
                case JFileChooser.APPROVE_OPTION:
                    File file = jfc.getSelectedFile();
                    fileListModel.addElement(file);
                    break;
                default:
                    break;
            }

        }
    }

    private final class AddDirectoryButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.setDialogTitle("Select A Directory");
            int ret = jfc.showDialog(new JLabel(), null);
            switch (ret) {
                case JFileChooser.APPROVE_OPTION:
                    File file = jfc.getSelectedFile();
                    fileListModel.addElement(file);
                    break;
                default:
                    break;
            }

        }
    }

    private final class RemoveSelectedButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            List<File> files = fileList.getSelectedValuesList();
            for (File file : files) {
                fileListModel.removeElement(file);
            }
        }
    }

    private final class ClearButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            fileListModel.removeAllElements();
        }
    }

    private final class SelectAllButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            int[] selectedIndices = new int[fileListModel.getSize()];
            for (int i = 0; i < selectedIndices.length; i++) {
                selectedIndices[i] = i;
            }
            fileList.clearSelection();
            fileList.setSelectedIndices(selectedIndices);
        }
    }

    private final class InverseSelectedButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            int[] newIndices = new int[fileListModel.getSize() - fileList.getSelectedIndices().length];
            for (int i = 0, j = 0; i < fileListModel.getSize(); ++i) {
                if (!fileList.isSelectedIndex(i)) {
                    newIndices[j++] = i;
                }
            }
            fileList.clearSelection();
            fileList.setSelectedIndices(newIndices);
        }
    }

}
