package edu.jiangxin.apktoolbox.file.password.recovery;

import edu.jiangxin.apktoolbox.file.core.EncoderDetector;
import edu.jiangxin.apktoolbox.file.password.recovery.bruteforce.BruteForceProxy;
import edu.jiangxin.apktoolbox.file.password.recovery.checker.*;
import edu.jiangxin.apktoolbox.file.password.recovery.dictionary.multithread.DictionaryMultiThreadProxy;
import edu.jiangxin.apktoolbox.file.password.recovery.dictionary.singlethread.DictionarySingleThreadProxy;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.filepanel.FilePanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

public final class RecoveryPanel extends EasyPanel implements Synchronizer {
    private JPanel optionPanel;

    private FilePanel recoveryFilePanel;

    private JTabbedPane categoryTabbedPane;

    private JPanel bruteForceCategoryPanel;

    private JPanel dictionaryCategoryPanel;

    private FilePanel dictionaryFilePanel;

    private Category currentCategory = Category.UNKNOWN;

    private JPanel operationPanel;

    private JCheckBox numberCheckBox;
    private JCheckBox lowercaseLetterCheckBox;
    private JCheckBox uppercaseLetterCheckBox;

    private JCheckBox userIncludedCheckBox;

    private JTextField userIncludedTextField;

    private JCheckBox userExcludedCheckBox;

    private JTextField userExcludedTextField;

    private JSpinner minSpinner;
    private JSpinner maxSpinner;

    private JSpinner threadNumSpinner;

    private JProgressBar progressBar;

    private JComboBox<FileChecker> checkerTypeComboBox;

    private FileChecker currentFileChecker;

    private JButton startButton;
    private JButton stopButton;

    private JLabel currentStateLabel;

    private JLabel currentPasswordLabel;

    private NumberFormat numberFormat;

    private State currentState = State.IDLE;

    public RecoveryPanel() {
        super();
        initBase();
    }

    private void initBase() {
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(3);
    }

    @Override
    public void initUI() {
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

        checkerTypeComboBox = new JComboBox<>();
        checkerTypeComboBox.addItem(new ArchiveUsing7ZipChecker());
        checkerTypeComboBox.addItem(new ArchiveUsingWinRarChecker());
        checkerTypeComboBox.addItem(new RarUsingRarChecker());
        checkerTypeComboBox.addItem(new ZipChecker());
        checkerTypeComboBox.addItem(new RarChecker());
        checkerTypeComboBox.addItem(new SevenZipChecker());
        checkerTypeComboBox.addItem(new PdfChecker());
        checkerTypeComboBox.addItem(new XmlBasedOfficeChecker());
        checkerTypeComboBox.addItem(new BinaryOfficeChecker());
        checkerTypeComboBox.setSelectedIndex(0);
        checkerTypeComboBox.addItemListener(e -> {
            FileChecker fileChecker = (FileChecker) e.getItem();
            if (fileChecker == null) {
                logger.error("fileChecker is null");
                return;
            }
            recoveryFilePanel.setDescriptionAndFileExtensions(
                    fileChecker.getFileDescription(), fileChecker.getFileExtensions());
        });

        FileChecker fileChecker = (FileChecker) checkerTypeComboBox.getSelectedItem();
        if (fileChecker == null) {
            logger.error("fileChecker is null");
            return;
        }

        recoveryFilePanel = new FilePanel("Choose Recovery File");
        recoveryFilePanel.setDescriptionAndFileExtensions(fileChecker.getFileDescription(), fileChecker.getFileExtensions());

        categoryTabbedPane = new JTabbedPane();

        createBruteForcePanel();
        categoryTabbedPane.addTab("Brute Force", null, bruteForceCategoryPanel, "Brute Force");
        categoryTabbedPane.setSelectedIndex(0);

        createDictionaryPanel();
        categoryTabbedPane.addTab("Dictionary", null, dictionaryCategoryPanel, "Dictionary");

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        String text = numberFormat.format(0);
        progressBar.setString(text);

        optionPanel.add(checkerTypeComboBox);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(recoveryFilePanel);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(categoryTabbedPane);
        optionPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        optionPanel.add(progressBar);
    }

