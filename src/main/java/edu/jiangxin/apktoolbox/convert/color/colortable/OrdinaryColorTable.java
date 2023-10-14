package edu.jiangxin.apktoolbox.convert.color.colortable;

public class OrdinaryColorTable implements IColorTable {

    @Override
    public String toString() {
        return "Ordinary Colors";
    }

    @Override
    public String[] getColumnNames() {
        return COLUMN_NAMES;
    }

    @Override
    public String[][] getTableRowData() {
        return TABLE_ROW_DATA;
    }

    @Override
    public int getLabelIndex() {
        return 0;
    }

    @Override
    public int getHexIndex() {
        return 3;
    }

    public static final String[] COLUMN_NAMES = {
            "颜色", "英文", "中文", "RGB代码"
    };

    public static final String[][] TABLE_ROW_DATA = {
            {"", "LightPink", "浅粉红", "#FFB6C1"},
            {"", "Pink", "粉红", "#FFC0CB"},
            {"", "Crimson", "猩红", "#DC143C"},
            {"", "LavenderBlush", "脸红的淡紫色", "#FFF0F5"},
            {"", "PaleVioletRed", "苍白的紫罗兰红色", "#DB7093"},
            {"", "HotPink", "热情的粉红", "#FF69B4"},
            {"", "DeepPink", "深粉色", "#FF1493"},
            {"", "MediumVioletRed", "适中的紫罗兰红色", "#C71585"},
            {"", "Orchid", "兰花的紫色", "#DA70D6"},
            {"", "Thistle", "蓟", "#D8BFD8"},
            {"", "plum", "李子", "#DDA0DD"},
            {"", "Violet", "紫罗兰", "#EE82EE"},
            {"", "Magenta", "洋红", "#FF00FF"},
            {"", "Fuchsia", "灯笼海棠(紫红色)", "#FF00FF"},
            {"", "DarkMagenta", "深洋红色", "#8B008B"},
            {"", "Purple", "紫色", "#800080"},
            {"", "MediumOrchid", "适中的兰花紫", "#BA55D3"},
            {"", "DarkVoilet", "深紫罗兰色", "#9400D3"},
            {"", "DarkOrchid", "深兰花紫", "#9932CC"},
            {"", "Indigo", "靛青", "#4B0082"},
            {"", "BlueViolet", "深紫罗兰的蓝色", "#8A2BE2"},
            {"", "MediumPurple", "适中的紫色", "#9370DB"},
            {"", "MediumSlateBlue", "适中的板岩暗蓝灰色", "#7B68EE"},
            {"", "SlateBlue", "板岩暗蓝灰色", "#6A5ACD"},
            {"", "DarkSlateBlue", "深岩暗蓝灰色", "#483D8B"},
            {"", "Lavender", "熏衣草花的淡紫色", "#E6E6FA"},
            {"", "GhostWhite", "幽灵的白色", "#F8F8FF"},
            {"", "Blue", "纯蓝", "#0000FF"},
            {"", "MediumBlue", "适中的蓝色", "#0000CD"},
            {"", "MidnightBlue", "午夜的蓝色", "#191970"},
            {"", "DarkBlue", "深蓝色", "#00008B"},
            {"", "Navy", "海军蓝", "#000080"},
            {"", "RoyalBlue", "皇家蓝", "#4169E1"},
            {"", "CornflowerBlue", "矢车菊的蓝色", "#6495ED"},
            {"", "LightSteelBlue", "淡钢蓝", "#B0C4DE"},
            {"", "LightSlateGray", "浅石板灰", "#778899"},
            {"", "SlateGray", "石板灰", "#708090"},
            {"", "DoderBlue", "道奇蓝", "#1E90FF"},
            {"", "AliceBlue", "爱丽丝蓝", "#F0F8FF"},
            {"", "SteelBlue", "钢蓝", "#4682B4"},
            {"", "LightSkyBlue", "淡蓝色", "#87CEFA"},
            {"", "SkyBlue", "天蓝色", "#87CEEB"},
            {"", "DeepSkyBlue", "深天蓝", "#00BFFF"},
            {"", "LightBLue", "淡蓝", "#ADD8E6"},
            {"", "PowDerBlue", "火药蓝", "#B0E0E6"},
            {"", "CadetBlue", "军校蓝", "#5F9EA0"},
            {"", "Azure", "蔚蓝色", "#F0FFFF"},
            {"", "LightCyan", "淡青色", "#E1FFFF"},
            {"", "PaleTurquoise", "苍白的绿宝石", "#AFEEEE"},
            {"", "Cyan", "青色", "#00FFFF"},
            {"", "Aqua", "水绿色", "#D4F2E7"},
            {"", "DarkTurquoise", "深绿宝石", "#00CED1"},
            {"", "DarkSlateGray", "深石板灰", "#2F4F4F"},
            {"", "DarkCyan", "深青色", "#008B8B"},
            {"", "Teal", "水鸭色", "#008080"},
            {"", "MediumTurquoise", "适中的绿宝石", "#48D1CC"},
            {"", "LightSeaGreen", "浅海洋绿", "#20B2AA"},
            {"", "Turquoise", "绿宝石", "#40E0D0"},
            {"", "Auqamarin", "绿玉/碧绿色", "#7FFFAA"},
            {"", "MediumAquamarine", "适中的碧绿色", "#00FA9A"},
            {"", "MediumSpringGreen", "适中的春天的绿色", "#00FF7F"},
            {"", "MintCream", "薄荷奶油", "#F5FFFA"},
            {"", "SpringGreen", "春天的绿色", "#3CB371"},
            {"", "SeaGreen", "海洋绿", "#2E8B57"},
            {"", "Honeydew", "蜂蜜", "#F0FFF0"},
            {"", "LightGreen", "淡绿色", "#90EE90"},
            {"", "PaleGreen", "苍白的绿色", "#98FB98"},
            {"", "DarkSeaGreen", "深海洋绿", "#8FBC8F"},
            {"", "LimeGreen", "酸橙绿", "#32CD32"},
            {"", "Lime", "酸橙色", "#00FF00"},
            {"", "ForestGreen", "森林绿", "#228B22"},
            {"", "Green", "纯绿", "#008000"},
            {"", "DarkGreen", "深绿色", "#006400"},
            {"", "Chartreuse", "查特酒绿", "#7FFF00"},
            {"", "LawnGreen", "草坪绿", "#7CFC00"},
            {"", "GreenYellow", "绿黄色", "#ADFF2F"},
            {"", "OliveDrab", "橄榄土褐色", "#556B2F"},
            {"", "Beige", "米色(浅褐色)", "#F5F5DC"},
            {"", "LightGoldenrodYellow", "浅秋麒麟黄", "#FAFAD2"},
            {"", "Ivory", "象牙", "#FFFFF0"},
            {"", "LightYellow", "浅黄色", "#FFFFE0"},
            {"", "Yellow", "纯黄", "#FFFF00"},
            {"", "Olive", "橄榄", "#808000"},
            {"", "DarkKhaki", "深卡其布", "#BDB76B"},
            {"", "LemonChiffon", "柠檬薄纱", "#FFFACD"},
            {"", "PaleGodenrod", "灰秋麒麟", "#EEE8AA"},
            {"", "Khaki", "卡其布", "#F0E68C"},
            {"", "Gold", "金", "#FFD700"},
            {"", "Cornislk", "玉米色", "#FFF8DC"},
            {"", "GoldEnrod", "秋麒麟", "#DAA520"},
            {"", "FloralWhite", "花的白色", "#FFFAF0"},
            {"", "OldLace", "老饰带", "#FDF5E6"},
            {"", "Wheat", "小麦色", "#F5DEB3"},
            {"", "Moccasin", "鹿皮鞋", "#FFE4B5"},
            {"", "Orange", "橙色", "#FFA500"},
            {"", "PapayaWhip", "番木瓜", "#FFEFD5"},
            {"", "BlanchedAlmond", "漂白的杏仁", "#FFEBCD"},
            {"", "NavajoWhite", "纳瓦霍白", "#FFDEAD"},
            {"", "AntiqueWhite", "古代的白色", "#FAEBD7"},
            {"", "Tan", "晒黑", "#D2B48C"},
            {"", "BrulyWood", "结实的树", "#DEB887"},
            {"", "Bisque", "(浓汤)乳脂,番茄等", "#FFE4C4"},
            {"", "DarkOrange", "深橙色", "#FF8C00"},
            {"", "Linen", "亚麻布", "#FAF0E6"},
            {"", "Peru", "秘鲁", "#CD853F"},
            {"", "PeachPuff", "桃色", "#FFDAB9"},
            {"", "SandyBrown", "沙棕色", "#F4A460"},
            {"", "Chocolate", "巧克力", "#D2691E"},
            {"", "SaddleBrown", "马鞍棕色", "#8B4513"},
            {"", "SeaShell", "海贝壳", "#FFF5EE"},
            {"", "Sienna", "黄土赭色", "#A0522D"},
            {"", "LightSalmon", "浅鲜肉(鲑鱼)色", "#FFA07A"},
            {"", "Coral", "珊瑚", "#FF7F50"},
            {"", "OrangeRed", "橙红色", "#FF4500"},
            {"", "DarkSalmon", "深鲜肉(鲑鱼)色", "#E9967A"},
            {"", "Tomato", "番茄", "#FF6347"},
            {"", "MistyRose", "薄雾玫瑰", "#FFE4E1"},
            {"", "Salmon", "鲜肉(鲑鱼)色", "#FA8072"},
            {"", "Snow", "雪", "#FFFAFA"},
            {"", "LightCoral", "淡珊瑚色", "#F08080"},
            {"", "RosyBrown", "玫瑰棕色", "#BC8F8F"},
            {"", "IndianRed", "印度红", "#CD5C5C"},
            {"", "Red", "纯红", "#FF0000"},
            {"", "Brown", "棕色", "#A52A2A"},
            {"", "FireBrick", "耐火砖", "#B22222"},
            {"", "DarkRed", "深红色", "#8B0000"},
            {"", "Maroon", "栗色", "#800000"},
            {"", "White", "纯白", "#FFFFFF"},
            {"", "WhiteSmoke", "白烟", "#F5F5F5"},
            {"", "Gainsboro", "亮灰色", "#DCDCDC"},
            {"", "LightGrey", "浅灰色", "#D3D3D3"},
            {"", "Silver", "银白色", "#C0C0C0"},
            {"", "DarkGray", "深灰色", "#A9A9A9"},
            {"", "Gray", "灰色", "#808080"},
            {"", "DimGray", "暗淡的灰色", "#696969"},
            {"", "Black", "纯黑", "#000000"}
    };
}
