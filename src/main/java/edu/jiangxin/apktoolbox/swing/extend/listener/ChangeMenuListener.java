package edu.jiangxin.apktoolbox.swing.extend.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ChangeMenuListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (onPreChangeMenu()) {
            onChangeMenu();
        }
    }

    public abstract boolean onPreChangeMenu();

    public abstract void onChangeMenu();
}
