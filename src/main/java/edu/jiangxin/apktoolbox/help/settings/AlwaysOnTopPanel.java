package edu.jiangxin.apktoolbox.help.settings;

import edu.jiangxin.apktoolbox.swing.extend.EasyChildTabbedPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;

public class AlwaysOnTopPanel extends EasyChildTabbedPanel {
    private JPanel optionPanel;

    @Override
    public void createUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createOptionPanel();
        add(optionPanel);

        add(Box.createVerticalStrut(15 * Constants.DEFAULT_Y_BORDER));
    }

    private void createOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));

        JLabel typeLabel = new JLabel("Always on top:");
        JCheckBox alwaysOnTopCheckBox = new JCheckBox();
        alwaysOnTopCheckBox.setSelected(conf.getBoolean("always.on.top", false));
        alwaysOnTopCheckBox.addActionListener(e -> {
            conf.setProperty("always.on.top", alwaysOnTopCheckBox.isSelected());
            getFrame().setAlwaysOnTop(alwaysOnTopCheckBox.isSelected());
            SwingUtilities.updateComponentTreeUI(getFrame());
            getFrame().refreshSizeAndLocation();
        });

        optionPanel.add(typeLabel);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(alwaysOnTopCheckBox);
    }
}
