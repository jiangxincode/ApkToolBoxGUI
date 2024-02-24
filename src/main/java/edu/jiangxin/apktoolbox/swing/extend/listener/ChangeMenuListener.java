package edu.jiangxin.apktoolbox.swing.extend.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ChangeMenuListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        onPreChangeMenu(this::onChangeMenu);
    }

    public abstract void onPreChangeMenu(IFinishCallBack callBack);

    public abstract void onChangeMenu();
}
