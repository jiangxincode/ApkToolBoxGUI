package edu.jiangxin.apktoolbox.i18n;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.jiangxin.apktoolbox.utils.Utils;

public class I18NAddActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		I18NAddFrame i18NAddFrame = new I18NAddFrame();
		i18NAddFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(i18NAddFrame);

	}
}
