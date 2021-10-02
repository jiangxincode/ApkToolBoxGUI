package edu.jiangxin.apktoolbox.help;

import edu.jiangxin.apktoolbox.main.MainFrame;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LookAndFeelPanel extends EasyPanel {

    private JPanel optionPanel;

    private JLabel typeLabel;

    private JComboBox<UIManager.LookAndFeelInfo> typeComboBox;

    private JPanel operationPanel;

    private JButton ApplyButton;


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
        String currentLookAndFeelClassName = conf.getString("look.and.feel.class.name");
        if (ArrayUtils.isEmpty(lookAndFeelInfos)) {
            typeComboBox.setEnabled(false);
        } else {
            typeComboBox.setEnabled(true);
            for (UIManager.LookAndFeelInfo info : lookAndFeelInfos) {
                typeComboBox.addItem(info);
                if (StringUtils.equals(info.getClassName(), currentLookAndFeelClassName)) {
                    typeComboBox.setSelectedItem(info);
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
            UIManager.LookAndFeelInfo lookAndFeelInfo = (UIManager.LookAndFeelInfo)typeComboBox.getSelectedItem();
            String lookAndFeelClassName = lookAndFeelInfo.getClassName();
            conf.setProperty("look.and.feel.class.name", lookAndFeelClassName);
            try {
                UIManager.setLookAndFeel(lookAndFeelClassName);
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                logger.error("setLookAndFeel failed, use default instead", e);
            }
            SwingUtilities.updateComponentTreeUI(getFrame());
        }
    }
}
