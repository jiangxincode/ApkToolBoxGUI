package edu.jiangxin.apktoolbox.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import edu.jiangxin.apktoolbox.help.AboutMouseListener;
import edu.jiangxin.apktoolbox.help.ContributeMouseListener;
import edu.jiangxin.apktoolbox.monkey.MonkeyMouseListener;
import edu.jiangxin.apktoolbox.reverse.AXMLPrinterMouseListener;
import edu.jiangxin.apktoolbox.reverse.ApkSignerMouseListener;
import edu.jiangxin.apktoolbox.reverse.ApktoolDecodeMouseListener;
import edu.jiangxin.apktoolbox.reverse.ApktoolRebuildMouseListener;
import edu.jiangxin.apktoolbox.reverse.JADXMouseListener;
import edu.jiangxin.apktoolbox.reverse.JDMouseListener;
import edu.jiangxin.apktoolbox.screenshot.ScreenshotMouseListener;
import edu.jiangxin.apktoolbox.utils.Utils;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField searchTextField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
		Utils.setJFrameCenterInScreen(this);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				Utils.saveConfiguration();
			}
		});

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu reverseMenu = new JMenu("Reverse");
		menuBar.add(reverseMenu);

		JMenuItem apktoolDecodeMenuItem = new JMenuItem("Apktool Decode");
		apktoolDecodeMenuItem.addMouseListener(new ApktoolDecodeMouseListener());
		reverseMenu.add(apktoolDecodeMenuItem);

		JMenuItem apktoolRebuildMenuItem = new JMenuItem("Apktool Rebuild");
		apktoolRebuildMenuItem.addMouseListener(new ApktoolRebuildMouseListener());
		reverseMenu.add(apktoolRebuildMenuItem);
		
		JMenuItem apkSignMenuItem = new JMenuItem("ApkSigner");
		apkSignMenuItem.addMouseListener(new ApkSignerMouseListener());
		reverseMenu.add(apkSignMenuItem);
		
		JMenuItem jDMenuItem = new JMenuItem("JD-GUI");
		jDMenuItem.addMouseListener(new JDMouseListener());
		reverseMenu.add(jDMenuItem);
		
		JMenuItem jADXMenuItem = new JMenuItem("JADX-GUI");
		jADXMenuItem.addMouseListener(new JADXMouseListener());
		reverseMenu.add(jADXMenuItem);
		
		JMenuItem aXMLPrinter = new JMenuItem("AXMLPrinter");
		aXMLPrinter.addMouseListener(new AXMLPrinterMouseListener());
		reverseMenu.add(aXMLPrinter);

		JMenu screenshotMenu = new JMenu("Screnshot");
		menuBar.add(screenshotMenu);

		JMenuItem screenShotMenuItem = new JMenuItem("Screnshot");
		screenShotMenuItem.addMouseListener(new ScreenshotMouseListener());
		screenshotMenu.add(screenShotMenuItem);

		JMenu testMenu = new JMenu("Test");
		menuBar.add(testMenu);

		JMenuItem monkeyMenuItem = new JMenuItem("Monkey Test");
		monkeyMenuItem.addMouseListener(new MonkeyMouseListener());
		testMenu.add(monkeyMenuItem);

		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);

		JMenuItem contributeMenuItem = new JMenuItem("Contribute");
		contributeMenuItem.addMouseListener(new ContributeMouseListener());
		helpMenu.add(contributeMenuItem);

		JMenuItem aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addMouseListener(new AboutMouseListener());
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

		JPanel searchPanel = new JPanel();
		searchPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.add(searchPanel, BorderLayout.NORTH);

		searchTextField = new JTextField();
		searchTextField.setPreferredSize(new Dimension(6, 20));
		searchTextField.setToolTipText("");
		searchTextField.setColumns(10);

		JButton searchButton = new JButton("Search");
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		searchPanel.add(searchTextField);
		searchPanel.add(searchButton);
	}

}
