package edu.jiangxin.apktoolbox.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import edu.jiangxin.apktoolbox.Version;
import edu.jiangxin.apktoolbox.help.AboutActionListener;
import edu.jiangxin.apktoolbox.help.CheckUpdateActionListener;
import edu.jiangxin.apktoolbox.help.ContributeActionListener;
import edu.jiangxin.apktoolbox.i18n.I18NAddActionListener;
import edu.jiangxin.apktoolbox.monkey.MonkeyActionListener;
import edu.jiangxin.apktoolbox.reverse.AXMLPrinterActionListener;
import edu.jiangxin.apktoolbox.reverse.ApkSignerActionListener;
import edu.jiangxin.apktoolbox.reverse.ApktoolDecodeActionListener;
import edu.jiangxin.apktoolbox.reverse.ApktoolRebuildActionListener;
import edu.jiangxin.apktoolbox.reverse.JADXActionListener;
import edu.jiangxin.apktoolbox.reverse.JDActionListener;
import edu.jiangxin.apktoolbox.screenshot.ScreenshotActionListener;
import edu.jiangxin.apktoolbox.swing.extend.JEasyFrame;
import edu.jiangxin.apktoolbox.text.EncodeConvertActionListener;
import edu.jiangxin.apktoolbox.text.OSConvertActionListener;
import edu.jiangxin.apktoolbox.utils.Utils;

public class MainFrame extends JEasyFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/*UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());*/
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
		setSize(700, 500);
		Utils.setJFrameCenterInScreen(this);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu reverseMenu = new JMenu("Reverse");
		reverseMenu.setMnemonic(KeyEvent.VK_R);
		menuBar.add(reverseMenu);

		JMenuItem apktoolDecodeMenuItem = new JMenuItem("Apktool Decode", KeyEvent.VK_D);
		apktoolDecodeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
		apktoolDecodeMenuItem.addActionListener(new ApktoolDecodeActionListener());
		reverseMenu.add(apktoolDecodeMenuItem);

		JMenuItem apktoolRebuildMenuItem = new JMenuItem("Apktool Rebuild");
		apktoolRebuildMenuItem.addActionListener(new ApktoolRebuildActionListener());
		reverseMenu.add(apktoolRebuildMenuItem);

		JMenuItem apkSignMenuItem = new JMenuItem("ApkSigner");
		apkSignMenuItem.addActionListener(new ApkSignerActionListener());
		reverseMenu.add(apkSignMenuItem);

		JMenuItem jDMenuItem = new JMenuItem("JD-GUI");
		jDMenuItem.addActionListener(new JDActionListener());
		reverseMenu.add(jDMenuItem);

		JMenuItem jADXMenuItem = new JMenuItem("JADX-GUI");
		jADXMenuItem.addActionListener(new JADXActionListener());
		reverseMenu.add(jADXMenuItem);

		JMenuItem aXMLPrinter = new JMenuItem("AXMLPrinter");
		aXMLPrinter.addActionListener(new AXMLPrinterActionListener());
		reverseMenu.add(aXMLPrinter);

		JMenu screenshotMenu = new JMenu("Screnshot");
		menuBar.add(screenshotMenu);

		JMenuItem screenShotMenuItem = new JMenuItem("Screnshot");
		screenShotMenuItem.addActionListener(new ScreenshotActionListener());
		screenShotMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		screenshotMenu.add(screenShotMenuItem);

		JMenu testMenu = new JMenu("Test");
		menuBar.add(testMenu);

		JMenuItem monkeyMenuItem = new JMenuItem("Monkey Test");
		monkeyMenuItem.addActionListener(new MonkeyActionListener());
		testMenu.add(monkeyMenuItem);
		
		JMenu textMenu = new JMenu("Text");
		menuBar.add(textMenu);

		JMenuItem osConvertMenuItem = new JMenuItem("OS Convert");
		osConvertMenuItem.addActionListener(new OSConvertActionListener());
		textMenu.add(osConvertMenuItem);
		
		JMenuItem encodeConvertMenuItem = new JMenuItem("Encode Convert");
		encodeConvertMenuItem.addActionListener(new EncodeConvertActionListener());
		textMenu.add(encodeConvertMenuItem);
		
		JMenu i18nMenu = new JMenu(bundle.getString("i18n.title"));
		menuBar.add(i18nMenu);

		JMenuItem i18nAddMenuItem = new JMenuItem(bundle.getString("i18n.add.title"));
		i18nAddMenuItem.addActionListener(new I18NAddActionListener());
		i18nMenu.add(i18nAddMenuItem);

		JMenu helpMenu = new JMenu(bundle.getString("help.title"));
		menuBar.add(helpMenu);
		
		JMenuItem checkUpdateMenuItem = new JMenuItem(bundle.getString("help.checkupdate.title"));
		checkUpdateMenuItem.addActionListener(new CheckUpdateActionListener(this));
		helpMenu.add(checkUpdateMenuItem);

		JMenuItem contributeMenuItem = new JMenuItem(bundle.getString("help.contribute.title"));
		contributeMenuItem.addActionListener(new ContributeActionListener());
		helpMenu.add(contributeMenuItem);

		JMenuItem aboutMenuItem = new JMenuItem(bundle.getString("help.about.title"));
		aboutMenuItem.addActionListener(new AboutActionListener());
		helpMenu.add(aboutMenuItem);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel leftPanel = new JPanel();
		contentPane.add(leftPanel, BorderLayout.WEST);

		JTree contentTree = new JTree();
		leftPanel.add(contentTree);

		JPanel centerPanel = new JPanel();
		centerPanel.setMinimumSize(new Dimension(5, 5));
		centerPanel.setSize(new Dimension(60, 80));
		centerPanel.setAlignmentY(0.3f);
		centerPanel.setAlignmentX(0.3f);
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));

		JList contentList = new JList();
		contentList.setModel(new AbstractListModel() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;
			String[] values = new String[] { "1", "4", "5", "6" };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		contentList.setSelectedIndex(0);
		centerPanel.add(contentList);

		JPanel statusPanel = new JPanel();
		contentPane.add(statusPanel, BorderLayout.SOUTH);

		
	}

}
