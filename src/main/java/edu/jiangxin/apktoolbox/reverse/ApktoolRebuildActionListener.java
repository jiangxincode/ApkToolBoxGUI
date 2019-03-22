package edu.jiangxin.apktoolbox.reverse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.jiangxin.apktoolbox.utils.Utils;

public class ApktoolRebuildActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ApktoolRebuildFrame apktoolRebuildFrame = new ApktoolRebuildFrame();
        apktoolRebuildFrame.setVisible(true);
        Utils.setJFrameCenterInScreen(apktoolRebuildFrame);
    }

}
