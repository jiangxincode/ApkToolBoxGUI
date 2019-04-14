package edu.jiangxin.apktoolbox.text.zhconvert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 中文简体繁体转换工具
 * 
 * @author jiangxin
 * @author 2018-10-28
 * @version
 *
 */
public class ZhConverter {

    private Properties charMap = new Properties();

    public static final int TRADITIONAL = 0;
    public static final int SIMPLIFIED = 1;
    private static final int NUM_OF_CONVERTERS = 2;
    private static final ZhConverter[] CONVERTERS = new ZhConverter[NUM_OF_CONVERTERS];
    private static final String[] PROPERTY_FILES = new String[2];
    
    private static final Logger LOGGER = LogManager.getLogger(ZhConverter.class);

    static {
        PROPERTY_FILES[TRADITIONAL] = "zh2Hant.properties";
        PROPERTY_FILES[SIMPLIFIED] = "zh2Hans.properties";
    }

    /**
     * @param converterType 0 for traditional and 1 for simplified
     * @return
     */
    public static ZhConverter getInstance(int converterType) {

        if (converterType >= 0 && converterType < NUM_OF_CONVERTERS) {

            if (CONVERTERS[converterType] == null) {
                synchronized (ZhConverter.class) {
                    if (CONVERTERS[converterType] == null) {
                        CONVERTERS[converterType] = new ZhConverter(PROPERTY_FILES[converterType]);
                    }
                }
            }
            return CONVERTERS[converterType];

        } else {
            return null;
        }
    }

    private ZhConverter(String propertyFile) {

        InputStream is = this.getClass().getResourceAsStream(propertyFile);

        if (is != null) {
            BufferedReader reader = null;
            try {
                // 目前仅支持"UTF-8"
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                charMap.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("close reader failed");
                }
            }
        }
    }

    public static String convert(String text, int converterType) {
        ZhConverter instance = getInstance(converterType);
        return instance.convert(text);
    }

    /**
     * @param in
     * @return
     */
    public String convert(String in) {
        StringBuilder outString = new StringBuilder();
        StringBuilder stackString = new StringBuilder();

        for (int i = 0; i < in.length(); i++) {

            char c = in.charAt(i);
            String key = "" + c;
            stackString.append(key);
            if (charMap.containsKey(stackString.toString())) {
                outString.append(charMap.get(stackString.toString()));
                stackString.setLength(0);
            } else {
                CharSequence sequence = stackString.subSequence(0, stackString.length() - 1);
                stackString.delete(0, stackString.length() - 1);
                flushStack(outString, new StringBuilder(sequence));
            }
        }

        flushStack(outString, stackString);

        return outString.toString();
    }

    private void flushStack(StringBuilder outString, StringBuilder stackString) {
        while (stackString.length() > 0) {
            if (charMap.containsKey(stackString.toString())) {
                outString.append(charMap.get(stackString.toString()));
                stackString.setLength(0);

            } else {
                outString.append("" + stackString.charAt(0));
                stackString.delete(0, 1);
            }

        }
    }

    public Properties getDict() {
        return charMap;
    }

}
