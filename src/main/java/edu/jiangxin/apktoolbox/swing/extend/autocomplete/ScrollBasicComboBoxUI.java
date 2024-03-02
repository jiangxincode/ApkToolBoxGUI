package edu.jiangxin.apktoolbox.swing.extend.autocomplete;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

class ScrollBasicComboBoxUI extends BasicComboBoxUI {
    @Override
    protected ComboPopup createPopup() {
        return new BasicComboPopup(comboBox) {
            private static final long serialVersionUID = 1L;

            @Override
            protected JScrollPane createScroller() {
                return new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            }
        };
    }

}
