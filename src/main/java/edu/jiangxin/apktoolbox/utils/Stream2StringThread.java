package edu.jiangxin.apktoolbox.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Stream2StringThread extends Thread {
    private static final Logger logger = LogManager.getLogger(Stream2StringThread.class);

    private StringBuilder tmpStringBuilder = new StringBuilder();

    private InputStream inputStream;

    public Stream2StringThread(InputStream is) {
        this.inputStream = is;
    }

    @Override
    public void run() {
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(inputStream, "UTF-8");
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                tmpStringBuilder.append(line).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            logger.error("read bis error", e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException ex) {
                logger.error("close error", ex);
            }
        }
    }

    public String getOutputString() {
        return tmpStringBuilder.toString();
    }
}
