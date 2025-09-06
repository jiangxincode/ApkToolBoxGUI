package edu.jiangxin.apktoolbox.swing.keeper;

import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.function.BiConsumer;

public final class UiStateKeeper {
    private static final Logger logger = LogManager.getLogger(UiStateKeeper.class.getSimpleName());
    private static final Configuration conf = Utils.getConfiguration();

    public static void save(Container root) {
        if (root == null) {
            return;
        }
        String prefix = root.getName() == null ? root.getClass().getSimpleName() : root.getName();
        scan(root, prefix, (c, key) -> {
            String value = valueOf(c);
            if (!StringUtils.isEmpty(value)) {
                conf.setProperty(key, value);
            }
        });
    }

    public static void restore(Container root) {
        if (root == null) {
            return;
        }
        String prefix = root.getName() == null ? root.getClass().getSimpleName() : root.getName();
        scan(root, prefix, (c, key) -> {
            String value = conf.getString(key);
            if (value != null) {
                setValue(c, value);
            }
        });
    }

    private static void scan(Container root, String prefix, BiConsumer<Component, String> action) {
        for (Component c : SwingUtils.getAllComponents(root)) {
            String name = c.getName();
            if (StringUtils.isEmpty(name) || !name.startsWith(Constants.KEY_PREFIX)) {
                continue;
            }
            action.accept(c, prefix + "." + Strings.CS.remove(name, Constants.KEY_PREFIX));
        }
    }

    private static String valueOf(Component c) {
        if (c instanceof JTextComponent) {
            //TODO why run two times?
            String text = ((JTextComponent) c).getText();
            logger.info("JTextComponent name={}, text={}", c.getName(), text);
            return ((JTextComponent) c).getText();
        }
        if (c instanceof JCheckBox) {
            return String.valueOf(((JCheckBox) c).isSelected());
        }
        if (c instanceof JRadioButton) {
            return String.valueOf(((JRadioButton) c).isSelected());
        }
        if (c instanceof JComboBox) {
            return Objects.toString(((JComboBox<?>) c).getSelectedItem(), "");
        }
        if (c instanceof JFileChooser) {
            return ((JFileChooser) c).getSelectedFile() == null ? "" :
                    ((JFileChooser) c).getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    private static void setValue(Component c, String v) {
        try {
            if (c instanceof JTextComponent) {
                ((JTextComponent) c).setText(v);
            } else if (c instanceof JCheckBox) {
                ((JCheckBox) c).setSelected(Boolean.parseBoolean(v));
            } else if (c instanceof JRadioButton) {
                ((JRadioButton) c).setSelected(Boolean.parseBoolean(v));
            } else if (c instanceof JComboBox) {
                ((JComboBox<?>) c).setSelectedItem(v);
            } else if (c instanceof JFileChooser) {
                File f = new File(v);
                if (f.exists()) ((JFileChooser) c).setSelectedFile(f);
            }
        } catch (Exception ignored) {
            logger.error("setValue error, component name={}, value={}", c.getName(), v);
        }
    }
}
