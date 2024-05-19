package edu.jiangxin.apktoolbox.file.checksum;

import edu.jiangxin.apktoolbox.file.checksum.panel.*;
import edu.jiangxin.apktoolbox.swing.extend.EasyChildTabbedPanel;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;

import javax.swing.*;

public class ChecksumPanel extends EasyPanel {
    private static final long serialVersionUID = 63924900336217723L;

    @Override
    public void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        JTabbedPane tabbedPane = new JTabbedPane();

        EasyChildTabbedPanel fileChecksumPanel = new FileChecksumPanel();
        tabbedPane.addTab("File Checksum", null, fileChecksumPanel, "File Checksum");

        EasyChildTabbedPanel verifyChecksumPanel = new VerifyChecksumPanel();
        tabbedPane.addTab("Verify Checksum", null, verifyChecksumPanel, "Verify Checksum");

        EasyChildTabbedPanel compareFilesPanel = new CompareFilesPanel();
        tabbedPane.addTab("Compare Files", null, compareFilesPanel, "Compare Files");

        EasyChildTabbedPanel scanFolderPanel = new ScanFolderPanel();
        tabbedPane.addTab("Scan Folder", null, scanFolderPanel, "Scan Folder");

        EasyChildTabbedPanel stringHashPanel = new StringHashPanel();
        tabbedPane.addTab("String Hash", null, stringHashPanel, "String Hash");

        tabbedPane.addChangeListener(e -> {
            EasyChildTabbedPanel selectedPanel = (EasyChildTabbedPanel) tabbedPane.getSelectedComponent();
            selectedPanel.onTabSelected();
        });

        tabbedPane.setSelectedComponent(stringHashPanel);
        stringHashPanel.onTabSelected();

        add(tabbedPane);
        add(Box.createVerticalGlue());
    }

}
