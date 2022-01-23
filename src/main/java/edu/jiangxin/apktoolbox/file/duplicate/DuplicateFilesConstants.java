package edu.jiangxin.apktoolbox.file.duplicate;

import java.awt.*;
import java.util.Vector;

public class DuplicateFilesConstants {
    public static final String COLUMN_NAME_GROUP_NO = "GroupNo";
    public static final String COLUMN_NAME_FILE_PARENT = "ParentPath";
    public static final String COLUMN_NAME_FILE_NAME = "FileName";
    public static final String COLUMN_NAME_FILE_TYPE = "FileType";
    public static final String COLUMN_NAME_FILE_SIZE = "FileSize";
    public static final String COLUMN_NAME_MODIFY_TIME = "ModifyTime";

    public static final Vector<String> COLUMN_NAMES;

    public static final Vector<Color> BACKGROUND;

    static {
        COLUMN_NAMES = new Vector<>();
        COLUMN_NAMES.add(COLUMN_NAME_GROUP_NO);
        COLUMN_NAMES.add(COLUMN_NAME_FILE_PARENT);
        COLUMN_NAMES.add(COLUMN_NAME_FILE_NAME);
        COLUMN_NAMES.add(COLUMN_NAME_FILE_TYPE);
        COLUMN_NAMES.add(COLUMN_NAME_FILE_SIZE);
        COLUMN_NAMES.add(COLUMN_NAME_MODIFY_TIME);

        BACKGROUND = new Vector<>();
        BACKGROUND.add(new Color(255, 182, 193, 30));
        BACKGROUND.add(new Color(123, 104, 238, 30));
        BACKGROUND.add(new Color(127, 255, 170, 30));
        BACKGROUND.add(new Color(255, 255, 0, 30));
    }
}
