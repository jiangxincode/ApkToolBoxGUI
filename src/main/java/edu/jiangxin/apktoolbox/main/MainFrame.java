package edu.jiangxin.apktoolbox.main;

import edu.jiangxin.apktoolbox.Version;
import edu.jiangxin.apktoolbox.config.ConfigPanel;
import edu.jiangxin.apktoolbox.convert.base.BaseConvertPanel;
import edu.jiangxin.apktoolbox.convert.color.ColorConvertPanel;
import edu.jiangxin.apktoolbox.convert.color.ColorPickerPanel;
import edu.jiangxin.apktoolbox.convert.relationship.RelationShipConvertPanel;
import edu.jiangxin.apktoolbox.convert.time.TimeConvertPanel;
import edu.jiangxin.apktoolbox.convert.zh2unicode.Zh2UnicodeConvertPanel;
import edu.jiangxin.apktoolbox.android.dumpsys.alarm.DumpsysAlarmPanel;
import edu.jiangxin.apktoolbox.file.BatchRenamePanel;
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
import edu.jiangxin.apktoolbox.reverse.*;
import edu.jiangxin.apktoolbox.android.screenshot.ScreenShotPanel;
import edu.jiangxin.apktoolbox.swing.extend.EasyFrame;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author jiangxin
 * @author 2018-08-19
 */
public class MainFrame extends EasyFrame {

    private static final long serialVersionUID = 1L;

    private TrayIcon trayIcon;
    private SystemTray systemTray;

    private JPanel contentPane;
    private JMenuBar menuBar;

    private JMenu configMenu;
    private JMenuItem configMenuItem;

    private JMenu reverseMenu;
    private JMenuItem apktoolDecodeMenuItem;
    private JMenuItem apktoolRebuildMenuItem;
    private JMenuItem apkSignMenuItem;
    private JMenuItem jDMenuItem;
    private JMenuItem jADXMenuItem;
    private JMenuItem aXMLPrinter;

    private JMenu androidMenu;
    private JMenuItem i18nAddMenuItem;
    private JMenuItem i18nFindLongestMenuItem;
    private JMenuItem i18nRemoveMenuItem;
    private JMenuItem screenShotMenuItem;
    private JMenuItem monkeyMenuItem;
    private JMenuItem dumpsysAlarmMenuItem;

    private JMenu fileMenu;
    private JMenuItem osConvertMenuItem;
    private JMenuItem encodeConvertMenuItem;
    private JMenuItem zhConvertMenuItem;
    private JMenuItem duplicateFindMenuItem;
    private JMenuItem batchRenameMenuItem;
    private JMenuItem checkSumMenuItem;
    private JMenuItem recoveryMenuItem;

    private JMenu convertMenu;
    private JMenuItem timeConvertMenuItem;
    private JMenuItem colorConvertMenuItem;
    private JMenuItem colorPickerMenuItem;
    private JMenuItem baseConvertMenuItem;
    private JMenuItem unicodeConvertMenuItem;
    private JMenuItem relationShipConvertMenuItem;

    private JMenu helpMenu;
    private JMenuItem lookAndFeelMenuItem;
    private JMenuItem localeMenuItem;
    private JMenuItem feedbackMenuItem;
    private JMenuItem checkUpdateMenuItem;
    private JMenuItem contributeMenuItem;
    private JMenuItem aboutMenuItem;

    public static void main(String[] args) {
        final Logger logger = LogManager.getLogger(MainFrame.class.getSimpleName());
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
        setContentPane(contentPane);
        refreshSizeAndLocation();
    }

    private void configSystemTray() {
        systemTray = SystemTray.getSystemTray();
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
        trayIcon = new TrayIcon(image, "系统托盘", popupMenu);
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

        createConfigMenu();

        createReverseMenu();

        createAndroidMenu();

        createFileMenu();

        createConvertMenu();

        createHelpMenu();
    }

    private void createHelpMenu() {
        helpMenu = new JMenu(bundle.getString("help.title"));
        menuBar.add(helpMenu);

        lookAndFeelMenuItem = new JMenuItem(bundle.getString("help.look.and.feel.title"));
        lookAndFeelMenuItem.addActionListener(new ChangePanelListener(LookAndFeelPanel.class, lookAndFeelMenuItem.getText()));
        helpMenu.add(lookAndFeelMenuItem);

        localeMenuItem = new JMenuItem(bundle.getString("help.locale.title"));
        localeMenuItem.addActionListener(new ChangePanelListener(LocalePanel.class, localeMenuItem.getText()));
        helpMenu.add(localeMenuItem);

        feedbackMenuItem = new JMenuItem(bundle.getString("help.feedback.title"));
        feedbackMenuItem.addActionListener(new OpenWebsiteListener(Constant.URL_FEEDBACK));
        helpMenu.add(feedbackMenuItem);

        checkUpdateMenuItem = new JMenuItem(bundle.getString("help.checkupdate.title"));
        checkUpdateMenuItem.addActionListener(new CheckUpdateActionListener(this));
        helpMenu.add(checkUpdateMenuItem);

        contributeMenuItem = new JMenuItem(bundle.getString("help.contribute.title"));
        contributeMenuItem.addActionListener(new OpenWebsiteListener(Constant.URL_CONTRIBUTE));
        helpMenu.add(contributeMenuItem);

        aboutMenuItem = new JMenuItem(bundle.getString("help.about.title"));
        aboutMenuItem.addActionListener(new ChangePanelListener(AboutPanel.class, aboutMenuItem.getText()));
        helpMenu.add(aboutMenuItem);
    }

