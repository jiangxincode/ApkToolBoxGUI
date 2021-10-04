package edu.jiangxin.apktoolbox.text.zhconvert;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Properties;

public class ZHConverterUtils {
    private Properties charMap = new Properties();

    private Properties charMap2 = new Properties();

    private String path = System.getProperty("user.dir");

    public ZHConverterUtils() {
        String dirpath = path + "/zhSimple2zhTw.properties";
        String dirpath2 = path+"/zhTw2zhSimple.properties";
//        initProperties("/zhSimple2zhTw.properties",charMap);
//        initProperties("/zhTw2zhSimple.properties",charMap2);
        initProperties(dirpath,charMap);
        initProperties(dirpath2,charMap2);
    }


    private void initProperties(String rPath,Properties properties){
        InputStream is = null;
//        is = getClass().getResourceAsStream(rPath);
        try {

            File file = new File(rPath);
            if (!file.exists()){
                file.createNewFile();
            }
            is = new FileInputStream(file);
            if (is != null) {
                BufferedReader reader = null;
                try {

                    reader = new BufferedReader(new InputStreamReader(is));
                    properties.load(reader);
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
     * @throws URISyntaxException
     * @throws IOException
     */
    public void storeDataToProperties(String key,String value) throws URISyntaxException, IOException {
        charMap.setProperty(key,value);
//        String path = System.getProperty("user.dir");
//        System.out.println(path);
//        System.out.println(path + "/zhSimple2zhTw.properties");
//        String filePath = getClass().getResource("/zhSimple2zhTw.properties").toURI().getPath();
        String filePath = path + "/zhSimple2zhTw.properties";
        System.out.println(filePath);
        OutputStream out = new FileOutputStream(path + "/zhSimple2zhTw.properties");
        charMap.store(out,"加入新元素");
        out.close();
//
        charMap2.setProperty(value,key);
        String filePath2 = path+"/zhTw2zhSimple.properties";
//        String filePath2 = getClass().getResource("/zhTw2zhSimple.properties").toURI().getPath();
        OutputStream out2 = new FileOutputStream(filePath2);
        charMap2.store(out2,"加入新元素");
        out2.close();

    }

    public Properties getCharMap() {
        return charMap;
    }



}
