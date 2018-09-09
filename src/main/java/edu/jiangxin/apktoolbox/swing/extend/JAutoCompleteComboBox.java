package edu.jiangxin.apktoolbox.swing.extend;

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

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
			if (_item.equals(itemString))
				return true;
		}
		return false;
	}
}