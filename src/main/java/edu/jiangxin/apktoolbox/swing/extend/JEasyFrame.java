package edu.jiangxin.apktoolbox.swing.extend;

import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.jiangxin.apktoolbox.utils.Utils;

public class JEasyFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	protected Logger logger;
	protected Configuration conf;
	protected ResourceBundle bundle;

	public JEasyFrame() throws HeadlessException {
		super();
		logger = LogManager.getLogger(this.getClass().getSimpleName());
		conf = Utils.getConfiguration();
		bundle = ResourceBundle.getBundle("apktoolbox");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				Utils.saveConfiguration();
				logger.info("Frame stop: " + JEasyFrame.this.getClass().getSimpleName());
			}
		});
		logger.info("Frame start: " + this.getClass().getSimpleName());
	}
}
