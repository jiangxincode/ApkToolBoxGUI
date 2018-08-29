package edu.jiangxin.apktoolbox.monkey;

import java.awt.Container;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.jiangxin.apktoolbox.utils.Utils;

public class MonkeyFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	Logger logger = LogManager.getLogger(MonkeyFrame.class);

	Thread threadTimeType = null;
	Thread threadMonkey = null;

	JPanel panel = new JPanel();
	JFileChooser fileChooser = new JFileChooser();
	JButton logPathButton = new JButton("选择路径");
	JButton excuteButton = new JButton("运行命令");
	JButton refreshButton = new JButton("刷新");
	JButton resetButton = new JButton("重置页面");
	JButton interruptButton = new JButton("终止运行");
	JButton exitButton = new JButton("退出运行");
	JLabel labelDevices = new JLabel("设备列表");
	JLabel labelProgram = new JLabel("应用程序");
	JLabel labelSpace = new JLabel("事件间隔");
	JLabel labelMillisecond = new JLabel("毫秒");
	JLabel labelTime = new JLabel("运行时长");
	JLabel labelLogLevel = new JLabel("日志级别");
	JLabel labelRestrain = new JLabel("约束条件");
	JLabel labelHour = new JLabel();
	JLabel labelMinute = new JLabel();
	JLabel labelSecond = new JLabel();
	JTextField textMillisecond = new JTextField(30);
	JTextField textTime = new JTextField(30);
	JTextField textLogPath = new JTextField(90);
	JComboBox comboBoxDevices = new JComboBox();
	JComboBox comboBoxProgram = new JComboBox();
	JComboBox comboBoxTime = new JComboBox();
	// --dbg-no-events：初始化启动的activity，但是不产生任何事件
	JCheckBox checkBoxDbgNoEvents = new JCheckBox("初始化启动的activity，但是不产生任何事件");
	// --hprof：指定该项后在事件序列发送前后会立即生成分析报告（一般建议指定该项）
	JCheckBox checkBoxHprof = new JCheckBox("在事件序列发送前后会立即生成分析报告");
	// --ignore-crashes：忽略崩溃
	JCheckBox checkBoxCrashes = new JCheckBox("忽略崩溃", true);
	// --ignore-timeouts：忽略超时
	JCheckBox checkBoxTimeouts = new JCheckBox("忽略超时", true);
	// --monitor-native-crashes：跟踪本地方法的崩溃问题
	JCheckBox checkBoxNativeCrashes = new JCheckBox("跟踪本地方法的崩溃问题", true);
	// --ignore-security-exceptions：忽略安全异常
	JCheckBox checkBoxExceptions = new JCheckBox("忽略安全异常", true);
	// --kill-process-after-error：发生错误后直接杀掉进程
	JCheckBox checkBoxKill = new JCheckBox("发生错误后直接杀掉进程");
	// --wait-dbg：知道连接了调试器才执行monkey测试
	JCheckBox checkBoxWaitDbg = new JCheckBox("停止Monkey执行，直到有调试器与其连接");

	ButtonGroup group = new ButtonGroup();
	JRadioButton radioButton0 = new JRadioButton("基本信息");// 缺省值
	JRadioButton radioButton1 = new JRadioButton("比较详细");// 比较详细
	JRadioButton radioButton2 = new JRadioButton("非常详细", true);// 非常详细//默认选中

	String dbgNoEvents = "";
	String hprof = "";
	String ignoreCrashes = "";
	String ignoreTimeouts = "";
	String monitorNativeCrashes = "";
	String ignoreSecurityExceptions = "";
	String killProcessAfterError = "";
	String waitDbg = "";

	String logLevel = "";
	ArrayList<String> list;
	ArrayList<String> arrayList = new ArrayList<String>();
	int flag = 0;
	//	String logFolder = "";
	String[] monkeyCmd = null;

	public MonkeyFrame() {
		super();
		setTitle("Monkey Test");
		add(panel, "Center");
		setSize(700, 400);
		setResizable(false);

		panel.setLayout(null);
		panel.add(labelDevices);
		panel.add(comboBoxDevices);
		panel.add(refreshButton);

		panel.add(labelProgram);
		panel.add(comboBoxProgram);

		panel.add(labelRestrain);
		panel.add(checkBoxCrashes);
		panel.add(checkBoxTimeouts);
		panel.add(checkBoxExceptions);
		panel.add(checkBoxNativeCrashes);
		panel.add(checkBoxKill);
		panel.add(checkBoxWaitDbg);
		panel.add(checkBoxHprof);

		panel.add(labelSpace);
		panel.add(labelHour);// 时
		panel.add(labelMinute);// 分
		panel.add(labelSecond);// 秒
		panel.add(textMillisecond);
		panel.add(labelMillisecond);

		panel.add(labelTime);
		panel.add(textTime);
		panel.add(comboBoxTime);

		panel.add(labelLogLevel);
		panel.add(radioButton0);
		panel.add(radioButton1);
		panel.add(radioButton2);

		panel.add(logPathButton);
		panel.add(textLogPath);

		panel.add(excuteButton);
		panel.add(resetButton);
		panel.add(interruptButton);
		panel.add(exitButton);

		labelDevices.setBounds(30, 25, 60, 20);
		comboBoxDevices.setBounds(90, 25, 240, 20);
		refreshButton.setBounds(340, 25, 60, 20);

		labelProgram.setBounds(30, 55, 60, 20);
		comboBoxProgram.setBounds(90, 55, 240, 20);
		comboBoxProgram.setEditable(true);
		comboBoxProgram.setSelectedItem("com.shinow.*");
		labelRestrain.setBounds(30, 85, 56, 20);

		checkBoxCrashes.setBounds(86, 85, 100, 20);
		checkBoxTimeouts.setBounds(186, 85, 100, 20);
		checkBoxExceptions.setBounds(286, 85, 110, 20);
		checkBoxNativeCrashes.setBounds(400, 85, 180, 20);
		checkBoxKill.setBounds(86, 115, 180, 20);
		checkBoxWaitDbg.setBounds(286, 115, 280, 20);
		checkBoxHprof.setBounds(86, 145, 280, 20);

		labelSpace.setBounds(30, 175, 60, 20);
		textMillisecond.setBounds(90, 175, 150, 20);
		textMillisecond.setDocument(new NumberLenghtLimitedDmt(7));
		textMillisecond.setText("500");
		labelMillisecond.setBounds(245, 175, 40, 20);

		labelTime.setBounds(30, 205, 60, 20);
		textTime.setBounds(90, 205, 150, 20);
		textTime.setDocument(new NumberLenghtLimitedDmt(7));
		textTime.setText("1");
		comboBoxTime.setBounds(245, 205, 50, 20);
		comboBoxTime.addItem(Common.Hour);
		comboBoxTime.addItem(Common.Minute);
		comboBoxTime.addItem(Common.Second);

		labelHour.setBounds(405, 205, 50, 20);
		labelMinute.setBounds(475, 205, 50, 20);
		labelSecond.setBounds(545, 205, 50, 20);

		labelLogLevel.setBounds(30, 235, 60, 20);

		group.add(radioButton0);
		group.add(radioButton1);
		group.add(radioButton2);

		radioButton0.setBounds(86, 235, 80, 20);
		radioButton1.setBounds(186, 235, 80, 20);
		radioButton2.setBounds(286, 235, 80, 20);

		logPathButton.setBounds(30, 265, 100, 20);
		textLogPath.setBounds(140, 265, 320, 20);
		textLogPath.setEditable(false);
		textLogPath.setText("D:");

		excuteButton.setBounds(30, 305, 100, 20);
		resetButton.setBounds(140, 305, 100, 20);
		interruptButton.setBounds(250, 305, 100, 20);
		exitButton.setBounds(360, 305, 100, 20);

		interruptButton.setEnabled(false);

		registListener();

	}

	/**
	 * 运行
	 */
	private void registListener() {

		logPathButton.addActionListener(new ActionListener() {

			/**
			 * 弹出窗体，选择文件夹
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == logPathButton) {
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int intRetVal = fileChooser.showOpenDialog(new Label());
					if (intRetVal == JFileChooser.APPROVE_OPTION) {
						textLogPath.setText(fileChooser.getSelectedFile().getPath());
					}
				}

			}
		});

		/**
		 * 刷新按钮点击时间监听
		 */
		refreshButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// 从项列表中移除所有项
				comboBoxDevices.removeAllItems();

				// 执行Dos命令获取设备列表
				getDevices(Common.CMD_Devices);

			}

		});

		/**
		 * 下拉框点击事件监听
		 */
		comboBoxDevices.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// 从项列表中移除所有项
				comboBoxProgram.removeAllItems();
				comboBoxProgram.setSelectedItem("com.shinow.*");
				// 选择设备，自动加载对应的应用程序列表，并在应用程序下拉框中显示
				getProgram(Common.CMD_Program_A + comboBoxDevices.getSelectedItem() + Common.CMD_Program_B);

			}

		});

		/**
		 *运行命令监听
		 */
		excuteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// 获取约束条件
				if (checkBoxCrashes.isSelected()) {
					ignoreCrashes = Common.Ignore_Crashes;
				}
				if (checkBoxTimeouts.isSelected()) {
					ignoreTimeouts = Common.Ignore_Timeouts;
				}
				if (checkBoxNativeCrashes.isSelected()) {
					monitorNativeCrashes = Common.Monitor_Native_Crashes;
				}
				if (checkBoxExceptions.isSelected()) {
					ignoreSecurityExceptions = Common.Ignore_Security_Exceptions;
				}
				if (checkBoxHprof.isSelected()) {
					hprof = Common.Hprof;
				}
				if (checkBoxKill.isSelected()) {
					killProcessAfterError = Common.Kill_Process_After_Error;
				}
				if (checkBoxWaitDbg.isSelected()) {
					waitDbg = Common.Wait_Dbg;
				}

				//自定义参数，是否能够执行Monkey？[0不可以][1可以]
				int start = 0;

				// 判断设备列表和应用程序是否为空
				if (comboBoxDevices.getItemCount() == 0) {
					new Dialog2(Common.Msg4).dialog2.setVisible(true);// 弹出对话框
					refreshButton.requestFocus();
				} else {
					if (comboBoxProgram.getItemCount() == 0) {
						new Dialog2(Common.Msg5).dialog2.setVisible(true);// 弹出对话框
					} else {
						// 判断事件间隔和运行时长是否为空
						if (textMillisecond.getText().length() == 0) {
							new Dialog2(Common.Msg2).dialog2.setVisible(true);// 弹出对话框
							textMillisecond.requestFocus();
						} else {
							if (textTime.getText().length() == 0) {
								new Dialog2(Common.Msg2).dialog2.setVisible(true);// 弹出对话框
								textTime.requestFocus();
							} else {
								if (textLogPath.getText().length() == 0) {
									new Dialog2(Common.Msg6).dialog2.setVisible(true);// 弹出对话框
									logPathButton.requestFocus();
								} else {
									start = 1;
								}
							}
						}
					}
				}

				// 获取日志级别
				if (radioButton0.isSelected()) {
					logLevel = Common.Level_0;
				}
				if (radioButton1.isSelected()) {
					logLevel = Common.Level_1;
				}
				if (radioButton2.isSelected()) {
					logLevel = Common.Level_2;
				}

				if (start == 1) {

					// 日志文件路径
					String logFile = textLogPath.getText() + "\\" + Common.Log_name + Utils.getCurrentDateString() + ".txt";

					//					logFolder = textLogPath.getText();

					//textLogPath.setText(logFile);

					// Dos执行命令
					monkeyCmd = new String[] { "cmd.exe", "/C",
							"adb -s " + comboBoxDevices.getSelectedItem() + " shell monkey -p "
									+ comboBoxProgram.getSelectedItem() + "" + ignoreCrashes + "" + ignoreTimeouts + ""
									+ ignoreSecurityExceptions + "" + monitorNativeCrashes + " --throttle "
									+ textMillisecond.getText() + "" + logLevel + "" + Common.TransactionCount + " > "
									+ logFile };

					logger.info("Monkey:" + monkeyCmd[2]);

					flag = 0;

					// 执行
					// new Thread(new CmdThread(monkeyCmd)).start();
					threadMonkey = new Thread(new ThreadExcuteMonkey(monkeyCmd));
					threadMonkey.start();

					excuteButton.setEnabled(false);
					resetButton.setEnabled(false);
					interruptButton.setEnabled(true);
					exitButton.setEnabled(false);

					//倒计时文本
					labelHour.setVisible(true);
					labelMinute.setVisible(true);
					labelSecond.setVisible(true);

					// 获取时间类型
					String timeType = (String) comboBoxTime.getSelectedItem();

					long time = Long.parseLong(textTime.getText());

					if (timeType == Common.Hour) {
						// new Thread(new MyThread(time * 3600)).start();
						threadTimeType = new Thread(new ThreadExcuteTime(time * 3600));
						threadTimeType.start();
					}
					if (timeType == Common.Minute) {
						// new Thread(new MyThread(time * 60)).start();
						threadTimeType = new Thread(new ThreadExcuteTime(time * 60));
						threadTimeType.start();
					}
					if (timeType == Common.Second) {
						// new Thread(new MyThread(time)).start();
						threadTimeType = new Thread(new ThreadExcuteTime(time));
						threadTimeType.start();
					}

				}

			}

		});

		/**
		 * 重置页面监听
		 */
		resetButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// 重置约束条件
				checkBoxCrashes.setSelected(true);
				checkBoxTimeouts.setSelected(true);
				checkBoxExceptions.setSelected(true);
				checkBoxNativeCrashes.setSelected(true);

				// 重置日志级别
				radioButton0.setSelected(false);
				radioButton1.setSelected(false);
				radioButton2.setSelected(true);

				// 重置事件间隔
				textMillisecond.setText("500");

				// 重置运行时长
				textTime.setText("1");

			}

		});

		/**
		 * 中断操作监听
		 */
		interruptButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// 杀掉Monkey执行进程
				interruptThread();

				excuteButton.setEnabled(true);
				resetButton.setEnabled(true);
				interruptButton.setEnabled(false);
				exitButton.setEnabled(true);

				System.gc();

			}

		});

		/**
		 * 退出
		 */

		exitButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				logger.info("已退出系统（Exit）");

				System.exit(0);

				//如果虚拟机因崩溃等问题造成关闭，进程中会少2条adb.exe

			}

		});

		/**
		 * 是否忽略崩溃
		 */
		checkBoxCrashes.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED)

					ignoreCrashes = Common.Ignore_Crashes;

				else

					ignoreCrashes = "";

			}

		});

		/**
		 * 是否忽略超时
		 */
		checkBoxTimeouts.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED)

					ignoreTimeouts = Common.Ignore_Timeouts;

				else

					ignoreTimeouts = "";

			}

		});

		/**
		 * 是否跟踪本地方法的崩溃问题
		 */
		checkBoxExceptions.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED)

					ignoreSecurityExceptions = Common.Ignore_Security_Exceptions;

				else

					ignoreSecurityExceptions = "";

			}

		});

		/**
		 * 是否忽略安全异常
		 */
		checkBoxNativeCrashes.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED)

					monitorNativeCrashes = Common.Monitor_Native_Crashes;

				else

					monitorNativeCrashes = "";

			}

		});

	}

	/**
	 * 中断Monkey命令
	 */
	public void interruptThread() {

		logger.info("中断Monkey命令--开始");

		String[] cmd1 = new String[] { "cmd.exe", "/c",
				Common.CMD_PS_A + comboBoxDevices.getSelectedItem() + Common.CMD_PS_B };
		excuteCommand(cmd1, Common.Monkey);

		List<String> listPid = list;
		logger.info("获取的中断Monkey进程数量：" + listPid.size());

		String[] cmd2 = null;
		String pid = "";
		for (int i = 0; i < listPid.size(); i++) {
			pid = listPid.get(i);
			cmd2 = new String[] { "cmd.exe", "/c",
					Common.CMD_Kill_A + comboBoxDevices.getSelectedItem() + Common.CMD_Kill_B + pid };
			excuteCommand(cmd2, Common.Monkey);
		}

		flag = 1;

		// threadTimeType.interrupt();
		threadMonkey.interrupt();

		labelHour.setVisible(false);
		labelMinute.setVisible(false);
		labelSecond.setVisible(false);

		logger.info("中断Monkey命令--结束");

	}

	/**
	 * 获取设备列表
	 * 
	 * @param cmd
	 */
	public void getDevices(String cmd) {

		logger.info("获取设备列表......开始");

		try {
			Process process = Runtime.getRuntime().exec(cmd);

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line = null;

			while ((line = reader.readLine()) != null) {

				if (line.indexOf("List") < 0 && line.length() != 0) {

					line = line.substring(0, line.length() - 7);

					logger.info("获取的设备名称：" + line);

					comboBoxDevices.addItem(line);

				}

			}

			// process.waitFor();

			process.getOutputStream().close(); // 不要忘记了一定要关

		} catch (Exception e) {

			logger.error("获取设备列表异常", e);

		}

		logger.info("获取设备列表......结束");

	}

	/**
	 * 获取应用程序列表
	 * 
	 * @param cmd
	 */
	public void getProgram(String cmd) {

		logger.info("获取应用程序列表......开始");

		try {
			Process process = Runtime.getRuntime().exec(cmd);

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line = null;

			while ((line = reader.readLine()) != null) {

				//if (line.indexOf(Common.Shinow) >= 0 && line.length() != 0) {

				logger.info("获取应用程序名称：" + line);

				comboBoxProgram.addItem(line);

				//}

			}

			// process.waitFor();

			process.getOutputStream().close(); // 不要忘记了一定要关

		} catch (Exception e) {

			logger.error("获取应用程序列表异常", e);

		}

		logger.info("获取应用程序列表......结束");

	}

	/**
	 * 运行Dos命令
	 * @param cmd	命令
	 * @param keyValue	关键字
	 */
	public void excuteCommand(String[] cmd, String keyValue) {

		logger.info("运行Dos命令......开始");

		list = new ArrayList<String>();

		try {

			Process process = Runtime.getRuntime().exec(cmd);

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line = null;

			while ((line = reader.readLine()) != null) {

				if (line.indexOf(keyValue) >= 0) {

					line = line.replace(" ", ":");

					System.out.println("[" + keyValue + "][" + line + "]");

					String[] str = line.split(":");

					System.out.println(str.length);

					for (int i = 1; i < str.length; i++) {

						if (str[i].length() > 0) {

							logger.info("Pid的值：" + str[i]);

							list.add(str[i]);

							break;

						}

					}

				}

			}

			// System.out.println("0表示正常终止：" + process.waitFor());

			process.getOutputStream().close(); // 不要忘记了一定要关

		} catch (Exception e) {

			logger.error("获取应用程序列表异常", e);

		}

		logger.info("运行Dos命令......结束");

	}

	/**
	 * 运行Monkey命令
	 */
	class ThreadExcuteMonkey implements Runnable {

		private String[] cmd;

		public ThreadExcuteMonkey(String[] cmd) {
			this.cmd = cmd;
		}

		public void run() {

			try {

				Runtime.getRuntime().exec(cmd);

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

	}

	/**
	 * 倒计时
	 * 
	 * @author Administrator
	 * 
	 */
	class ThreadExcuteTime implements Runnable {

		private long times;

		public ThreadExcuteTime(long times) {
			this.times = times;
		}

		public void run() {

			long time = 1 * times; // 自定义倒计时时间
			long hour = 0;
			long minute = 0;
			long seconds = 0;

			if (time >= 0) {

				for (int i = 0; i <= time; i++) {

					//监控Monkey命令
					moniterMonkey(time, Common.Monkey);

					//监控Monkey命令操作的App
					moniterApp(time, comboBoxProgram.getSelectedItem().toString());

					//判断标识，是否中断操作
					System.out.println("flag==1?Interrupt:Continue:" + flag);

					if (flag == 1) {
						break;
					}

					hour = time / 3600;

					minute = (time - hour * 3600) / 60;

					seconds = time - hour * 3600 - minute * 60;

					labelHour.setText(hour + Common.Hour);

					labelMinute.setText(minute + Common.Minute);

					labelSecond.setText(seconds + Common.Second);

					try {

						Thread.sleep(1000);

					} catch (Exception e) {

						e.printStackTrace();

					}

					//正常结束
					if (time == 0) {

						Thread.currentThread().interrupt();

						interruptThread();

						excuteButton.setEnabled(true);
						resetButton.setEnabled(true);
						interruptButton.setEnabled(false);
						exitButton.setEnabled(true);

					}

					i--;

					time--;

				}

				if (flag == 1) {

					Thread.currentThread().interrupt();

					threadTimeType.interrupt();

					excuteButton.setEnabled(true);
					resetButton.setEnabled(true);
					interruptButton.setEnabled(false);
					exitButton.setEnabled(true);

				}

			}

		}

		/**
		 * 间隔60秒监控一次运行monkey命令是否为运行状态，或已关闭
		 * @param time	当前剩余执行时间
		 * @param keyValue	Monkey命令的进程名称
		 */
		public void moniterMonkey(long time, String keyValue) {

			if ((time - 1) % 120 == 0) {

				logger.info("监控[" + keyValue + "]线程是否执行完毕---开始");

				logger.info("每60秒监听一次，此时time的值：" + (time - 1));

				String[] cmd = new String[] { "cmd.exe", "/c",
						Common.CMD_PS_A + comboBoxDevices.getSelectedItem() + Common.CMD_PS_B };
				logger.info("当前命令：" + cmd[2]);

				excuteCommand(cmd, keyValue);

				logger.info("当前线程数：" + list.size());

				if (list.size() == 0) {

					String log = textLogPath.getText();

					System.out.println(textLogPath.getText());

					String getDate = Utils.getCurrentDateString();

					// 日志文件路径
					String logFile = log.substring(0, log.indexOf("java_monkey_log")) + Common.Log_name + getDate
							+ ".txt";

					textLogPath.setText(logFile);

					System.out.println("monkeyCmd[2];" + monkeyCmd[2]);

					monkeyCmd[2] = monkeyCmd[2].substring(0, monkeyCmd[2].indexOf("java_monkey_log"))
							+ "java_monkey_log" + getDate + ".txt";

					logger.info("Monkey命令已经停止，再次执行");
					logger.info("monkeyCmd[2];" + monkeyCmd[2]);

					String[] monkeyCommand = new String[] { "cmd.exe", "/c", monkeyCmd[2] };

					logger.info("执行");

					// 再次执行
					threadMonkey = new Thread(new ThreadExcuteMonkey(monkeyCommand));
					threadMonkey.start();

				}

				logger.info("监控[" + keyValue + "]线程是否执行完毕---结束");

			}

		}

		/**
		 * 间隔60秒监控一次运行app是否是启动状态，或崩溃掉
		 * @param time	当前剩余执行时间
		 * @param keyValue	App的进程名称
		 */
		public void moniterApp(long time, String keyValue) {

			if ((time - 1) % 120 == 0) {

				logger.info("监控[" + keyValue + "]线程是否执行完毕---开始");

				logger.info("每60秒监听一次，此时time的值：" + (time - 1));

				String[] cmd = new String[] { "cmd.exe", "/c",
						Common.CMD_PS_A + comboBoxDevices.getSelectedItem() + Common.CMD_PS_B };
				logger.info("当前命令：" + cmd[2]);

				excuteCommand(cmd, keyValue);

				logger.info("当前线程数：" + list.size());

				if (list.size() == 0) {

					logger.info("[" + keyValue + "]已经关闭或崩溃，无法继续执行Monkey，退出系统");

					logger.info("监控[" + keyValue + "]线程是否执行完毕---结束");

					// 杀掉Monkey执行进程
					interruptThread();

					System.gc();

					//退出系统
					System.exit(0);

				}

				logger.info("监控[" + keyValue + "]线程是否执行完毕---结束");

			}

		}

	}

	/**
	 * 执行提示Dialog
	 * 
	 * @author Administrator
	 * 
	 */
	class Dialog1 extends JDialog {

		private static final long serialVersionUID = 1L;

		JDialog dialog1 = new JDialog(this, "JDialog窗体", true);

		// dialog.setUndecorated(true);

		Dialog1(String msg) {

			dialog1.setSize(320, 180);

			Container container = dialog1.getContentPane();

			container.setLayout(null);

			JLabel jl = new JLabel(msg);

			jl.setBounds(110, 1, 100, 100);

			JButton jbb = new JButton("确    定");

			jbb.setBounds(97, 80, 100, 25);

			container.add(jl);

			container.add(jbb);

			// 设置位置
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - 320) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - 180) / 2;

			dialog1.setLocation(w, h);

			jbb.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// dialog.dispose();

					logger.info("Already Closed");

					System.exit(0);
				}

			});

			dialog1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		}

	}

	/**
	 * 执行提示Dialog
	 * 
	 * @author Administrator
	 * 
	 */
	class Dialog2 extends JDialog {

		private static final long serialVersionUID = 1L;

		JDialog dialog2 = new JDialog(this, "JDialog窗体", true);

		Dialog2(String msg3) {

			dialog2.setSize(320, 180);

			Container container = dialog2.getContentPane();

			container.setLayout(null);

			JLabel jl = new JLabel(msg3);

			jl.setBounds(70, 1, 200, 100);

			JButton jbb = new JButton("确    定");

			jbb.setBounds(97, 80, 100, 25);

			container.add(jl);

			container.add(jbb);

			// 设置位置
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - 320) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - 180) / 2;

			dialog2.setLocation(w, h);

			jbb.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					dialog2.dispose();

				}

			});

			dialog2.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		}

	}

	/**
	 * 执行提示Dialog
	 * 
	 * @author Administrator
	 * 
	 */
	class Dialog3 extends JDialog {

		private static final long serialVersionUID = 1L;

		JDialog dialog3 = new JDialog(this, "JDialog窗体", true);

		Dialog3(String msg4) {

			dialog3.setSize(320, 180);

			Container container = dialog3.getContentPane();

			container.setLayout(null);

			JLabel jl = new JLabel(msg4);

			jl.setBounds(70, 1, 200, 100);

			JButton jbb = new JButton("确    定");

			jbb.setBounds(97, 80, 100, 25);

			container.add(jl);

			container.add(jbb);

			// 设置位置
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - 320) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - 180) / 2;

			dialog3.setLocation(w, h);

			jbb.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					dialog3.dispose();

				}

			});

			dialog3.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		}

	}

}


