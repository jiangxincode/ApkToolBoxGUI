package edu.jiangxin.apktoolbox.reverse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.jiangxin.apktoolbox.utils.Utils;

public class AXMLPrinterActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		AXMLPrinterFrame arm2asmFrame = new AXMLPrinterFrame();
		arm2asmFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(arm2asmFrame);
	}
}