    private void createFileMenu() {
        fileMenu = new JMenu(bundle.getString("file.title"));
        menuBar.add(fileMenu);

        osConvertMenuItem = new JMenuItem(bundle.getString("file.os.convert.title"));
        osConvertMenuItem.addActionListener(new ChangePanelListener(OsConvertPanel.class, osConvertMenuItem.getText()));
        fileMenu.add(osConvertMenuItem);

        encodeConvertMenuItem = new JMenuItem(bundle.getString("file.encode.convert.title"));
        encodeConvertMenuItem.addActionListener(new ChangePanelListener(EncodeConvertPanel.class, encodeConvertMenuItem.getText()));
        fileMenu.add(encodeConvertMenuItem);

        zhConvertMenuItem = new JMenuItem(bundle.getString("file.zh.convert.title"));
        zhConvertMenuItem.addActionListener(new ChangePanelListener(ZhConvertPanel.class, zhConvertMenuItem.getText()));
        fileMenu.add(zhConvertMenuItem);

        duplicateFindMenuItem = new JMenuItem(bundle.getString("file.duplicate.title"));
        duplicateFindMenuItem.addActionListener(new ChangePanelListener(DuplicateSearchPanel.class, duplicateFindMenuItem.getText()));
        fileMenu.add(duplicateFindMenuItem);

        batchRenameMenuItem = new JMenuItem(bundle.getString("file.batch.rename.title"));
        batchRenameMenuItem.addActionListener(new ChangePanelListener(BatchRenamePanel.class, batchRenameMenuItem.getText()));
        fileMenu.add(batchRenameMenuItem);

        checkSumMenuItem = new JMenuItem(bundle.getString("file.check.summary.title"));
        checkSumMenuItem.addActionListener(new ChangePanelListener(ChecksumPanel.class, checkSumMenuItem.getText()));
        fileMenu.add(checkSumMenuItem);

        recoveryMenuItem = new JMenuItem(bundle.getString("file.password.recovery.title"));
        recoveryMenuItem.addActionListener(new ChangePanelListener(RecoveryPanel.class, recoveryMenuItem.getText()));
        fileMenu.add(recoveryMenuItem);
    }

    private void createConvertMenu() {
        convertMenu = new JMenu(bundle.getString("convert.title"));
        menuBar.add(convertMenu);

        timeConvertMenuItem = new JMenuItem(bundle.getString("convert.time.title"));
        timeConvertMenuItem.addActionListener(new ChangePanelListener(TimeConvertPanel.class, timeConvertMenuItem.getText()));
        convertMenu.add(timeConvertMenuItem);

        colorConvertMenuItem = new JMenuItem(bundle.getString("convert.color.title"));
        colorConvertMenuItem.addActionListener(new ChangePanelListener(ColorConvertPanel.class, colorConvertMenuItem.getText()));
        convertMenu.add(colorConvertMenuItem);

        colorPickerMenuItem = new JMenuItem(bundle.getString("picker.color.title"));
        colorPickerMenuItem.addActionListener(new ChangePanelListener(ColorPickerPanel.class, colorPickerMenuItem.getText()));
        convertMenu.add(colorPickerMenuItem);

        baseConvertMenuItem = new JMenuItem(bundle.getString("convert.base.title"));
        baseConvertMenuItem.addActionListener(new ChangePanelListener(BaseConvertPanel.class, baseConvertMenuItem.getText()));
        convertMenu.add(baseConvertMenuItem);

        unicodeConvertMenuItem = new JMenuItem(bundle.getString("convert.unicode.title"));
        unicodeConvertMenuItem.addActionListener(new ChangePanelListener(Zh2UnicodeConvertPanel.class, unicodeConvertMenuItem.getText()));
        convertMenu.add(unicodeConvertMenuItem);

        relationShipConvertMenuItem = new JMenuItem(bundle.getString("convert.relationship.title"));
        relationShipConvertMenuItem.addActionListener(new ChangePanelListener(RelationShipConvertPanel.class, relationShipConvertMenuItem.getText()));
        convertMenu.add(relationShipConvertMenuItem);
    }

