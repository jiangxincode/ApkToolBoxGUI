package edu.jiangxin.apktoolbox.i18n;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;

import edu.jiangxin.apktoolbox.i18n.core.I18NUtils;
import edu.jiangxin.apktoolbox.utils.Utils;

public class I18NAddFrame extends JFrame {
	private static final Logger logger = LogManager.getLogger(I18NAddFrame.class);

	private static final long serialVersionUID = 1L;

	private Configuration conf = Utils.getConfiguration();

	public I18NAddFrame() throws HeadlessException {
		super();
		setTitle("I18N Add or Replace");
		setSize(600, 160);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		BoxLayout boxLayout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
		contentPane.setLayout(boxLayout);
		setContentPane(contentPane);

		JPanel sourcePanel = new JPanel();
		sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.X_AXIS));
		contentPane.add(sourcePanel);

		JTextField srcTextField = new JTextField();
		srcTextField.setText(conf.getString("i18n.add.src.dir"));

		JButton srcButton = new JButton("Source Directory");
		srcButton.addMouseListener(new MouseAdapter() {
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
					srcTextField.setText(file.getAbsolutePath());
					break;
				default:
					break;
				}

			}
		});

		sourcePanel.add(srcTextField);
		sourcePanel.add(srcButton);

		JPanel targetPanel = new JPanel();
		targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.X_AXIS));
		contentPane.add(targetPanel);

		JTextField targetTextField = new JTextField();
		targetTextField.setText(conf.getString("i18n.add.target.dir"));

		JButton targetButton = new JButton("Save Directory");
		targetButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.setDialogTitle("save to");
				int ret = jfc.showDialog(new JLabel(), null);
				switch (ret) {
				case JFileChooser.APPROVE_OPTION:
					File file = jfc.getSelectedFile();
					targetTextField.setText(file.getAbsolutePath());
					break;

				default:
					break;
				}

			}
		});

		targetPanel.add(targetTextField);
		targetPanel.add(targetButton);

		JPanel itemPanel = new JPanel();
		itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
		contentPane.add(itemPanel);

		JTextField itemTextField = new JTextField();
		itemTextField.setText(conf.getString("i18n.add.items"));

		JLabel itemLabel = new JLabel("Items");

		itemPanel.add(itemTextField);
		itemPanel.add(itemLabel);

		JPanel operationPanel = new JPanel();
		operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
		contentPane.add(operationPanel);

		JButton sceenshotButton = new JButton("Add/Replace");
		sceenshotButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				File srcFile = new File(srcTextField.getText());
				if (!srcFile.exists() || !srcFile.isDirectory()) {
					logger.error("srcFile is invalid");
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(I18NAddFrame.this, "Source directory is invalid", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					srcTextField.requestFocus();
					return;
				}
				String srcPath;
				try {
					srcPath = srcFile.getCanonicalPath();
				} catch (IOException e2) {
					logger.error("getCanonicalPath fail");
					return;
				}
				conf.setProperty("i18n.add.src.dir", srcPath);

				List<File> targetFiles = new ArrayList<>();
				List<String> targetPaths = new ArrayList<>();
				String[] tmps = targetTextField.getText().split(";");
				for (String tmp : tmps) {
					File targetFile = new File(tmp);
					if (!targetFile.exists() || !targetFile.isDirectory()) {
						logger.error("targetFile is invalid");
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(I18NAddFrame.this, "Target directory is invalid", "ERROR",
								JOptionPane.ERROR_MESSAGE);
						targetTextField.requestFocus();
						return;
					}
					targetFiles.add(targetFile);
					try {
						targetPaths.add(targetFile.getCanonicalPath());
					} catch (IOException e1) {
						logger.error("getCanonicalPath fail");
						return;
					}
				}
				conf.setProperty("i18n.add.target.dir", targetTextField.getText());

				List<String> items = new ArrayList<>();
				tmps = itemTextField.getText().split(";");
				for (String tmp : tmps) {
					items.add(tmp);
				}
				conf.setProperty("i18n.add.items", itemTextField.getText());

				try {
					for (String targetPath : targetPaths) {
						for (String item : items) {
							I18NUtils.innerProcessor(srcPath, targetPath, item);
						}
					}
				} catch (IOException | JDOMException e1) {
					logger.error("i18n fail", e);
				}
			}
		});

		operationPanel.add(sceenshotButton);
	}

}