/**
 * 自动完成器。自动找到最匹配的项目，并排在列表的最前面。
 */
class AutoCompleter3 implements KeyListener, ItemListener {

	private JComboBox owner = null;
	private JTextField editor = null;
	private ComboBoxModel model = null;

	public AutoCompleter3(JComboBox comboBox) {
		owner = comboBox;
		editor = (JTextField) comboBox.getEditor().getEditorComponent();
		editor.addKeyListener(this);

		// 返回 JComboBox 当前使用的数据模型，也就是获取下拉列表个数
		model = comboBox.getModel();

		owner.addItemListener(this);
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		char ch = e.getKeyChar();
		if (ch == KeyEvent.CHAR_UNDEFINED || Character.isISOControl(ch)
				|| ch == KeyEvent.VK_DELETE)
			return;

		// 返回文本组件的文本插入符的位置
		int caretPosition = editor.getCaretPosition();

		// 返回此 TextComponent 中包含的文本
		String str = editor.getText();

		System.out.println("str" + str);
		System.out.println("car" + caretPosition);

		if (str.length() == 0)
			return;
		autoComplete(str, caretPosition);
	}

	/**
	 * 自动完成。根据输入的内容，在列表中找到相似的项目.
	 */
	protected void autoComplete(String strf, int caretPosition) {

		Object[] opts;

		opts = getMatchingOptions(strf.substring(0, caretPosition));

		// 给owner赋值
		if (owner != null) {

			model = new DefaultComboBoxModel(opts);

			// 设置 JComboBox 用于获取项列表的数据模型
			owner.setModel(model);

		}

		if (opts.length > 0) {

			// 设置 TextComponent 的文本插入符的位置
			editor.setCaretPosition(caretPosition);

			if (owner != null) {

				try {

					// 促使组合框显示其弹出窗口
					owner.showPopup();

				} catch (Exception ex) {

					ex.printStackTrace();

				}

			}

		}

	}

