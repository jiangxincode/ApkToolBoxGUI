package edu.jiangxin.apktoolbox.android.dumpsys.tojson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public class Alarm2Json implements IDumpsys2Json {
    @Override
    public String dumpsys2Json(File file) {
        String content;
        try {
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException("读取文件失败: " + file.getAbsolutePath(), e);
        }
        JSONObject result = new JSONObject();
        parseSections(content, result);
        return result.toString(2);
    }

    private void parseSections(String content, JSONObject result) {
        // 以两个换行分割各大段
        String[] sections = content.split("\n\\s*\n");
        for (String section : sections) {
            String title = getSectionTitle(section);
            if (title != null) {
                Object value = parseSectionContent(title, section);
                result.put(title, value);
            }
        }
    }

    private String getSectionTitle(String section) {
        Matcher m = Pattern.compile("^\\s*([\\w .-]+):", Pattern.MULTILINE).matcher(section);
        if (m.find()) {
            return m.group(1).trim();
        }
        return null;
    }

    private Object parseSectionContent(String title, String section) {
        if (title.equals("Settings") || title.equals("Feature Flags")) {
            return parseKeyValueSection(section);
        } else if (title.equals("Current AppStateTracker State")) {
            return parseAppStateSection(section);
        } else if (title.contains("pending alarms")) {
            return parsePendingAlarms(section);
        } else {
            return section.trim();
        }
    }

    private JSONObject parseKeyValueSection(String section) {
        JSONObject obj = new JSONObject();
        Matcher m = Pattern.compile("^\\s*([\\w._-]+)=(.+)$", Pattern.MULTILINE).matcher(section);
        while (m.find()) {
            obj.put(m.group(1).trim(), m.group(2).trim());
        }
        return obj;
    }

    private JSONObject parseAppStateSection(String section) {
        JSONObject obj = new JSONObject();
        Matcher m = Pattern.compile("^\\s*([\\w .-]+): (.+)$", Pattern.MULTILINE).matcher(section);
        while (m.find()) {
            String key = m.group(1).trim();
            String value = m.group(2).trim();
            if (value.startsWith("[") && value.endsWith("]")) {
                value = value.substring(1, value.length() - 1);
                JSONArray arr = new JSONArray();
                for (String v : value.split(", ?")) {
                    if (!v.isEmpty()) arr.put(v.trim());
                }
                obj.put(key, arr);
            } else {
                obj.put(key, value);
            }
        }
        Matcher userMatcher = Pattern.compile("User (\\d+)\\n([\\s\\S]+?)(?=\\n\\s*\\w|$)").matcher(section);
        while (userMatcher.find()) {
            String userKey = "User " + userMatcher.group(1);
            String pkgs = userMatcher.group(2);
            JSONArray pkgArr = new JSONArray();
            for (String line : pkgs.split("\n")) {
                line = line.trim();
                if (!line.isEmpty()) pkgArr.put(line);
            }
            obj.put(userKey, pkgArr);
        }
        return obj;
    }

    private JSONArray parsePendingAlarms(String section) {
        JSONArray alarms = new JSONArray();
        // 修正正则，支持前面有空格，且允许行首不是严格的alarm类型
        Pattern alarmStartPattern = Pattern.compile("^\s*(ELAPSED|ELAPSED_WAKEUP|RTC|RTC_WAKEUP) #(\\d+): (Alarm\\{.*?)(?=(^\\s*(ELAPSED|ELAPSED_WAKEUP|RTC|RTC_WAKEUP) #(\\d+):)|(\\s{2}))", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = alarmStartPattern.matcher(section);
        while (m.find()) {
            JSONObject alarm = new JSONObject();
            alarm.put("type", m.group(1));
            alarm.put("number", m.group(2));
            String details = m.group(3).trim();
            String[] tmpArray = details.split("\\n", 2);
            alarm.put("alarmInfo", tmpArray[0]);
            details = details.replace(tmpArray[0], "");
            // 提取 tag、type、origWhen、window、repeatInterval、count、flags、operation、listener、idle-options 等字段
            Pattern fieldPattern = Pattern.compile(
                "tag=([^\n]+)|type=([^\\s]+)|origWhen=([^\\s]+)|window=([^\\s]+)|repeatInterval=([^\\s]+)|count=([^\\s]+)|flags=([^\\s]+)|operation=([^\n]+)|listener=([^\n]+)|idle-options=([^\n]+)",
                Pattern.MULTILINE);
            Matcher fieldMatcher = fieldPattern.matcher(details);
            while (fieldMatcher.find()) {
                if (fieldMatcher.group(1) != null) alarm.put("tag", fieldMatcher.group(1).trim());
                if (fieldMatcher.group(2) != null) alarm.put("alarmType", fieldMatcher.group(2).trim());
                if (fieldMatcher.group(3) != null) alarm.put("origWhen", fieldMatcher.group(3).trim());
                if (fieldMatcher.group(4) != null) alarm.put("window", fieldMatcher.group(4).trim());
                if (fieldMatcher.group(5) != null) alarm.put("repeatInterval", fieldMatcher.group(5).trim());
                if (fieldMatcher.group(6) != null) alarm.put("count", fieldMatcher.group(6).trim());
                if (fieldMatcher.group(7) != null) alarm.put("flags", fieldMatcher.group(7).trim());
                if (fieldMatcher.group(8) != null) alarm.put("operation", fieldMatcher.group(8).trim());
                if (fieldMatcher.group(9) != null) alarm.put("listener", fieldMatcher.group(9).trim());
                if (fieldMatcher.group(10) != null) alarm.put("idleOptions", fieldMatcher.group(10).trim());
            }
            // 额外提取 policyWhenElapsed、whenElapsed、maxWhenElapsed、exactAllowReason、procName、PendingIntent、等
            Pattern extraPattern = Pattern.compile(
                "policyWhenElapsed: ([^\n]+)|whenElapsed=([^\\s]+)|maxWhenElapsed=([^\\s]+)|exactAllowReason=([^\\s]+)|procName ([^\\s]+)|PendingIntent\\{([^:]+): ([^}]+)\\}",
                Pattern.MULTILINE);
            Matcher extraMatcher = extraPattern.matcher(details);
            while (extraMatcher.find()) {
                if (extraMatcher.group(1) != null) alarm.put("policyWhenElapsed", extraMatcher.group(1).trim());
                if (extraMatcher.group(2) != null) alarm.put("whenElapsed", extraMatcher.group(2).trim());
                if (extraMatcher.group(3) != null) alarm.put("maxWhenElapsed", extraMatcher.group(3).trim());
                if (extraMatcher.group(4) != null) alarm.put("exactAllowReason", extraMatcher.group(4).trim());
                if (extraMatcher.group(5) != null) alarm.put("procName", extraMatcher.group(5).trim());
                if (extraMatcher.group(6) != null && extraMatcher.group(7) != null) {
                    alarm.put("pendingIntent", extraMatcher.group(7).trim());
                }
            }
            alarm.put("rawDetails", details); // 保留原始内容
            alarms.put(alarm);
        }
        return alarms;
    }
}
