package edu.jiangxin.apktoolbox.reverse;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.jiangxin.apktoolbox.utils.Utils;

public class ApkSignerMouseListener extends MouseAdapter {
	@Override
	public void mousePressed(MouseEvent e) {
		super.mouseClicked(e);
		ApkSignerFrame apktoolDecodeFrame = new ApkSignerFrame();
		apktoolDecodeFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(apktoolDecodeFrame);
	}

}
