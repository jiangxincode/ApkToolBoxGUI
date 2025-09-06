package edu.jiangxin.apktoolbox.swing.keeper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class SwingUtils {
    public static List<Component> getAllComponents(Container root) {
        List<Component> list = new ArrayList<>();
        addAll(root, list);
        return list;
    }
    private static void addAll(Container parent, List<Component> list) {
        for (Component c : parent.getComponents()) {
            list.add(c);
            if (c instanceof Container) {
                addAll((Container) c, list);
            }
        }
    }
}
