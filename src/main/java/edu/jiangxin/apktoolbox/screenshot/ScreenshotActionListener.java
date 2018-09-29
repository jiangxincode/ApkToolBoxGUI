package edu.jiangxin.apktoolbox.screenshot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.jiangxin.apktoolbox.utils.Utils;

public class ScreenshotActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		ScreenShotFrame screenShotFrame = new ScreenShotFrame();
		screenShotFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(screenShotFrame);
	}
}
