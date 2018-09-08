package edu.jiangxin.apktoolbox.i18n;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.jiangxin.apktoolbox.utils.Utils;

public class I18NAddMouseListener extends MouseAdapter {
	@Override
	public void mousePressed(MouseEvent e) {
		super.mouseClicked(e);
		I18NAddFrame i18NAddFrame = new I18NAddFrame();
		i18NAddFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(i18NAddFrame);
	}
}
