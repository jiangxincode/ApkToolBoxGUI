package edu.jiangxin.apktoolbox.help;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class LookAndFeelPanel extends EasyPanel {

    private JPanel optionPanel;

    private JLabel typeLabel;

    private JComboBox<String> typeComboBox;

    private JPanel operationPanel;

    private JButton ApplyButton;

    static {
        // Avoid install duplicated Look And Feel, we install them in static block
        UIManager.installLookAndFeel("Flat Light", "com.formdev.flatlaf.FlatLightLaf");
        UIManager.installLookAndFeel("Flat Dark", "com.formdev.flatlaf.FlatDarkLaf");
        UIManager.installLookAndFeel("Flat IntelliJ", "com.formdev.flatlaf.FlatIntelliJLaf");
        UIManager.installLookAndFeel("Flat Darcula", "com.formdev.flatlaf.FlatDarculaLaf");
    }

    public LookAndFeelPanel() throws HeadlessException {
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

        typeLabel = new JLabel("Type:");
        typeComboBox = new JComboBox<>();

        UIManager.LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
        if (ArrayUtils.isEmpty(lookAndFeelInfos)) {
            typeComboBox.setEnabled(false);
        } else {
            typeComboBox.setEnabled(true);
            String className = conf.getString("look.and.feel.class.name");
            for (UIManager.LookAndFeelInfo info : lookAndFeelInfos) {
                typeComboBox.addItem(info.getName());
                if (StringUtils.equals(className, info.getClassName())) {
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

        ApplyButton = new JButton("Apply");
        ApplyButton.addMouseListener(new ApplyButtonMouseAdapter());

        operationPanel.add(ApplyButton);
    }

    private final class ApplyButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            super.mousePressed(mouseEvent);
            String name = (String)typeComboBox.getSelectedItem();
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
            if (StringUtils.equals(name, info.getName())) {
                return info.getClassName();
            }
        }
        return null;
    }
}
