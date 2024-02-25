package edu.jiangxin.apktoolbox.swing.extend.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public interface ChangeMenuListener extends ActionListener {

    @Override
    default void actionPerformed(ActionEvent e) {
        if (isNeedPreChangeMenu()) {
            onPreChangeMenu(this::onChangeMenu);
        } else {
            onChangeMenu();
        }
    }

    default boolean isNeedPreChangeMenu() {
        return false;
    }

    default void onPreChangeMenu(IPreChangeMenuCallBack callBack) {
        // do nothing
    }

    void onChangeMenu();
}
