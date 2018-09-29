package edu.jiangxin.apktoolbox.help;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.jiangxin.apktoolbox.utils.Utils;

public class AboutActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		AboutFrame aboutFrame = new AboutFrame();
		aboutFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(aboutFrame);
	}

}
