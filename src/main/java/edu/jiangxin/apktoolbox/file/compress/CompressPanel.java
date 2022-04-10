package edu.jiangxin.apktoolbox.file.compress;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;

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

	public CompressPanel() {
		super();
		initUI();
	}

	private void initUI() {
		setLayout(new BorderLayout(0, 0));
		add(getTopPanel(), BorderLayout.NORTH);
		add(getMainPanel(), BorderLayout.CENTER);
	}

	private JPanel getWestPanel() {
		JPanel ret = new JPanel();
		ret.setLayout(new GridLayout(6, 1));

		JButton buttonZip = new JButton("打包并压缩文件成ZIP格式...");
		buttonZip.addActionListener(new ActionAdapter() {
			public void run() {
				onArchiverFile(new MyZip());
			}
		});
		ret.add(buttonZip);

		JButton buttonGZip = new JButton("压缩文件成GZIP格式...");
		buttonGZip.addActionListener(new ActionAdapter() {
			public void run() {
				onCompressFile(new MyGZip());
			}
		});
		ret.add(buttonGZip);

		JButton buttonTar = new JButton("打包文件成TAR格式...");
		buttonTar.addActionListener(new ActionAdapter() {
			public void run() {
				onArchiverFile(new MyTar());
			}
		});
		ret.add(buttonTar);

		JButton buttonBz2 = new JButton("压缩文件成BZIP2格式...");
		buttonBz2.addActionListener(new ActionAdapter());
		ret.add(buttonBz2);

		// JButton button7Zip = new JButton("打包并压缩文件成7ZIP格式...");
		// button7Zip.addActionListener(new ActionAdapter());
		// ret.add(button7Zip);
		//
		// JButton buttonRar = new JButton("打包并压缩文件成RAR格式...");
		// buttonRar.addActionListener(new ActionAdapter());
		// ret.add(buttonRar);

		JButton buttonCrackRar = new JButton("暴力破解rar文件密码...");
		buttonCrackRar.addActionListener(new ActionAdapter() {
			public void run() {
				crackRar();
			}
		});
		ret.add(buttonCrackRar);

		JButton buttonCrackZip = new JButton("暴力破解zip文件密码...");
		buttonCrackZip.addActionListener(new ActionAdapter() {
			public void run() {
				JOptionPane.showMessageDialog(CompressPanel.this, "暂未实现，敬请期待");
			}
		});
		ret.add(buttonCrackZip);

		return ret;
	}

	private JPanel getEastPanel() {
		JPanel ret = new JPanel();
		ret.setLayout(new GridLayout(6, 1));

		JButton buttonUpZip = new JButton("解压解包ZIP文件...");
		buttonUpZip.addActionListener(new ActionAdapter() {
			public void run() {
				onUnArchiverFile(new MyZip());
			}
		});
		ret.add(buttonUpZip);

		JButton buttonUnGZip = new JButton("解压GZIP文件...");
		buttonUnGZip.addActionListener(new ActionAdapter() {
			public void run() {
				onUnCompressFile(new MyGZip());
			}
		});
		ret.add(buttonUnGZip);

		JButton buttonUnTar = new JButton("解包TAR文件...");
		buttonUnTar.addActionListener(new ActionAdapter() {
			public void run() {
				onUnArchiverFile(new MyTar());
			}
		});
		ret.add(buttonUnTar);

		JButton buttonUnRar = new JButton("解压解包RAR文件...");
		buttonUnRar.addActionListener(new ActionAdapter() {
			public void run() {
				onUnArchiverFile(new MyRar());
			}
		});
		ret.add(buttonUnRar);

		JButton buttonUn7zip = new JButton("解压解包7ZIP文件...");
		buttonUn7zip.addActionListener(new ActionAdapter() {
		});
		ret.add(buttonUn7zip);

		JButton buttonUnBzip2 = new JButton("解压BZIP2文件...");
		buttonUnBzip2.addActionListener(new ActionAdapter() {
			public void run() {
				onUnCompressFile(new MyBZip2());
			}
		});
		ret.add(buttonUnBzip2);

		return ret;
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

	private void crackRar() {
		if(!MyRar.isReady()) {
			JOptionPane.showMessageDialog(this, "没有找到测试程序，无法破解rar文件！");
			return ;
		}
		MyRar rar = new MyRar();
		File f = getSelectedArchiverFile(rar.getFileFilter());
		if (f == null) {
			return;
		}
//		String pass = rar.crackRar(f, ".~tmp", new CodeIterator());
		String pass;
		try {
			long t = System.currentTimeMillis();
			pass = rar.crackRar(f, new CodeIterator());
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

	private JPanel getTopLeftPanel() {
		JPanel ret = new JPanel();

		JLabel tips = new JLabel("文件编码:");
		ret.add(tips);
		JRadioButton utf8 = new JRadioButton("UTF-8");
		ret.add(utf8);
		JRadioButton gbk = new JRadioButton("GBK");
		ret.add(gbk);

		ButtonGroup bg = new ButtonGroup();
		bg.add(utf8);
		bg.add(gbk);

		utf8.setSelected(true);

		gbk.setEnabled(false);

		return ret;
	}

	private JPanel getTopRightPanel() {
		JPanel ret = new JPanel();

		JRadioButton uncode = new JRadioButton("不加密");
		ret.add(uncode);
		JRadioButton encode = new JRadioButton("加密");
		ret.add(encode);

		ButtonGroup bg = new ButtonGroup();
		bg.add(uncode);
		bg.add(encode);

		uncode.setSelected(true);
		encode.setEnabled(false);

		return ret;
	}

	private JPanel getTopPanel() {
		JPanel ret = new JPanel();
		ret.setLayout(new GridLayout(1, 2));
		ret.add(getTopLeftPanel());
		ret.add(getTopRightPanel());
		return ret;
	}

	private JPanel getMainPanel() {
		JPanel ret = new JPanel();
		ret.setLayout(new GridLayout(1, 2));
		ret.add(getWestPanel());
		ret.add(getEastPanel());
		return ret;
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
