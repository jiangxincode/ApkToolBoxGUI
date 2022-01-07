package edu.jiangxin.apktoolbox.file.duplicate;

import edu.jiangxin.apktoolbox.file.FileListPanel;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class DuplicateFindPanel extends EasyPanel {

    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;

    private JPanel optionPanel;

    private FileListPanel fileListPanel;

    private JPanel resultPanel;

    private JTable duplicateFileTable;

    private DefaultTableModel defaultTableModel;

    private Vector<Vector<String>> duplicateFileData;

    private JMenuItem openDirMenuItem;
    private JMenuItem deleteFileMenuItem;

    public DuplicateFindPanel() {
        super();
        initUI();
    }

    private void initUI() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        createOptionPanel();
        tabbedPane.addTab("Option", null, optionPanel, "Option");

        createResultPanel();
        tabbedPane.addTab("Result", null, resultPanel, "Result");
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));

        fileListPanel = new FileListPanel();

        JPanel checkOptionPanel = new JPanel();
        checkOptionPanel.setLayout(new BoxLayout(checkOptionPanel, BoxLayout.X_AXIS));
        checkOptionPanel.setBorder(BorderFactory.createTitledBorder("校验选项"));

        JCheckBox isSizeChecked = new JCheckBox("Size");
        JCheckBox isFileNameChecked = new JCheckBox("Filename");
        JCheckBox isMD5Checked = new JCheckBox("MD5");
        JCheckBox isModifiedTimeChecked = new JCheckBox("Modified Time");
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
        searchOptionPanel.setBorder(BorderFactory.createTitledBorder("搜索选项"));
        JCheckBox isHiddenFileSearched = new JCheckBox("Hidden Files");
        JCheckBox isSystemFileSearched = new JCheckBox("System Files");
        searchOptionPanel.add(isHiddenFileSearched);
        searchOptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        searchOptionPanel.add(isSystemFileSearched);
        searchOptionPanel.add(Box.createHorizontalGlue());

        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        operationPanel.setBorder(BorderFactory.createTitledBorder("执行操作"));
        JButton findButton = new JButton("Find");
        findButton.addActionListener(new FindButtonActionListener());
        JButton cancelButton = new JButton("Cancel");
        operationPanel.add(findButton);
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
        Vector<String> columnNames = new Vector<>();
        columnNames.add("GroupNo");
        columnNames.add("Path");
        columnNames.add("Name");
        duplicateFileData = new Vector<>();
        defaultTableModel = new MyDefaultTableModel(duplicateFileData, columnNames);
        duplicateFileTable = new JTable(defaultTableModel);

        duplicateFileTable.setDefaultRenderer(Vector.class, new MyTableCellRenderer());

        for (int i = 0; i < duplicateFileTable.getColumnCount(); i++) {
            duplicateFileTable.getColumn(duplicateFileTable.getColumnName(i)).setCellRenderer(new MyTableCellRenderer());
        }

        duplicateFileTable.addMouseListener(new MyMouseListener());

        duplicateFileTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(duplicateFileTable);
        resultPanel.add(scrollPane);
    }

    class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            int r = duplicateFileTable.rowAtPoint(e.getPoint());
            if (r >= 0 && r < duplicateFileTable.getRowCount()) {
                duplicateFileTable.setRowSelectionInterval(r, r);
            } else {
                duplicateFileTable.clearSelection();
            }
            int rowIndex = duplicateFileTable.getSelectedRow();
            if (rowIndex < 0) {
                return;
            }
            if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
                JPopupMenu popupmenu = new JPopupMenu();
                openDirMenuItem = new JMenuItem("打开对应文件夹");
                deleteFileMenuItem = new JMenuItem("删除该文件");
                MyMenuActionListener menuActionListener = new MyMenuActionListener();
                openDirMenuItem.addActionListener(menuActionListener);
                deleteFileMenuItem.addActionListener(menuActionListener);
                popupmenu.add(openDirMenuItem);
                popupmenu.add(deleteFileMenuItem);
                popupmenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    class MyMenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            if (source.equals(openDirMenuItem)) {
                int rowIndex = duplicateFileTable.getSelectedRow();
                String parentPath = defaultTableModel.getValueAt(rowIndex, duplicateFileTable.getColumn("Path").getModelIndex()).toString();
                File parent = new File(parentPath);
                if (parent != null && parent.isDirectory()) {
                    try {
                        Desktop.getDesktop().open(parent);
                    } catch (IOException e) {
                        logger.error("open parent failed: " + parent.getPath());
                    }
                }
            } else if (source.equals(deleteFileMenuItem)) {

            }
        }
    }

    class FindButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<File> fileList = fileListPanel.getFileList();
            for (File file : fileList) {
                Map<String, List<File>> duplicateFileGroupMap = new HashMap<>();
                find(duplicateFileGroupMap, file);
                int groupIndex = 0;
                for (List<File> duplicateFileGroup : duplicateFileGroupMap.values()) {
                    if (duplicateFileGroup.size() < 2) {
                        continue;
                    }
                    groupIndex++;
                    for (File duplicateFile : duplicateFileGroup) {
                        Vector<Object> rowData = new Vector<>();
                        rowData.add(groupIndex);
                        rowData.add(duplicateFile.getParent());
                        rowData.add(duplicateFile.getName());
                        defaultTableModel.addRow(rowData);
                    }
                }
            }
            tabbedPane.setSelectedIndex(1);
            getFrame().pack();
        }
    }

    private static void find(Map<String, List<File>> duplicateFiles, File dir) {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                find(duplicateFiles, f);
            } else {
                String hash = f.getName() + f.length();
                List<File> list = duplicateFiles.get(hash);
                if (list == null) {
                    list = new LinkedList<>();
                    duplicateFiles.put(hash, list);
                }
                list.add(f);
            }
        }
    }

    class MyTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int groupNo = (Integer) table.getValueAt(row, 0);
            switch (groupNo % 4) {
                case 0: {
                    setBackground(new Color(255, 182, 193, 100));
                    break;
                }
                case 1: {
                    setBackground(new Color(123, 104, 238, 100));
                    break;
                }
                case 2: {
                    setBackground(new Color(127, 255, 170, 100));
                    break;
                }
                case 3: {
                    setBackground(new Color(255, 255, 0, 100));
                    break;
                }
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    class MyDefaultTableModel extends DefaultTableModel {
        public MyDefaultTableModel(Vector data, Vector columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 0) {
                return false;
            }
            return super.isCellEditable(row, column);
        }
    }
}
