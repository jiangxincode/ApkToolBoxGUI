package edu.jiangxin.apktoolbox.swing.extend;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import org.apache.commons.lang3.StringUtils;

/**
 * @author jiangxin
 * @author 2018-09-09
 *
 */
public class JAutoCompleteComboBox<E> extends JComboBox<E> {

    private static final long serialVersionUID = 1L;
    private AutoCompleter<E> completer;

    public JAutoCompleteComboBox() {
        super();
        setUI(new ScrollBasicComboBoxUI());
        addCompleter();
    }

    public JAutoCompleteComboBox(ComboBoxModel<E> cm) {
        super(cm);
        addCompleter();
    }

    public JAutoCompleteComboBox(E[] items) {
        super(items);
        addCompleter();
    }

    public JAutoCompleteComboBox(Vector<E> v) {
        super(v);
        addCompleter();
    }

    private void addCompleter() {
        setEditable(true);
        completer = new AutoCompleter<E>(this);
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
            String _item = " " + this.getModel().getElementAt(i);
            if (_item.equals(itemString)) {
                return true;
            }
        }
        return false;
    }
}

class ScrollBasicComboBoxUI extends BasicComboBoxUI {
    @Override
    protected ComboPopup createPopup() {
        BasicComboPopup popup = new BasicComboPopup(comboBox) {
            private static final long serialVersionUID = 1L;

            @Override
            protected JScrollPane createScroller() {
                return new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            }
        };
        return popup;
    }

}

class AutoCompleter<E> implements KeyListener {

    private JComboBox<E> comboBox = null;
    private JTextField textField = null;
    private ComboBoxModel<E> comboBoxModel = null;

    public AutoCompleter(JComboBox<E> comboBox) {
        if (comboBox == null) {
            throw new RuntimeException("comboBox is null");
        }
        this.comboBox = comboBox;
        ComboBoxEditor comboBoxEditor = comboBox.getEditor();
        if (comboBoxEditor == null) {
            throw new RuntimeException("comboBoxEditor is null");
        }
        Component component = comboBoxEditor.getEditorComponent();
        if (!(component instanceof JTextField)) {
            throw new RuntimeException("component is invalid");
        }
        this.textField = (JTextField) component;
        this.textField.addKeyListener(this);
        this.comboBoxModel = comboBox.getModel();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (Character.isAlphabetic(keyChar) || Character.isDigit(keyChar) || Character.isWhitespace(keyChar)
                || keyChar == KeyEvent.VK_BACK_SPACE) {
            String text = textField.getText();
            autoComplete(text);
            textField.setText(text);
        }
    }

    protected void autoComplete(String text) {
        E[] opts = getMatchingOptions(text);
        comboBox.setModel(new DefaultComboBoxModel<E>(opts));
        if (comboBox.getItemCount() > 0) {
            comboBox.showPopup();
        }
    }

    @SuppressWarnings("unchecked")
    private E[] getMatchingOptions(String text) {
        Vector<E> v = new Vector<E>();
        ComboBoxModel<E> tmp = this.comboBoxModel;
        for (int i = 0; i < tmp.getSize(); i++) {
            E itemObj = tmp.getElementAt(i);
            if (StringUtils.isEmpty(text) || StringUtils.containsIgnoreCase(itemObj.toString(), text)) {
                v.add(itemObj);
            }
        }
        return (E[]) v.toArray();
    }

}