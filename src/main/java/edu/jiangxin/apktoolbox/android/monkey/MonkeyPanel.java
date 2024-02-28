package edu.jiangxin.apktoolbox.android.monkey;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.NumberPlainDocument;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.DateUtils;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class MonkeyPanel extends EasyPanel {

    @Serial
    private static final long serialVersionUID = 1L;
    
    private static final String LOG_NAME = "java_monkey_log";

    private static final String HOUR = "时";
    private static final String MINUTE = "分";
    private static final String SECOND = "秒";

    private static final String CMD_PS_A = "adb -s ";
    private static final String CMD_PS_B = " shell ps";
    private static final String CMD_KILL_A = "adb -s ";
    private static final String CMD_KILL_B = " shell kill ";

    private static final String MONKEY = "com.android.commands.monkey";

    private static final String IGNORE_CRASHES = " --ignore-crashes";
    private static final String IGNORE_TIMEOUTS = " --ignore-timeouts";
    private static final String MONITOR_NATIVE_CRASHES = " --monitor-native-crashes";
    private static final String IGNORE_SECURITY_EXCEPTIONS = " --ignore-security-exceptions";
    private static final String KILL_PROCESS_AFTER_ERROR = " --kill-process-after-error";
    private static final String WAIT_DBG = " --wait-dbg";
    private static final String HPROF = " --hprof";

    private static final String LEVEL_0 = " -v ";
    private static final String LEVEL_1 = " -v -v ";
    private static final String LEVEL_2 = " -v -v -v ";

    private static final String TRANSACTIONCOUNT = "9999";

    /**
     * 设备列表不能为空！
     */
    private static final String MSG4 = "设备列表不能为空！请[刷新]！";

    /**
     * 应用程序不能为空！
     */
    private static final String MSG5 = "应用程序不能为空！请选其他设备！";

    /**
     * 事件间隔或时间数量不能为空！
     */
    private static final String MSG2 = "事件间隔或时间数量不能为空！";

    /**
     * 日志保存路径不能为空！
     */
    private static final String MSG6 = "日志保存路径不能为空！";

    Thread threadTimeType = null;
    Process monkeyProcess = null;

    JComboBox<String> comboBoxDevices = new JComboBox<>();
    JButton refreshButton = new JButton("刷新");

    JButton logPathButton = new JButton("选择路径");
    JButton executeButton = new JButton("运行命令");

    JButton resetButton = new JButton("重置页面");
    JButton interruptButton = new JButton("终止运行");
    JButton exitButton = new JButton("退出运行");

    JLabel labelHour = new JLabel();
    JLabel labelMinute = new JLabel();
    JLabel labelSecond = new JLabel();
    JTextField textMillisecond = new JTextField(30);
    JTextField textTime = new JTextField(30);
    JTextField textLogPath = new JTextField(90);

    JComboBox<String> comboBoxProgram = new JComboBox<>();
    JComboBox<String> comboBoxTime = new JComboBox<>();

    /**
     * --dbg-no-events：初始化启动的activity，但是不产生任何事件
     */
    JCheckBox checkBoxDbgNoEvents = new JCheckBox("初始化启动的activity，但是不产生任何事件");

    /**
     * --hprof：指定该项后在事件序列发送前后会立即生成分析报告（一般建议指定该项）
     */
    JCheckBox checkBoxHprof = new JCheckBox("在事件序列发送前后会立即生成分析报告");

    /**
     * --ignore-crashes：忽略崩溃
     */
    JCheckBox checkBoxCrashes = new JCheckBox("忽略崩溃", true);

    /**
     * --ignore-timeouts：忽略超时
     */
    JCheckBox checkBoxTimeouts = new JCheckBox("忽略超时", true);

    /**
     * --monitor-native-crashes：跟踪本地方法的崩溃问题
     */
    JCheckBox checkBoxNativeCrashes = new JCheckBox("跟踪本地方法的崩溃问题", true);

    /**
     * --ignore-security-exceptions：忽略安全异常
     */
    JCheckBox checkBoxExceptions = new JCheckBox("忽略安全异常", true);

    /**
     * --kill-process-after-error：发生错误后直接杀掉进程
     */
    JCheckBox checkBoxKill = new JCheckBox("发生错误后直接杀掉进程");

    /**
     * --wait-dbg：知道连接了调试器才执行monkey测试
     */
    JCheckBox checkBoxWaitDbg = new JCheckBox("停止Monkey执行，直到有调试器与其连接");

    ButtonGroup group = new ButtonGroup();

    /**
     * 缺省值
     */
    JRadioButton radioButton0 = new JRadioButton("基本信息");

    /**
     * 比较详细
     */
    JRadioButton radioButton1 = new JRadioButton("比较详细");

    /**
     * 非常详细(默认选中)
     */
    JRadioButton radioButton2 = new JRadioButton("非常详细", true);

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
    int flag = 0;
    String[] monkeyCmd = null;

    @Override
    public void initUI() {
        setPreferredSize(new Dimension(Constants.DEFAULT_PANEL_WIDTH, Constants.DEFAULT_PANEL_HEIGHT));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel devicesPanel = new JPanel();
        devicesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        initDevices(devicesPanel);
        add(devicesPanel);

        JPanel programPanel = new JPanel();
        programPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        initProgram(programPanel);
        add(programPanel);

        JPanel restrainConditionPanel = new JPanel();
        restrainConditionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        initRestrainCondition(restrainConditionPanel);
        add(restrainConditionPanel);

        JPanel eventIntervalPanel = new JPanel();
        eventIntervalPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        initEventInterval(eventIntervalPanel);
        add(eventIntervalPanel);

        JPanel runTimePanel = new JPanel();
        runTimePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        initRunTime(runTimePanel);
        add(runTimePanel);

        JPanel logLevelPanel = new JPanel();
        logLevelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        initLogLevel(logLevelPanel);
        add(logLevelPanel);

        JPanel logPathPanel = new JPanel();
        logPathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        initLogPath(logPathPanel);
        add(logPathPanel);

        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        initOperation(operationPanel);
        add(operationPanel);

        registerListener();
    }

    private void initDevices(JPanel panel) {
        JLabel labelDevices = new JLabel("设备列表");
        labelDevices.setPreferredSize(new Dimension(60, 25));
        comboBoxDevices.setPreferredSize(new Dimension(240, 25));
        refreshButton.setPreferredSize(new Dimension(60, 25));

        panel.add(labelDevices);
        panel.add(comboBoxDevices);
        panel.add(refreshButton);

        refreshButton.addActionListener(e -> {
            comboBoxDevices.removeAllItems();
            List<String> devices = getDevices();
            for (String device : devices) {
                comboBoxDevices.addItem(device);
            }
        });
    }

    private void initProgram(JPanel panel) {
        JLabel labelProgram = new JLabel("应用程序");
        labelProgram.setPreferredSize(new Dimension(60, 25));
        comboBoxProgram.setPreferredSize(new Dimension(240, 25));
        comboBoxProgram.setEditable(true);
        comboBoxProgram.setSelectedItem("com.shinow.*");

        panel.add(labelProgram);
        panel.add(comboBoxProgram);

        comboBoxDevices.addActionListener(e -> {
            comboBoxProgram.removeAllItems();
            comboBoxProgram.setSelectedItem("com.shinow.*");
            List<String> programs = getApplication(comboBoxDevices.getSelectedItem().toString());
            for (String program : programs) {
                comboBoxProgram.addItem(program);
            }

        });
    }

    private void initRestrainCondition(JPanel panel) {
        JLabel labelRestrain = new JLabel("约束条件");
        labelRestrain.setPreferredSize(new Dimension(60, 25));
        checkBoxCrashes.setPreferredSize(new Dimension(100, 25));
        checkBoxTimeouts.setPreferredSize(new Dimension(100, 25));
        checkBoxExceptions.setPreferredSize(new Dimension(110, 25));
        checkBoxNativeCrashes.setPreferredSize(new Dimension(180, 25));
        checkBoxKill.setPreferredSize(new Dimension(180, 25));
        checkBoxWaitDbg.setPreferredSize(new Dimension(280, 25));
        checkBoxHprof.setPreferredSize(new Dimension(280, 25));

        panel.add(labelRestrain);
        panel.add(checkBoxCrashes);
        panel.add(checkBoxTimeouts);
        panel.add(checkBoxExceptions);
        panel.add(checkBoxNativeCrashes);
        panel.add(checkBoxKill);
        panel.add(checkBoxWaitDbg);
        panel.add(checkBoxHprof);

        checkBoxCrashes.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ignoreCrashes = IGNORE_CRASHES;
            } else {
                ignoreCrashes = "";
            }
        });

        checkBoxTimeouts.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ignoreTimeouts = IGNORE_TIMEOUTS;
            } else {
                ignoreTimeouts = "";
            }
        });

        checkBoxExceptions.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ignoreSecurityExceptions = IGNORE_SECURITY_EXCEPTIONS;
            } else {
                ignoreSecurityExceptions = "";
            }
        });

        checkBoxNativeCrashes.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                monitorNativeCrashes = MONITOR_NATIVE_CRASHES;
            } else {
                monitorNativeCrashes = "";
            }
        });

    }

    private void initEventInterval(JPanel panel) {
        JLabel labelSpace = new JLabel("事件间隔");
        labelSpace.setPreferredSize(new Dimension(60, 25));
        textMillisecond.setPreferredSize(new Dimension(150, 25));
        textMillisecond.setDocument(new NumberPlainDocument(7));
        textMillisecond.setText("500");
        JLabel labelMillisecond = new JLabel("毫秒");
        labelMillisecond.setPreferredSize(new Dimension(40, 25));

        panel.add(labelSpace);
        panel.add(textMillisecond);
        panel.add(labelMillisecond);
    }

    private void initRunTime(JPanel panel) {
        JLabel labelTime = new JLabel("运行时长");
        labelTime.setPreferredSize(new Dimension(60, 25));
        textTime.setPreferredSize(new Dimension(150, 25));
        textTime.setDocument(new NumberPlainDocument(7));
        textTime.setText("1");
        comboBoxTime.setPreferredSize(new Dimension(60, 25));
        comboBoxTime.addItem(HOUR);
        comboBoxTime.addItem(MINUTE);
        comboBoxTime.addItem(SECOND);

        labelHour.setPreferredSize(new Dimension(50, 25));
        labelMinute.setPreferredSize(new Dimension(50, 25));
        labelSecond.setPreferredSize(new Dimension(50, 25));

        panel.add(labelTime);
        panel.add(textTime);
        panel.add(comboBoxTime);

        panel.add(labelHour);
        panel.add(labelMinute);
        panel.add(labelSecond);
    }

    private void initLogLevel(JPanel panel) {
        JLabel labelLogLevel = new JLabel("日志级别");
        labelLogLevel.setPreferredSize(new Dimension(60, 25));

        group.add(radioButton0);
        group.add(radioButton1);
        group.add(radioButton2);

        radioButton0.setPreferredSize(new Dimension(80, 25));
        radioButton1.setPreferredSize(new Dimension(80, 25));
        radioButton2.setPreferredSize(new Dimension(80, 25));

        panel.add(labelLogLevel);
        panel.add(radioButton0);
        panel.add(radioButton1);
        panel.add(radioButton2);
    }

    private void initLogPath(JPanel panel) {
        logPathButton.setPreferredSize(new Dimension(100, 25));
        textLogPath.setColumns(60);
        textLogPath.setEditable(false);
        textLogPath.setText("D:");

        panel.add(logPathButton);
        panel.add(textLogPath);

        logPathButton.addActionListener(e -> {
            if (e.getSource() == logPathButton) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int intRetVal = fileChooser.showOpenDialog(new Label());
                if (intRetVal == JFileChooser.APPROVE_OPTION) {
                    textLogPath.setText(fileChooser.getSelectedFile().getPath());
                }
            }
        });
    }

    private void initOperation(JPanel panel) {
        executeButton.setPreferredSize(new Dimension(100, 25));
        resetButton.setPreferredSize(new Dimension(100, 25));
        interruptButton.setPreferredSize(new Dimension(100, 25));
        exitButton.setPreferredSize(new Dimension(100, 25));

        panel.add(executeButton);
        panel.add(resetButton);
        panel.add(interruptButton);
        panel.add(exitButton);

        interruptButton.setEnabled(false);

        executeButton.addActionListener(e -> {
            ignoreCrashes = checkBoxCrashes.isSelected() ? IGNORE_CRASHES : "";
            ignoreTimeouts = checkBoxTimeouts.isSelected() ? IGNORE_TIMEOUTS : "";
            monitorNativeCrashes = checkBoxNativeCrashes.isSelected() ? MONITOR_NATIVE_CRASHES : "";
            ignoreSecurityExceptions = checkBoxExceptions.isSelected() ? IGNORE_SECURITY_EXCEPTIONS : "";
            hprof = checkBoxHprof.isSelected() ? HPROF : "";
            killProcessAfterError = checkBoxKill.isSelected() ? KILL_PROCESS_AFTER_ERROR : "";
            waitDbg = checkBoxWaitDbg.isSelected() ? WAIT_DBG : "";

            // 判断设备列表和应用程序是否为空
            if (comboBoxDevices.getItemCount() == 0) {
                new MyDialog(MSG4).setVisible(true);
                refreshButton.requestFocus();
                return;
            }
            if (comboBoxProgram.getItemCount() == 0) {
                new MyDialog(MSG5).setVisible(true);
                return;
            }
            // 判断事件间隔和运行时长是否为空
            if (textMillisecond.getText().length() == 0) {
                new MyDialog(MSG2).setVisible(true);
                textMillisecond.requestFocus();
                return;
            }
            if (textTime.getText().length() == 0) {
                new MyDialog(MSG2).setVisible(true);
                textTime.requestFocus();
                return;
            }
            if (textLogPath.getText().length() == 0) {
                new MyDialog(MSG6).setVisible(true);
                logPathButton.requestFocus();
                return;
            }

            // 获取日志级别
            if (radioButton0.isSelected()) {
                logLevel = LEVEL_0;
            } else if (radioButton1.isSelected()) {
                logLevel = LEVEL_1;
            } else if (radioButton2.isSelected()) {
                logLevel = LEVEL_2;
            }
            String logFile = textLogPath.getText() + "\\" + LOG_NAME + DateUtils.getCurrentDateString() + ".txt";

            monkeyCmd = new String[] { "cmd.exe", "/C",
                    "adb -s " + comboBoxDevices.getSelectedItem() + " shell monkey -p "
                            + comboBoxProgram.getSelectedItem() + "" + ignoreCrashes + "" + ignoreTimeouts + ""
                            + ignoreSecurityExceptions + "" + monitorNativeCrashes + " --throttle "
                            + textMillisecond.getText() + "" + logLevel + "" + TRANSACTIONCOUNT + " > " + logFile };

            logger.info("Monkey:" + monkeyCmd[2]);

            flag = 0;

            try {
                monkeyProcess = Runtime.getRuntime().exec(monkeyCmd);
            } catch (Exception ex) {
                logger.error("Exception", ex);
            }

            executeButton.setEnabled(false);
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
            if (Objects.equals(timeType, HOUR)) {
                time *= 3600;
            } else if (Objects.equals(timeType, MINUTE)) {
                time *= 60;
            }
            threadTimeType = new Thread(new CountdownRunnable(time));
            threadTimeType.start();
        });

        resetButton.addActionListener(e -> {
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
        });

        interruptButton.addActionListener(e -> {
            // 杀掉Monkey执行进程
            interruptThread();
            executeButton.setEnabled(true);
            resetButton.setEnabled(true);
            interruptButton.setEnabled(false);
            exitButton.setEnabled(true);
        });

    }

    /**
     * 运行
     */
    private void registerListener() {

    }

    /**
     * 中断Monkey命令
     */
    public void interruptThread() {

        logger.info("中断Monkey命令--开始");

        String[] cmd1 = new String[] { "cmd.exe", "/c", CMD_PS_A + comboBoxDevices.getSelectedItem() + CMD_PS_B };
        executeCommand(cmd1, MONKEY);

        List<String> listPid = list;
        logger.info("获取的中断Monkey进程数量：" + listPid.size());

        String[] cmd2 = null;
        String pid = "";
        for (String s : listPid) {
            pid = s;
            cmd2 = new String[]{"cmd.exe", "/c", CMD_KILL_A + comboBoxDevices.getSelectedItem() + CMD_KILL_B + pid};
            executeCommand(cmd2, MONKEY);
        }

        flag = 1;

        monkeyProcess.destroy();

        labelHour.setVisible(false);
        labelMinute.setVisible(false);
        labelSecond.setVisible(false);

        logger.info("中断Monkey命令--结束");

    }

    private List<String> getDevices() {
        List<String> devices = new ArrayList<>();
        logger.info("get device list start");
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("adb devices");
        } catch (IOException e) {
            logger.error("exec command failed: " + e.getMessage());
        }
        if (process == null) {
            logger.error("process is null");
            return devices;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("device") && !line.contains("List of")) {
                    line = line.substring(0, line.length() - 7);
                    logger.info("device name: " + line);
                    devices.add(line);
                }
            }
        } catch (IOException e) {
            logger.error("read failed: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(process.getOutputStream());
            IOUtils.closeQuietly(process.getErrorStream());
            IOUtils.closeQuietly(process.getInputStream());
        }
        logger.info("get device list end");
        return devices;
    }

    private List<String> getApplication(String device) {
        List<String> apps = new ArrayList<>();
        logger.info("get application list start");
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"adb", "-s", device, "shell", "pm", "list", "packages"});
        } catch (IOException e) {
            logger.error("exec command failed: " + e.getMessage());
        }
        if (process == null) {
            logger.error("process is null");
            return apps;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("package:")) {
                    logger.info("application name：" + line);
                    apps.add(line.replace("package:", ""));
                }
            }
        } catch (IOException e) {
            logger.error("read failed: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(process.getOutputStream());
            IOUtils.closeQuietly(process.getErrorStream());
            IOUtils.closeQuietly(process.getInputStream());
        }
        logger.info("get application list end");
        return apps;
    }

    private void executeCommand(String[] cmd, String keyValue) {
        logger.info("exec cmd start");
        list = new ArrayList<>();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            logger.error("exec command failed: " + e.getMessage());
        }
        if (process == null) {
            logger.error("process is null");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(keyValue)) {
                    line = line.replace(" ", ":");
                    System.out.println("[" + keyValue + "][" + line + "]");
                    String[] str = line.split(":");
                    System.out.println(str.length);
                    for (int i = 1; i < str.length; i++) {
                        if (str[i].length() > 0) {
                            logger.info("Pid: " + str[i]);
                            list.add(str[i]);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("read failed: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(process.getOutputStream());
            IOUtils.closeQuietly(process.getErrorStream());
            IOUtils.closeQuietly(process.getInputStream());
        }
        logger.info("exec cmd end");
    }

    /**
     * 倒计时
     * 
     */
    class CountdownRunnable implements Runnable {
        private long times;

        public CountdownRunnable(long times) {
            this.times = times;
        }

        @Override
        public void run() {
            // 自定义倒计时时间
            long time = times;
            long hour = 0;
            long minute = 0;
            long seconds = 0;

            if (time >= 0) {
                for (int i = 0; i <= time; i++) {
                    // 监控Monkey命令
                    monitorMonkey(time, MONKEY);
                    // 监控Monkey命令操作的App
                    monitorApp(time, comboBoxProgram.getSelectedItem().toString());
                    // 判断标识，是否中断操作
                    System.out.println("flag==1?Interrupt:Continue:" + flag);
                    if (flag == 1) {
                        break;
                    }
                    hour = time / 3600;
                    minute = (time - hour * 3600) / 60;
                    seconds = time - hour * 3600 - minute * 60;
                    labelHour.setText(hour + HOUR);
                    labelMinute.setText(minute + MINUTE);
                    labelSecond.setText(seconds + SECOND);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        logger.error("Exception", e);
                    }
                    // 正常结束
                    if (time == 0) {
                        Thread.currentThread().interrupt();
                        interruptThread();
                        executeButton.setEnabled(true);
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
                    executeButton.setEnabled(true);
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
        private void monitorMonkey(long time, String keyValue) {
            if ((time - 1) % 120 == 0) {
                logger.info("监控[" + keyValue + "]线程是否执行完毕---开始");
                logger.info("每60秒监听一次，此时time的值：" + (time - 1));
                String[] cmd = new String[] { "cmd.exe", "/c",
                        CMD_PS_A + comboBoxDevices.getSelectedItem() + CMD_PS_B };
                logger.info("当前命令：" + cmd[2]);
                executeCommand(cmd, keyValue);
                logger.info("当前线程数：" + list.size());
                if (list.size() == 0) {
                    String log = textLogPath.getText();
                    System.out.println(textLogPath.getText());
                    String getDate = DateUtils.getCurrentDateString();
                    // 日志文件路径
                    String logFile = log.substring(0, log.indexOf("java_monkey_log")) + LOG_NAME + getDate + ".txt";
                    textLogPath.setText(logFile);
                    System.out.println("monkeyCmd[2];" + monkeyCmd[2]);
                    monkeyCmd[2] = monkeyCmd[2].substring(0, monkeyCmd[2].indexOf("java_monkey_log"))
                            + "java_monkey_log" + getDate + ".txt";
                    logger.info("Monkey命令已经停止，再次执行");
                    logger.info("monkeyCmd[2];" + monkeyCmd[2]);
                    String[] monkeyCommand = new String[] { "cmd.exe", "/c", monkeyCmd[2] };
                    logger.info("执行");
                    // 再次执行
                    try {
                        monkeyProcess = Runtime.getRuntime().exec(monkeyCommand);
                    } catch (Exception e) {
                        logger.error("Exception", e);
                    }
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
        private void monitorApp(long time, String keyValue) {
            if ((time - 1) % 120 == 0) {
                logger.info("监控[" + keyValue + "]线程是否执行完毕---开始");
                logger.info("每60秒监听一次，此时time的值：" + (time - 1));
                String[] cmd = new String[] { "cmd.exe", "/c",
                        CMD_PS_A + comboBoxDevices.getSelectedItem() + CMD_PS_B };
                logger.info("当前命令：" + cmd[2]);
                executeCommand(cmd, keyValue);
                logger.info("当前线程数：" + list.size());
                if (list.size() == 0) {
                    logger.info("[" + keyValue + "]已经关闭或崩溃，无法继续执行Monkey，退出系统");
                    logger.info("监控[" + keyValue + "]线程是否执行完毕---结束");
                    // 杀掉Monkey执行进程
                    interruptThread();
                    // System.gc();
                }
                logger.info("监控[" + keyValue + "]线程是否执行完毕---结束");
            }
        }
    }

    static class MyDialog extends JDialog {

        @Serial
        private static final long serialVersionUID = 1L;

        MyDialog(String msg3) {
            super((Frame)null, "提示", true);
            setSize(320, 180);
            Container container = getContentPane();
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

            setLocation(w, h);

            jbb.addActionListener(e -> dispose());
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
    }
}