	/**
	 * 
	 * 找到相似的项目, 并且将之排列到数组的最前面。
	 * 
	 * @param str
	 * @return 返回所有项目的列表。
	 */
	@SuppressWarnings("unchecked")
	protected Object[] getMatchingOptions(String str) {

		System.out.println("substr:" + str);

		List v = new Vector();
		List v1 = new Vector();

		System.out.println("modelSize:" + model.getSize());

		for (int k = 0; k < model.getSize(); k++) {

			// 返回指定索引处的值
			Object itemObj = model.getElementAt(k);

			if (itemObj != null) {

				String item = itemObj.toString().toLowerCase();

				// 测试此字符串是否以指定的前缀开始
				if (item.startsWith(str.toLowerCase())) {
					System.out.println("v前缀开始"
							+ item.startsWith(str.toLowerCase()));
					v.add(itemObj);

				} else {
					System.out.println("v1前缀开始"
							+ item.startsWith(str.toLowerCase()));
					v1.add(itemObj);
				}
			} else {

				System.out.println("下拉框有null数据");
				v1.add(itemObj);
			}
		}

		System.out.println("v1.size():" + v1.size());

		// 这样循环写的目的是将数据在数组中按顺序排序，符合条件在头，不符合在后
		for (int i = 0; i < v1.size(); i++) {

			v.add(v1.get(i));

		}

		if (v.isEmpty())

			v.add(str);

		System.out.println("v.size():" + v.size());

		return v.toArray();

	}

	public void itemStateChanged(ItemEvent event) {

		if (event.getStateChange() == ItemEvent.SELECTED) {

			int caretPosition = editor.getCaretPosition();

			if (caretPosition != -1) {

				try {

					editor.moveCaretPosition(caretPosition);

				} catch (IllegalArgumentException ex) {

					ex.printStackTrace();

				}

			}

		}

	}

}

