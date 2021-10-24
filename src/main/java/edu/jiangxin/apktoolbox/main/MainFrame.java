package edu.jiangxin.apktoolbox.main;

import edu.jiangxin.apktoolbox.Version;
import edu.jiangxin.apktoolbox.convert.color.ColorConvertPanel;
import edu.jiangxin.apktoolbox.dumpsys.alarm.DumpsysAlarmPanel;
import edu.jiangxin.apktoolbox.file.DuplicateFindPanel;
import edu.jiangxin.apktoolbox.help.*;
import edu.jiangxin.apktoolbox.i18n.I18nAddPanel;
import edu.jiangxin.apktoolbox.i18n.I18nFindLongestPanel;
import edu.jiangxin.apktoolbox.i18n.I18nRemovePanel;
import edu.jiangxin.apktoolbox.monkey.MonkeyPanel;
import edu.jiangxin.apktoolbox.reverse.*;
import edu.jiangxin.apktoolbox.screenshot.ScreenShotPanel;
import edu.jiangxin.apktoolbox.swing.extend.EasyFrame;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.text.EncodeConvertPanel;
import edu.jiangxin.apktoolbox.text.OsConvertPanel;
import edu.jiangxin.apktoolbox.text.zhconvert.ZhConvertPanel;
import edu.jiangxin.apktoolbox.convert.time.TimeConvertPanel;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;

/**
 * @author jiangxin
 * @author 2018-08-19
 */
