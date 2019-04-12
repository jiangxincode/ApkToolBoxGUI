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

import edu.jiangxin.apktoolbox.Version;
import edu.jiangxin.apktoolbox.help.AboutPanel;
import edu.jiangxin.apktoolbox.help.CheckUpdateActionListener;
import edu.jiangxin.apktoolbox.help.ContributeActionListener;
import edu.jiangxin.apktoolbox.help.FeedbackActionListener;
import edu.jiangxin.apktoolbox.i18n.I18NAddPanel;
import edu.jiangxin.apktoolbox.i18n.I18NFindLongestPanel;
import edu.jiangxin.apktoolbox.i18n.I18NRemovePanel;
import edu.jiangxin.apktoolbox.monkey.MonkeyPanel;
import edu.jiangxin.apktoolbox.reverse.AXMLPrinterPanel;
import edu.jiangxin.apktoolbox.reverse.ApkSignerPanel;
import edu.jiangxin.apktoolbox.reverse.ApktoolDecodePanel;
import edu.jiangxin.apktoolbox.reverse.ApktoolRebuildPanel;
import edu.jiangxin.apktoolbox.reverse.JADXActionListener;
import edu.jiangxin.apktoolbox.reverse.JDActionListener;
import edu.jiangxin.apktoolbox.screenshot.ScreenShotPanel;
import edu.jiangxin.apktoolbox.swing.extend.JEasyFrame;
import edu.jiangxin.apktoolbox.text.EncodeConvertPanel;
import edu.jiangxin.apktoolbox.text.OSConvertPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;

public class MainFrame extends JEasyFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    /* UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); */
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
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
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu reverseMenu = new JMenu("Reverse");
        reverseMenu.setMnemonic(KeyEvent.VK_R);
        menuBar.add(reverseMenu);

        JMenuItem apktoolDecodeMenuItem = new JMenuItem("Apktool Decode", KeyEvent.VK_D);
        apktoolDecodeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
        apktoolDecodeMenuItem.addActionListener(new changePanelListener(new ApktoolDecodePanel()));
        reverseMenu.add(apktoolDecodeMenuItem);

        JMenuItem apktoolRebuildMenuItem = new JMenuItem("Apktool Rebuild");
        apktoolRebuildMenuItem.addActionListener(new changePanelListener(new ApktoolRebuildPanel()));
        reverseMenu.add(apktoolRebuildMenuItem);

        JMenuItem apkSignMenuItem = new JMenuItem("ApkSigner");
        apkSignMenuItem.addActionListener(new changePanelListener(new ApkSignerPanel()));
        reverseMenu.add(apkSignMenuItem);

        JMenuItem jDMenuItem = new JMenuItem("JD-GUI");
        jDMenuItem.addActionListener(new JDActionListener());
        reverseMenu.add(jDMenuItem);

        JMenuItem jADXMenuItem = new JMenuItem("JADX-GUI");
        jADXMenuItem.addActionListener(new JADXActionListener());
        reverseMenu.add(jADXMenuItem);

        JMenuItem aXMLPrinter = new JMenuItem("AXMLPrinter");
        aXMLPrinter.addActionListener(new changePanelListener(new AXMLPrinterPanel()));
        reverseMenu.add(aXMLPrinter);

        JMenu screenshotMenu = new JMenu(bundle.getString("screenshot.title"));
        menuBar.add(screenshotMenu);

        JMenuItem screenShotMenuItem = new JMenuItem(bundle.getString("screenshot.screenshot.title"));
        screenShotMenuItem.addActionListener(new changePanelListener(new ScreenShotPanel()));
        screenShotMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        screenshotMenu.add(screenShotMenuItem);

        JMenu testMenu = new JMenu(bundle.getString("test.title"));
        menuBar.add(testMenu);

        JMenuItem monkeyMenuItem = new JMenuItem(bundle.getString("test.monkey.title"));
        monkeyMenuItem.addActionListener(new changePanelListener(new MonkeyPanel()));
        testMenu.add(monkeyMenuItem);

        JMenu textMenu = new JMenu(bundle.getString("text.title"));
        menuBar.add(textMenu);

        JMenuItem osConvertMenuItem = new JMenuItem("OS Convert");
        osConvertMenuItem.addActionListener(new changePanelListener(new OSConvertPanel()));
        textMenu.add(osConvertMenuItem);

        JMenuItem encodeConvertMenuItem = new JMenuItem(bundle.getString("text.encode.convert.title"));
        encodeConvertMenuItem.addActionListener(new changePanelListener(new EncodeConvertPanel()));
        textMenu.add(encodeConvertMenuItem);

        JMenu i18nMenu = new JMenu(bundle.getString("i18n.title"));
        menuBar.add(i18nMenu);

        JMenuItem i18nAddMenuItem = new JMenuItem(bundle.getString("i18n.add.title"));
        i18nAddMenuItem.addActionListener(new changePanelListener(new I18NAddPanel()));
        i18nMenu.add(i18nAddMenuItem);

        JMenuItem i18nFindLongestMenuItem = new JMenuItem(bundle.getString("i18n.longest.title"));
        i18nFindLongestMenuItem.addActionListener(new changePanelListener(new I18NFindLongestPanel()));
        i18nMenu.add(i18nFindLongestMenuItem);

        JMenuItem i18nRemoveMenuItem = new JMenuItem(bundle.getString("i18n.remove.title"));
        i18nRemoveMenuItem.addActionListener(new changePanelListener(new I18NRemovePanel()));
        i18nMenu.add(i18nRemoveMenuItem);

        JMenu helpMenu = new JMenu(bundle.getString("help.title"));
        menuBar.add(helpMenu);

        JMenuItem feedbackMenuItem = new JMenuItem(bundle.getString("help.feedback.title"));
        feedbackMenuItem.addActionListener(new FeedbackActionListener());
        helpMenu.add(feedbackMenuItem);

        JMenuItem checkUpdateMenuItem = new JMenuItem(bundle.getString("help.checkupdate.title"));
        checkUpdateMenuItem.addActionListener(new CheckUpdateActionListener(this));
        helpMenu.add(checkUpdateMenuItem);

        JMenuItem contributeMenuItem = new JMenuItem(bundle.getString("help.contribute.title"));
        contributeMenuItem.addActionListener(new ContributeActionListener());
        helpMenu.add(contributeMenuItem);

        JMenuItem aboutMenuItem = new JMenuItem(bundle.getString("help.about.title"));
        aboutMenuItem.addActionListener(new changePanelListener(new AboutPanel()));
        helpMenu.add(aboutMenuItem);
    }
    
    class changePanelListener implements ActionListener {
        
        JPanel panel;

        public changePanelListener(JPanel panel) {
            this.panel = panel;
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Utils.saveConfiguration();
            contentPane.removeAll();
            contentPane.add(Box.createVerticalGlue());
            contentPane.add(panel);
            logger.info("Panel changed: " + panel.getClass().getSimpleName());
            contentPane.add(Box.createVerticalGlue());
            contentPane.revalidate();
            contentPane.repaint();
        }
        
    }
    
}


