package edu.jiangxin.apktoolbox.help;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.jiangxin.apktoolbox.utils.Utils;

public class AboutMouseListener extends MouseAdapter {

	@Override
	public void mousePressed(MouseEvent e) {
		super.mouseClicked(e);
		AboutFrame aboutFrame = new AboutFrame();
		aboutFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(aboutFrame);
	}

}
