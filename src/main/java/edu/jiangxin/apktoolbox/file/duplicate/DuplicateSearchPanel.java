package edu.jiangxin.apktoolbox.file.duplicate;

import edu.jiangxin.apktoolbox.convert.time.DateTransform;
import edu.jiangxin.apktoolbox.file.FileListPanel;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.FileUtilsEx;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.List;
import java.util.*;

public class DuplicateSearchPanel extends EasyPanel {

    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;

    private JPanel optionPanel;

    private FileListPanel fileListPanel;

    private JCheckBox isSizeChecked;
    private JCheckBox isFileNameChecked;
    private JCheckBox isMD5Checked;
    private JCheckBox isModifiedTimeChecked;

    private JCheckBox isHiddenFileSearched;
    private JCheckBox isRecursiveSearched;
    private JTextField suffixTextField;

    private JPanel resultPanel;

    private JTable resultTable;

    private DefaultTableModel resultTableModel;

    private JButton searchButton;
    private JButton cancelButton;

    private JMenuItem openDirMenuItem;
    private JMenuItem deleteFileMenuItem;
    private JMenuItem deleteFilesInSameDirMenuItem;
    private JMenuItem deleteFilesInSameDirRecursiveMenuItem;

    private Thread searchThread;

    final private Map<String, List<File>> duplicateFileGroupMap = new HashMap<>();

    public DuplicateSearchPanel() {
        super();
        initUI();
    }

    private void initUI() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        createOptionPanel();
        tabbedPane.addTab("Option", null, optionPanel, "Show Search Options");

