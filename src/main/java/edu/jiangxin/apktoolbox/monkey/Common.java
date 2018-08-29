package edu.jiangxin.apktoolbox.monkey;

public class Common {
	
	/**
	 * 日志文件路径
	 */
	public static String Log_Path = "C:\\JK\\monkey_log\\";
	
	/**
	 * 日志文件名称
	 */
	public static String Log_name = "java_monkey_log";
	
	public static String Hour = "时";
	public static String Minute = "分";
	public static String Second = "秒";
	
	public static String CMD_Devices = "adb devices";
	public static String CMD_Program_A = "adb -s ";
	public static String CMD_Program_B = " shell ls data/data";
	public static String CMD_PS_A = "adb -s ";
	public static String CMD_PS_B = " shell ps";
	public static String CMD_Kill_A = "adb -s ";
	public static String CMD_Kill_B = " shell kill ";
	
	public static String Shinow = "shinow";
	public static String Root = "root";
	public static String Monkey = "com.android.commands.monkey";
	public static String Donor = "com.shinow.donor";
	
	public static String Ignore_Crashes = " --ignore-crashes";
	public static String Ignore_Timeouts = " --ignore-timeouts";
	public static String Monitor_Native_Crashes = " --monitor-native-crashes";
	public static String Ignore_Security_Exceptions = " --ignore-security-exceptions";
	public static String Kill_Process_After_Error = " --kill-process-after-error";
	public static String Wait_Dbg = " --wait-dbg";
	public static String Hprof = " --hprof";
	
	public static String Level_0 = " -v ";
	public static String Level_1 = " -v -v ";
	public static String Level_2 = " -v -v -v ";
	
	public static String TransactionCount = "9999";
	
	/**
	 * 替换完成！！！
	 */
	public static String Msg1 = "执行完毕！！！";

	/**
	 * 设备列表不能为空！
	 */
	public static String Msg4 = "设备列表不能为空！请[刷新]！";
	
	/**
	 * 应用程序不能为空！
	 */
	public static String Msg5 = "应用程序不能为空！请选其他设备！";
	
	/**
	 * 事件间隔或时间数量不能为空！
	 */
	public static String Msg2 = "事件间隔或时间数量不能为空！";

	/**
	 * 日志保存路径不能为空！
	 */
	public static String Msg6 = "日志保存路径不能为空！";

	/**
	 * 约束条件都设置为空吗？
	 */
	public static String Msg3 = "约束条件都设置为空吗？";
	
}
