package edu.jiangxin.apktoolbox.i18n;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import edu.jiangxin.apktoolbox.swing.extend.JEasyFrame;

public class I18NAddFrame extends JEasyFrame {
	private static final long serialVersionUID = 1L;

	public I18NAddFrame() throws HeadlessException {
		super();
		setTitle(bundle.getString("i18n.add.title"));
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

				for (String targetPath : targetPaths) {
					for (String item : items) {
						int ret = innerProcessor(srcPath, targetPath, item);
						if (ret != 0) {
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(I18NAddFrame.this, "Failed, please see the log", "ERROR",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}
			}
		});

		operationPanel.add(sceenshotButton);
	}

	private static final String charset = "UTF-8";

	private static final boolean isRemoveLastLF = true;

	private static Map<String, String> replace = new HashedMap<String, String>();

	static {
		replace.put("&quot;", "jiangxin001");
		replace.put("&#160;", "jiangxin002");
	}

	private int innerProcessor(String sourceBaseStr, String targetBaseStr, String itemName) {
		if (StringUtils.isAnyEmpty(sourceBaseStr, targetBaseStr, itemName)) {
			logger.error("params are invalid: sourceBaseStr: " + sourceBaseStr + ", targetBaseStr: " + targetBaseStr
					+ ", itemName: " + itemName);
			return -1;
		}
		File sourceBaseFile = new File(sourceBaseStr);
		File targetBaseFile = new File(targetBaseStr);
		int count = 0;

		File[] sourceParentFiles = sourceBaseFile.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().startsWith("values");
			}
		});
		if (sourceParentFiles == null) {
			logger.error("sourceParentFiles is null");
			return -1;
		}
		for (File sourceParentFile : sourceParentFiles) {
			File sourceFile = new File(sourceParentFile, "strings.xml");
			if (!sourceFile.exists()) {
				logger.warn("sourceFile does not exist: " + getCanonicalPath(sourceFile));
				continue;
			}

			SAXBuilder builder = new SAXBuilder();
			Document sourceDoc = null;
			try {
				sourceDoc = builder.build(sourceFile);
				logger.info("build source document: " + getCanonicalPath(sourceFile));
			} catch (JDOMException | IOException e) {
				logger.error("build source document failed: " + getCanonicalPath(sourceFile), e);
				return -1;
			}
			Element sourceElement = null;
			for (Element sourceChild : sourceDoc.getRootElement().getChildren()) {
				String sourceValue = sourceChild.getAttributeValue("name");
				if (sourceValue != null && sourceValue.equals(itemName)) {
					sourceElement = sourceChild.clone();
					break;
				}
			}
			if (sourceElement == null) {
				logger.warn("sourceElement is null: " + getCanonicalPath(sourceFile));
				continue;
			}

			File targetFile = new File(new File(targetBaseFile, sourceParentFile.getName()), "strings.xml");
			if (!targetFile.exists()) {
				logger.warn("targetFile does not exist: " + getCanonicalPath(sourceFile));
				continue;
			}
			try {
				prePocess(targetFile);
			} catch (IOException e) {
				logger.error("prePocess failed.", e);
				return -1;
			}

			Document targetDoc;
			try {
				targetDoc = builder.build(targetFile);
				logger.info("build target document: " + getCanonicalPath(targetFile));
			} catch (JDOMException | IOException e) {
				logger.error("build target document failed: " + getCanonicalPath(targetFile), e);
				return -1;
			}
			Element targetRoot = targetDoc.getRootElement();
			boolean isFinished = false;

			for (Element targetChild : targetRoot.getChildren()) {
				String targetValue = targetChild.getAttributeValue("name");
				if (targetValue != null && targetValue.equals(itemName)) {
					targetChild.setText(sourceElement.getText());
					isFinished = true;
					break;
				}
			}

			if (!isFinished) {
				targetRoot.addContent("    ");
				targetRoot.addContent(sourceElement);
				targetRoot.addContent("\n");
			}

			XMLOutputter out = new XMLOutputter();
			Format format = Format.getRawFormat();
			format.setEncoding("UTF-8");
			format.setLineSeparator("\n");
			out.setFormat(format);
			try {
				out.output(targetDoc, new FileOutputStream(targetFile));
			} catch (IOException e) {
				logger.error("output fail", e);
				return -1;
			}
			try {
				postProcess(targetFile);
			} catch (IOException e) {
				logger.error("postProcess failed.", e);
				return -1;
			}
			logger.info("count: " + (++count) + ", in path: " + getCanonicalPath(sourceFile) + ", out path: "
					+ getCanonicalPath(targetFile));
		}
		logger.info("finish one cycle");
		return 0;

	}

	private static void prePocess(File file) throws IOException {
		String content = FileUtils.readFileToString(file, charset);
		for (Map.Entry<String, String> entry : replace.entrySet()) {
			content = content.replaceAll(entry.getKey(), entry.getValue());
		}
		FileUtils.writeStringToFile(file, content, charset);
	}

	private static void postProcess(File file) throws IOException {
		String content = FileUtils.readFileToString(file, charset);
		for (Map.Entry<String, String> entry : replace.entrySet()) {
			content = content.replaceAll(entry.getValue(), entry.getKey());
		}
		if (isRemoveLastLF) {
			content = StringUtils.removeEnd(content, "\n");
		}
		FileUtils.writeStringToFile(file, content, charset);
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

}
