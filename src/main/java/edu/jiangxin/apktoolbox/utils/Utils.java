package edu.jiangxin.apktoolbox.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Utils {

	public static void setJFrameCenterInScreen(JFrame jFrame) {
		int windowWidth = jFrame.getWidth();
		int windowHeight = jFrame.getHeight();
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		jFrame.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);//设置窗口居中显示 
	}

}
