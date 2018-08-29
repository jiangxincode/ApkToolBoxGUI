package edu.jiangxin.apktoolbox.monkey;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.jiangxin.apktoolbox.utils.Utils;

public class MonkeyMouseListener extends MouseAdapter {
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mouseClicked(e);
		MonkeyFrame monkeyJFrame = new MonkeyFrame();
		monkeyJFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(monkeyJFrame);
	}

}
