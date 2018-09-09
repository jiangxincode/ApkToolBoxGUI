package edu.jiangxin.apktoolbox.text;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import edu.jiangxin.apktoolbox.swing.extend.JAutoCompleteComboBox;
import edu.jiangxin.apktoolbox.swing.extend.JEasyFrame;
import edu.jiangxin.apktoolbox.text.core.EncoderConvert;
import edu.jiangxin.apktoolbox.text.core.EncoderDetector;
import edu.jiangxin.apktoolbox.text.core.FileFilterWrapper;
import edu.jiangxin.apktoolbox.utils.Constants;

public class EncodeConvertFrame extends JEasyFrame {
	private static final long serialVersionUID = 1L;

	private static final int CONTENT_WIDTH = 500;

	public EncodeConvertFrame() throws HeadlessException {
		super();
		setTitle("Character Encoding Convert");
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(Constants.BORDER, Constants.BORDER, Constants.BORDER, Constants.BORDER));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		setContentPane(contentPane);

		JPanel sourcePanel = new JPanel();
		sourcePanel.setPreferredSize(new Dimension(CONTENT_WIDTH, Constants.BUTTON_HGIGHT));
		sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.X_AXIS));
		contentPane.add(sourcePanel);
		contentPane.add(Box.createVerticalStrut(10));

		JPanel optionPanel = new JPanel();
		optionPanel.setPreferredSize(new Dimension(CONTENT_WIDTH, Constants.BUTTON_HGIGHT));
		optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));
		contentPane.add(optionPanel);
		contentPane.add(Box.createVerticalStrut(10));

		JPanel operationPanel = new JPanel();
		operationPanel.setPreferredSize(new Dimension(CONTENT_WIDTH, Constants.BUTTON_HGIGHT));
		operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
		contentPane.add(operationPanel);

		JTextField srcTextField = new JTextField();
		srcTextField.setText(conf.getString("encodeconvert.src.dir"));

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
		sourcePanel.add(Box.createHorizontalStrut(5));
		sourcePanel.add(srcButton);
		srcTextField.setAlignmentY(Component.CENTER_ALIGNMENT);
		srcButton.setAlignmentY(Component.CENTER_ALIGNMENT);

		JLabel suffixLabel = new JLabel("Suffix:");
		JTextField suffixTextField = new JTextField();
		suffixTextField.setText(conf.getString("encodeconvert.suffix"));
		suffixTextField.setPreferredSize(new Dimension(60, Constants.BUTTON_HGIGHT));
		suffixTextField.setMaximumSize(new Dimension(60, Constants.BUTTON_HGIGHT));

		Set<String> charsets = Charset.availableCharsets().keySet();

		JLabel fromLabel = new JLabel("From:");
		JAutoCompleteComboBox<String> fromComboBox = new JAutoCompleteComboBox<String>();
		fromComboBox.setPreferredSize(new Dimension(80, Constants.BUTTON_HGIGHT));
		fromComboBox.setEnabled(false);

		JCheckBox autoDetectCheckBox = new JCheckBox("Auto Detect");
		autoDetectCheckBox.setSelected(true);
		autoDetectCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					fromComboBox.setEnabled(false);
				} else {
					fromComboBox.setEnabled(true);
				}

			}
		});

		JLabel toLabel = new JLabel("To:");
		JAutoCompleteComboBox<String> toComboBox = new JAutoCompleteComboBox<String>();
		toComboBox.setPreferredSize(new Dimension(80, Constants.BUTTON_HGIGHT));
		toComboBox.setSelectedItem(conf.getString("encodeconvert.to"));

		for (String charset : charsets) {
			fromComboBox.addItem(charset);
			toComboBox.addItem(charset);
		}

		optionPanel.add(suffixLabel);
		optionPanel.add(Box.createHorizontalStrut(5));
		optionPanel.add(suffixTextField);
		optionPanel.add(Box.createHorizontalGlue());
		optionPanel.add(fromLabel);
		optionPanel.add(Box.createHorizontalStrut(5));
		optionPanel.add(fromComboBox);
		optionPanel.add(Box.createHorizontalGlue());
		optionPanel.add(autoDetectCheckBox);
		optionPanel.add(Box.createHorizontalGlue());
		optionPanel.add(toLabel);
		optionPanel.add(Box.createHorizontalStrut(5));
		optionPanel.add(toComboBox);

		JButton sceenshotButton = new JButton("Convert");
		sceenshotButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				File srcFile = new File(srcTextField.getText());
				if (!srcFile.exists() || !srcFile.isDirectory()) {
					logger.error("srcFile is invalid");
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(EncodeConvertFrame.this, "Source file is invalid", "ERROR",
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
				conf.setProperty("encodeconvert.src.dir", srcPath);
				conf.setProperty("encodeconvert.suffix", suffixTextField.getText());
				conf.setProperty("encodeconvert.to", toComboBox.getSelectedItem().toString());

				try {
					ArrayList<File> files = new ArrayList<File>();
					files.addAll(new FileFilterWrapper().list(srcPath, suffixTextField.getText()));
					for (File file : files) {
						logger.info("process: " + file.getCanonicalPath());
						String fromEncoder;
						if (autoDetectCheckBox.isSelected()) {
							fromEncoder = EncoderDetector.judgeFile(file.getCanonicalPath());
						} else {
							fromEncoder = fromComboBox.getSelectedItem().toString();
						}
						logger.info("from: " + fromEncoder);
						String toEncoder = toComboBox.getSelectedItem().toString();
						EncoderConvert.encodeFile(file.getCanonicalPath(), fromEncoder, toEncoder);
					}

					logger.info("convert finish");
				} catch (IOException e1) {
					logger.error("convert fail", e1);
				}
			}
		});

		operationPanel.add(sceenshotButton);
	}

}
