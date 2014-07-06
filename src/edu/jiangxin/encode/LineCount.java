/**
 * 文件行数统计
 * @author jiangxin
 */
package edu.jiangxin.encode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;

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
        FileReader in = new FileReader(fileName);
        LineNumberReader reader = new LineNumberReader(in);
        String strLine = reader.readLine();
        int totalLines = 0;
        while (strLine != null) {
            totalLines++;
            strLine = reader.readLine();
        }
        reader.close();
        in.close();
        return totalLines;
    }
    public static int getTotalLines2(String fileName) throws IOException {
    	BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
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
                if (c[i] == '\n')
                    ++count;
            }
        }
        is.close();
        return count;
    }
}
