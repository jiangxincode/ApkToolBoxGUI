package edu.jiangxin.apktoolbox.help.settings;

import edu.jiangxin.apktoolbox.swing.extend.EasyChildTabbedPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;

public class AddToStartupPanel extends EasyChildTabbedPanel {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String STARTUP_FILE;

    private JPanel optionPanel;

    static {
        final String SEPARATOR = File.separator;
        final String STARTUP_FOLDER;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            STARTUP_FOLDER = System.getenv("APPDATA") + SEPARATOR + "Microsoft" + SEPARATOR + "Windows" + SEPARATOR + "Start Menu" + SEPARATOR + "Programs" + SEPARATOR + "Startup";
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            STARTUP_FOLDER = System.getProperty("user.home") + SEPARATOR + "Library" + SEPARATOR + "LaunchAgents";
        } else {
            STARTUP_FOLDER = System.getProperty("user.home") + SEPARATOR + ".config" + SEPARATOR + "autostart";
        }
        STARTUP_FILE = STARTUP_FOLDER + SEPARATOR + "ApkToolBoxGUI.lnk";
    }

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

        JLabel typeLabel = new JLabel("Add to startup:");
        JCheckBox addToStartupCheckBox = new JCheckBox();
        addToStartupCheckBox.setSelected(conf.getBoolean("add.to.startup", false));
        addToStartupCheckBox.addActionListener(e -> {
            conf.setProperty("add.to.startup", addToStartupCheckBox.isSelected());
            if (addToStartupCheckBox.isSelected()) {
                addToStartup();
            } else {
                FileUtils.deleteQuietly(new File(STARTUP_FILE));
            }
        });

        optionPanel.add(typeLabel);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(addToStartupCheckBox);
    }

    private void addToStartup() {
        String executableFilePath = getExecutableFilePath();
        if (executableFilePath == null) {
            return;
        }

        File shortcutFile = new File(STARTUP_FILE);

        if (!shortcutFile.exists()) {
            try {
                createShortcut(executableFilePath);
                logger.info("create shortcut file successfully: {}", STARTUP_FILE);
            } catch (IOException e) {
                logger.error("create shortcut file failed: {}", STARTUP_FILE, e);
            }
        }
    }

    private String getExecutableFilePath() {
        URL jarUrl = AddToStartupPanel.class.getProtectionDomain().getCodeSource().getLocation();
        if (jarUrl == null) {
            logger.error("jarUrl is null");
            return null;
        }
        File jarFile = new File(jarUrl.getPath());
        logger.info("jarFile: {}", jarFile.getAbsolutePath());
        File grandpaFile = jarFile.getParentFile().getParentFile();
        logger.info("parentFile: {}", grandpaFile.getAbsolutePath());
        Collection<File> executableFiles = FileUtils.listFiles(grandpaFile, new String[]{"exe"}, false);
        if (CollectionUtils.isEmpty(executableFiles)) {
            logger.error("Can not find executable file");
            return null;
        }
        if (executableFiles.size() > 1) {
            logger.error("There are more than one executable file");
            return null;
        }
        File executableFile = executableFiles.iterator().next();
        logger.info("executableFile: {}", executableFile.getAbsolutePath());
        return executableFile.getAbsolutePath();
    }

    private static void createShortcut(String targetPath) throws IOException {
        String vbsScript = """
                Set WshShell = CreateObject("WScript.Shell")
                Set Shortcut = WshShell.CreateShortcut("%s")
                Shortcut.TargetPath = "%s"
                Shortcut.Save
                """.formatted(STARTUP_FILE, targetPath);

        File tempVbs = Files.createTempFile("createShortcut", ".vbs").toFile();
        Files.writeString(tempVbs.toPath(), vbsScript);

        ProcessBuilder builder = new ProcessBuilder("wscript", tempVbs.getAbsolutePath());
        builder.start();
        tempVbs.deleteOnExit();
    }
}
