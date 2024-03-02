package edu.jiangxin.apktoolbox.swing.extend.autocomplete;

import javax.swing.*;
import java.io.Serial;
import java.util.Vector;

/**
 * @author jiangxin
 * @author 2018-09-09
 *
 */
public class AutoCompleteComboBox<E> extends JComboBox<E> {

    @Serial
    private static final long serialVersionUID = 1L;
    private transient AutoCompleter<E> completer;

    public AutoCompleteComboBox() {
        super();
        setUI(new ScrollBasicComboBoxUI());
        addCompleter();
    }

    public AutoCompleteComboBox(ComboBoxModel<E> cm) {
        super(cm);
        addCompleter();
    }

    public AutoCompleteComboBox(E[] items) {
        super(items);
        addCompleter();
    }

    public AutoCompleteComboBox(Vector<E> v) {
        super(v);
        addCompleter();
    }

    private void addCompleter() {
        setEditable(true);
        completer = new AutoCompleter<>(this);
    }

    public void autoComplete(String str) {
        this.completer.autoComplete(str);
    }

    public String getText() {
        return ((JTextField) getEditor().getEditorComponent()).getText();
    }

    public void setText(String text) {
        ((JTextField) getEditor().getEditorComponent()).setText(text);
    }

    public boolean containsItem(String itemString) {
        for (int i = 0; i < this.getModel().getSize(); i++) {
            String item = " " + this.getModel().getElementAt(i);
            if (item.equals(itemString)) {
                return true;
            }
        }
        return false;
    }
}

