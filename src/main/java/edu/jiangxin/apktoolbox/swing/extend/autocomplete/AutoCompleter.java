package edu.jiangxin.apktoolbox.swing.extend.autocomplete;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

class AutoCompleter<E> implements KeyListener {
    private JComboBox<E> comboBox;
    private JTextField textField;
    private ComboBoxModel<E> comboBoxModel;

    public AutoCompleter(JComboBox<E> comboBox) {
        if (comboBox == null) {
            throw new AutoCompleterInitException("comboBox is null");
        }
        this.comboBox = comboBox;
        ComboBoxEditor comboBoxEditor = comboBox.getEditor();
        if (comboBoxEditor == null) {
            throw new AutoCompleterInitException("comboBoxEditor is null");
        }
        Component component = comboBoxEditor.getEditorComponent();
        if (!(component instanceof JTextField)) {
            throw new AutoCompleterInitException("component is invalid");
        }
        this.textField = (JTextField) component;
        this.textField.addKeyListener(this);
        this.comboBoxModel = comboBox.getModel();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // do nothing
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
        Vector<E> opts = getMatchingOptions(text);
        comboBox.setModel(new DefaultComboBoxModel<>(opts));
        if (comboBox.getItemCount() > 0) {
            comboBox.showPopup();
        }
    }

    private Vector<E> getMatchingOptions(String text) {
        Vector<E> vector = new Vector<>();
        ComboBoxModel<E> tmp = this.comboBoxModel;
        for (int i = 0; i < tmp.getSize(); i++) {
            E itemObj = tmp.getElementAt(i);
            if (StringUtils.isEmpty(text) || StringUtils.containsIgnoreCase(itemObj.toString(), text)) {
                vector.add(itemObj);
            }
        }
        return vector;
    }

}
