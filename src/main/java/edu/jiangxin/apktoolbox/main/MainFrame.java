package edu.jiangxin.apktoolbox.main;

import edu.jiangxin.apktoolbox.Version;
import edu.jiangxin.apktoolbox.convert.base.BaseConvertPanel;
import edu.jiangxin.apktoolbox.convert.color.ColorConvertPanel;
import edu.jiangxin.apktoolbox.convert.color.ColorPickerPanel;
import edu.jiangxin.apktoolbox.convert.protobuf.unsupervised.ProtobufConvertPanel;
import edu.jiangxin.apktoolbox.convert.relationship.RelationShipConvertPanel;
import edu.jiangxin.apktoolbox.convert.time.TimeConvertPanel;
import edu.jiangxin.apktoolbox.convert.zh2unicode.Zh2UnicodeConvertPanel;
import edu.jiangxin.apktoolbox.android.dumpsys.alarm.DumpsysAlarmPanel;
import edu.jiangxin.apktoolbox.file.batchrename.BatchRenamePanel;
import edu.jiangxin.apktoolbox.file.EncodeConvertPanel;
import edu.jiangxin.apktoolbox.file.OsConvertPanel;
import edu.jiangxin.apktoolbox.file.checksum.ChecksumPanel;
import edu.jiangxin.apktoolbox.file.password.recovery.RecoveryPanel;
import edu.jiangxin.apktoolbox.file.duplicate.DuplicateSearchPanel;
import edu.jiangxin.apktoolbox.file.zhconvert.ZhConvertPanel;
import edu.jiangxin.apktoolbox.help.*;
import edu.jiangxin.apktoolbox.android.i18n.I18nAddPanel;
import edu.jiangxin.apktoolbox.android.i18n.I18nFindLongestPanel;
import edu.jiangxin.apktoolbox.android.i18n.I18nRemovePanel;
import edu.jiangxin.apktoolbox.android.monkey.MonkeyPanel;
import edu.jiangxin.apktoolbox.help.settings.SettingsPanel;
import edu.jiangxin.apktoolbox.pdf.PdfFinderPanel;
import edu.jiangxin.apktoolbox.reverse.*;
import edu.jiangxin.apktoolbox.android.screenshot.ScreenShotPanel;
import edu.jiangxin.apktoolbox.reverse.ApktoolPanel;
import edu.jiangxin.apktoolbox.swing.extend.EasyFrame;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.listener.ChangeMenuListener;
import edu.jiangxin.apktoolbox.swing.extend.listener.ChangeMenuToUrlListener;
import edu.jiangxin.apktoolbox.swing.extend.listener.IPreChangeMenuCallBack;
import edu.jiangxin.apktoolbox.swing.extend.plugin.ChangeMenuPreparePluginController;
import edu.jiangxin.apktoolbox.swing.extend.plugin.PluginPanel;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * @author jiangxin
 * @author 2018-08-19
 */
public class MainFrame extends EasyFrame {

    @Serial
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JMenuBar menuBar;

    public static void main(String[] args) {
        final Logger logger = LogManager.getLogger(MainFrame.class.getSimpleName());
        boolean isEnvironmentOk = Utils.checkAndInitEnvironment();
        if (!isEnvironmentOk) {
            logger.error("Environment is not ready, exit");
            return;
        }
        EventQueue.invokeLater(() -> {
            Configuration conf = Utils.getConfiguration();
            String lookAndFeelClassName = conf.getString("look.and.feel.class.name");
            if (StringUtils.isEmpty(lookAndFeelClassName)) {
                lookAndFeelClassName = "com.formdev.flatlaf.FlatDarculaLaf";
                conf.setProperty("look.and.feel.class.name", lookAndFeelClassName);
            }
            try {
                UIManager.setLookAndFeel(lookAndFeelClassName);
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                logger.error("setLookAndFeel failed, use default instead", e);
            }

            String currentLocaleLanguage = conf.getString("locale.language");
            if (StringUtils.isEmpty(currentLocaleLanguage)) {
                currentLocaleLanguage = Locale.ENGLISH.getLanguage();
                conf.setProperty("locale.language", currentLocaleLanguage);
            }
            Locale.setDefault(new Locale(currentLocaleLanguage));

            MainFrame frame = new MainFrame();

            boolean isAlwaysOnTop = conf.getBoolean("always.on.top", false);
            conf.setProperty("always.on.top", isAlwaysOnTop);
            frame.setAlwaysOnTop(isAlwaysOnTop);

            frame.setVisible(true);
        });
    }

