package edu.jiangxin.apktoolbox.help.settings;

import edu.jiangxin.apktoolbox.swing.extend.EasyChildTabbedPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;

public class LookAndFeelPanel extends EasyChildTabbedPanel {
    @Serial
    private static final long serialVersionUID = 1L;

    private JPanel optionPanel;

    private JComboBox<String> typeComboBox;

    private JPanel operationPanel;

    static {
        // Avoid install duplicated Look And Feel, we install them in static block
        UIManager.installLookAndFeel("Flat Light", "com.formdev.flatlaf.FlatLightLaf");
        UIManager.installLookAndFeel("Flat Dark", "com.formdev.flatlaf.FlatDarkLaf");
        UIManager.installLookAndFeel("Flat IntelliJ", "com.formdev.flatlaf.FlatIntelliJLaf");
        UIManager.installLookAndFeel("Flat Darcula", "com.formdev.flatlaf.FlatDarculaLaf");
    }

    @Override
    public void createUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createOptionPanel();
        add(optionPanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);

        add(Box.createVerticalStrut(15 * Constants.DEFAULT_Y_BORDER));
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));

        JLabel typeLabel = new JLabel("Type:");
        typeComboBox = new JComboBox<>();
        typeComboBox.setMaximumSize(new Dimension(Constants.DEFAULT_COMBOBOX_WIDTH, Constants.DEFAULT_COMBOBOX_HEIGHT));

        UIManager.LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
        if (ArrayUtils.isEmpty(lookAndFeelInfos)) {
            typeComboBox.setEnabled(false);
        } else {
            typeComboBox.setEnabled(true);
            String className = conf.getString("look.and.feel.class.name");
            for (UIManager.LookAndFeelInfo info : lookAndFeelInfos) {
                typeComboBox.addItem(info.getName());
                if (Strings.CS.equals(className, info.getClassName())) {
                    typeComboBox.setSelectedItem(info.getName());
                }
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
        public void actionPerformed(ActionEvent actionEvent) {
            String name = (String) typeComboBox.getSelectedItem();
            String className = getLookAndFeelClassNameFromName(name);
            if (className == null) {
                logger.warn("className is null");
                return;
            }
            conf.setProperty("look.and.feel.class.name", className);
            try {
                UIManager.setLookAndFeel(className);
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                logger.error("setLookAndFeel failed, use default instead", e);
            }
            SwingUtilities.updateComponentTreeUI(getFrame());
            getFrame().refreshSizeAndLocation();
        }
    }

    private String getLookAndFeelClassNameFromName(String name) {
        UIManager.LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo info : lookAndFeelInfos) {
            if (Strings.CS.equals(name, info.getName())) {
                return info.getClassName();
            }
        }
        return null;
    }
}