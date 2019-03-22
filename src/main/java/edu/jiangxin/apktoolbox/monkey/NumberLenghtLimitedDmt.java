package edu.jiangxin.apktoolbox.monkey;

import javax.swing.text.*;

/**
 * 实现JTextfield只能输入数字并限制长度
 * 
 * @author Administrator
 *
 */
public class NumberLenghtLimitedDmt extends PlainDocument {

    private static final long serialVersionUID = 1L;

    private int limit;

    public NumberLenghtLimitedDmt(int limit) {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }
        if ((getLength() + str.length()) <= limit) {

            char[] upper = str.toCharArray();
            int length = 0;
            for (int i = 0; i < upper.length; i++) {
                if (upper[i] >= '0' && upper[i] <= '9') {
                    upper[length++] = upper[i];
                }
            }
            super.insertString(offset, new String(upper, 0, length), attr);
        }
    }
}
