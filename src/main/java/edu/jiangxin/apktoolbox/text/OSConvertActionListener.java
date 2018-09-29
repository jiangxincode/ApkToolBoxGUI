package edu.jiangxin.apktoolbox.text;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.jiangxin.apktoolbox.utils.Utils;

public class OSConvertActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		OSConvertFrame oSConvertFrame = new OSConvertFrame();
		oSConvertFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(oSConvertFrame);
	}

}