    private void createAndroidMenu() {
        androidMenu = new JMenu(bundle.getString("android.title"));
        menuBar.add(androidMenu);

        i18nAddMenuItem = new JMenuItem(bundle.getString("android.i18n.add.title"));
        i18nAddMenuItem.addActionListener(new ChangePanelListener(I18nAddPanel.class, i18nAddMenuItem.getText()));
        androidMenu.add(i18nAddMenuItem);

        i18nFindLongestMenuItem = new JMenuItem(bundle.getString("android.i18n.longest.title"));
        i18nFindLongestMenuItem.addActionListener(new ChangePanelListener(I18nFindLongestPanel.class, i18nFindLongestMenuItem.getText()));
        androidMenu.add(i18nFindLongestMenuItem);

        i18nRemoveMenuItem = new JMenuItem(bundle.getString("android.i18n.remove.title"));
        i18nRemoveMenuItem.addActionListener(new ChangePanelListener(I18nRemovePanel.class, i18nRemoveMenuItem.getText()));
        androidMenu.add(i18nRemoveMenuItem);

        screenShotMenuItem = new JMenuItem(bundle.getString("android.screenshot.title"));
        screenShotMenuItem.addActionListener(new ChangePanelListener(ScreenShotPanel.class, screenShotMenuItem.getText()));
        screenShotMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        androidMenu.add(screenShotMenuItem);

        monkeyMenuItem = new JMenuItem(bundle.getString("android.monkey.title"));
        monkeyMenuItem.addActionListener(new ChangePanelListener(MonkeyPanel.class, monkeyMenuItem.getText()));
        androidMenu.add(monkeyMenuItem);

        dumpsysAlarmMenuItem = new JMenuItem(bundle.getString("android.dumpsys.alarm.title"));
        dumpsysAlarmMenuItem.addActionListener(new ChangePanelListener(DumpsysAlarmPanel.class, dumpsysAlarmMenuItem.getText()));
        androidMenu.add(dumpsysAlarmMenuItem);
    }

    private void createConfigMenu() {
        configMenu = new JMenu(bundle.getString("config.title"));
        menuBar.add(configMenu);

        configMenuItem = new JMenuItem(bundle.getString("config.title"));
        configMenuItem.addActionListener(new ChangePanelListener(ConfigPanel.class, configMenuItem.getText()));
        configMenu.add(configMenuItem);
    }

    private void createReverseMenu() {
        reverseMenu = new JMenu(bundle.getString("reverse.title"));
        reverseMenu.setMnemonic(KeyEvent.VK_R);
        menuBar.add(reverseMenu);

        apktoolDecodeMenuItem = new JMenuItem(bundle.getString("reverse.apktool.decode.title"), KeyEvent.VK_D);
        apktoolDecodeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
        apktoolDecodeMenuItem.addActionListener(new ChangePanelListener(ApktoolDecodePanel.class, apktoolDecodeMenuItem.getText()));
        reverseMenu.add(apktoolDecodeMenuItem);

        apktoolRebuildMenuItem = new JMenuItem(bundle.getString("reverse.apktool.rebuild.title"));
        apktoolRebuildMenuItem.addActionListener(new ChangePanelListener(ApktoolRebuildPanel.class, apktoolRebuildMenuItem.getText()));
        reverseMenu.add(apktoolRebuildMenuItem);

        apkSignMenuItem = new JMenuItem(bundle.getString("reverse.apksigner.title"));
        apkSignMenuItem.addActionListener(new ChangePanelListener(ApkSignerPanel.class, apkSignMenuItem.getText()));
        reverseMenu.add(apkSignMenuItem);

        jDMenuItem = new JMenuItem(bundle.getString("reverse.jd.gui.title"));
        jDMenuItem.addActionListener(new JdActionListener());
        reverseMenu.add(jDMenuItem);

        jADXMenuItem = new JMenuItem(bundle.getString("reverse.jadx.gui.title"));
        jADXMenuItem.addActionListener(new JadxActionListener());
        reverseMenu.add(jADXMenuItem);

        aXMLPrinter = new JMenuItem(bundle.getString("reverse.axmlprinter.title"));
        aXMLPrinter.addActionListener(new ChangePanelListener(AxmlPrinterPanel.class, aXMLPrinter.getText()));
        reverseMenu.add(aXMLPrinter);
    }

    class ChangePanelListener implements ActionListener {

        Class<EasyPanel> easyPanelClass;

        String title;

        public ChangePanelListener(Class easyPanelClass, String title) {
            this.easyPanelClass = easyPanelClass;
            this.title = title;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Utils.saveConfiguration();
            contentPane.removeAll();
            contentPane.add(Box.createVerticalGlue());
            EasyPanel panel = createEasyPanel();
            panel.setFrame(MainFrame.this);
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
            EasyPanel panel = null;
            try {
                panel = easyPanelClass.newInstance();
            } catch (InstantiationException e) {
                logger.info("createEasyPanel failed because of InstantiationException: ");
            } catch (IllegalAccessException e) {
                logger.info("createEasyPanel failed because of IllegalAccessException: ");
            }
            if (panel == null) {
                return new EasyPanel();
            }
            return panel;
        }
    }
}


