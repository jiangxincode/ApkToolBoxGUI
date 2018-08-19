package edu.jiangxin.apktoolbox.main;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

import edu.jiangxin.apktoolbox.about.AddressMouseListener;
import edu.jiangxin.apktoolbox.screenshot.ScreenshotMouseListener;
import edu.jiangxin.apktoolbox.utils.Utils;

public class MainFrame extends JFrame {

	/**
	 *
	 */
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

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
		Utils.setJFrameCenterInScreen(this);

		JMenuBar jMenuBar = new JMenuBar();
		setJMenuBar(jMenuBar);
		
		
		JMenu screenshotJMenu = new JMenu("Screnshot");
		jMenuBar.add(screenshotJMenu);
		
		JMenuItem screenShotJMenuItem = new JMenuItem("Screnshot");
		screenShotJMenuItem.addMouseListener(new ScreenshotMouseListener());
		screenshotJMenu.add(screenShotJMenuItem);

		
		JMenu aboutJMenu = new JMenu("About");
		jMenuBar.add(aboutJMenu);
		
		JMenuItem addressAboutMenuItem = new JMenuItem("Address");
		addressAboutMenuItem.addMouseListener(new AddressMouseListener());
		aboutJMenu.add(addressAboutMenuItem);
		
		
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
			String[] values = new String[] {"1", "4", "5", "6"};
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
