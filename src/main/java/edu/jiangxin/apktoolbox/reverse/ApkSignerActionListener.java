package edu.jiangxin.apktoolbox.reverse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.jiangxin.apktoolbox.utils.Utils;

public class ApkSignerActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ApkSignerFrame apktoolDecodeFrame = new ApkSignerFrame();
        apktoolDecodeFrame.setVisible(true);
        Utils.setJFrameCenterInScreen(apktoolDecodeFrame);
    }

}
