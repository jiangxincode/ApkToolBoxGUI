package edu.jiangxin.apktoolbox.help;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
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
 * 
 * @author jiangxin
 *
 */
public class CheckUpdateActionListener implements ActionListener {
    private static final String URI = "https://api.github.com/repos/jiangxincode/ApkToolBoxGUI/releases/latest";
    private static Logger logger = LogManager.getLogger(CheckUpdateActionListener.class);
    private Component parent;
    private CloseableHttpClient closeableHttpClient;
    private CloseableHttpResponse closeableHttpResponse;

    public CheckUpdateActionListener(Component component) {
	super();
	parent = component;
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

    @Override
    public void actionPerformed(ActionEvent e) {
	String responseString = null;

	closeableHttpClient = HttpClients.createDefault();

	try {
	    closeableHttpResponse = closeableHttpClient.execute(new HttpGet(URI));
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

	if (statusLine.getStatusCode() == 200) {
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

}
