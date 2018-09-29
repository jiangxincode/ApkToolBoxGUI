package edu.jiangxin.apktoolbox.reverse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.jiangxin.apktoolbox.utils.Utils;

public class ApktoolDecodeActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		ApktoolDecodeFrame apkSignerFrame = new ApktoolDecodeFrame();
		apkSignerFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(apkSignerFrame);
	}
}
