package edu.jiangxin.apktoolbox.text;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.jiangxin.apktoolbox.utils.Utils;

public class EncodeConvertMouseListener  extends MouseAdapter{
	@Override
	public void mousePressed(MouseEvent e) {
		super.mouseClicked(e);
		EncodeConvertFrame encodeConvertFrame = new EncodeConvertFrame();
		encodeConvertFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(encodeConvertFrame);
		encodeConvertFrame.pack();
	}

}
