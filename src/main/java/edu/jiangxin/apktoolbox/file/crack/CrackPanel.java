package edu.jiangxin.apktoolbox.file.crack;

import edu.jiangxin.apktoolbox.file.core.EncoderDetector;
import edu.jiangxin.apktoolbox.file.crack.bruteforce.BruteForceCrackerConsts;
import edu.jiangxin.apktoolbox.file.crack.bruteforce.BruteForceCrackerTask;
import edu.jiangxin.apktoolbox.file.crack.bruteforce.BruteForceFuture;
import edu.jiangxin.apktoolbox.file.crack.cracker.*;
import edu.jiangxin.apktoolbox.file.crack.dictionary.BigFileReader;
import edu.jiangxin.apktoolbox.file.crack.dictionary.FileHandle;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class CrackPanel extends EasyPanel {
    private JPanel optionPanel;

    private JTabbedPane categoryTabbedPane;
    private JPanel bruteForcePanel;
    private JPanel dictionaryPanel;

    private enum CATEGORY {
        UNKNOWN,
        BRUTE_FORCE,
        DICTIONARY_SINGLE_THREAD,
        DICTIONARY_MULTI_THREAD
    }
    private CATEGORY currentCategory = CATEGORY.UNKNOWN;

    private JPanel operationPanel;

    private JCheckBox numberCheckBox;
    private JCheckBox lowercaseLetterCheckBox;
    private JCheckBox uppercaseLetterCheckBox;

    private JSpinner minSpinner;
    private JSpinner maxSpinner;

    private JTextField dictionaryFileTextField;

    private JSpinner threadNumSpinner;

    private JProgressBar progressBar;

    private JComboBox<FileCracker> crackerTypeComboBox;
    private FileCracker currentFileCracker;

    private JTextField fileNameTextField;

    private JButton startButton;
    private JButton stopButton;

    private File selectedFile;
    private File dictionaryFile;
    private ExecutorService workerPool;

    private BigFileReader bigFileReader;
    private FileHandle fileHandle;

    public CrackPanel() {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createOptionPanel();
        add(optionPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));

        categoryTabbedPane = new JTabbedPane();

        createBruteForcePanel();
        categoryTabbedPane.addTab("Brute Force", null, bruteForcePanel, "Brute Force");
        categoryTabbedPane.setSelectedIndex(0);

        createDictionaryPanel();
        categoryTabbedPane.addTab("Dictionary", null, dictionaryPanel, "Dictionary");

        crackerTypeComboBox = new JComboBox<>();
        crackerTypeComboBox.addItem(new RarCracker());
        crackerTypeComboBox.addItem(new RarUsingRarCracker());
        crackerTypeComboBox.addItem(new ZipCracker());
        crackerTypeComboBox.addItem(new ZipUsing7ZipCracker());
        crackerTypeComboBox.addItem(new SevenZipCracker());
        crackerTypeComboBox.addItem(new PdfCracker());
        crackerTypeComboBox.setSelectedIndex(0);

        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setValue(0);

        optionPanel.add(categoryTabbedPane);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(crackerTypeComboBox);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(filePanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(progressBar);

        fileNameTextField = new JTextField();

        JButton chooseFileButton = new JButton("Choose File");
        chooseFileButton.addActionListener(new OpenFileActionListener());

        filePanel.add(fileNameTextField);
        filePanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        filePanel.add(chooseFileButton);
    }

    private void createBruteForcePanel() {
        bruteForcePanel = new JPanel();

        JPanel topLevelPanel = new JPanel();
        topLevelPanel.setLayout(new BoxLayout(topLevelPanel, BoxLayout.Y_AXIS));
        bruteForcePanel.add(topLevelPanel);

        JPanel charsetPanel = new JPanel();
        charsetPanel.setLayout(new BoxLayout(charsetPanel, BoxLayout.X_AXIS));

        JPanel passwordLengthPanel = new JPanel();
        passwordLengthPanel.setLayout(new BoxLayout(passwordLengthPanel, BoxLayout.X_AXIS));

        topLevelPanel.add(charsetPanel);
        topLevelPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        topLevelPanel.add(passwordLengthPanel);

        numberCheckBox = new JCheckBox("Number");
        numberCheckBox.setSelected(true);
        lowercaseLetterCheckBox = new JCheckBox("Lowercase Letter");
        uppercaseLetterCheckBox = new JCheckBox("Uppercase Letter");

        charsetPanel.add(numberCheckBox);
        charsetPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        charsetPanel.add(lowercaseLetterCheckBox);
        charsetPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        charsetPanel.add(uppercaseLetterCheckBox);

        JLabel minLabel = new JLabel("Minimum Length: ");
        JLabel maxLabel = new JLabel("Maximum Length: ");

        minSpinner = new JSpinner();
        minSpinner.setModel(new SpinnerNumberModel(1, 1, 9, 1));
        minSpinner.setToolTipText("Minimum Length");

        maxSpinner = new JSpinner();
        maxSpinner.setModel(new SpinnerNumberModel(6, 1, 9, 1));
        maxSpinner.setToolTipText("Maximum Length");

        passwordLengthPanel.add(minLabel);
        passwordLengthPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        passwordLengthPanel.add(minSpinner);
        passwordLengthPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        passwordLengthPanel.add(maxLabel);
        passwordLengthPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        passwordLengthPanel.add(maxSpinner);
    }

    private void createDictionaryPanel() {
        dictionaryPanel = new JPanel();

        JPanel topLevelPanel = new JPanel();
        topLevelPanel.setLayout(new BoxLayout(topLevelPanel, BoxLayout.Y_AXIS));
        dictionaryPanel.add(topLevelPanel);

        JPanel dictionaryPathPanel = new JPanel();
        dictionaryPathPanel.setLayout(new BoxLayout(dictionaryPathPanel, BoxLayout.X_AXIS));

        JPanel threadNumPanel = new JPanel();
        threadNumPanel.setLayout(new BoxLayout(threadNumPanel, BoxLayout.X_AXIS));

        topLevelPanel.add(dictionaryPathPanel);
        topLevelPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        topLevelPanel.add(threadNumPanel);

        dictionaryFileTextField = new JTextField();
        dictionaryFileTextField.setPreferredSize(new Dimension(600, 0));

        JButton chooseFileButton = new JButton("Choose Dictionary File");
        chooseFileButton.addActionListener(new OpenDictionaryFileActionListener());

        dictionaryPathPanel.add(dictionaryFileTextField);
        dictionaryPathPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        dictionaryPathPanel.add(chooseFileButton);

        JLabel threadNumLabel = new JLabel("Thread Number: ");

        threadNumSpinner = new JSpinner();
        threadNumSpinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        threadNumSpinner.setToolTipText("Thread Number");

        threadNumPanel.add(threadNumLabel);
        threadNumPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        threadNumPanel.add(threadNumSpinner);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");

        operationPanel.add(startButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(stopButton);

        startButton.addActionListener(e -> {
            if (selectedFile == null) {
                logger.error("file is null");
                return;
            }
            FileCracker fileCracker = (FileCracker) crackerTypeComboBox.getSelectedItem();
            if (fileCracker == null) {
                logger.error("fileCracker is null");
                return;
            }
            new Thread(this::onStart).start();
        });

        stopButton.addActionListener(e -> new Thread(this::onStop).start());
    }

    class OpenFileActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            FileCracker fileCracker = (FileCracker) crackerTypeComboBox.getSelectedItem();
            if (fileCracker == null) {
                logger.error("fileCracker is null");
                return;
            }

            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(fileCracker.getFileDescription(), fileCracker.getFileExtensions());
            fileChooser.addChoosableFileFilter(filter);
            int returnVal = fileChooser.showOpenDialog(CrackPanel.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                fileCracker.attachFile(selectedFile);
                fileNameTextField.setText(selectedFile.getAbsolutePath());
            }
        }
    }

    class OpenDictionaryFileActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.dic;*.txt", "dic", "txt");
            fileChooser.addChoosableFileFilter(filter);
            int returnVal = fileChooser.showOpenDialog(CrackPanel.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                dictionaryFile = fileChooser.getSelectedFile();
                if (dictionaryFile != null) {
                    dictionaryFileTextField.setText(dictionaryFile.getAbsolutePath());
                }
            }
        }
    }

    private void onStart() {
        Component selectedPanel = categoryTabbedPane.getSelectedComponent();
        if (selectedPanel.equals(bruteForcePanel)) {
            currentCategory = CATEGORY.BRUTE_FORCE;
        } else if (selectedPanel.equals(dictionaryPanel)) {
            int threadNum = (Integer) threadNumSpinner.getValue();
            if (threadNum == 1) {
                currentCategory = CATEGORY.DICTIONARY_SINGLE_THREAD;
            } else {
                currentCategory = CATEGORY.DICTIONARY_MULTI_THREAD;
            }
        } else {
            currentCategory = CATEGORY.UNKNOWN;
        }

        currentFileCracker = (FileCracker) crackerTypeComboBox.getSelectedItem();
        if (currentFileCracker == null || !currentFileCracker.prepareCracker()) {
            JOptionPane.showMessageDialog(this, "onStart failed: Crack condition does not been prepared!");
            return;
        }
        switch (currentCategory) {
            case BRUTE_FORCE:
                onStartByBruteForceCategory();
                break;
            case DICTIONARY_SINGLE_THREAD:
                onStartByDictionarySingleThreadCategory();
                break;
            case DICTIONARY_MULTI_THREAD:
                onStartByDictionaryMultiThreadCategory();
                break;
            case UNKNOWN:
                JOptionPane.showMessageDialog(this, "onStart failed: Invalid category!");
                break;
        }
    }

    private void onStop() {
        switch (currentCategory) {
            case BRUTE_FORCE:
                onStopByBruteForceCategory();
                break;
            case DICTIONARY_SINGLE_THREAD:
                onStopByDictionarySingleThreadCategory();
                break;
            case DICTIONARY_MULTI_THREAD:
                onStopByDictionaryMultiThreadCategory();
                break;
            case UNKNOWN:
                JOptionPane.showMessageDialog(this, "onStop failed: Invalid category!");
                break;
        }
    }

    private void onStartByBruteForceCategory() {
        logger.info("onStartByBruteForceCategory");
        StringBuilder sb = new StringBuilder();
        if (numberCheckBox.isSelected()) {
            sb.append("0123456789");
        }
        if (lowercaseLetterCheckBox.isSelected()) {
            sb.append("abcdefghijklmnopqrstuvwxyz");
        }
        if (uppercaseLetterCheckBox.isSelected()) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        if (sb.length() <= 0) {
            JOptionPane.showMessageDialog(this, "Character set is empty!");
            return;
        }
        final String charSet = sb.toString();

        int minLength = (Integer) minSpinner.getValue();
        int maxLength = (Integer) maxSpinner.getValue();
        if (minLength > maxLength) {
            JOptionPane.showMessageDialog(this, "Minimum length is bigger than maximum length!");
            return;
        }

        setWaitMode(true);
        String password = null;
        for (int length = minLength; length <= maxLength; length++) {
            setProgressMaxValue((int)Math.pow(charSet.length(), length));
            setProgressBarValue(0);
            long startTime = System.currentTimeMillis();
            int numThreads = Math.min(getThreadCount(charSet.length(), length), currentFileCracker.getMaxThreadNum());
            logger.info("[" + currentFileCracker + "]Current attempt length: " + length + ", thread number: " + numThreads);

            workerPool = Executors.newFixedThreadPool(numThreads);
            BruteForceFuture bruteForceFuture = new BruteForceFuture(numThreads);
            BruteForceCrackerConsts consts = new BruteForceCrackerConsts(numThreads, length, currentFileCracker, charSet);

            for (int taskId = 0; taskId < numThreads; taskId++) {
                BruteForceCrackerTask task = new BruteForceCrackerTask(taskId, true, consts, bruteForceFuture, this);
                workerPool.execute(task);
            }
            try {
                password = bruteForceFuture.get();
            } catch (Exception e) {
                logger.info("Exception test: ", e);
            } finally {
                if (workerPool.isShutdown()) {
                    workerPool.shutdown();
                }
            }
            long endTime = System.currentTimeMillis();
            logger.info("Current attempt length: " + length + ", Cost time: " + (endTime - startTime) + "ms");
            if (password != null) {
                break;
            }
        }
        if (password == null) {
            JOptionPane.showMessageDialog(this, "Can not find password");
        } else {
            JOptionPane.showMessageDialog(this, password);
        }
        onStopByBruteForceCategory();
    }

    private void onStartByDictionarySingleThreadCategory() {
        logger.info("onStartByDictionarySingleThreadCategory");
        if (dictionaryFile == null || !dictionaryFile.isFile()) {
            JOptionPane.showMessageDialog(this, "Invalid dictionary file");
            return;
        }
        String charsetName = EncoderDetector.judgeFile(dictionaryFile.getAbsolutePath());
        logger.info("dictionary file: " + dictionaryFile.getAbsolutePath() + ", charset: " + charsetName);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dictionaryFile), charsetName))) {
            setWaitMode(true);
            setProgressMaxValue(Utils.getFileLineCount(dictionaryFile));
            setProgressBarValue(0);
            Function<String, Stream<String>> generator = password -> {
                increaseProgressBarValue();
                return Stream.of(password.toLowerCase(), password.toUpperCase());
            };
            Predicate<String> verifier = currentFileCracker::checkPassword;
            Optional<String> password = br.lines().flatMap(generator).filter(verifier).findFirst();
            if (password.isPresent()) {
                JOptionPane.showMessageDialog(this, password.get());
            } else {
                JOptionPane.showMessageDialog(this, "Can not find password");
            }
        } catch (FileNotFoundException e) {
            logger.info("FileNotFoundException");
        } catch (IOException e) {
            logger.info("IOException");
        }
        onStopByDictionarySingleThreadCategory();
    }

    private void onStartByDictionaryMultiThreadCategory() {
        logger.info("onStartByDictionaryMultiThreadCategory");
        if (dictionaryFile == null || !dictionaryFile.isFile()) {
            JOptionPane.showMessageDialog(this, "Invalid dictionary file");
            return;
        }
        int threadNum = (Integer) threadNumSpinner.getValue();
        try {
            setWaitMode(true);
            setProgressMaxValue(Utils.getFileLineCount(dictionaryFile));
            setProgressBarValue(0);
            fileHandle = new FileHandle(currentFileCracker, new AtomicBoolean(false), this);
            String charsetName = EncoderDetector.judgeFile(dictionaryFile.getAbsolutePath());
            BigFileReader.Builder builder = new BigFileReader.Builder(dictionaryFile.getAbsolutePath(), fileHandle);
            bigFileReader = builder.withThreadSize(threadNum).withCharset(charsetName).withBufferSize(1024 * 1024).build();
            bigFileReader.setCompleteCallback(() -> {
                if (fileHandle != null && !fileHandle.getSuccess().get()) {
                    logger.error("Can not find password");
                    JOptionPane.showMessageDialog(CrackPanel.this, "Can not find password");
                    onStopByDictionaryMultiThreadCategory();
                }
            });
            bigFileReader.start();
        } catch (IOException e) {
            logger.error("onStartByDictionaryMultiThreadCategory", e);
        }
    }

    private void onStopByBruteForceCategory() {
        logger.info("onStopByBruteForceCategory");
        if (workerPool == null) {
            setWaitMode(false);
            return;
        }
        if (!workerPool.isShutdown()) {
            workerPool.shutdownNow();
        }
        while (!workerPool.isTerminated()) {
            try {
                final boolean isTimeout = workerPool.awaitTermination(100, TimeUnit.SECONDS);
                logger.info("awaitTermination isTimeout: " + isTimeout);
            } catch (InterruptedException e) {
                logger.error("awaitTermination failed");
            }
        }
        setProgressBarValue(0);
        setWaitMode(false);
    }

    private void onStopByDictionarySingleThreadCategory() {
        logger.info("onStopByDictionarySingleThreadCategory");
        setProgressBarValue(0);
        setWaitMode(false);
    }

    private void onStopByDictionaryMultiThreadCategory() {
        logger.info("onStopByDictionaryMultiThreadCategory");
        if (bigFileReader != null) {
            fileHandle.stop();
        }
        if (bigFileReader != null) {
            bigFileReader.shutdown();
        }
        setProgressBarValue(0);
        setWaitMode(false);
    }

    private int getThreadCount(int charSetSize, int length) {
        int result = 1;
        for (int i = 1; i <= length; i++) {
            result *= charSetSize;
        }
        return result;
    }

    public void setWaitMode(boolean isWaitMode) {
        if (isWaitMode) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        } else {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }

    public void setProgressMaxValue(int maxValue) {
        if (progressBar != null) {
            progressBar.setMaximum(maxValue);
        }
    }
    public void setProgressBarValue(int value) {
        if (progressBar != null) {
            progressBar.setValue(value);
        }
    }
    public void increaseProgressBarValue() {
        if (progressBar != null) {
            int currentValue = progressBar.getValue();
            progressBar.setValue(currentValue + 1);
        }
    }
}

