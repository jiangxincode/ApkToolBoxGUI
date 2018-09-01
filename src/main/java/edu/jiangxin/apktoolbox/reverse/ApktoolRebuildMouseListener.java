package edu.jiangxin.apktoolbox.reverse;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.jiangxin.apktoolbox.utils.Utils;

public class ApktoolRebuildMouseListener extends MouseAdapter {
	@Override
	public void mousePressed(MouseEvent e) {
		super.mouseClicked(e);
		ApktoolRebuildFrame apktoolRebuildFrame = new ApktoolRebuildFrame();
		apktoolRebuildFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(apktoolRebuildFrame);
	}

}
