package edu.jiangxin.apktoolbox.help;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import edu.jiangxin.apktoolbox.Version;

public class CheckUpdateMouseListener extends MouseAdapter {
	private static final String URI = "https://api.github.com/repos/jiangxincode/ApkToolBoxGUI/releases/latest";
	private static Logger logger = LogManager.getLogger(CheckUpdateMouseListener.class);
	private Component parent;

	public CheckUpdateMouseListener(Component component) {
		super();
		parent = component;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		super.mouseClicked(e);

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpResponse response;
		String responseString = null;
		try {
			response = httpclient.execute(new HttpGet(URI));
			StatusLine statusLine = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			if (statusLine.getStatusCode() == 200) {
				logger.info("200: ");
				responseString = EntityUtils.toString(entity, "UTF-8");
			} else {
				entity.getContent().close();
			}
		} catch (Exception ex) {
		} finally {
			try {
				httpclient.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		try {
			JSONObject release = new JSONObject(responseString);
			String latestVersion = release.getString("tag_name");
			logger.info("latestVersion: " + latestVersion);
			if (latestVersion.compareTo(Version.VERSION) > 0) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(parent, "New release exists", "Update", JOptionPane.INFORMATION_MESSAGE);
			} else {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(parent, "It is the latest", "Update", JOptionPane.INFORMATION_MESSAGE);
			}
			boolean isPreRelease = release.getBoolean("prerelease");
			logger.info("prerelease: " + isPreRelease);
			String downloadUrl = release.getJSONArray("assets").getJSONObject(0).getString("browser_download_url");
			logger.info("downloadUrl: " + downloadUrl);
		} catch (Exception ex) {
		}
	}

}
