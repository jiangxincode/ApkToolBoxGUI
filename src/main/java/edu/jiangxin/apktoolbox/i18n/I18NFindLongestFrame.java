package edu.jiangxin.apktoolbox.i18n;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import edu.jiangxin.apktoolbox.swing.extend.JEasyFrame;

public class I18NFindLongestFrame extends JEasyFrame {
	private static final long serialVersionUID = 1L;

	List<I18NInfo> infos = new ArrayList<I18NInfo>();

	public I18NFindLongestFrame() throws HeadlessException {
		super();
		setTitle(bundle.getString("i18n.longest.title"));
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
		srcTextField.setText(conf.getString("i18n.longest.src.dir"));

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

		JPanel itemPanel = new JPanel();
		itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
		contentPane.add(itemPanel);

		JTextField itemTextField = new JTextField();
		itemTextField.setText(conf.getString("i18n.longest.items"));

		JLabel itemLabel = new JLabel("Items");

		itemPanel.add(itemTextField);
		itemPanel.add(itemLabel);

		JPanel operationPanel = new JPanel();
		operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
		contentPane.add(operationPanel);

		JButton sceenshotButton = new JButton(bundle.getString("i18n.longest.find"));
		sceenshotButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				infos.clear();
				File srcFile = new File(srcTextField.getText());
				if (!srcFile.exists() || !srcFile.isDirectory()) {
					logger.error("srcFile is invalid");
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(I18NFindLongestFrame.this, "Source directory is invalid", "ERROR",
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
				conf.setProperty("i18n.longest.src.dir", srcPath);

				String item = itemTextField.getText();
				if (StringUtils.isEmpty(item)) {
					logger.error("item is empty");
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(I18NFindLongestFrame.this, "item is empty", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					itemTextField.requestFocus();
					return;
				}

				conf.setProperty("i18n.longest.items", item);
				int ret = findLongest(srcPath, itemTextField.getText());
				if (ret != 0 || CollectionUtils.isEmpty(infos)) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(I18NFindLongestFrame.this, "Failed, please see the log", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				} else {
					I18NInfo info = infos.get(0);
					StringBuilder sb = new StringBuilder();
					sb.append("length: ").append(info.length).append(System.getProperty("line.separator"))
							.append("text: ").append(info.text).append(System.getProperty("line.separator"))
							.append("path: ").append(info.path);
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(I18NFindLongestFrame.this, sb.toString(), "INFO",
							JOptionPane.INFORMATION_MESSAGE);
				}

			}
		});

		operationPanel.add(sceenshotButton);
	}

	private String getCanonicalPath(File file) {
		if (file == null) {
			logger.error("file is null");
			return null;
		}
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			logger.error("getCanonicalPath failed: " + file.getAbsolutePath(), e);
			return null;
		}
	}

	private int findLongest(String sourceBaseStr, String itemName) {
		File sourceBaseFile = new File(sourceBaseStr);

		for (File sourceParentFile : sourceBaseFile.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.getName().startsWith("values");
			}
		})) {
			File sourceFile = new File(sourceParentFile, "strings.xml");
			if (sourceFile.exists()) {

				SAXBuilder builder = new SAXBuilder();
				Document sourceDoc;
				try {
					sourceDoc = builder.build(sourceFile);
				} catch (JDOMException | IOException e) {
					logger.error("build failed", e);
					return -1;
				}
				Element sourceRoot = sourceDoc.getRootElement();
				for (Element child : sourceRoot.getChildren()) {
					String value = child.getAttributeValue("name");
					if (value != null && value.equals(itemName)) {
						String text = child.getText();
						if (text != null) {
							I18NInfo info = new I18NInfo(getCanonicalPath(sourceFile), text, text.length());
							infos.add(info);
							break;
						}
					}
				}

			}

		}
		Collections.sort(infos, new Comparator<I18NInfo>() {
			public int compare(I18NInfo o1, I18NInfo o2) {
				return o2.length - o1.length;
			}
		});

		for (I18NInfo info : infos) {
			logger.info("[length: " + info.length + ", text: " + info.text + ", path: " + info.path + "]");
		}
		return 0;
	}

}

class I18NInfo {
	String path;
	String text;
	int length;

	public I18NInfo(String path, String text, int length) {
		this.path = path;
		this.text = text;
		this.length = length;
	}

}
