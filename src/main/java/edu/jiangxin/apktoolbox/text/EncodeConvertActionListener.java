package edu.jiangxin.apktoolbox.text;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.jiangxin.apktoolbox.utils.Utils;

public class EncodeConvertActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        EncodeConvertFrame encodeConvertFrame = new EncodeConvertFrame();
        encodeConvertFrame.setVisible(true);
        Utils.setJFrameCenterInScreen(encodeConvertFrame);
        encodeConvertFrame.pack();
    }

}
