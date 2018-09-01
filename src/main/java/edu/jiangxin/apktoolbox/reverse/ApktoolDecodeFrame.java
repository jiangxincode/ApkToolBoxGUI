package edu.jiangxin.apktoolbox.reverse;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.jiangxin.apktoolbox.utils.StreamHandler;
import edu.jiangxin.apktoolbox.utils.Utils;

public class ApktoolDecodeFrame extends JFrame {

	private static final Logger logger = LogManager.getLogger(ApktoolDecodeFrame.class);

	private static final long serialVersionUID = 1L;

	private Configuration conf = Utils.getConfiguration();

	public ApktoolDecodeFrame() throws HeadlessException {
		super();
		setTitle("Apktool Decode");
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
		srcTextField.setText(conf.getString("default.apktool.decode.src.file"));

		JButton srcButton = new JButton("Source File");
		srcButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setDialogTitle("select a file");
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
		targetTextField.setText(conf.getString("default.apktool.decode.target.dir"));

		JButton targetButton = new JButton("Save Dir");
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

		JPanel optionPanel = new JPanel();
		optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));
		contentPane.add(optionPanel);

		JCheckBox resouceIgnore = new JCheckBox("Ignore res");
		resouceIgnore.setSelected(false);
		optionPanel.add(resouceIgnore);
		
		JCheckBox override = new JCheckBox("Override");
		override.setSelected(true);
		optionPanel.add(override);

		JPanel operationPanel = new JPanel();
		operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
		contentPane.add(operationPanel);

		JButton sceenshotButton = new JButton("Decode");
		sceenshotButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				File srcFile = new File(srcTextField.getText());
				if (!srcFile.exists() || !srcFile.isFile()) {
					logger.error("srcFile is invalid");
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(ApktoolDecodeFrame.this, "Source file is invalid", "ERROR",
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
				conf.setProperty("default.apktool.decode.src.file", srcPath);
				File targetFile = new File(targetTextField.getText());
				if (!targetFile.exists() || !targetFile.isDirectory()) {
					logger.error("targetFile is invalid");
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(ApktoolDecodeFrame.this, "Target directory is invalid", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					targetTextField.requestFocus();
					return;
				}
				String targetPath;
				try {
					targetPath = targetFile.getCanonicalPath();
				} catch (IOException e2) {
					logger.error("getCanonicalPath fail");
					return;
				}
				conf.setProperty("default.apktool.decode.target.dir", targetPath);
				try {
					StringBuilder sb = new StringBuilder();
					sb.append("java -jar \"-Duser.language=en\" \"-Dfile.encoding=UTF8\"")
							.append(" \"").append(Utils.getToolsPath()).append(File.separator).append("apktool_2.3.3.jar\"")
							.append(" d ").append(srcPath)
							.append(" -o ").append(targetPath).append(File.separator)
							.append(FilenameUtils.getBaseName(srcFile.getCanonicalPath()));
					if (resouceIgnore.isSelected()) {
						sb.append(" -r");
					}
					if (override.isSelected()) {
						sb.append(" -f");
					}
					String cmd = sb.toString();
					logger.info(cmd);
					Process process = Runtime.getRuntime().exec(cmd);
					new StreamHandler(process.getInputStream(), 0).start();
					new StreamHandler(process.getErrorStream(), 1).start();
				} catch (IOException e1) {
					logger.error("decode fail", e);
				}
			}
		});

		operationPanel.add(sceenshotButton);
	}

}