    private void createBruteForcePanel() {
        bruteForceCategoryPanel = new JPanel();

        JPanel topLevelPanel = new JPanel();
        topLevelPanel.setLayout(new BoxLayout(topLevelPanel, BoxLayout.Y_AXIS));
        bruteForceCategoryPanel.add(topLevelPanel);

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
        userIncludedCheckBox = new JCheckBox("User-defined");
        userIncludedTextField = new JTextField(10);
        userExcludedCheckBox = new JCheckBox("User-excluded");
        userExcludedTextField = new JTextField(10);


        charsetPanel.add(numberCheckBox);
        charsetPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        charsetPanel.add(lowercaseLetterCheckBox);
        charsetPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        charsetPanel.add(uppercaseLetterCheckBox);
        charsetPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        charsetPanel.add(userIncludedCheckBox);
        charsetPanel.add(userIncludedTextField);
        charsetPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        charsetPanel.add(userExcludedCheckBox);
        charsetPanel.add(userExcludedTextField);

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
        dictionaryCategoryPanel = new JPanel();

        JPanel topLevelPanel = new JPanel();
        topLevelPanel.setLayout(new BoxLayout(topLevelPanel, BoxLayout.Y_AXIS));
        dictionaryCategoryPanel.add(topLevelPanel);

        dictionaryFilePanel = new FilePanel("Choose Dictionary File");
        dictionaryFilePanel.setDescriptionAndFileExtensions("*.dic;*.txt", new String[]{"dic", "txt"});

        JPanel threadNumPanel = new JPanel();
        threadNumPanel.setLayout(new BoxLayout(threadNumPanel, BoxLayout.X_AXIS));

        topLevelPanel.add(dictionaryFilePanel);
        topLevelPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        topLevelPanel.add(threadNumPanel);

        JLabel threadNumLabel = new JLabel("Thread Number: ");

        threadNumSpinner = new JSpinner();
        threadNumSpinner.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
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
        currentStateLabel = new JLabel();
        currentPasswordLabel = new JLabel();
        currentPasswordLabel.setText("");

        operationPanel.add(startButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(stopButton);
        operationPanel.add(Box.createHorizontalStrut(2 * Constants.DEFAULT_X_BORDER));
        operationPanel.add(currentStateLabel);
        operationPanel.add(Box.createHorizontalStrut(2 * Constants.DEFAULT_X_BORDER));
        operationPanel.add(currentPasswordLabel);
        operationPanel.add(Box.createHorizontalGlue());

        startButton.addActionListener(e -> new Thread(this::onStart).start());

        stopButton.addActionListener(e -> new Thread(this::onStop).start());

        setCurrentState(State.IDLE);
    }

    private void onStart() {
        FileChecker fileChecker = (FileChecker) checkerTypeComboBox.getSelectedItem();
        if (fileChecker == null) {
            JOptionPane.showMessageDialog(this, "fileChecker is null");
            return;
        }
        if (!fileChecker.prepareChecker()) {
            JOptionPane.showMessageDialog(this, "onStart failed: Checker condition does not been prepared!");
            return;
        }

        File selectedFile = recoveryFilePanel.getFile();
        if (!selectedFile.isFile()) {
            JOptionPane.showMessageDialog(this, "file is null");
            return;
        }

        String extension = FilenameUtils.getExtension(recoveryFilePanel.getFile().getName());
        if (!ArrayUtils.contains(fileChecker.getFileExtensions(), StringUtils.toRootLowerCase(extension))) {
            JOptionPane.showMessageDialog(this, "invalid file");
            return;
        }

        fileChecker.attachFile(selectedFile);
        currentFileChecker = fileChecker;

        Component selectedPanel = categoryTabbedPane.getSelectedComponent();
        if (selectedPanel.equals(bruteForceCategoryPanel)) {
            currentCategory = Category.BRUTE_FORCE;
        } else if (selectedPanel.equals(dictionaryCategoryPanel)) {
            int threadNum = (Integer) threadNumSpinner.getValue();
            if (threadNum == 1) {
                currentCategory = Category.DICTIONARY_SINGLE_THREAD;
            } else {
                currentCategory = Category.DICTIONARY_MULTI_THREAD;
            }
        } else {
            currentCategory = Category.UNKNOWN;
        }
        logger.info("onStart: " + currentCategory);
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
            default:
                JOptionPane.showMessageDialog(this, "onStart failed: Invalid category!");
                break;
        }
    }