        createResultPanel();
        tabbedPane.addTab("Result", null, resultPanel, "Show Search Result");
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));

        fileListPanel = new FileListPanel();

        JPanel checkOptionPanel = new JPanel();
        checkOptionPanel.setLayout(new BoxLayout(checkOptionPanel, BoxLayout.X_AXIS));
        checkOptionPanel.setBorder(BorderFactory.createTitledBorder("Check Options"));

        isSizeChecked = new JCheckBox("Size");
        isSizeChecked.setSelected(true);
        isFileNameChecked = new JCheckBox("Filename");
        isMD5Checked = new JCheckBox("MD5");
        isModifiedTimeChecked = new JCheckBox("Last Modified Time");
        checkOptionPanel.add(isSizeChecked);
        checkOptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        checkOptionPanel.add(isFileNameChecked);
        checkOptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        checkOptionPanel.add(isMD5Checked);
        checkOptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        checkOptionPanel.add(isModifiedTimeChecked);
        checkOptionPanel.add(Box.createHorizontalGlue());

        JPanel searchOptionPanel = new JPanel();
        searchOptionPanel.setLayout(new BoxLayout(searchOptionPanel, BoxLayout.X_AXIS));
        searchOptionPanel.setBorder(BorderFactory.createTitledBorder("Search Options"));

        isHiddenFileSearched = new JCheckBox("Hidden Files");
        isRecursiveSearched = new JCheckBox("Recursive");
        isRecursiveSearched.setSelected(true);
        JLabel suffixLabel = new JLabel("Suffix: ");
        suffixTextField = new JTextField();
        suffixTextField.setToolTipText("an array of extensions, ex. {\"java\",\"xml\"}. If this parameter is empty, all files are returned.");
        searchOptionPanel.add(isHiddenFileSearched);
        searchOptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        searchOptionPanel.add(isRecursiveSearched);
        searchOptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        searchOptionPanel.add(suffixLabel);
        searchOptionPanel.add(suffixTextField);
        searchOptionPanel.add(Box.createHorizontalGlue());

        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        operationPanel.setBorder(BorderFactory.createTitledBorder("Operations"));

        searchButton = new JButton("Search");
        cancelButton = new JButton("Cancel");
        searchButton.addActionListener(new OperationButtonActionListener());
        cancelButton.addActionListener(new OperationButtonActionListener());
        operationPanel.add(searchButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(cancelButton);
        operationPanel.add(Box.createHorizontalGlue());

        optionPanel.add(fileListPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(checkOptionPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(searchOptionPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(operationPanel);
    }

    private void createResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        resultTableModel = new DuplicateFilesTableModel(new Vector<>(), DuplicateFilesConstants.COLUMN_NAMES);
        resultTable = new JTable(resultTableModel);

        resultTable.setDefaultRenderer(Vector.class, new DuplicateFilesTableCellRenderer());

        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumn(resultTable.getColumnName(i)).setCellRenderer(new DuplicateFilesTableCellRenderer());
        }

        resultTable.addMouseListener(new MyMouseListener());

        resultTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        resultPanel.add(scrollPane);
    }

    public String getComparedKey(File file) {
        StringBuilder sb = new StringBuilder();
        if (isSizeChecked.isSelected()) {
            sb.append("[Size][");
            sb.append(DigestUtils.md5Hex(String.valueOf(file.length())));
            sb.append("]");
        }
        if (isFileNameChecked.isSelected()) {
            sb.append("[Filename][");
            sb.append(DigestUtils.md5Hex(file.getName()));
            sb.append("]");
        }
        if (isMD5Checked.isSelected()) {
            sb.append("[MD5][");
            try (InputStream is = new FileInputStream(file)) {
                sb.append(DigestUtils.md5Hex(is));
            } catch (FileNotFoundException e) {
                logger.error("getComparedKey FileNotFoundException");
            } catch (IOException e) {
                logger.error("getComparedKey IOException");
            }
            sb.append("]");
        }
        if (isModifiedTimeChecked.isSelected()) {
            sb.append("[ModifiedTime][");
            sb.append(DigestUtils.md5Hex(String.valueOf(file.lastModified())));
            sb.append("]");
        }
        logger.info("path: " + file.getAbsolutePath() + ", key: " + sb);
        return sb.toString();
    }

    class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            int r = resultTable.rowAtPoint(e.getPoint());
            if (r >= 0 && r < resultTable.getRowCount()) {
                resultTable.setRowSelectionInterval(r, r);
            } else {
                resultTable.clearSelection();
            }
            int rowIndex = resultTable.getSelectedRow();
            if (rowIndex < 0) {
                return;
            }
            if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                JPopupMenu popupmenu = new JPopupMenu();
                MyMenuActionListener menuActionListener = new MyMenuActionListener();

                openDirMenuItem = new JMenuItem("Open parent folder of this file");
                openDirMenuItem.addActionListener(menuActionListener);
                popupmenu.add(openDirMenuItem);

                deleteFileMenuItem = new JMenuItem("Delete this duplicate file");
                deleteFileMenuItem.addActionListener(menuActionListener);
                popupmenu.add(deleteFileMenuItem);

                deleteFilesInSameDirMenuItem = new JMenuItem("Delete these duplicate files in the same directory");
                deleteFilesInSameDirMenuItem.addActionListener(menuActionListener);
                popupmenu.add(deleteFilesInSameDirMenuItem);

                deleteFilesInSameDirRecursiveMenuItem = new JMenuItem("Delete these duplicate files in the same directory(Recursive)");
                deleteFilesInSameDirRecursiveMenuItem.addActionListener(menuActionListener);
                popupmenu.add(deleteFilesInSameDirRecursiveMenuItem);

                popupmenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    class MyMenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            if (source.equals(openDirMenuItem)) {
                int rowIndex = resultTable.getSelectedRow();
                String parentPath = resultTableModel.getValueAt(rowIndex, resultTable.getColumn(DuplicateFilesConstants.COLUMN_NAME_FILE_PARENT).getModelIndex()).toString();
                File parent = new File(parentPath);
                if (parent.isDirectory()) {
                    try {
                        Desktop.getDesktop().open(parent);
                    } catch (IOException e) {
                        logger.error("open parent failed: " + parent.getPath());
                    }
                }
            } else if (source.equals(deleteFileMenuItem)) {
                int rowIndex = resultTable.getSelectedRow();
                String parentPath = resultTableModel.getValueAt(rowIndex, resultTable.getColumn(DuplicateFilesConstants.COLUMN_NAME_FILE_PARENT).getModelIndex()).toString();
                String name = resultTableModel.getValueAt(rowIndex, resultTable.getColumn(DuplicateFilesConstants.COLUMN_NAME_FILE_NAME).getModelIndex()).toString();
                File selectedFile = new File(parentPath, name);
                String key = getComparedKey(selectedFile);
                List<File> files = duplicateFileGroupMap.get(key);
                for (File file : files) {
                    if (!selectedFile.equals(file)) {
                        continue;
                    }
                    files.remove(file);
                    boolean isSuccessful = file.delete();
                    logger.info("delete file: " + file.getAbsolutePath() + ", result: " + isSuccessful);
                    break;
                }
                resultTableModel.setRowCount(0);
                showResult();
            } else if (source.equals(deleteFilesInSameDirMenuItem)) {
                int rowIndex = resultTable.getSelectedRow();
                String parentPath = resultTableModel.getValueAt(rowIndex, resultTable.getColumn(DuplicateFilesConstants.COLUMN_NAME_FILE_PARENT).getModelIndex()).toString();
                for (Map.Entry<String, List<File>> entry : duplicateFileGroupMap.entrySet()) {
                    List<File> duplicateFileGroup = entry.getValue();
                    for (File duplicateFile : duplicateFileGroup) {
                        String parentPathTmp = duplicateFile.getParent();
                        if (Objects.equals(parentPath, parentPathTmp)) {
                            duplicateFileGroup.remove(duplicateFile);
                            boolean isSuccessful = duplicateFile.delete();
                            logger.info("delete file: " + duplicateFile.getAbsolutePath() + ", result: " + isSuccessful);
                            break;
                        }
                    }
                }
                resultTableModel.setRowCount(0);
                showResult();
            } else if (source.equals(deleteFilesInSameDirRecursiveMenuItem)) {
                int rowIndex = resultTable.getSelectedRow();
                String parentPath = resultTableModel.getValueAt(rowIndex, resultTable.getColumn(DuplicateFilesConstants.COLUMN_NAME_FILE_PARENT).getModelIndex()).toString();
                for (Map.Entry<String, List<File>> entry : duplicateFileGroupMap.entrySet()) {
                    List<File> duplicateFileGroup = entry.getValue();
                    for (File duplicateFile : duplicateFileGroup) {
                        String parentPathTmp = duplicateFile.getParent();
                        if (Objects.equals(parentPath, parentPathTmp) || FilenameUtils.directoryContains(parentPath, parentPathTmp)) {
                            duplicateFileGroup.remove(duplicateFile);
                            boolean isSuccessful = duplicateFile.delete();
                            logger.info("delete file: " + duplicateFile.getAbsolutePath() + ", result: " + isSuccessful);
                            break;
                        }
                    }
                }
                resultTableModel.setRowCount(0);
                showResult();
            } else {
                logger.error("invalid source");
            }
        }
    }

    class OperationButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source.equals(searchButton)) {
                String[] extensions = null;
                if (StringUtils.isNotEmpty(suffixTextField.getText())) {
                    extensions = suffixTextField.getText().split(",");
                }
                searchThread = new SearchThread(extensions, isRecursiveSearched.isSelected(), isHiddenFileSearched.isSelected(), duplicateFileGroupMap);
                searchThread.start();
            } else if (source.equals(cancelButton)) {
                if (searchThread.isAlive()) {
                    searchThread.interrupt();
                }
            }

        }
    }

    private void showResult() {
        SwingUtilities.invokeLater(() -> {
            int groupIndex = 0;
            for (Map.Entry<String, List<File>> entry : duplicateFileGroupMap.entrySet()) {
                List<File> duplicateFileGroup = entry.getValue();
                if (duplicateFileGroup.size() < 2) {
                    continue;
                }
                groupIndex++;
                for (File duplicateFile : duplicateFileGroup) {
                    Vector<Object> rowData = getRowVector(groupIndex, duplicateFile);
                    resultTableModel.addRow(rowData);
                }
            }
            tabbedPane.setSelectedIndex(1);
        });
    }

    private Vector<Object> getRowVector(int groupIndex, File file) {
        Vector<Object> rowData = new Vector<>();
        rowData.add(groupIndex);
        rowData.add(file.getParent());
        rowData.add(file.getName());
        rowData.add(FilenameUtils.getExtension(file.getName()));
        rowData.add(FileUtilsEx.byteCountToDisplaySize(file.length()));
        rowData.add(DateTransform.milTimestampToString(String.valueOf(file.lastModified())));
        return rowData;
    }

    class SearchThread extends Thread {
        private String[] extensions;
        private boolean isRecursiveSearched;
        private boolean isHiddenFileSearched;
        private Map<String, List<File>> duplicateFileGroupMap;

        public SearchThread(String[] extensions, boolean isRecursiveSearched, boolean isHiddenFileSearched, Map<String, List<File>> duplicateFileGroupMap) {
            super();
            this.extensions = extensions;
            this.isRecursiveSearched = isRecursiveSearched;
            this.isHiddenFileSearched = isHiddenFileSearched;
            this.duplicateFileGroupMap = duplicateFileGroupMap;
        }

        @Override
        public void run() {
            super.run();
            duplicateFileGroupMap.clear();
            List<File> fileList = fileListPanel.getFileList();

            Set<File> fileSet = new TreeSet<>(fileList);
            for (File file : fileList) {
                fileSet.addAll(FileUtils.listFiles(file, extensions, isRecursiveSearched));
            }

            for (File file : fileSet) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                if (file.isHidden() && !isHiddenFileSearched) {
                    continue;
                }
                String hash = getComparedKey(file);
                List<File> list = duplicateFileGroupMap.computeIfAbsent(hash, k -> new LinkedList<>());
                list.add(file);
            }
            showResult();
        }
    }
}
