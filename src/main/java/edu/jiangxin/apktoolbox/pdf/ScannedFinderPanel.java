package edu.jiangxin.apktoolbox.pdf;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.FileListPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.DateUtils;
import edu.jiangxin.apktoolbox.utils.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ScannedFinderPanel extends EasyPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;

    private JPanel optionPanel;

    private FileListPanel fileListPanel;

    private JSpinner thresholdSpinner;

    private JCheckBox isRecursiveSearched;

    private JPanel resultPanel;

    private JTable resultTable;

    private DefaultTableModel resultTableModel;

    private JButton searchButton;
    private JButton cancelButton;

    private JProgressBar progressBar;

    private JMenuItem openDirMenuItem;

    private Thread searchThread;

    final private List<File> scannedFileList = new ArrayList<>();


    @Override
    public void initUI() {
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

        JLabel thresholdLabel = new JLabel("Threshold: ");
        thresholdSpinner = new JSpinner();
        thresholdSpinner.setModel(new SpinnerNumberModel(1, 0, 100, 1));

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

        optionPanel.add(fileListPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(checkOptionPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(searchOptionPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(operationPanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(progressBar);
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
        int threshold = (Integer) thresholdSpinner.getValue();
        int length = 0;
        boolean isEncrypted;

        try (PDDocument document = Loader.loadPDF(file)) {
            isEncrypted = document.isEncrypted();
            if (isEncrypted) {
                document.setAllSecurityToBeRemoved(true);
            }

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document).trim();
            length = text.length();
        } catch (IOException e) {
            logger.error("Error reading PDF file: {}", file.getAbsolutePath());
            return;
        }
        logger.info("Processing file: {}, is encrypted: {}, text size: {}", file.getPath(), isEncrypted, length);

        if (length < threshold) {
            scannedFileList.add(file);
        }
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
            } else {
                logger.error("invalid source");
            }
        }

        private void onOpenDir() {
            int rowIndex = resultTable.getSelectedRow();
            String parentPath = resultTableModel.getValueAt(rowIndex, resultTable.getColumn(PdfFilesConstants.COLUMN_NAME_FILE_PARENT).getModelIndex()).toString();
            File parent = new File(parentPath);
            if (parent.isDirectory()) {
                try {
                    Desktop.getDesktop().open(parent);
                } catch (IOException e) {
                    logger.error("open parent failed: {}", parent.getPath());
                }
            }
        }
    }

    class OperationButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source.equals(searchButton)) {
                searchThread = new SearchThread(isRecursiveSearched.isSelected(), scannedFileList);
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
            int index = 0;
            for (File file : scannedFileList) {
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
        private final ExecutorService executorService;
        private final AtomicInteger processedFiles = new AtomicInteger(0);
        private int totalFiles = 0;
        private final boolean isRecursiveSearched;
        private final List<File> scannedFileList;

        public SearchThread(boolean isRecursiveSearched, List<File> scannedFileList) {
            super();
            this.isRecursiveSearched = isRecursiveSearched;
            this.scannedFileList = scannedFileList;
            this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            SwingUtilities.invokeLater(() -> {
                progressBar.setValue(0);
                progressBar.setString("Starting search...");
            });
        }

        @Override
        public void run() {
            try {
                scannedFileList.clear();
                SwingUtilities.invokeLater(() -> resultTableModel.setRowCount(0));

                List<File> fileList = fileListPanel.getFileList();
                Set<File> fileSet = new TreeSet<>();
                String[] extensions = new String[]{"pdf", "PDF"};
                for (File file : fileList) {
                    fileSet.addAll(org.apache.commons.io.FileUtils.listFiles(file, extensions, isRecursiveSearched));
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
}
