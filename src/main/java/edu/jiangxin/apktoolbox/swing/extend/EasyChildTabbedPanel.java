package edu.jiangxin.apktoolbox.swing.extend;

import java.io.Serial;

public class EasyChildTabbedPanel extends EasyPanel {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean isUICreated = false;

    public void onTabSelected() {
        if (!isUICreated) {
            createUI();
            isUICreated = true;
        }
    }

    protected void createUI() {
        // do nothing
    }
}
