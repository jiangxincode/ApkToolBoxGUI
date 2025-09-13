package edu.jiangxin.apktoolbox.swing.treetable;

import java.awt.Dimension;
import java.io.Serial;

import javax.swing.JTable;

public class MyTreeTable extends JTable {
    @Serial
    private static final long serialVersionUID = 1L;

    public MyTreeTable(MyAbstractTreeTableModel treeTableModel) {
        super();

        // JTree erstellen.
        MyTreeTableCellRenderer tree = new MyTreeTableCellRenderer(this, treeTableModel);

        // Modell setzen.
        super.setModel(new MyTreeTableModelAdapter(treeTableModel, tree));

        // Gleichzeitiges Selektieren fuer Tree und Table.
        MyTreeTableSelectionModel selectionModel = new MyTreeTableSelectionModel();
        tree.setSelectionModel(selectionModel); //For the tree
        setSelectionModel(selectionModel.getListSelectionModel()); //For the table


        // Renderer fuer den Tree.
        setDefaultRenderer(MyTreeTableModel.class, tree);
        // Editor fuer die TreeTable
        setDefaultEditor(MyTreeTableModel.class, new MyTreeTableCellEditor(tree, this));

        // Kein Grid anzeigen.
        setShowGrid(false);

        // Keine Abstaende.
        setIntercellSpacing(new Dimension(0, 0));

    }
}
