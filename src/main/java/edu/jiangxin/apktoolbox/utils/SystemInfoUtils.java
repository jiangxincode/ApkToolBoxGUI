package edu.jiangxin.apktoolbox.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SystemInfoUtils {
    private static final Logger logger = LogManager.getLogger(SystemInfoUtils.class.getSimpleName());

    public static final List<String> SYSTEM_INFO_LIST = List.of("os.name", "os.arch", "os.version",
            "java.version", "java.vendor", "java.vm.version", "java.vm.vendor", "java.vm.name",
            "java.specification.version", "java.specification.vendor", "java.specification.name",
            "java.class.version", "java.class.path", "java.library.path", "java.io.tmpdir",
            "java.compiler", "java.ext.dirs", "user.name", "user.home", "user.dir", "file.encoding",
            "sun.jnu.encoding", "sun.arch.data.model", "sun.desktop", "sun.cpu.endian",
            "sun.io.unicode.encoding", "sun.cpu.isalist");

    private SystemInfoUtils() {
    }

    public static void logSystemInfo() {
        if (!logger.isInfoEnabled()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String key : SYSTEM_INFO_LIST) {
            sb.append(key).append(": ").append(System.getProperty(key)).append("\n");
        }
        logger.info(sb.toString());
    }
}
