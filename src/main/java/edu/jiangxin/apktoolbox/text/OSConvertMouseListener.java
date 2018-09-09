package edu.jiangxin.apktoolbox.text;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.jiangxin.apktoolbox.utils.Utils;

public class OSConvertMouseListener  extends MouseAdapter{
	@Override
	public void mousePressed(MouseEvent e) {
		super.mouseClicked(e);
		OSConvertFrame oSConvertFrame = new OSConvertFrame();
		oSConvertFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(oSConvertFrame);
	}

}
