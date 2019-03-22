package edu.jiangxin.apktoolbox.i18n;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.jiangxin.apktoolbox.utils.Utils;

public class I18NFindLongestActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        I18NFindLongestFrame i18NGetLongestFrame = new I18NFindLongestFrame();
        i18NGetLongestFrame.setVisible(true);
        Utils.setJFrameCenterInScreen(i18NGetLongestFrame);

    }

}
