package edu.jiangxin.apktoolbox.help;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ContributeMouseListener extends MouseAdapter {
	@Override
	public void mousePressed(MouseEvent e) {
		super.mouseClicked(e);
		URI uri;
		try {
			uri = new URI("https://github.com/jiangxincode/ApkToolBoxGUI");
			Desktop.getDesktop().browse(uri);
		} catch (URISyntaxException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
