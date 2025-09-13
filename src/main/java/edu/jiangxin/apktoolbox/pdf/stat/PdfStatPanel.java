package edu.jiangxin.apktoolbox.pdf.stat;

import edu.jiangxin.apktoolbox.pdf.PdfUtils;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.FileListPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.DateUtils;
import edu.jiangxin.apktoolbox.utils.FileUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serial;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class PdfStatPanel extends EasyPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;

    private JPanel mainPanel;

    private FileListPanel fileListPanel;

    private JRadioButton pageCountRadioButton;

    private JCheckBox isRecursiveSearched;

    private JPanel resultPanel;

    private JTable resultTable;

    private DefaultTableModel resultTableModel;

    private JLabel statInfoLabel;

    private JButton statButton;
    private JButton cancelButton;

    private JProgressBar progressBar;

    private SearchThread searchThread;

    private long totalFileSize = 0;

    private int totalPageCount = 0;

    final private List<Vector<Object>> resultFileList = new ArrayList<>();


    @Override
    public void initUI() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        createMainPanel();
        tabbedPane.addTab("Option", null, mainPanel, "Show Stat Options");

        createResultPanel();
        tabbedPane.addTab("Result", null, resultPanel, "Show Stat Result");
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        fileListPanel = new FileListPanel();

        JPanel checkOptionPanel = new JPanel();
        checkOptionPanel.setLayout(new BoxLayout(checkOptionPanel, BoxLayout.X_AXIS));
        checkOptionPanel.setBorder(BorderFactory.createTitledBorder("Check Options"));

        ButtonGroup buttonGroup = new ButtonGroup();

        pageCountRadioButton = new JRadioButton("统计页数");
        pageCountRadioButton.setSelected(true);
        pageCountRadioButton.setEnabled(false);
        buttonGroup.add(pageCountRadioButton);

        JPanel typePanel = new JPanel();
        typePanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,3));
        typePanel.add(pageCountRadioButton);

        checkOptionPanel.add(typePanel);
        checkOptionPanel.add(Box.createHorizontalGlue());

        JPanel searchOptionPanel = new JPanel();
        searchOptionPanel.setLayout(new BoxLayout(searchOptionPanel, BoxLayout.X_AXIS));
        searchOptionPanel.setBorder(BorderFactory.createTitledBorder("Stat Options"));

        isRecursiveSearched = new JCheckBox("Recursive");
        isRecursiveSearched.setSelected(true);
        searchOptionPanel.add(isRecursiveSearched);
        searchOptionPanel.add(Box.createHorizontalGlue());

        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        operationPanel.setBorder(BorderFactory.createTitledBorder("Operations"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        statButton = new JButton("Stat");
        cancelButton = new JButton("Cancel");
        cancelButton.setEnabled(false);
        statButton.addActionListener(new OperationButtonActionListener());
        cancelButton.addActionListener(new OperationButtonActionListener());
        operationPanel.add(statButton);
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
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        JPanel statInfoPanel = new JPanel();
        statInfoPanel.setLayout(new BoxLayout(statInfoPanel, BoxLayout.X_AXIS));
        statInfoLabel = new JLabel("");
        statInfoPanel.add(statInfoLabel);
        statInfoPanel.add(Box.createHorizontalGlue());

        resultPanel.add(scrollPane);
        resultPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        resultPanel.add(statInfoPanel);
    }

    private void processFile(File file) {
        if (pageCountRadioButton.isSelected()) {
            Vector<Object> fileVector = getRowVector(resultFileList.size() + 1, file);
            resultFileList.add(fileVector);
        } else {
            logger.error("Invalid option selected");
        }
    }

    class OperationButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source.equals(statButton)) {
                statButton.setEnabled(false);
                cancelButton.setEnabled(true);
                searchThread = new SearchThread(isRecursiveSearched.isSelected());
                searchThread.start();
            } else if (source.equals(cancelButton)) {
                statButton.setEnabled(true);
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
            for (Vector<Object> file : resultFileList) {
                resultTableModel.addRow(file);
            }
            tabbedPane.setSelectedIndex(1);
            statInfoLabel.setText("Page Count: " + totalPageCount + ", Total Size: " + FileUtils.sizeOfInHumanFormat(totalFileSize));
        });
    }

    private Vector<Object> getRowVector(int index, File file) {
        Vector<Object> rowData = new Vector<>();
        rowData.add(index);
        rowData.add(file.getParent());
        rowData.add(file.getName());
        totalFileSize += file.length();
        rowData.add(FileUtils.sizeOfInHumanFormat(file));
        rowData.add(DateUtils.millisecondToHumanFormat(file.lastModified()));
        int pageCount = PdfUtils.getPageCount(file);
        totalPageCount += pageCount;
        rowData.add(pageCount);
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

            resultFileList.clear();
            totalFileSize = 0;
            totalPageCount = 0;

            SwingUtilities.invokeLater(() -> {
                progressBar.setValue(0);
                progressBar.setString("Starting search...");
                resultTableModel.setRowCount(0);
                statInfoLabel.setText("");
            });
        }

        @Override
        public void run() {
            try {
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
                    if (currentThread().isInterrupted()) {
                        return;
                    }
                    futures.add(executorService.submit(() -> {
                        if (currentThread().isInterrupted()) {
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
                        currentThread().interrupt(); // Restore interrupted status
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
                    statButton.setEnabled(true);
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
                    int percentage = (int) (processed * 100.0 / totalFiles);
                    progressBar.setValue(percentage);
                    progressBar.setString(String.format("Processing: %d/%d files (%d%%)", processed, totalFiles, percentage));
                });
            }
        }
    }
}
