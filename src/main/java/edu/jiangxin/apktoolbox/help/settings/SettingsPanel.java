package edu.jiangxin.apktoolbox.help.settings;

import edu.jiangxin.apktoolbox.swing.extend.EasyChildTabbedPanel;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;

import javax.swing.*;

public class SettingsPanel extends EasyPanel {
    private static final long serialVersionUID = 63924900336217723L;

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        JTabbedPane tabbedPane = new JTabbedPane();

        EasyChildTabbedPanel lookAndFeelPanel = new LookAndFeelPanel();
        String lookAndFeelTitle = bundle.getString("help.settings.look.and.feel.title");
        tabbedPane.addTab(lookAndFeelTitle, null, lookAndFeelPanel, lookAndFeelTitle);

        EasyChildTabbedPanel localePanel = new LocalePanel();
        String localeTitle = bundle.getString("help.settings.locale.title");
        tabbedPane.addTab(localeTitle, null, localePanel, localeTitle);

        EasyChildTabbedPanel dependencyPathPanel = new DependencyPathPanel();
        String dependencyPathTitle = bundle.getString("help.settings.dependency.path");
        tabbedPane.addTab(dependencyPathTitle, null, dependencyPathPanel, dependencyPathTitle);

        tabbedPane.addChangeListener(e -> {
            EasyChildTabbedPanel selectedPanel = (EasyChildTabbedPanel) tabbedPane.getSelectedComponent();
            selectedPanel.onTabSelected();
        });

        tabbedPane.setSelectedComponent(dependencyPathPanel);
        dependencyPathPanel.onTabSelected();

        add(tabbedPane);
        add(Box.createVerticalGlue());
    }

}

