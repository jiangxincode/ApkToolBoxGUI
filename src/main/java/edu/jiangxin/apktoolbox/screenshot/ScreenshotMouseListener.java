package edu.jiangxin.apktoolbox.screenshot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import edu.jiangxin.apktoolbox.utils.Utils;

public class ScreenshotMouseListener extends MouseAdapter {
	@Override
	public void mousePressed(MouseEvent e) {
		super.mouseClicked(e);
		JFrame screenShotJFrame = new JFrame();

		screenShotJFrame.setTitle("Screenshot");

		screenShotJFrame.setSize(400, 250);
		screenShotJFrame.setVisible(true);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		screenShotJFrame.setContentPane(contentPane);

		JPanel directoryPanel = new JPanel();
		directoryPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.add(directoryPanel, BorderLayout.NORTH);

		JTextField directoryTextField = new JTextField();
		directoryTextField.setPreferredSize(new Dimension(6, 20));
		directoryTextField.setToolTipText("");
		directoryTextField.setColumns(10);

		JButton directoryButton = new JButton("Save Directory");
		directoryButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.setDialogTitle("select a directory");
				int ret = jfc.showDialog(new JLabel(), null);
				switch (ret) {
				case JFileChooser.APPROVE_OPTION:
					File file = jfc.getSelectedFile();
					directoryTextField.setText(file.getAbsolutePath());
					break;

				default:
					break;
				}
				
				
			}
		});
		
		directoryPanel.setLayout(new BoxLayout(directoryPanel, BoxLayout.X_AXIS));
		directoryPanel.add(directoryTextField);
		directoryPanel.add(directoryButton);
		
		
		JPanel fileNamePanel = new JPanel();
		fileNamePanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.add(fileNamePanel, BorderLayout.CENTER);

		JTextField fileNameTextField = new JTextField();
		fileNameTextField.setPreferredSize(new Dimension(6, 20));
		fileNameTextField.setToolTipText("");
		fileNameTextField.setColumns(10);
		
		JButton fileNameButton = new JButton("File name");
		
		
		fileNamePanel.setLayout(new BoxLayout(fileNamePanel, BoxLayout.X_AXIS));
		fileNamePanel.add(fileNameTextField);
		fileNamePanel.add(fileNameButton);
		
		JPanel sceenshotPanel = new JPanel();
		sceenshotPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.add(sceenshotPanel, BorderLayout.SOUTH);
		
		JButton sceenshotButton = new JButton("Sceenshot");
		sceenshotButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				System.out.println("Sceenshot");
			}
		});
		sceenshotPanel.add(sceenshotButton);


		Utils.setJFrameCenterInScreen(screenShotJFrame);
	}

}
