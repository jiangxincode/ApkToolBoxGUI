package edu.jiangxin.apktoolbox.android.dumpsys.tojson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Alarm2JsonTest {
    @Test
    public void testDumpsys2Json_parsesAlarmDumpCorrectly() throws Exception {
        URL resource = getClass().getClassLoader().getResource("dumpsys/alarm.dumpsys");
        assertNotNull(resource, "Test resource not found");
        File file = new File(resource.toURI());

        Alarm2Json parser = new Alarm2Json();
        String jsonStr = parser.dumpsys2Json(file);
        JSONObject result = new JSONObject(jsonStr);
        JSONArray pendingAlarms = result.getJSONArray("119 pending alarms");
        assertEquals(119, pendingAlarms.length());
    }
}

