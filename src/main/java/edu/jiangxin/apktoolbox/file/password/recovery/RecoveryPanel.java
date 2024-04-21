package edu.jiangxin.apktoolbox.file.password.recovery;

import edu.jiangxin.apktoolbox.file.password.recovery.category.CategoryFactory;
import edu.jiangxin.apktoolbox.file.password.recovery.category.CategoryType;
import edu.jiangxin.apktoolbox.file.password.recovery.category.ICategory;
import edu.jiangxin.apktoolbox.file.password.recovery.checker.*;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.filepanel.FilePanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class RecoveryPanel extends EasyPanel {
    private JPanel optionPanel;

    private FilePanel recoveryFilePanel;

    private JTabbedPane categoryTabbedPane;

    private JPanel bruteForceCategoryPanel;

    private JPanel dictionaryCategoryPanel;

    private FilePanel dictionaryFilePanel;

    private CategoryType currentCategoryType = CategoryType.UNKNOWN;

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

    private JLabel currentSpeedLabel;

    private int passwordTryCount = 0;

    private NumberFormat numberFormat;

    private State currentState = State.IDLE;

    private Timer timer;

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

        timer = new Timer(1000, e -> {
            if (currentState == State.WORKING) {
                int speed = progressBar.getValue() - passwordTryCount;
                passwordTryCount = progressBar.getValue();
                currentSpeedLabel.setText("Speed: " + speed + " passwords/s");
            } else {
                currentSpeedLabel.setText("");
            }
        });
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
        currentStateLabel = new JLabel("");
        currentPasswordLabel = new JLabel("");
        currentSpeedLabel = new JLabel("");

        operationPanel.add(startButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(stopButton);
        operationPanel.add(Box.createHorizontalStrut(2 * Constants.DEFAULT_X_BORDER));
        operationPanel.add(currentStateLabel);
        operationPanel.add(Box.createHorizontalStrut(2 * Constants.DEFAULT_X_BORDER));
        operationPanel.add(currentPasswordLabel);
        operationPanel.add(Box.createHorizontalStrut(2 * Constants.DEFAULT_X_BORDER));
        operationPanel.add(currentSpeedLabel);
        operationPanel.add(Box.createHorizontalGlue());

        startButton.addActionListener(e -> new Thread(this::onStart).start());
        stopButton.addActionListener(e -> new Thread(this::onStop).start());

        setCurrentState(State.IDLE);
    }

    private void onStart() {
        FileChecker fileChecker = (FileChecker) checkerTypeComboBox.getSelectedItem();
        if (fileChecker == null) {
            JOptionPane.showMessageDialog(this, "fileChecker is null!");
            return;
        }
        if (!fileChecker.prepareChecker()) {
            JOptionPane.showMessageDialog(this, "onStart failed: Checker condition does not been prepared!");
            return;
        }

        File selectedFile = recoveryFilePanel.getFile();
        if (!selectedFile.isFile()) {
            JOptionPane.showMessageDialog(this, "file is not a file!");
            return;
        }

        String extension = FilenameUtils.getExtension(recoveryFilePanel.getFile().getName());
        if (!ArrayUtils.contains(fileChecker.getFileExtensions(), StringUtils.toRootLowerCase(extension))) {
            JOptionPane.showMessageDialog(this, "invalid file!");
            return;
        }

        fileChecker.attachFile(selectedFile);
        currentFileChecker = fileChecker;

        Component selectedPanel = categoryTabbedPane.getSelectedComponent();
        if (selectedPanel.equals(bruteForceCategoryPanel)) {
            currentCategoryType = CategoryType.BRUTE_FORCE;
        } else if (selectedPanel.equals(dictionaryCategoryPanel)) {
            int threadNum = (Integer) threadNumSpinner.getValue();
            if (threadNum == 1) {
                currentCategoryType = CategoryType.DICTIONARY_SINGLE_THREAD;
            } else {
                currentCategoryType = CategoryType.DICTIONARY_MULTI_THREAD;
            }
        } else {
            currentCategoryType = CategoryType.UNKNOWN;
        }
        logger.info("onStart: {}", currentCategoryType);
        if (currentCategoryType == CategoryType.UNKNOWN) {
            JOptionPane.showMessageDialog(this, "onStart failed: Invalid category!");
            return;
        }
        setCurrentState(State.WORKING);
        timer.start();
        ICategory category = CategoryFactory.getCategoryInstance(currentCategoryType);
        category.start(this);
        if (currentState == State.WORKING) {
            onStop();
        }
    }

    private void onStop() {
        logger.info("onStop: currentState: {}, currentCategoryType: {}", currentState, currentCategoryType);
        if (currentState != State.WORKING) {
            logger.error("onStop failed: Not in working state!");
            return;
        }
        if (currentCategoryType == CategoryType.UNKNOWN) {
            logger.error("onStop failed: Invalid category!");
            return;
        }
        timer.stop();
        setCurrentState(State.STOPPING);
        ICategory category = CategoryFactory.getCategoryInstance(currentCategoryType);
        category.cancel();
        setCurrentState(State.IDLE);
        currentCategoryType = CategoryType.UNKNOWN;
    }

    public void showResultWithDialog(String password) {
        if (password == null) {
            logger.error("Can not find password");
            JOptionPane.showMessageDialog(RecoveryPanel.this, "Can not find password");
        } else {
            logger.info("Find out the password: " + password);
            JOptionPane.showMessageDialog(RecoveryPanel.this, "Find out the password: " + password);
        }
    }

    public void setCurrentState(State currentState) {
        SwingUtilities.invokeLater(() -> {
            this.currentState = currentState;
            if (currentStateLabel != null) {
                currentStateLabel.setText("State: " + currentState.toString());
            }
            if (currentState == State.WORKING) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                updateUiComponent(false);
            } else if (currentState == State.STOPPING) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                startButton.setEnabled(false);
                stopButton.setEnabled(false);
                updateUiComponent(false);
            } else if (currentState == State.IDLE) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                currentPasswordLabel.setText("");
                updateUiComponent(true);
            }
        });
    }

    private void updateUiComponent(boolean enable) {
        for (Component component : getComponents(optionPanel)) {
            component.setEnabled(enable);
        }
    }

    private Component[] getComponents(Component container) {
        List<Component> list;

        try {
            list = new ArrayList<>(Arrays.asList(
                    ((Container) container).getComponents()));
            for (int index = 0; index < list.size(); index++) {
                list.addAll(Arrays.asList(getComponents(list.get(index))));
            }
        } catch (ClassCastException e) {
            list = new ArrayList<>();
        }

        return list.toArray(new Component[0]);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void resetProgressMaxValue(int maxValue) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setMaximum(maxValue);
            setProgressBarValue(0);
        });
    }

    public void increaseProgressBarValue() {
        SwingUtilities.invokeLater(() -> setProgressBarValue(progressBar.getValue() + 1));
    }

    private void setProgressBarValue(int value) {
        progressBar.setValue(value);
        String text = numberFormat.format(((double) value) / progressBar.getMaximum());
        progressBar.setString(text);
    }

    public void setCurrentPassword(String password) {
        SwingUtilities.invokeLater(() -> currentPasswordLabel.setText("Trying: " + password));
    }

    public String getCharset() {
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

    public File getDictionaryFile() {
        return dictionaryFilePanel.getFile();
    }

    public int getMinLength() {
        return (Integer) minSpinner.getValue();
    }

    public int getMaxLength() {
        return (Integer) maxSpinner.getValue();
    }

    public int getThreadNum() {
        return (Integer) threadNumSpinner.getValue();
    }

    public FileChecker getCurrentFileChecker() {
        return currentFileChecker;
    }
}

