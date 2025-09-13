package edu.jiangxin.apktoolbox.pdf.passwordremover;

import edu.jiangxin.apktoolbox.pdf.PdfUtils;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.FileListPanel;
import edu.jiangxin.apktoolbox.swing.extend.filepanel.FilePanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class PdfPasswordRemoverPanel extends EasyPanel {
    @Serial
    private static final long serialVersionUID = 1L;

    private FileListPanel fileListPanel;

    private JCheckBox isRecursiveSearched;

    private FilePanel targetDirPanel;

    private JButton startButton;
    private JButton cancelButton;

    private JProgressBar progressBar;

    private transient WorkderThread searchThread;

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createInputPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOutputPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOptionPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createProcessBarPanel();
    }

    private void createInputPanel() {
        fileListPanel = new FileListPanel();
        fileListPanel.initialize();
        add(fileListPanel);
    }

    private void createOutputPanel() {
        targetDirPanel = new FilePanel("Target Directory");
        targetDirPanel.initialize();
        targetDirPanel.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        add(targetDirPanel);
    }

    private void createOptionPanel() {
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));
        optionPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        isRecursiveSearched = new JCheckBox("Recursive Search");
        isRecursiveSearched.setSelected(true);
        optionPanel.add(isRecursiveSearched);
        optionPanel.add(Box.createHorizontalGlue());

        add(optionPanel);
    }

    private void createOperationPanel() {
        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        operationPanel.setBorder(BorderFactory.createTitledBorder("Operations"));

        startButton = new JButton("Start");
        cancelButton = new JButton("Cancel");
        cancelButton.setEnabled(false);
        startButton.addActionListener(new OperationButtonActionListener());
        cancelButton.addActionListener(new OperationButtonActionListener());
        operationPanel.add(startButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(cancelButton);
        operationPanel.add(Box.createHorizontalGlue());

        add(operationPanel);
    }

    private void createProcessBarPanel() {
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");

        add(progressBar);
    }

    private void processFile(File encryptedFile, File targetDir) {
        try {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            PdfUtils.removePasswordWithIText(encryptedFile, targetDir);
        } catch (Throwable t) {
            logger.error("Process file failed: {}", encryptedFile.getAbsolutePath(), t);
        }
    }

    class OperationButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source.equals(startButton)) {
                startButton.setEnabled(false);
                cancelButton.setEnabled(true);
                searchThread = new WorkderThread(isRecursiveSearched.isSelected());
                searchThread.start();
            } else if (source.equals(cancelButton)) {
                startButton.setEnabled(true);
                cancelButton.setEnabled(false);
                if (searchThread.isAlive()) {
                    searchThread.interrupt();
                    searchThread.executorService.shutdownNow();
                }
            }

        }
    }

    class WorkderThread extends Thread {
        public final ExecutorService executorService;
        private final AtomicInteger processedFiles = new AtomicInteger(0);
        private int totalFiles = 0;
        private final boolean isRecursiveSearched;

        public WorkderThread(boolean isRecursiveSearched) {
            super();
            this.isRecursiveSearched = isRecursiveSearched;
            this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            SwingUtilities.invokeLater(() -> {
                progressBar.setValue(0);
                progressBar.setString("Starting remove process...");
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
                        processFile(file, targetDirPanel.getFile());
                        incrementProcessedFiles();
                        return null;
                    }));
                }

                // Wait for all tasks to complete
                for (Future<?> future : futures) {
                    try {
                        future.get();
                    } catch (InterruptedException e) {
                        logger.error("Remove process interrupted", e);
                        currentThread().interrupt(); // Restore interrupted status
                        return;
                    }
                }
            } catch (Exception e) {
                logger.error("Remove process failed", e);
                SwingUtilities.invokeLater(() -> progressBar.setString("Remove process failed"));
            } finally {
                executorService.shutdown();
                SwingUtilities.invokeLater(() -> {
                    startButton.setEnabled(true);
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