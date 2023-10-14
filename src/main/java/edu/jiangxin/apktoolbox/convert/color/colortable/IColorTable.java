package edu.jiangxin.apktoolbox.convert.color.colortable;

public interface IColorTable {
    String toString();

    String[] getColumnNames();

    String[][] getTableRowData();

    int getLabelIndex();

    int getHexIndex();


}
