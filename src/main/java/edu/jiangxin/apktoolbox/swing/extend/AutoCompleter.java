package edu.jiangxin.apktoolbox.swing.extend;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

public class AutoCompleter<E> implements KeyListener {

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