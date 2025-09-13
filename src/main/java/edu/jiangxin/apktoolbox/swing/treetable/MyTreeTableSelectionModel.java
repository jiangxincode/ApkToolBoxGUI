package edu.jiangxin.apktoolbox.swing.treetable;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import java.io.Serial;

public class MyTreeTableSelectionModel extends DefaultTreeSelectionModel {
    @Serial
    private static final long serialVersionUID = 1L;

    public MyTreeTableSelectionModel() {
        super();

        getListSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

            }
        });
    }

    ListSelectionModel getListSelectionModel() {
        return listSelectionModel;
    }
}
