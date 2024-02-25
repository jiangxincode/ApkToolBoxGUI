package edu.jiangxin.apktoolbox.help;

import java.awt.Component;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JOptionPane;

import edu.jiangxin.apktoolbox.swing.extend.listener.ChangeMenuListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import edu.jiangxin.apktoolbox.Version;

/**
 * @author jiangxin
 * @author 2018-09-30
 *
 */
public class CheckUpdateActionListener implements ChangeMenuListener {
    private static final int SOCKET_TIMEOUT_TIME = 4000;
    
    private static final int CONNECT_TIMEOUT_TIME = 4000;

    private static final Logger logger = LogManager.getLogger(CheckUpdateActionListener.class.getSimpleName());

    private Component parent;

    private CloseableHttpClient closeableHttpClient;

    private CloseableHttpResponse closeableHttpResponse;

    public CheckUpdateActionListener(Component component) {
        super();
        parent = component;
    }

    @Override
    public void onChangeMenu() {
        String responseString = null;

        closeableHttpClient = HttpClients.createDefault();

        try {
            HttpGet httpGet = new HttpGet(Constant.URL_CHECK_UPDATE);
            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT_TIME)
                    .setConnectTimeout(CONNECT_TIMEOUT_TIME).build();
            httpGet.setConfig(requestConfig);
            closeableHttpResponse = closeableHttpClient.execute(httpGet);
            logger.info("execute request finished");
        } catch (IOException ex) {
            processException(ex);
            return;
        }

        StatusLine statusLine = closeableHttpResponse.getStatusLine();
        if (statusLine == null) {
            processException(new Exception("statusLine is null"));
            return;
        }

        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = closeableHttpResponse.getEntity();
            try {
                responseString = EntityUtils.toString(entity);
                logger.info(responseString);
            } catch (ParseException | IOException ex) {
                processException(ex);
                return;
            }
        } else {
            processException(new Exception("invalid statusCode"));
            return;
        }

        JSONObject release;
        try {
            release = new JSONObject(responseString);
        } catch (JSONException ex) {
            processException(ex);
            return;
        }
        String latestVersion = release.getString("tag_name");
        if (StringUtils.isEmpty(latestVersion)) {
            processException(new Exception("latestVersion is empyt"));
            return;
        }
        processResult(latestVersion);
    }

    private void processException(Exception ex) {
        logger.error("checking for updates failed: ", ex);
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(parent, "checking for updates failed", "ERROR", JOptionPane.ERROR_MESSAGE);
        releaseResource();
    }

    private void processResult(String latestVersion) {
        logger.info("checking for updates successed");
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(parent,
                "Latest version: " + latestVersion + "\nLocal version: " + Version.VERSION, "Update",
                JOptionPane.INFORMATION_MESSAGE);
        releaseResource();
    }

    private void releaseResource() {
        if (closeableHttpResponse != null) {
            try {
                closeableHttpResponse.close();
            } catch (IOException e) {
                logger.error("closeableHttpResponse close failed", e);
            }
        }
        if (closeableHttpClient != null) {
            try {
                closeableHttpClient.close();
            } catch (IOException e) {
                logger.error("closeableHttpClient close failed", e);
            }
        }
    }

}
