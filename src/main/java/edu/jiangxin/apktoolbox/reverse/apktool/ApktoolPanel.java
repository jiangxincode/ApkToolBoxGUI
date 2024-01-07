package edu.jiangxin.apktoolbox.reverse.apktool;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ApktoolPanel extends EasyPanel {
    private static final long serialVersionUID = 1L;

    private JTabbedPane categoryTabbedPane;

    private JPanel decodeCategoryPanel;

    private JPanel rebuildCategoryPanel;

    private JButton decodeButton;

    public ApktoolPanel() throws HeadlessException {
        super();
        initUI();
    }

    private void initUI() {
        String toolPath = conf.getString(Constants.APKTOOL_PATH_KEY);
        File toolFile = null;
        if (!StringUtils.isEmpty(toolPath)) {
            toolFile = new File(toolPath);
        }
        if (StringUtils.isEmpty(toolPath) || toolFile == null || !toolFile.exists() || !toolFile.isFile()) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Need Configuration", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        categoryTabbedPane = new JTabbedPane();
        add(categoryTabbedPane);

        decodeCategoryPanel = new ApktoolDecodePanel();
        rebuildCategoryPanel = new ApktoolRebuildPanel();

        categoryTabbedPane.addTab("Decode", null, decodeCategoryPanel, "Decode the APK file");
        categoryTabbedPane.setSelectedIndex(0);
        categoryTabbedPane.addTab("Rebuild", null, rebuildCategoryPanel, "Rebuild the APK file");
    }


}