    public MainFrame() {
        super();
        if (SystemTray.isSupported()) {
            configSystemTray();
        }

        setTitle(MessageFormat.format(bundle.getString("main.title"), Version.VERSION));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMenuBar();
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(Box.createVerticalGlue());
        EasyPanel initPanel = new AboutPanel();
        initPanel.init();
        initPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("help.about.title")));
        contentPane.add(initPanel);
        contentPane.add(Box.createVerticalGlue());
        setContentPane(contentPane);
        refreshSizeAndLocation();
    }

    private void configSystemTray() {
        SystemTray systemTray = SystemTray.getSystemTray();
        PopupMenu popupMenu = new PopupMenu();
        final MenuItem show = new MenuItem("Open");
        final MenuItem exit = new MenuItem("Close");
        ActionListener actionListener = e -> {
            if (e.getSource() == show) {
                setExtendedState(JFrame.NORMAL);
                setVisible(true);
            }
            if (e.getSource() == exit) {
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        };
        exit.addActionListener(actionListener);
        show.addActionListener(actionListener);
        popupMenu.add(show);
        popupMenu.add(exit);
        TrayIcon trayIcon = new TrayIcon(image, "系统托盘", popupMenu);
        trayIcon.setImageAutoSize(true);
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            logger.error("add icon to tray failed");
        }
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    setExtendedState(JFrame.NORMAL);
                    setVisible(true);
                }
            }
        });
    }

    private void setMenuBar() {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        createReverseMenu();

        createAndroidMenu();

        createPdfMenu();

        createFileMenu();

        createConvertMenu();

        createHelpMenu();
    }

    private void createHelpMenu() {
        JMenu helpMenu = new JMenu(bundle.getString("help.title"));
        menuBar.add(helpMenu);

        JMenuItem documentMenuItem = new JMenuItem(bundle.getString("help.document.title"));
        documentMenuItem.addActionListener(new ChangeMenuToUrlListener(Constant.URL_DOCUMENT));
        helpMenu.add(documentMenuItem);

        JMenuItem settingsMenuItem = new JMenuItem(bundle.getString("help.settings.title"));
        settingsMenuItem.addActionListener(new ChangeMenuToPanelListener(SettingsPanel.class, settingsMenuItem.getText()));
        helpMenu.add(settingsMenuItem);

        JMenuItem feedbackMenuItem = new JMenuItem(bundle.getString("help.feedback.title"));
        feedbackMenuItem.addActionListener(new ChangeMenuToUrlListener(Constant.URL_FEEDBACK));
        helpMenu.add(feedbackMenuItem);

        JMenuItem checkUpdateMenuItem = new JMenuItem(bundle.getString("help.checkupdate.title"));
        checkUpdateMenuItem.addActionListener(new CheckUpdateActionListener(this));
        helpMenu.add(checkUpdateMenuItem);

        JMenuItem contributeMenuItem = new JMenuItem(bundle.getString("help.contribute.title"));
        contributeMenuItem.addActionListener(new ChangeMenuToUrlListener(Constant.URL_CONTRIBUTE));
        helpMenu.add(contributeMenuItem);

        JMenuItem aboutMenuItem = new JMenuItem(bundle.getString("help.about.title"));
        aboutMenuItem.addActionListener(new ChangeMenuToPanelListener(AboutPanel.class, aboutMenuItem.getText()));
        helpMenu.add(aboutMenuItem);
    }

    private void createPdfMenu() {
        JMenu pdfMenu = new JMenu(bundle.getString("pdf.title"));
        menuBar.add(pdfMenu);

        JMenuItem pdfFinderMenuItem = new JMenuItem(bundle.getString("pdf.finder.title"));
        pdfFinderMenuItem.addActionListener(new ChangeMenuToPanelListener(PdfFinderPanel.class, pdfFinderMenuItem.getText()));
        pdfMenu.add(pdfFinderMenuItem);
    }

    private void createFileMenu() {
        JMenu fileMenu = new JMenu(bundle.getString("file.title"));
        menuBar.add(fileMenu);

        JMenuItem osConvertMenuItem = new JMenuItem(bundle.getString("file.os.convert.title"));
        osConvertMenuItem.addActionListener(new ChangeMenuToPanelListener(OsConvertPanel.class, osConvertMenuItem.getText()));
        fileMenu.add(osConvertMenuItem);

        JMenuItem encodeConvertMenuItem = new JMenuItem(bundle.getString("file.encode.convert.title"));
        encodeConvertMenuItem.addActionListener(new ChangeMenuToPanelListener(EncodeConvertPanel.class, encodeConvertMenuItem.getText()));
        fileMenu.add(encodeConvertMenuItem);

        JMenuItem zhConvertMenuItem = new JMenuItem(bundle.getString("file.zh.convert.title"));
        zhConvertMenuItem.addActionListener(new ChangeMenuToPanelListener(ZhConvertPanel.class, zhConvertMenuItem.getText()));
        fileMenu.add(zhConvertMenuItem);

        JMenuItem duplicateFindMenuItem = new JMenuItem(bundle.getString("file.duplicate.title"));
        duplicateFindMenuItem.addActionListener(new ChangeMenuToPanelListener(DuplicateSearchPanel.class, duplicateFindMenuItem.getText()));
        fileMenu.add(duplicateFindMenuItem);

        JMenuItem batchRenameMenuItem = new JMenuItem(bundle.getString("file.batch.rename.title"));
        batchRenameMenuItem.addActionListener(new ChangeMenuToPanelListener(BatchRenamePanel.class, batchRenameMenuItem.getText()));
        fileMenu.add(batchRenameMenuItem);

        JMenuItem checkSumMenuItem = new JMenuItem(bundle.getString("file.check.summary.title"));
        checkSumMenuItem.addActionListener(new ChangeMenuToPanelListener(ChecksumPanel.class, checkSumMenuItem.getText()));
        fileMenu.add(checkSumMenuItem);

        JMenuItem recoveryMenuItem = new JMenuItem(bundle.getString("file.password.recovery.title"));
        recoveryMenuItem.addActionListener(new ChangeMenuToPanelListener(RecoveryPanel.class, recoveryMenuItem.getText()));
        fileMenu.add(recoveryMenuItem);
    }

    private void createConvertMenu() {
        JMenu convertMenu = new JMenu(bundle.getString("convert.title"));
        menuBar.add(convertMenu);

        JMenuItem timeConvertMenuItem = new JMenuItem(bundle.getString("convert.time.title"));
        timeConvertMenuItem.addActionListener(new ChangeMenuToPanelListener(TimeConvertPanel.class, timeConvertMenuItem.getText()));
        convertMenu.add(timeConvertMenuItem);

        JMenuItem colorConvertMenuItem = new JMenuItem(bundle.getString("convert.color.title"));
        colorConvertMenuItem.addActionListener(new ChangeMenuToPanelListener(ColorConvertPanel.class, colorConvertMenuItem.getText()));
        convertMenu.add(colorConvertMenuItem);

        JMenuItem colorPickerMenuItem = new JMenuItem(bundle.getString("picker.color.title"));
        colorPickerMenuItem.addActionListener(new ChangeMenuToPanelListener(ColorPickerPanel.class, colorPickerMenuItem.getText()));
        convertMenu.add(colorPickerMenuItem);

        JMenuItem baseConvertMenuItem = new JMenuItem(bundle.getString("convert.base.title"));
        baseConvertMenuItem.addActionListener(new ChangeMenuToPanelListener(BaseConvertPanel.class, baseConvertMenuItem.getText()));
        convertMenu.add(baseConvertMenuItem);

        JMenuItem unicodeConvertMenuItem = new JMenuItem(bundle.getString("convert.unicode.title"));
        unicodeConvertMenuItem.addActionListener(new ChangeMenuToPanelListener(Zh2UnicodeConvertPanel.class, unicodeConvertMenuItem.getText()));
        convertMenu.add(unicodeConvertMenuItem);

        JMenuItem relationShipConvertMenuItem = new JMenuItem(bundle.getString("convert.relationship.title"));
        relationShipConvertMenuItem.addActionListener(new ChangeMenuToPanelListener(RelationShipConvertPanel.class, relationShipConvertMenuItem.getText()));
        convertMenu.add(relationShipConvertMenuItem);

        JMenuItem protobufConvertMenuItem = new JMenuItem(bundle.getString("convert.protobuf.title"));
        protobufConvertMenuItem.addActionListener(new ChangeMenuToPanelListener(ProtobufConvertPanel.class, protobufConvertMenuItem.getText()));
        convertMenu.add(protobufConvertMenuItem);
    }

    private void createAndroidMenu() {
        JMenu androidMenu = new JMenu(bundle.getString("android.title"));
        menuBar.add(androidMenu);

        JMenuItem i18nAddMenuItem = new JMenuItem(bundle.getString("android.i18n.add.title"));
        i18nAddMenuItem.addActionListener(new ChangeMenuToPanelListener(I18nAddPanel.class, i18nAddMenuItem.getText()));
        androidMenu.add(i18nAddMenuItem);

        JMenuItem i18nFindLongestMenuItem = new JMenuItem(bundle.getString("android.i18n.longest.title"));
        i18nFindLongestMenuItem.addActionListener(new ChangeMenuToPanelListener(I18nFindLongestPanel.class, i18nFindLongestMenuItem.getText()));
        androidMenu.add(i18nFindLongestMenuItem);

        JMenuItem i18nRemoveMenuItem = new JMenuItem(bundle.getString("android.i18n.remove.title"));
        i18nRemoveMenuItem.addActionListener(new ChangeMenuToPanelListener(I18nRemovePanel.class, i18nRemoveMenuItem.getText()));
        androidMenu.add(i18nRemoveMenuItem);

        JMenuItem screenShotMenuItem = new JMenuItem(bundle.getString("android.screenshot.title"));
        screenShotMenuItem.addActionListener(new ChangeMenuToPanelListener(ScreenShotPanel.class, screenShotMenuItem.getText()));
        screenShotMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        androidMenu.add(screenShotMenuItem);

        JMenuItem monkeyMenuItem = new JMenuItem(bundle.getString("android.monkey.title"));
        monkeyMenuItem.addActionListener(new ChangeMenuToPanelListener(MonkeyPanel.class, monkeyMenuItem.getText()));
        androidMenu.add(monkeyMenuItem);

        JMenuItem dumpsysAlarmMenuItem = new JMenuItem(bundle.getString("android.dumpsys.alarm.title"));
        dumpsysAlarmMenuItem.addActionListener(new ChangeMenuToPanelListener(DumpsysAlarmPanel.class, dumpsysAlarmMenuItem.getText()));
        androidMenu.add(dumpsysAlarmMenuItem);
    }

    private void createReverseMenu() {
        JMenu reverseMenu = new JMenu(bundle.getString("reverse.title"));
        reverseMenu.setMnemonic(KeyEvent.VK_R);
        menuBar.add(reverseMenu);

        JMenuItem pluginVersionMenuItem = new JMenuItem(bundle.getString("reverse.plugin.version.title"));
        pluginVersionMenuItem.addActionListener(new ChangeMenuToUrlListener(Constant.URL_PLUGIN_VERSION));
        reverseMenu.add(pluginVersionMenuItem);

        JMenuItem apktoolMenuItem = new JMenuItem(bundle.getString("reverse.apktool.title"), KeyEvent.VK_D);
        apktoolMenuItem.addActionListener(new ChangeMenuToPanelListener(ApktoolPanel.class, apktoolMenuItem.getText()));
        reverseMenu.add(apktoolMenuItem);

        JMenuItem apkSignMenuItem = new JMenuItem(bundle.getString("reverse.apksigner.title"));
        apkSignMenuItem.addActionListener(new ChangeMenuToPanelListener(ApkSignerPanel.class, apkSignMenuItem.getText()));
        reverseMenu.add(apkSignMenuItem);

        JMenuItem jDMenuItem = new JMenuItem(bundle.getString("reverse.jd.gui.title"));
        jDMenuItem.addActionListener(new ChangeMenToPluginJdListener());
        reverseMenu.add(jDMenuItem);

        JMenuItem luytenMenuItem = new JMenuItem(bundle.getString("reverse.luyten.title"));
        luytenMenuItem.addActionListener(new ChangeMenuToPluginLuytenListener());
        reverseMenu.add(luytenMenuItem);

        JMenuItem jdDuoMenuItem = new JMenuItem(bundle.getString("reverse.jd.duo.title"));
        jdDuoMenuItem.addActionListener(new ChangeMenuToPluginJdDuoListener());
        reverseMenu.add(jdDuoMenuItem);

        JMenuItem jdaMenuItem = new JMenuItem(bundle.getString("reverse.jda.title"));
        jdaMenuItem.addActionListener(new ChangeMenuToPluginJdaListener());
        reverseMenu.add(jdaMenuItem);

        JMenuItem jADXMenuItem = new JMenuItem(bundle.getString("reverse.jadx.title"));
        jADXMenuItem.addActionListener(new ChangeMenuToPluginJadxListener());
        reverseMenu.add(jADXMenuItem);

        JMenuItem aXMLPrinter = new JMenuItem(bundle.getString("reverse.axmlprinter.title"));
        aXMLPrinter.addActionListener(new ChangeMenuToPanelListener(AxmlPrinterPanel.class, aXMLPrinter.getText()));
        reverseMenu.add(aXMLPrinter);
    }

    class ChangeMenuToPanelListener implements ChangeMenuListener {

        Class<? extends EasyPanel> easyPanelClass;

        EasyPanel panel = null;

        String title;

        public ChangeMenuToPanelListener(Class<? extends EasyPanel> easyPanelClass, String title) {
            this.easyPanelClass = easyPanelClass;
            this.title = title;
            panel = createEasyPanel();
        }

        @Override
        public boolean isNeedPreChangeMenu() {
            return panel.isNeedPreChangeMenu();
        }

        @Override
        public void onPreChangeMenu(IPreChangeMenuCallBack callBack) {
            if (panel instanceof PluginPanel pluginPanel) {
                pluginPanel.preparePlugin(new ChangeMenuPreparePluginController(pluginPanel.getPluginFilename(), pluginPanel.isPluginNeedUnzip(), pluginPanel.isPluginNeedUnzipToSeparateDir(), callBack));
            }
        }

        @Override
        public void onChangeMenu() {
            contentPane.removeAll();
            contentPane.add(Box.createVerticalGlue());
            panel.init();
            panel.setBorder(BorderFactory.createTitledBorder(title));
            contentPane.add(panel);
            logger.info("Panel changed: " + panel.getClass().getSimpleName());
            contentPane.add(Box.createVerticalGlue());
            contentPane.revalidate();
            contentPane.repaint();
            refreshSizeAndLocation();
        }

        private EasyPanel createEasyPanel() {
            if (easyPanelClass == null) {
                return new EasyPanel();
            }
            EasyPanel retEasyPanel = null;
            try {
                retEasyPanel = easyPanelClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                logger.info("createEasyPanel failed: {}", e.getMessage());
            }
            return Objects.requireNonNullElseGet(retEasyPanel, EasyPanel::new);
        }
    }
}


