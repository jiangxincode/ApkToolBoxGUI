package edu.jiangxin.apktoolbox.text.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * 文件行数统计
 * 
 * @author jiangxin
 * @author 2018-09-09
 */
public class LineCount {
    public static void main(String[] args) throws IOException {
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

    public static int getTotalLines(String fileName) throws IOException {
        // 目前仅支持UTF-8
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        String strLine = reader.readLine();
        int totalLines = 0;
        while (strLine != null) {
            totalLines++;
            strLine = reader.readLine();
        }
        reader.close();
        return totalLines;
    }

    public static int getTotalLines2(String fileName) throws IOException {
        // 目前仅支持UTF-8
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        String strLine = in.readLine();
        int totalLines = 0;
        while (strLine != null) {
            totalLines++;
            strLine = in.readLine();
        }
        in.close();
        return totalLines;
    }

    public static int count(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        byte[] c = new byte[1024];
        int count = 0;
        int readChars = 0;
        while ((readChars = is.read(c)) != -1) {
            for (int i = 0; i < readChars; ++i) {
                if (c[i] == '\n') {
                    ++count;
                }
            }
        }
        is.close();
        return count;
    }
}
