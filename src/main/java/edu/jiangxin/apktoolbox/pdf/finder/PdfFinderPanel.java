package edu.jiangxin.apktoolbox.pdf.finder;

import edu.jiangxin.apktoolbox.pdf.PdfUtils;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.FileListPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.DateUtils;
import edu.jiangxin.apktoolbox.utils.FileUtils;
import edu.jiangxin.apktoolbox.utils.RevealFileUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.Serial;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class PdfFinderPanel extends EasyPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;

    private JPanel mainPanel;

    private FileListPanel fileListPanel;

    private JRadioButton scannedRadioButton;

    private JRadioButton encryptedRadioButton;

    private JRadioButton nonOutlineRadioButton;

    private JRadioButton hasAnnotationsRadioButton;

    private JSpinner thresholdSpinner;

    private JCheckBox isRecursiveSearched;

    private JPanel resultPanel;

    private JTable resultTable;

    private DefaultTableModel resultTableModel;

    private JButton searchButton;
    private JButton cancelButton;

    private JProgressBar progressBar;

    private JMenuItem openDirMenuItem;

    private JMenuItem copyFilesMenuItem;

    private SearchThread searchThread;

    final private List<File> resultFileList = new ArrayList<>();


    @Override
    public void initUI() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        createMainPanel();
        tabbedPane.addTab("Option", null, mainPanel, "Show Search Options");

        createResultPanel();
        tabbedPane.addTab("Result", null, resultPanel, "Show Search Result");
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        fileListPanel = new FileListPanel();

        JPanel checkOptionPanel = new JPanel();
        checkOptionPanel.setLayout(new BoxLayout(checkOptionPanel, BoxLayout.X_AXIS));
        checkOptionPanel.setBorder(BorderFactory.createTitledBorder("Check Options"));

        ButtonGroup buttonGroup = new ButtonGroup();
        ItemListener itemListener = new RadioButtonItemListener();

        scannedRadioButton = new JRadioButton("查找扫描的PDF文件");
        scannedRadioButton.setSelected(true);
        scannedRadioButton.addItemListener(itemListener);
        buttonGroup.add(scannedRadioButton);

        encryptedRadioButton = new JRadioButton("查找加密的PDF文件");
        encryptedRadioButton.addItemListener(itemListener);
        buttonGroup.add(encryptedRadioButton);

        nonOutlineRadioButton = new JRadioButton("查找没有目录的PDF文件");
        nonOutlineRadioButton.addItemListener(itemListener);
        buttonGroup.add(nonOutlineRadioButton);

        hasAnnotationsRadioButton = new JRadioButton("查找有注释的PDF文件");
        hasAnnotationsRadioButton.addItemListener(itemListener);
        buttonGroup.add(hasAnnotationsRadioButton);

        JPanel typePanel = new JPanel();
        typePanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,3));
        typePanel.add(scannedRadioButton);
        typePanel.add(encryptedRadioButton);
        typePanel.add(nonOutlineRadioButton);
        typePanel.add(hasAnnotationsRadioButton);

        JLabel thresholdLabel = new JLabel("Threshold: ");
        thresholdSpinner = new JSpinner();
        thresholdSpinner.setModel(new SpinnerNumberModel(1, 0, 100, 1));

        checkOptionPanel.add(typePanel);
        checkOptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        checkOptionPanel.add(thresholdLabel);
        checkOptionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        checkOptionPanel.add(thresholdSpinner);
        checkOptionPanel.add(Box.createHorizontalGlue());

        JPanel searchOptionPanel = new JPanel();
        searchOptionPanel.setLayout(new BoxLayout(searchOptionPanel, BoxLayout.X_AXIS));
        searchOptionPanel.setBorder(BorderFactory.createTitledBorder("Search Options"));

        isRecursiveSearched = new JCheckBox("Recursive");
        isRecursiveSearched.setSelected(true);
        searchOptionPanel.add(isRecursiveSearched);
        searchOptionPanel.add(Box.createHorizontalGlue());

        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        operationPanel.setBorder(BorderFactory.createTitledBorder("Operations"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        searchButton = new JButton("Search");
        cancelButton = new JButton("Cancel");
        cancelButton.setEnabled(false);
        searchButton.addActionListener(new OperationButtonActionListener());
        cancelButton.addActionListener(new OperationButtonActionListener());
        operationPanel.add(searchButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(cancelButton);
        operationPanel.add(Box.createHorizontalGlue());

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");

        mainPanel.add(fileListPanel);
        mainPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        mainPanel.add(checkOptionPanel);
        mainPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        mainPanel.add(searchOptionPanel);
        mainPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        mainPanel.add(operationPanel);
        mainPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        mainPanel.add(progressBar);
    }

    private void createResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        resultTableModel = new PdfFilesTableModel(new Vector<>(), PdfFilesConstants.COLUMN_NAMES);
        resultTable = new JTable(resultTableModel);

        resultTable.setDefaultRenderer(Vector.class, new PdfFilesTableCellRenderer());

        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumn(resultTable.getColumnName(i)).setCellRenderer(new PdfFilesTableCellRenderer());
        }

        resultTable.addMouseListener(new MyMouseListener());

        resultTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        resultPanel.add(scrollPane);
    }

    private void processFile(File file) {
        if (scannedRadioButton.isSelected()) {
            int threshold = (Integer) thresholdSpinner.getValue();
            if (PdfUtils.isScannedPdf(file, threshold)) {
                resultFileList.add(file);
            }
        } else if (encryptedRadioButton.isSelected()) {
            if (PdfUtils.isEncryptedPdf(file)) {
                resultFileList.add(file);
            }
        } else if (nonOutlineRadioButton.isSelected()) {
            if (PdfUtils.isNonOutlinePdf(file)) {
                resultFileList.add(file);
            }
        } else if (hasAnnotationsRadioButton.isSelected()) {
            if (PdfUtils.hasAnnotations(file)) {
                resultFileList.add(file);
            }
        } else {
            logger.error("Invalid option selected");
        }
    }

    class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            int r = resultTable.rowAtPoint(e.getPoint());
            if (r >= 0 && r < resultTable.getRowCount()) {
                if (!resultTable.isRowSelected(r)) {
                    resultTable.setRowSelectionInterval(r, r);
                }
            } else {
                resultTable.clearSelection();
            }
            int[] rowsIndex = resultTable.getSelectedRows();
            if (rowsIndex == null || rowsIndex.length == 0) {
                return;
            }
            if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                JPopupMenu popupmenu = new JPopupMenu();
                MyMenuActionListener menuActionListener = new MyMenuActionListener();

                if (rowsIndex.length == 1) {
                    openDirMenuItem = new JMenuItem("Open parent folder of this file");
                    openDirMenuItem.addActionListener(menuActionListener);
                    popupmenu.add(openDirMenuItem);
                    popupmenu.addSeparator();
                }

                copyFilesMenuItem = new JMenuItem("Copy selected files to...");
                copyFilesMenuItem.addActionListener(menuActionListener);
                popupmenu.add(copyFilesMenuItem);

                popupmenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    class MyMenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            if (source.equals(openDirMenuItem)) {
                onOpenDir();
            } else if (source.equals(copyFilesMenuItem)) {
                onCopyFiles();
            } else {
                logger.error("invalid source");
            }
        }

        private void onOpenDir() {
            int rowIndex = resultTable.getSelectedRow();
            String parentPath = resultTableModel.getValueAt(rowIndex, resultTable.getColumn(PdfFilesConstants.COLUMN_NAME_FILE_PARENT).getModelIndex()).toString();
            File parent = new File(parentPath);
            RevealFileUtils.revealDirectory(parent);
        }

        private void onCopyFiles() {
            int[] selectedRows = resultTable.getSelectedRows();
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(PdfFinderPanel.this, "No rows selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<File> filesToCopy = new ArrayList<>();
            for (int rowIndex : selectedRows) {
                String filePath = resultTableModel.getValueAt(rowIndex, resultTable.getColumn(PdfFilesConstants.COLUMN_NAME_FILE_NAME).getModelIndex()).toString();
                String parentPath = resultTableModel.getValueAt(rowIndex, resultTable.getColumn(PdfFilesConstants.COLUMN_NAME_FILE_PARENT).getModelIndex()).toString();
                File file = new File(parentPath, filePath);
                filesToCopy.add(file);
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDialogTitle("Select Target Directory");
            int returnValue = fileChooser.showOpenDialog(PdfFinderPanel.this);
            if (returnValue != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File targetDir = fileChooser.getSelectedFile();
            for (File file : filesToCopy) {
                try {
                    org.apache.commons.io.FileUtils.copyFileToDirectory(file, targetDir);
                } catch (Exception e) {
                    logger.error("Copy file failed: " + file.getAbsolutePath(), e);
                }
            }
        }


    }

    class OperationButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source.equals(searchButton)) {
                searchButton.setEnabled(false);
                cancelButton.setEnabled(true);
                searchThread = new SearchThread(isRecursiveSearched.isSelected());
                searchThread.start();
            } else if (source.equals(cancelButton)) {
                searchButton.setEnabled(true);
                cancelButton.setEnabled(false);
                if (searchThread.isAlive()) {
                    searchThread.interrupt();
                    searchThread.executorService.shutdownNow();
                }
            }

        }
    }

    private void showResult() {
        SwingUtilities.invokeLater(() -> {
            int index = 0;
            for (File file : resultFileList) {
                index++;
                Vector<Object> rowData = getRowVector(index, file);
                resultTableModel.addRow(rowData);
            }
            tabbedPane.setSelectedIndex(1);
        });
    }

    private Vector<Object> getRowVector(int index, File file) {
        Vector<Object> rowData = new Vector<>();
        rowData.add(index);
        rowData.add(file.getParent());
        rowData.add(file.getName());
        rowData.add(FileUtils.sizeOfInHumanFormat(file));
        rowData.add(DateUtils.millisecondToHumanFormat(file.lastModified()));
        return rowData;
    }

    class SearchThread extends Thread {
        public final ExecutorService executorService;
        private final AtomicInteger processedFiles = new AtomicInteger(0);
        private int totalFiles = 0;
        private final boolean isRecursiveSearched;

        public SearchThread(boolean isRecursiveSearched) {
            super();
            this.isRecursiveSearched = isRecursiveSearched;
            this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            SwingUtilities.invokeLater(() -> {
                progressBar.setValue(0);
                progressBar.setString("Starting search...");
            });
        }

        @Override
        public void run() {
            try {
                resultFileList.clear();
                SwingUtilities.invokeLater(() -> resultTableModel.setRowCount(0));

                List<File> fileList = fileListPanel.getFileList();
                Set<File> fileSet = new TreeSet<>();
                String[] extensions = new String[]{"pdf", "PDF"};
                for (File file : fileList) {
                    fileSet.addAll(FileUtils.listFiles(file, extensions, isRecursiveSearched));
                }

                List<Future<?>> futures = new ArrayList<>();
                totalFiles = fileSet.size();
                updateProgress();

                for (File file : fileSet) {
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    futures.add(executorService.submit(() -> {
                        if (Thread.currentThread().isInterrupted()) {
                            return null;
                        }
                        processFile(file);
                        incrementProcessedFiles();
                        return null;
                    }));
                }

                // Wait for all tasks to complete
                for (Future<?> future : futures) {
                    try {
                        future.get();
                    } catch (InterruptedException e) {
                        logger.error("Search interrupted", e);
                        Thread.currentThread().interrupt(); // Restore interrupted status
                        return;
                    }
                }

                showResult();
            } catch (Exception e) {
                logger.error("Search failed", e);
                SwingUtilities.invokeLater(() -> progressBar.setString("Search failed"));
            } finally {
                executorService.shutdown();
                SwingUtilities.invokeLater(() -> {
                    searchButton.setEnabled(true);
                    cancelButton.setEnabled(false);
                });
            }
        }

        private void incrementProcessedFiles() {
            processedFiles.incrementAndGet();
            updateProgress();
        }

        private void updateProgress() {
            if (totalFiles > 0) {
                SwingUtilities.invokeLater(() -> {
                    int processed = processedFiles.get();
                    int percentage = (int) ((processed * 100.0) / totalFiles);
                    progressBar.setValue(percentage);
                    progressBar.setString(String.format("Processing: %d/%d files (%d%%)", processed, totalFiles, percentage));
                });
            }
        }
    }

    class RadioButtonItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            thresholdSpinner.setEnabled(scannedRadioButton.isSelected());
        }
    }
}
