package edu.jiangxin.apktoolbox.help;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import edu.jiangxin.apktoolbox.Version;
import edu.jiangxin.apktoolbox.swing.extend.JEasyFrame;

public class AboutFrame extends JEasyFrame {

	private static final long serialVersionUID = 1L;

	public AboutFrame() throws HeadlessException {
		super();
		setTitle(bundle.getString("help.about.title"));
		setSize(600, 400);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		BoxLayout boxLayout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
		contentPane.setLayout(boxLayout);
		setContentPane(contentPane);

		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		StringBuffer stringBuffer = new StringBuffer();
		try {
			inputStream = AboutFrame.class.getResourceAsStream("/about.html");
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
		} catch (IOException ex) {
			logger.error("processing file failed", ex);
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception ex) {
				logger.error("close bufferedReader failed", ex);
			}

			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception ex) {
				logger.error("close inputStream failed", ex);
			}

		}

		JEditorPane editorPane = new JEditorPane("text/html",
				stringBuffer.toString().replace("{VERSION}", Version.VERSION));

		JScrollPane scrollPane = new JScrollPane(editorPane);

		contentPane.add(scrollPane);
	}

}
