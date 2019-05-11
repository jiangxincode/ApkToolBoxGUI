package edu.jiangxin.apktoolbox.text.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 文件行数统计
 * 
 * @author jiangxin
 * @author 2018-09-09
 */
public class LineCount {
    private static final Logger logger = LogManager.getLogger(LineCount.class);
    
    public static void main(String[] args) {
        String fileName = "temp/linux.txt";
        long time = System.currentTimeMillis();
        System.out.println("LineNumberReader" + getTotalLines(fileName));
        System.out.println(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        System.out.println("BufferedReader" + getTotalLines2(fileName));
        System.out.println(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        System.out.println("BufferedInputStream" + count(fileName));
        System.out.println(System.currentTimeMillis() - time);
    }

    public static int getTotalLines(String fileName) {
        // 目前仅支持UTF-8
        LineNumberReader reader = null;
        int totalLines = 0;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            String strLine = reader.readLine();
            while (strLine != null) {
                totalLines++;
                strLine = reader.readLine();
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                logger.error("IOException", e);
            }
        }
        return totalLines;
    }

    public static int getTotalLines2(String fileName) {
        // 目前仅支持UTF-8
        BufferedReader in = null;
        int totalLines = 0;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            String strLine = in.readLine();
            while (strLine != null) {
                totalLines++;
                strLine = in.readLine();
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("IOException", e);
                }
            }
        }
        return totalLines;
    }

    public static int count(String filename) {
        InputStream is = null;
        int count = 0;
        try {
            is = new BufferedInputStream(new FileInputStream(filename));
            byte[] c = new byte[1024];
            int readChars = 0;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("IOException", e);
                }
            }
        }
        return count;
    }
}