public class MainFrame extends EasyFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JMenuBar menuBar;

    private JMenu reverseMenu;
    private JMenuItem apktoolDecodeMenuItem;
    private JMenuItem apktoolRebuildMenuItem;
    private JMenuItem apkSignMenuItem;
    private JMenuItem jDMenuItem;
    private JMenuItem jADXMenuItem;
    private JMenuItem aXMLPrinter;

    private JMenu screenshotMenu;
    private JMenuItem screenShotMenuItem;

    private JMenu dumpsysMenu;
    private JMenuItem dumpsysAlarmMenuItem;

    private JMenu testMenu;
    private JMenuItem monkeyMenuItem;

    private JMenu textMenu;
    private JMenuItem osConvertMenuItem;
    private JMenuItem encodeConvertMenuItem;
    private JMenuItem zhConvertMenuItem;

    private JMenu fileMenu;
    private JMenuItem duplicateFindMenuItem;

    private JMenu convertMenu;
    private JMenuItem timeConvertMenuItem;
    private JMenuItem colorConvertMenuItem;

    private JMenu i18nMenu;
    private JMenuItem i18nAddMenuItem;
    private JMenuItem i18nFindLongestMenuItem;
    private JMenuItem i18nRemoveMenuItem;

    private JMenu helpMenu;
    private JMenuItem lookAndFeelMenuItem;
    private JMenuItem feedbackMenuItem;
    private JMenuItem checkUpdateMenuItem;
    private JMenuItem contributeMenuItem;
    private JMenuItem aboutMenuItem;

    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(MainFrame.class);
        EventQueue.invokeLater(() -> {
            Configuration conf = Utils.getConfiguration();
            String lookAndFeelClassName = conf.getString("look.and.feel.class.name");
            if (StringUtils.isEmpty(lookAndFeelClassName)) {
                lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
            }
            try {
                UIManager.setLookAndFeel(lookAndFeelClassName);
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                logger.error("setLookAndFeel failed, use default instead", e);
            }
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

    public MainFrame() {
        setTitle(MessageFormat.format(bundle.getString("main.title"), Version.VERSION));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMenuBar();
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);
        refreshSizeAndLocation();
    }

    private void setMenuBar() {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        createReverseMenu();

        createScreenshotMenu();

        createDumpsysMenu();

        createTestMenu();

        createTextMenu();

        createFileMenu();

        createConvertMenu();

        createI18nMenu();

        createHelpMenu();
    }

    private void createHelpMenu() {
        helpMenu = new JMenu(bundle.getString("help.title"));
        menuBar.add(helpMenu);

        lookAndFeelMenuItem = new JMenuItem(bundle.getString("help.look.and.feel.title"));
        lookAndFeelMenuItem.addActionListener(new ChangePanelListener(LookAndFeelPanel.class, lookAndFeelMenuItem.getText()));
        helpMenu.add(lookAndFeelMenuItem);

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

    private void createI18nMenu() {
        i18nMenu = new JMenu(bundle.getString("i18n.title"));
        menuBar.add(i18nMenu);

        i18nAddMenuItem = new JMenuItem(bundle.getString("i18n.add.title"));
        i18nAddMenuItem.addActionListener(new ChangePanelListener(I18nAddPanel.class, i18nAddMenuItem.getText()));
        i18nMenu.add(i18nAddMenuItem);

        i18nFindLongestMenuItem = new JMenuItem(bundle.getString("i18n.longest.title"));
        i18nFindLongestMenuItem.addActionListener(new ChangePanelListener(I18nFindLongestPanel.class, i18nFindLongestMenuItem.getText()));
        i18nMenu.add(i18nFindLongestMenuItem);

        i18nRemoveMenuItem = new JMenuItem(bundle.getString("i18n.remove.title"));
        i18nRemoveMenuItem.addActionListener(new ChangePanelListener(I18nRemovePanel.class, i18nRemoveMenuItem.getText()));
        i18nMenu.add(i18nRemoveMenuItem);
    }

    private void createTextMenu() {
        textMenu = new JMenu(bundle.getString("text.title"));
        menuBar.add(textMenu);

        osConvertMenuItem = new JMenuItem(bundle.getString("text.os.convert.title"));
        osConvertMenuItem.addActionListener(new ChangePanelListener(OsConvertPanel.class, osConvertMenuItem.getText()));
        textMenu.add(osConvertMenuItem);

        encodeConvertMenuItem = new JMenuItem(bundle.getString("text.encode.convert.title"));
        encodeConvertMenuItem.addActionListener(new ChangePanelListener(EncodeConvertPanel.class, encodeConvertMenuItem.getText()));
        textMenu.add(encodeConvertMenuItem);

        zhConvertMenuItem = new JMenuItem(bundle.getString("text.zh.convert.title"));
        zhConvertMenuItem.addActionListener(new ChangePanelListener(ZhConvertPanel.class, zhConvertMenuItem.getText()));
        textMenu.add(zhConvertMenuItem);
    }

    private void createFileMenu() {
        fileMenu = new JMenu(bundle.getString("file.title"));
        menuBar.add(fileMenu);

        duplicateFindMenuItem = new JMenuItem(bundle.getString("file.duplicate.title"));
        duplicateFindMenuItem.addActionListener(new ChangePanelListener(DuplicateFindPanel.class, duplicateFindMenuItem.getText()));
        fileMenu.add(duplicateFindMenuItem);
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
    }

    private void createTestMenu() {
        testMenu = new JMenu(bundle.getString("test.title"));
        menuBar.add(testMenu);

        monkeyMenuItem = new JMenuItem(bundle.getString("test.monkey.title"));
        monkeyMenuItem.addActionListener(new ChangePanelListener(MonkeyPanel.class, monkeyMenuItem.getText()));
        testMenu.add(monkeyMenuItem);
    }

    private void createScreenshotMenu() {
        screenshotMenu = new JMenu(bundle.getString("screenshot.title"));
        menuBar.add(screenshotMenu);

        screenShotMenuItem = new JMenuItem(bundle.getString("screenshot.screenshot.title"));
        screenShotMenuItem.addActionListener(new ChangePanelListener(ScreenShotPanel.class, screenShotMenuItem.getText()));
        screenShotMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        screenshotMenu.add(screenShotMenuItem);
    }

    private void createDumpsysMenu() {
        dumpsysMenu = new JMenu(bundle.getString("dumpsys.title"));
        menuBar.add(dumpsysMenu);

        dumpsysAlarmMenuItem = new JMenuItem(bundle.getString("dumpsys.alarm.title"));
        dumpsysAlarmMenuItem.addActionListener(new ChangePanelListener(DumpsysAlarmPanel.class, dumpsysAlarmMenuItem.getText()));
        dumpsysMenu.add(dumpsysAlarmMenuItem);
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


