package edu.jiangxin.apktoolbox.help;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

public class LocalePanel extends EasyPanel {
    private JPanel optionPanel;

    private JLabel typeLabel;

    private JComboBox<String> typeComboBox;

    private JPanel operationPanel;

    private JButton ApplyButton;

    private static String[] supportedLanguages = {Locale.CHINESE.getLanguage(), Locale.ENGLISH.getLanguage()};

    public LocalePanel() throws HeadlessException {
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
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));

        typeLabel = new JLabel("Locale:");
        typeComboBox = new JComboBox<>();

        String currentLocaleLanguage = conf.getString("locale.language");
        if (StringUtils.isEmpty(currentLocaleLanguage)) {
            currentLocaleLanguage = Locale.ENGLISH.getLanguage();
            conf.setProperty("locale.language", currentLocaleLanguage);
        }

        for (String language : supportedLanguages) {
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

        ApplyButton = new JButton("Apply");
        ApplyButton.addMouseListener(new ApplyButtonMouseAdapter());

        operationPanel.add(ApplyButton);
    }

    private final class ApplyButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            super.mousePressed(mouseEvent);
            String language = (String)typeComboBox.getSelectedItem();
            if (StringUtils.isNotEmpty(language)) {
                conf.setProperty("locale.language", language);
                JOptionPane.showMessageDialog(LocalePanel.this, "Setting locale successfully, restart the program please");
            }
        }
    }
}
