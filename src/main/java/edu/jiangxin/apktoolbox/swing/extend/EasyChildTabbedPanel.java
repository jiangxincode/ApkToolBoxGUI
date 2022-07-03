package edu.jiangxin.apktoolbox.swing.extend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class EasyChildTabbedPanel extends JScrollPane {
    private boolean isUICreated = false;
    protected Logger logger;

    public EasyChildTabbedPanel() {
        this(false);
    }

    public EasyChildTabbedPanel(boolean selected) {
        super();
        logger = LogManager.getLogger(this.getClass().getSimpleName());
        if (selected) {
            onTabSelected();
        }
    }

    public void onTabSelected() {
        if (!isUICreated) {
            createUI();
            isUICreated = true;
        }
    }

    public void createUI() {
    }
}
