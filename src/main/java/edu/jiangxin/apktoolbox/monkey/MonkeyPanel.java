package edu.jiangxin.apktoolbox.monkey;

import java.awt.Container;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import edu.jiangxin.apktoolbox.swing.extend.JEasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.NumberPlainDocument;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class MonkeyPanel extends JEasyPanel {

    private static final long serialVersionUID = 1L;

    Thread threadTimeType = null;
    Thread threadMonkey = null;

    JLabel labelDevices = new JLabel("设备列表");
    JComboBox<String> comboBoxDevices = new JComboBox<String>();
    JButton refreshButton = new JButton("刷新");

    JFileChooser fileChooser = new JFileChooser();
    JButton logPathButton = new JButton("选择路径");
    JButton excuteButton = new JButton("运行命令");

    JButton resetButton = new JButton("重置页面");
    JButton interruptButton = new JButton("终止运行");
    JButton exitButton = new JButton("退出运行");

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

    JComboBox<String> comboBoxProgram = new JComboBox<String>();
    JComboBox<String> comboBoxTime = new JComboBox<String>();
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
    // String logFolder = "";
    String[] monkeyCmd = null;

    public MonkeyPanel() {
        super();
        Utils.setJComponentSize(this, Constants.DEFAULT_WIDTH, Constants.DEFAULT_HIGHT);

        setLayout(null);

        initDevicesAndProgram();

        initRestrainCondition();

        initTimeSetting();

        initLogSetting();

        initOperation();

        registListener();

    }

    private void initDevicesAndProgram() {
        labelDevices.setBounds(30, 25, 60, 20);
        comboBoxDevices.setBounds(90, 25, 240, 20);
        refreshButton.setBounds(340, 25, 60, 20);

        labelProgram.setBounds(30, 55, 60, 20);
        comboBoxProgram.setBounds(90, 55, 240, 20);
        comboBoxProgram.setEditable(true);
        comboBoxProgram.setSelectedItem("com.shinow.*");

        add(labelDevices);
        add(comboBoxDevices);
        add(refreshButton);

        add(labelProgram);
        add(comboBoxProgram);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBoxDevices.removeAllItems();
                List<String> devices = getDevices();
                for (String device : devices) {
                    comboBoxDevices.addItem(device);
                }
            }
        });

        comboBoxDevices.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBoxProgram.removeAllItems();
                comboBoxProgram.setSelectedItem("com.shinow.*");
                List<String> programs = getProgram(comboBoxDevices.getSelectedItem().toString());
                for (String program : programs) {
                    comboBoxProgram.addItem(program);
                }

            }

        });

    }

    private void initRestrainCondition() {
        labelRestrain.setBounds(30, 85, 56, 20);
        checkBoxCrashes.setBounds(86, 85, 100, 20);
        checkBoxTimeouts.setBounds(186, 85, 100, 20);
        checkBoxExceptions.setBounds(286, 85, 110, 20);
        checkBoxNativeCrashes.setBounds(400, 85, 180, 20);
        checkBoxKill.setBounds(86, 115, 180, 20);
        checkBoxWaitDbg.setBounds(286, 115, 280, 20);
        checkBoxHprof.setBounds(86, 145, 280, 20);

        add(labelRestrain);
        add(checkBoxCrashes);
        add(checkBoxTimeouts);
        add(checkBoxExceptions);
        add(checkBoxNativeCrashes);
        add(checkBoxKill);
        add(checkBoxWaitDbg);
        add(checkBoxHprof);

        checkBoxCrashes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ignoreCrashes = Common.Ignore_Crashes;
                } else {
                    ignoreCrashes = "";
                }
            }
        });

        checkBoxTimeouts.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ignoreTimeouts = Common.Ignore_Timeouts;
                } else {
                    ignoreTimeouts = "";
                }
            }
        });

        checkBoxExceptions.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ignoreSecurityExceptions = Common.Ignore_Security_Exceptions;
                } else {
                    ignoreSecurityExceptions = "";
                }
            }
        });

        checkBoxNativeCrashes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    monitorNativeCrashes = Common.Monitor_Native_Crashes;
                } else {
                    monitorNativeCrashes = "";
                }
            }
        });

    }

    private void initTimeSetting() {
        labelSpace.setBounds(30, 175, 60, 20);
        textMillisecond.setBounds(90, 175, 150, 20);
        textMillisecond.setDocument(new NumberPlainDocument(7));
        textMillisecond.setText("500");
        labelMillisecond.setBounds(245, 175, 40, 20);

        labelTime.setBounds(30, 205, 60, 20);
        textTime.setBounds(90, 205, 150, 20);
        textTime.setDocument(new NumberPlainDocument(7));
        textTime.setText("1");
        comboBoxTime.setBounds(245, 205, 50, 20);
        comboBoxTime.addItem(Common.Hour);
        comboBoxTime.addItem(Common.Minute);
        comboBoxTime.addItem(Common.Second);

        labelHour.setBounds(405, 205, 50, 20);
        labelMinute.setBounds(475, 205, 50, 20);
        labelSecond.setBounds(545, 205, 50, 20);

        add(labelSpace);
        add(textMillisecond);
        add(labelMillisecond);

        add(labelTime);
        add(textTime);
        add(comboBoxTime);

        add(labelHour); // 时
        add(labelMinute); // 分
        add(labelSecond); // 秒

    }

    private void initLogSetting() {
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

        add(labelLogLevel);
        add(radioButton0);
        add(radioButton1);
        add(radioButton2);

        add(logPathButton);
        add(textLogPath);

        logPathButton.addActionListener(new ActionListener() {
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

    }

    private void initOperation() {
        excuteButton.setBounds(30, 305, 100, 20);
        resetButton.setBounds(140, 305, 100, 20);
        interruptButton.setBounds(250, 305, 100, 20);
        exitButton.setBounds(360, 305, 100, 20);

        add(excuteButton);
        add(resetButton);
        add(interruptButton);
        add(exitButton);

        interruptButton.setEnabled(false);

        excuteButton.addActionListener(new ActionListener() {
            @Override
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

                // 自定义参数，是否能够执行Monkey？[0不可以][1可以]
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
                    String logFile = textLogPath.getText() + "\\" + Common.Log_name + Utils.getCurrentDateString()
                            + ".txt";

                    // logFolder = textLogPath.getText();

                    // textLogPath.setText(logFile);

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

                    // 倒计时文本
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

        resetButton.addActionListener(new ActionListener() {
            @Override
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

        interruptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 杀掉Monkey执行进程
                interruptThread();
                excuteButton.setEnabled(true);
                resetButton.setEnabled(true);
                interruptButton.setEnabled(false);
                exitButton.setEnabled(true);
                //System.gc();
            }
        });

    }

    /**
     * 运行
     */
    private void registListener() {

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
    private List<String> getDevices() {
        List<String> devices = new ArrayList<>();
        logger.info("获取设备列表......开始");
        try {
            Process process = Runtime.getRuntime().exec("adb devices");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("device") && !line.contains("List of")) {
                    line = line.substring(0, line.length() - 7);
                    logger.info("获取的设备名称：" + line);
                    devices.add(line);
                }
            }
            reader.close();
            process.getOutputStream().close(); // 不要忘记了一定要关
        } catch (Exception e) {
            logger.error("获取设备列表异常", e);
        }
        logger.info("获取设备列表......结束");
        return devices;
    }

    /**
     * 获取应用程序列表
     * 
     * @param cmd
     */
    private List<String> getProgram(String device) {
        List<String> programs = new ArrayList<>();
        logger.info("获取应用程序列表......开始");
        try {
            String cmd = "adb -s " + device + " shell pm list packages";
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("package:")) {
                    logger.info("获取应用程序名称：" + line);
                    programs.add(line.replace("package:", ""));
                }
            }
            reader.close();
            process.getOutputStream().close(); // 不要忘记了一定要关
        } catch (Exception e) {
            logger.error("获取应用程序列表异常", e);
        }
        logger.info("获取应用程序列表......结束");
        return programs;
    }

    /**
     * 运行Dos命令
     * 
     * @param cmd      命令
     * @param keyValue 关键字
     */
    public void excuteCommand(String[] cmd, String keyValue) {
        logger.info("运行Dos命令......开始");
        list = new ArrayList<String>();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
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
            reader.close();
            process.getOutputStream().close(); // 不要忘记了一定要关
        } catch (Exception e) {
            logger.error("获取应用程序列表异常", e);
        }
        logger.info("运行Dos命令......结束");
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

                    // 监控Monkey命令
                    moniterMonkey(time, Common.Monkey);

                    // 监控Monkey命令操作的App
                    moniterApp(time, comboBoxProgram.getSelectedItem().toString());

                    // 判断标识，是否中断操作
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

                    // 正常结束
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
         * 
         * @param time     当前剩余执行时间
         * @param keyValue Monkey命令的进程名称
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
         * 
         * @param time     当前剩余执行时间
         * @param keyValue App的进程名称
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
                    //System.gc();
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
