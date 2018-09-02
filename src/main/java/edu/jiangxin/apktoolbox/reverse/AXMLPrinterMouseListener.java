package edu.jiangxin.apktoolbox.reverse;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.jiangxin.apktoolbox.utils.Utils;

public class AXMLPrinterMouseListener extends MouseAdapter {
	@Override
	public void mousePressed(MouseEvent e) {
		super.mouseClicked(e);
		AXMLPrinterFrame arm2asmFrame = new AXMLPrinterFrame();
		arm2asmFrame.setVisible(true);
		Utils.setJFrameCenterInScreen(arm2asmFrame);
	}
}