    private void onStop() {
        logger.info("onStop: " + currentCategory);
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
            default:
                JOptionPane.showMessageDialog(this, "onStop failed: Invalid category!");
                break;
        }
    }

    private void onStartByBruteForceCategory() {
        String charset = calcCharset();
        logger.info("Charset: {}", charset);
        if (StringUtils.isEmpty(charset)) {
            JOptionPane.showMessageDialog(this, "Character set is empty!");
            return;
        }

        int minLength = (Integer) minSpinner.getValue();
        int maxLength = (Integer) maxSpinner.getValue();
        if (minLength > maxLength) {
            JOptionPane.showMessageDialog(this, "Minimum length is bigger than maximum length!");
            return;
        }

        setCurrentState(State.WORKING);
        String password = null;
        for (int length = minLength; length <= maxLength; length++) {
            if (currentState != State.WORKING) {
                logger.info("Break because of state: " + currentState);
                break;
            }
            setProgressMaxValue((int) Math.pow(charset.length(), length));
            setProgressBarValue(0);
            long startTime = System.currentTimeMillis();
            int numThreads = getThreadCount(charset.length(), length, currentFileChecker.getMaxThreadNum());
            logger.info("[" + currentFileChecker + "]Current attempt length: " + length + ", thread number: " + numThreads);
            BruteForceProxy proxy = BruteForceProxy.getInstance();
            password = proxy.startAndGet(numThreads, length, currentFileChecker, charset, this);
            long endTime = System.currentTimeMillis();
            logger.info("Current attempt length: " + length + ", Cost time: " + (endTime - startTime) + "ms");
            if (password != null) {
                break;
            }
        }
        showResultWithDialog(password);
        onStopByBruteForceCategory();
    }

    private String calcCharset() {
        Set<Character> charsetSet = new HashSet<>();
        if (numberCheckBox.isSelected()) {
            CollectionUtils.addAll(charsetSet, ArrayUtils.toObject("0123456789".toCharArray()));
        }
        if (lowercaseLetterCheckBox.isSelected()) {
            CollectionUtils.addAll(charsetSet, ArrayUtils.toObject("abcdefghijklmnopqrstuvwxyz".toCharArray()));
        }
        if (uppercaseLetterCheckBox.isSelected()) {
            CollectionUtils.addAll(charsetSet, ArrayUtils.toObject("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()));
        }
        if (userIncludedCheckBox.isSelected()) {
            CollectionUtils.addAll(charsetSet, ArrayUtils.toObject(userIncludedTextField.getText().toCharArray()));
        }
        if (userExcludedCheckBox.isSelected()) {
            for (char ch : userExcludedTextField.getText().toCharArray()) {
                charsetSet.remove(ch);
            }
        }
        return String.valueOf(ArrayUtils.toPrimitive(charsetSet.toArray(new Character[0])));
    }

    private void onStartByDictionarySingleThreadCategory() {
        File dictionaryFile = dictionaryFilePanel.getFile();
        if (!dictionaryFile.isFile()) {
            JOptionPane.showMessageDialog(this, "Invalid dictionary file");
            return;
        }
        String charsetName = EncoderDetector.judgeFile(dictionaryFile.getAbsolutePath());
        logger.info("dictionary file: " + dictionaryFile.getAbsolutePath() + ", charset: " + charsetName);
        if (charsetName == null) {
            JOptionPane.showMessageDialog(this, "Invalid charsetName");
            return;
        }
        setCurrentState(State.WORKING);
        setProgressMaxValue(Utils.getFileLineCount(dictionaryFile));
        setProgressBarValue(0);
        DictionarySingleThreadProxy proxy = DictionarySingleThreadProxy.getInstance();
        String password = proxy.startAndGet(dictionaryFile, currentFileChecker, charsetName, this);
        showResultWithDialog(password);
        onStopByDictionarySingleThreadCategory();
    }

    private void onStartByDictionaryMultiThreadCategory() {
        File dictionaryFile = dictionaryFilePanel.getFile();
        if (!dictionaryFile.isFile()) {
            JOptionPane.showMessageDialog(this, "Invalid dictionary file");
            return;
        }
        int threadNum = (Integer) threadNumSpinner.getValue();
        int fileLineCount = Utils.getFileLineCount(dictionaryFile);
        logger.info("File line count: " + fileLineCount);

        setCurrentState(State.WORKING);
        setProgressMaxValue(fileLineCount);
        setProgressBarValue(0);

        DictionaryMultiThreadProxy proxy = DictionaryMultiThreadProxy.getInstance();
        String password = proxy.startAndGet(threadNum, dictionaryFile, currentFileChecker, this);
        showResultWithDialog(password);
        onStopByDictionaryMultiThreadCategory();
    }

    private void onStopByBruteForceCategory() {
        if (getCurrentState() != State.WORKING) {
            return;
        }
        setCurrentState(State.STOPPING);
        BruteForceProxy.getInstance().cancel();
        setCurrentState(State.IDLE);
    }

    private void onStopByDictionarySingleThreadCategory() {
        if (getCurrentState() != State.WORKING) {
            return;
        }
        setCurrentState(State.STOPPING);
        DictionarySingleThreadProxy.getInstance().cancel();
        setCurrentState(State.IDLE);
    }

    private void onStopByDictionaryMultiThreadCategory() {
        if (getCurrentState() != State.WORKING) {
            return;
        }
        setCurrentState(State.STOPPING);
        DictionaryMultiThreadProxy.getInstance().cancel();
        setCurrentState(State.IDLE);
    }

    private int getThreadCount(int charSetSize, int length, int maxThreadCount) {
        int result = 1;
        for (int i = 1; i <= length; i++) {
            result *= charSetSize;
            if (result >= maxThreadCount) {
                return maxThreadCount;
            }
        }
        return result;
    }

    private void showResultWithDialog(String password) {
        if (password == null) {
            logger.error("Can not find password");
            JOptionPane.showMessageDialog(RecoveryPanel.this, "Can not find password");
        } else {
            logger.info("Find out the password: " + password);
            JOptionPane.showMessageDialog(RecoveryPanel.this, "Find out the password: " + password);
        }
    }

    @Override
    public void setCurrentState(State currentState) {
        this.currentState = currentState;
        if (currentStateLabel != null) {
            currentStateLabel.setText("State: " + currentState.toString());
        }
        if (currentState == State.WORKING) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        } else if (currentState == State.STOPPING) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            startButton.setEnabled(false);
            stopButton.setEnabled(false);
        } else if (currentState == State.IDLE) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            currentPasswordLabel.setText("");
        }
    }

    @Override
    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void setProgressMaxValue(int maxValue) {
        progressBar.setMaximum(maxValue);
    }

    @Override
    public void increaseProgressBarValue() {
        setProgressBarValue(progressBar.getValue() + 1);
    }

    @Override
    public void setProgressBarValue(int value) {
        progressBar.setValue(value);
        String text = numberFormat.format(((double) value) / progressBar.getMaximum());
        progressBar.setString(text);
    }

    @Override
    public void setCurrentPassword(String password) {
        currentPasswordLabel.setText("Trying: " + password);
    }
}

