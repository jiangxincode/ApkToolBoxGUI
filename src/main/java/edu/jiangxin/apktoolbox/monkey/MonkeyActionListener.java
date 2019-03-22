package edu.jiangxin.apktoolbox.monkey;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.jiangxin.apktoolbox.utils.Utils;

public class MonkeyActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        MonkeyFrame monkeyJFrame = new MonkeyFrame();
        monkeyJFrame.setVisible(true);
        Utils.setJFrameCenterInScreen(monkeyJFrame);
    }

}
