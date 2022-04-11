package edu.jiangxin.apktoolbox.file.compress;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * 压缩解压zip文件的类
 * ref:https://doc.360qnw.com/web/#/p/2ad9e75ae0615dec5e016054cf905581
 * https://www.yunjiemi.net/Passper/index.html
 * 
 */
public final class CompressPanel extends EasyPanel {
	private JPanel optionPanel;

	private JPanel operationPanel;

	public CompressPanel() {
		super();
		initUI();
	}

	private void initUI() {
		BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(boxLayout);

		createOptionPanel();
		add(optionPanel);
		add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

		createOperationPanel();
		add(operationPanel);
	}

	private void createOptionPanel() {
		optionPanel = new JPanel();
		optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));

		JLabel tips = new JLabel("文件编码:");
		optionPanel.add(tips);
		optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

		JRadioButton utf8 = new JRadioButton("UTF-8");
		optionPanel.add(utf8);
		optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

		JRadioButton gbk = new JRadioButton("GBK");
		optionPanel.add(gbk);
		optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER * 2));

		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(utf8);
		buttonGroup1.add(gbk);
		utf8.setSelected(true);

		JRadioButton plainRadioButton = new JRadioButton("不加密");
		optionPanel.add(plainRadioButton);
		optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
		JRadioButton encryptRadioButton = new JRadioButton("加密");
		optionPanel.add(encryptRadioButton);

		ButtonGroup buttonGroup2 = new ButtonGroup();
		buttonGroup2.add(plainRadioButton);
		buttonGroup2.add(encryptRadioButton);
		plainRadioButton.setSelected(true);

	}

	private void createOperationPanel() {
		operationPanel = new JPanel();
		operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.Y_AXIS));

		JButton buttonZip = new JButton("打包并压缩文件成ZIP格式...");
		buttonZip.addActionListener(new ActionAdapter() {
			public void run() {
				onArchiverFile(new MyZip());
			}
		});
		operationPanel.add(buttonZip);

		JButton buttonGZip = new JButton("压缩文件成GZIP格式...");
		buttonGZip.addActionListener(new ActionAdapter() {
			public void run() {
				onCompressFile(new MyGZip());
			}
		});
		operationPanel.add(buttonGZip);

		JButton buttonTar = new JButton("打包文件成TAR格式...");
		buttonTar.addActionListener(new ActionAdapter() {
			public void run() {
				onArchiverFile(new MyTar());
			}
		});
		operationPanel.add(buttonTar);

		JButton buttonBz2 = new JButton("压缩文件成BZIP2格式...");
		buttonBz2.addActionListener(new ActionAdapter());
		operationPanel.add(buttonBz2);

		JButton button7Zip = new JButton("打包并压缩文件成7ZIP格式...");
		button7Zip.addActionListener(new ActionAdapter());
		operationPanel.add(button7Zip);

		JButton buttonRar = new JButton("打包并压缩文件成RAR格式...");
		buttonRar.addActionListener(new ActionAdapter());
		operationPanel.add(buttonRar);

		JButton buttonCrackRar = new JButton("暴力破解rar文件密码...");
		buttonCrackRar.addActionListener(new ActionAdapter() {
			public void run() {
				crack("rar");
			}
		});
		operationPanel.add(buttonCrackRar);

		JButton buttonCrackZip = new JButton("暴力破解zip文件密码...");
		buttonCrackZip.addActionListener(new ActionAdapter() {
			public void run() {
				JOptionPane.showMessageDialog(CompressPanel.this, "暂未实现，敬请期待");
			}
		});
		operationPanel.add(buttonCrackZip);

		JButton buttonUpZip = new JButton("解压解包ZIP文件...");
		buttonUpZip.addActionListener(new ActionAdapter() {
			public void run() {
				onUnArchiverFile(new MyZip());
			}
		});
		operationPanel.add(buttonUpZip);

		JButton buttonUnGZip = new JButton("解压GZIP文件...");
		buttonUnGZip.addActionListener(new ActionAdapter() {
			public void run() {
				onUnCompressFile(new MyGZip());
			}
		});
		operationPanel.add(buttonUnGZip);

		JButton buttonUnTar = new JButton("解包TAR文件...");
		buttonUnTar.addActionListener(new ActionAdapter() {
			public void run() {
				onUnArchiverFile(new MyTar());
			}
		});
		operationPanel.add(buttonUnTar);

		JButton buttonUnRar = new JButton("解压解包RAR文件...");
		buttonUnRar.addActionListener(new ActionAdapter() {
			public void run() {
				onUnArchiverFile(new MyRar());
			}
		});
		operationPanel.add(buttonUnRar);

		JButton buttonUn7zip = new JButton("解压解包7ZIP文件...");
		buttonUn7zip.addActionListener(new ActionAdapter() {
		});
		operationPanel.add(buttonUn7zip);

		JButton buttonUnBzip2 = new JButton("解压BZIP2文件...");
		buttonUnBzip2.addActionListener(new ActionAdapter() {
			public void run() {
				onUnCompressFile(new MyBZip2());
			}
		});
		operationPanel.add(buttonUnBzip2);
	}

	private File getSelectedArchiverFile(FileNameExtensionFilter filter) {
		JFileChooser o = new JFileChooser(".");
		o.setFileSelectionMode(JFileChooser.FILES_ONLY);
		o.setMultiSelectionEnabled(false);
		o.addChoosableFileFilter(filter);
		int returnVal = o.showOpenDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		return o.getSelectedFile();
	}

	private void crack(String type) {
		Cracker cracker = null;
		if ("rar".equals(type)) {
			cracker = new MyRar();
		}
		if(!cracker.isCrackerReady()) {
			JOptionPane.showMessageDialog(this, "没有找到测试程序，无法破解rar文件！");
			return ;
		}
		File f = getSelectedArchiverFile(new MyRar().getFileFilter());
		if (f == null) {
			return;
		}
		String pass;
		try {
			long t = System.currentTimeMillis();
			pass = cracker.crack(f, new CodeIterator());
			t = System.currentTimeMillis() - t;
			System.out.println(t);

			if (pass == null) {
				JOptionPane.showMessageDialog(this, "指定的密码无法解开文件!");
			} else {
				JOptionPane.showMessageDialog(this, pass);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "破解过程中出错!");
		}
	}

	private void onUnArchiverFile(Archiver ma) {
		File f = getSelectedArchiverFile(ma.getFileFilter());
		if (f == null) {
			return;
		}
		JFileChooser s = new JFileChooser(".");
		s.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = s.showSaveDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}
		String filepath = s.getSelectedFile().getAbsolutePath();

		String password = null;
		while (true) {
			try {
				ma.doUnArchiver(f, filepath, password);
				break;
			} catch (WrongPassException re) {
				password = JOptionPane.showInputDialog(this,
						"压缩文件疑似已加密，请输入解压密码");
				if (password == null) {
					return;
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				break;
			}
		}
	}

	private void onUnCompressFile(Compressor ma) {
		File file = getSelectedArchiverFile(ma.getFileFilter());
		if (file == null) {
			return;
		}
		String fn = file.getName();
		fn = fn.substring(0, fn.lastIndexOf('.'));
		JFileChooser s = new JFileChooser(".");
		s.setSelectedFile(new File(fn));
		s.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = s.showSaveDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}
		String filepath = s.getSelectedFile().getAbsolutePath();

		try {
			ma.doUnCompress(file, filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void onCompressFile(Compressor c) {
		File f = getSelectedArchiverFile(null);
		if (f == null) {
			return;
		}
		FileNameExtensionFilter filter = c.getFileFilter();
		String ext = "." + filter.getExtensions()[0];
		String destpath = f.getName() + ext;
		JFileChooser s = new JFileChooser(".");
		s.addChoosableFileFilter(filter);
		s.setSelectedFile(new File(destpath));
		int returnVal = s.showSaveDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File ff = s.getSelectedFile();
		destpath = ff.getAbsolutePath();
		if (!filter.accept(ff)) {// 确保一定有后缀
			destpath += ext;
		}

		try {
			c.doCompress(f, destpath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void onArchiverFile(Archiver ma) {
		JFileChooser o = new JFileChooser(".");
		o.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		o.setMultiSelectionEnabled(true);
		int returnVal = o.showOpenDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File[] files = o.getSelectedFiles();

		JFileChooser s = new JFileChooser(".");
		FileNameExtensionFilter filter = ma.getFileFilter();
		s.addChoosableFileFilter(filter);
		returnVal = s.showSaveDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File f = s.getSelectedFile();
		String filepath = f.getAbsolutePath();
		if (!filter.accept(f)) {// 确保一定有后缀
			filepath = filepath + "." + filter.getExtensions()[0];
		}

		try {
			ma.doArchiver(files, filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class ActionAdapter implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			run();
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		public void run() {
			JOptionPane.showMessageDialog(CompressPanel.this, "暂未实现，敬请期待");
		}
	}

}
