package edu.jiangxin.apktoolbox.file.checksum;

import edu.jiangxin.apktoolbox.file.checksum.panel.*;
import edu.jiangxin.apktoolbox.swing.extend.EasyChildTabbedPanel;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;

import javax.swing.*;

public class ChecksumPanel extends EasyPanel {
    private static final long serialVersionUID = 63924900336217723L;

    public ChecksumPanel() {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("File Checksum", null, new FileChecksumPanel(true), "File Checksum");
        tabbedPane.addTab("Verify Checksum", null, new VerifyChecksumPanel(), "Verify Checksum");
        tabbedPane.addTab("Compare Files", null, new CompareFilesPanel(), "Compare Files");
        tabbedPane.addTab("Scan Folder", null, new ScanFolderPanel(), "Scan Folder");
        tabbedPane.addTab("String Hash", null, new StringHashPanel(), "String Hash");

        tabbedPane.addChangeListener(e -> {
            EasyChildTabbedPanel selectedPanel = (EasyChildTabbedPanel) tabbedPane.getSelectedComponent();
            selectedPanel.onTabSelected();
        });

        add(tabbedPane);
    }

}
