package edu.jiangxin.apktoolbox.file.zhconvert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Properties;

public class ZHConverterUtils {
    private static final Logger logger = LogManager.getLogger(ZHConverterUtils.class);

    private Properties charMap = new Properties();

    private Properties charMap2 = new Properties();

    public ZHConverterUtils() {
        String dirpath = "zhSimple2zhTw.properties";
        String dirpath2 = "zhTw2zhSimple.properties";
        initProperties(dirpath,charMap);
        initProperties(dirpath2,charMap2);
    }

    private void initProperties(String rPath, Properties properties) {
        File file = new File(rPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("createNewFile failed: " + e.getMessage());
                return;
            }
        }

        try (InputStream is = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            properties.load(reader);
        } catch (IOException e) {
            logger.error("load failed: " + e.getMessage());
        }
    }


    /**
     *
     * @param str
     * @return
     */
    public String myConvertToTW(String str){
        Iterator iterator = charMap.keySet().iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            if (key.length() >= 1) {
                if (str.contains(key)){
                    str = str.replaceAll(key,charMap.getProperty(key));
                }
            }
        }
        return str;
    }

    /**
     *
     * @param str
     * @return
     */
    public String myConvertToSimple(String str){
        Iterator iterator = charMap2.keySet().iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            if (key.length() >= 1) {
                if (str.contains(key)){
                    str = str.replaceAll(key,charMap2.getProperty(key));
                }
            }
        }
        return str;
    }


    /**
     * 加入新词组对应
     * @param key
     * @param value
     */
    public void storeDataToProperties(String key,String value) {
        charMap.setProperty(key,value);
        String filePath = "zhSimple2zhTw.properties";
        try (OutputStream out = new FileOutputStream(filePath)) {
            charMap.store(out, "加入新元素");
        } catch (IOException e) {
            logger.error("storeDataToProperties failed: IOException");
        }
        charMap2.setProperty(value,key);
        String filePath2 = "zhTw2zhSimple.properties";
        try (OutputStream out2 = new FileOutputStream(filePath2)) {
            charMap2.store(out2, "加入新元素");
        } catch (IOException e) {
            logger.error("storeDataToProperties failed: IOException");
        }
    }

    public Properties getCharMap() {
        return charMap;
    }



}
