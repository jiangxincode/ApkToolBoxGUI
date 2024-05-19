package edu.jiangxin.apktoolbox.swing.extend;

public class EasyChildTabbedPanel extends EasyPanel {
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
