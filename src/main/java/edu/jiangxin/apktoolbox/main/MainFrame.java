package edu.jiangxin.apktoolbox.main;

import java.awt.Event;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.jiangxin.apktoolbox.Version;
import edu.jiangxin.apktoolbox.help.AboutPanel;
import edu.jiangxin.apktoolbox.help.CheckUpdateActionListener;
import edu.jiangxin.apktoolbox.help.Constant;
import edu.jiangxin.apktoolbox.help.OpenWebsiteListener;
import edu.jiangxin.apktoolbox.i18n.I18nAddPanel;
import edu.jiangxin.apktoolbox.i18n.I18nFindLongestPanel;
import edu.jiangxin.apktoolbox.i18n.I18nRemovePanel;
import edu.jiangxin.apktoolbox.monkey.MonkeyPanel;
import edu.jiangxin.apktoolbox.reverse.ApkSignerPanel;
import edu.jiangxin.apktoolbox.reverse.ApktoolDecodePanel;
import edu.jiangxin.apktoolbox.reverse.ApktoolRebuildPanel;
import edu.jiangxin.apktoolbox.reverse.AxmlPrinterPanel;
import edu.jiangxin.apktoolbox.reverse.JadxActionListener;
import edu.jiangxin.apktoolbox.reverse.JdActionListener;
import edu.jiangxin.apktoolbox.screenshot.ScreenShotPanel;
import edu.jiangxin.apktoolbox.swing.extend.EasyFrame;
import edu.jiangxin.apktoolbox.text.EncodeConvertPanel;
import edu.jiangxin.apktoolbox.text.OsConvertPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;

/**
 * @author jiangxin
 * @author 2018-08-19
 *
 */
public class MainFrame extends EasyFrame {

    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = LogManager.getLogger(MainFrame.class);
    
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
    private JMenu testMenu;
    private JMenuItem monkeyMenuItem;
    private JMenu textMenu;
    private JMenuItem osConvertMenuItem;
    private JMenuItem encodeConvertMenuItem;
    private JMenu i18nMenu;
    private JMenuItem i18nAddMenuItem;
    private JMenuItem i18nFindLongestMenuItem;
    private JMenuItem i18nRemoveMenuItem;
    private JMenu helpMenu;
    private JMenuItem feedbackMenuItem;
    private JMenuItem checkUpdateMenuItem;
    private JMenuItem contributeMenuItem;
    private JMenuItem aboutMenuItem;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    /* UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); */
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    LOGGER.error("Exception", e);
                }
            }
        });
    }

    public MainFrame() {
        setTitle(MessageFormat.format(bundle.getString("main.title"), Version.VERSION));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HIGHT);
        setResizable(false);
        Utils.setJFrameCenterInScreen(this);

        setMenuBar();

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);
    }

    private void setMenuBar() {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        createReverseMenu();

        createScreenshotMenu();

        createTestMenu();

        createTextMenu();

        createI18nMenu();

        createHelpMenu();
    }

    private void createHelpMenu() {
        helpMenu = new JMenu(bundle.getString("help.title"));
        menuBar.add(helpMenu);

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
        aboutMenuItem.addActionListener(new ChangePanelListener(new AboutPanel()));
        helpMenu.add(aboutMenuItem);
    }

    private void createI18nMenu() {
        i18nMenu = new JMenu(bundle.getString("i18n.title"));
        menuBar.add(i18nMenu);

        i18nAddMenuItem = new JMenuItem(bundle.getString("i18n.add.title"));
        i18nAddMenuItem.addActionListener(new ChangePanelListener(new I18nAddPanel()));
        i18nMenu.add(i18nAddMenuItem);

        i18nFindLongestMenuItem = new JMenuItem(bundle.getString("i18n.longest.title"));
        i18nFindLongestMenuItem.addActionListener(new ChangePanelListener(new I18nFindLongestPanel()));
        i18nMenu.add(i18nFindLongestMenuItem);

        i18nRemoveMenuItem = new JMenuItem(bundle.getString("i18n.remove.title"));
        i18nRemoveMenuItem.addActionListener(new ChangePanelListener(new I18nRemovePanel()));
        i18nMenu.add(i18nRemoveMenuItem);
    }

    private void createTextMenu() {
        textMenu = new JMenu(bundle.getString("text.title"));
        menuBar.add(textMenu);

        osConvertMenuItem = new JMenuItem("OS Convert");
        osConvertMenuItem.addActionListener(new ChangePanelListener(new OsConvertPanel()));
        textMenu.add(osConvertMenuItem);

        encodeConvertMenuItem = new JMenuItem(bundle.getString("text.encode.convert.title"));
        encodeConvertMenuItem.addActionListener(new ChangePanelListener(new EncodeConvertPanel()));
        textMenu.add(encodeConvertMenuItem);
    }

    private void createTestMenu() {
        testMenu = new JMenu(bundle.getString("test.title"));
        menuBar.add(testMenu);

        monkeyMenuItem = new JMenuItem(bundle.getString("test.monkey.title"));
        monkeyMenuItem.addActionListener(new ChangePanelListener(new MonkeyPanel()));
        testMenu.add(monkeyMenuItem);
    }

    private void createScreenshotMenu() {
        screenshotMenu = new JMenu(bundle.getString("screenshot.title"));
        menuBar.add(screenshotMenu);

        screenShotMenuItem = new JMenuItem(bundle.getString("screenshot.screenshot.title"));
        screenShotMenuItem.addActionListener(new ChangePanelListener(new ScreenShotPanel()));
        screenShotMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        screenshotMenu.add(screenShotMenuItem);
    }

    private void createReverseMenu() {
        reverseMenu = new JMenu("Reverse");
        reverseMenu.setMnemonic(KeyEvent.VK_R);
        menuBar.add(reverseMenu);

        apktoolDecodeMenuItem = new JMenuItem("Apktool Decode", KeyEvent.VK_D);
        apktoolDecodeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
        apktoolDecodeMenuItem.addActionListener(new ChangePanelListener(new ApktoolDecodePanel()));
        reverseMenu.add(apktoolDecodeMenuItem);

        apktoolRebuildMenuItem = new JMenuItem("Apktool Rebuild");
        apktoolRebuildMenuItem.addActionListener(new ChangePanelListener(new ApktoolRebuildPanel()));
        reverseMenu.add(apktoolRebuildMenuItem);

        apkSignMenuItem = new JMenuItem("ApkSigner");
        apkSignMenuItem.addActionListener(new ChangePanelListener(new ApkSignerPanel()));
        reverseMenu.add(apkSignMenuItem);

        jDMenuItem = new JMenuItem("JD-GUI");
        jDMenuItem.addActionListener(new JdActionListener());
        reverseMenu.add(jDMenuItem);

        jADXMenuItem = new JMenuItem("JADX-GUI");
        jADXMenuItem.addActionListener(new JadxActionListener());
        reverseMenu.add(jADXMenuItem);

        aXMLPrinter = new JMenuItem("AXMLPrinter");
        aXMLPrinter.addActionListener(new ChangePanelListener(new AxmlPrinterPanel()));
        reverseMenu.add(aXMLPrinter);
    }
    
    class ChangePanelListener implements ActionListener {
        
        JPanel panel;

        public ChangePanelListener(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Utils.saveConfiguration();
            contentPane.removeAll();
            contentPane.add(Box.createVerticalGlue());
            contentPane.add(panel);
            LOGGER.info("Panel changed: " + panel.getClass().getSimpleName());
            contentPane.add(Box.createVerticalGlue());
            contentPane.revalidate();
            contentPane.repaint();
        }
        
    }
    
}


