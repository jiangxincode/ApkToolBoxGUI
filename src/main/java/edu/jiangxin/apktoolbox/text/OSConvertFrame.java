package edu.jiangxin.apktoolbox.text;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import edu.jiangxin.apktoolbox.reverse.ApktoolDecodeFrame;
import edu.jiangxin.apktoolbox.text.core.FileFilterWrapper;
import edu.jiangxin.apktoolbox.text.core.OSPatternConvert;
import edu.jiangxin.apktoolbox.utils.Utils;

public class OSConvertFrame extends JFrame {
	private static final Logger logger = LogManager.getLogger(ApktoolDecodeFrame.class);

	private static final long serialVersionUID = 1L;

	private Configuration conf = Utils.getConfiguration();

	public OSConvertFrame() throws HeadlessException {
		super();
		setTitle("OS Convert");
		setSize(600, 120);
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
		srcTextField.setText(conf.getString("osconvert.src.dir"));

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

		JPanel optionPanel = new JPanel();
		optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));
		contentPane.add(optionPanel);

		JLabel suffixLabel = new JLabel("suffix");
		JTextField suffixTextField = new JTextField();
		suffixTextField.setText(conf.getString("osconvert.suffix"));
		optionPanel.add(suffixLabel);
		optionPanel.add(suffixTextField);

		JLabel typeLabel = new JLabel("Type:");
		JComboBox<String> typeComboBox = new JComboBox<String>();
		typeComboBox.addItem("tounix");
		typeComboBox.addItem("tomac");
		typeComboBox.addItem("towindows");
		
		optionPanel.add(typeLabel);
		optionPanel.add(typeComboBox);

		JPanel operationPanel = new JPanel();
		operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
		contentPane.add(operationPanel);

		JButton sceenshotButton = new JButton("Convert");
		sceenshotButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				File srcFile = new File(srcTextField.getText());
				if (!srcFile.exists() || !srcFile.isDirectory()) {
					logger.error("srcFile is invalid");
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(OSConvertFrame.this, "Source file is invalid", "ERROR",
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
				conf.setProperty("osconvert.src.dir", srcPath);
				conf.setProperty("osconvert.suffix", suffixTextField.getText());
				try {
					ArrayList<File> files = new ArrayList<File>();
					files.addAll(new FileFilterWrapper().list(srcPath, suffixTextField.getText()));
					OSPatternConvert.osConvertFiles(files, typeComboBox.getSelectedItem().toString());
					logger.info("convert finish");
				} catch (IOException e1) {
					logger.error("convert fail", e);
				}
			}
		});

		operationPanel.add(sceenshotButton);
	}

}
