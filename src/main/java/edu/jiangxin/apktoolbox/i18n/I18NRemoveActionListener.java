package edu.jiangxin.apktoolbox.i18n;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.jiangxin.apktoolbox.utils.Utils;

public class I18NRemoveActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        I18NRemoveFrame i18NRemoveFrame = new I18NRemoveFrame();
        i18NRemoveFrame.setVisible(true);
        Utils.setJFrameCenterInScreen(i18NRemoveFrame);
    }
}
