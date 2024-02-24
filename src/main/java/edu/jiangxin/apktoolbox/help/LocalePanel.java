package edu.jiangxin.apktoolbox.help;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class LocalePanel extends EasyPanel {
    private JPanel optionPanel;

    private JComboBox<String> typeComboBox;

    private JPanel operationPanel;

    private static final String[] SUPPORTED_LANGUAGES = {Locale.CHINESE.getLanguage(), Locale.ENGLISH.getLanguage()};

    public LocalePanel() throws HeadlessException {
        super();
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
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));

        JLabel typeLabel = new JLabel("Locale:");
        typeComboBox = new JComboBox<>();

        String currentLocaleLanguage = conf.getString("locale.language");
        if (StringUtils.isEmpty(currentLocaleLanguage)) {
            currentLocaleLanguage = Locale.ENGLISH.getLanguage();
            conf.setProperty("locale.language", currentLocaleLanguage);
        }

        for (String language : SUPPORTED_LANGUAGES) {
            typeComboBox.addItem(language);
            if (StringUtils.equals(currentLocaleLanguage, language)) {
                typeComboBox.setSelectedItem(language);
            }
        }

        optionPanel.add(typeLabel);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(typeComboBox);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ApplyButtonActionListener());

        operationPanel.add(applyButton);
    }

    private final class ApplyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String language = (String)typeComboBox.getSelectedItem();
            if (StringUtils.isNotEmpty(language)) {
                conf.setProperty("locale.language", language);
                JOptionPane.showMessageDialog(LocalePanel.this, "Setting locale successfully, restart the program please");
            }
        }
    }
}
