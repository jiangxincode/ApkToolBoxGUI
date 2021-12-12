package edu.jiangxin.apktoolbox.convert.color;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;

public class ColorPickerPanel extends EasyPanel {

    enum ColorMode {
        RGB, HTML, HEX, HSB
    }

    private static final long serialVersionUID = 1L;

    private static final int TOP_PADDING = 20;

    private static final int BOTTOM_PADDING = 20;

    private static final int LEFT_PADDING = 10;

    private static final int RIGHT_PADDING = 10;

    private static final int COLOR_RECT_HEIGHT = 60;

    private static final int COORDINATE_HEIGHT = 20;

    private static final int COLOR_LABEL_HEIGHT = 20;

    private static final int COMBO_HEIGHT = 20;

    private static final int ZOOM_RECT_HEIGHT = 100;

    private static final int SHOW_FIELD_HEIGHT = 20;

    private static final int COLOR_RECORD_ITEM_HEIGHT = 20;

    private static final int RECORD_RECT_HEIGHT = 100;

    private static final int COPY_BUTTON_HEIGHT = 20;

    private static final int TEXT_AREA_HEIGHT = 100;

    private static final int TEXT_AREA_WIDTH = 200;

    private static final int DEFAULT_WIDTH = 100;

    private static final int WIDTH = LEFT_PADDING + DEFAULT_WIDTH * 3 + Constants.DEFAULT_X_BORDER * 3 + TEXT_AREA_WIDTH + RIGHT_PADDING;

    private static final int HEIGHT = TOP_PADDING + COLOR_RECT_HEIGHT + COORDINATE_HEIGHT + COLOR_LABEL_HEIGHT + Constants.DEFAULT_Y_BORDER + COMBO_HEIGHT + BOTTOM_PADDING;

    private Robot robot;

    private JPanel colorPanel; // 颜色展示面板
    private JLabel coordsLabel; // 坐标信息
    private JLabel colorLabel; // 颜色信息
    private Point mousePoint; // 光标点
    private Image areaImage; // 待放大的图片
    private final int zoomValue = 3; // 放大倍数，（只会放大到100像素）
    private static final int ZoomMax = 100; // 总的放大倍数，不能更改
    private final int sideLength = ZoomMax / zoomValue;
    // 上一次的光标位置
    private Point prevPoint = null;
    // 当前的颜色，按键记录才记录163, 184, 204
    private Color currentColor;
    // 当前颜色模式
    private static ColorMode currentColorMode = ColorMode.RGB;
    // 交叉线
    private Line2D crossHorizontal;
    private Line2D crossVertical;
    // 记录颜色历史记录, 用LinkedList队列，
    private int colorRecordMax = 5; // 记录的color record个数
    private LinkedList<Color> colorQueue = new LinkedList<Color>();
    // 颜色值展示框
    JTextField showField;

    // 边框
    private final Rectangle2D colorRect = new Rectangle2D.Double();
    private final Rectangle2D zoomRect = new Rectangle2D.Double();
    private final Rectangle2D recordRect = new Rectangle2D.Double();
    // 颜色记录JLabel数组
    private JLabel colorRecordValue[] = new JLabel[colorRecordMax];
    // hsb 数组
    float[] hsbArr;

    private boolean isLocked = false;

    public ColorPickerPanel() {
        super();
        initUI();
    }

    private void initUI() {
        // 无布局方式
        setLayout(null);

        // 左侧的颜色框和显示的坐标，颜色值
        // 添加 panel 组件用来做颜色框
        colorPanel = new JPanel();
        colorPanel.setBounds(LEFT_PADDING, TOP_PADDING, DEFAULT_WIDTH, COLOR_RECT_HEIGHT);
        add(colorPanel);
        // 坐标
        coordsLabel = new JLabel();
        coordsLabel.setBounds(LEFT_PADDING, TOP_PADDING + COLOR_RECT_HEIGHT, DEFAULT_WIDTH, COORDINATE_HEIGHT);
        add(coordsLabel);
        // 颜色值
        colorLabel = new JLabel();
        colorLabel.setBounds(LEFT_PADDING, TOP_PADDING + COLOR_RECT_HEIGHT + COORDINATE_HEIGHT, DEFAULT_WIDTH, COLOR_LABEL_HEIGHT);
        add(colorLabel);

        // 放大镜的十字线（大小100,100）
        crossHorizontal = new Line2D.Double(
                (double) LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER,
                (double) TOP_PADDING + ZOOM_RECT_HEIGHT / 2.0,
                (double) LEFT_PADDING + DEFAULT_WIDTH * 2 + Constants.DEFAULT_X_BORDER,
                (double) TOP_PADDING + ZOOM_RECT_HEIGHT / 2.0);
        crossVertical = new Line2D.Double(
                (double) LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER + DEFAULT_WIDTH / 2.0,
                TOP_PADDING,
                (double) LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER + DEFAULT_WIDTH / 2.0,
                (double) TOP_PADDING + ZOOM_RECT_HEIGHT);

        // 右侧的颜色背景和颜色值label。大小100, 100
        for (int i = 0; i < colorRecordMax; i++) {
            colorRecordValue[i] = new JLabel();
            colorRecordValue[i].setOpaque(true); // 背景不透明
            colorRecordValue[i].setBounds(LEFT_PADDING + DEFAULT_WIDTH * 2 + Constants.DEFAULT_X_BORDER * 2,
                    TOP_PADDING + i * COLOR_RECORD_ITEM_HEIGHT, DEFAULT_WIDTH, COLOR_RECORD_ITEM_HEIGHT);
            add(colorRecordValue[i]);
        }

        // 颜色模式选择框
        JComboBox<String> colorModeCombo = new JComboBox<>();
        for (ColorMode t : ColorMode.values()) {
            colorModeCombo.addItem(t.toString());
        }
        // 给选择框绑定一个事件，改变时触发，用来设置当前颜色模式
        colorModeCombo.addActionListener(event -> {
            currentColorMode = ColorMode.valueOf((String) colorModeCombo.getSelectedItem());
        });
        colorModeCombo.setBounds(LEFT_PADDING, TOP_PADDING + COLOR_RECT_HEIGHT + COORDINATE_HEIGHT + COLOR_LABEL_HEIGHT + Constants.DEFAULT_Y_BORDER, DEFAULT_WIDTH, COMBO_HEIGHT);
        add(colorModeCombo);

        // 颜色值框体
        showField = new JTextField();
        showField.setEditable(false);
        showField.setBounds(LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER, TOP_PADDING + ZOOM_RECT_HEIGHT + Constants.DEFAULT_Y_BORDER, DEFAULT_WIDTH, SHOW_FIELD_HEIGHT);
        add(showField);

        // 复制按钮
        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(event -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            // 封装文本内容
            Transferable trans = new StringSelection(getColorText(currentColor));
            // 把文本内容设置到系统剪贴板
            clipboard.setContents(trans, null);

        });
        copyButton.setBounds(LEFT_PADDING + DEFAULT_WIDTH * 2 + Constants.DEFAULT_X_BORDER * 2, TOP_PADDING + RECORD_RECT_HEIGHT + Constants.DEFAULT_Y_BORDER, DEFAULT_WIDTH, COPY_BUTTON_HEIGHT);
        add(copyButton);

        JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.PINK);
        textArea.setBounds(LEFT_PADDING + DEFAULT_WIDTH * 3 + Constants.DEFAULT_X_BORDER * 3, TOP_PADDING, TEXT_AREA_WIDTH, TEXT_AREA_HEIGHT);
        textArea.append("记录颜色: ALT+C" + System.getProperty("line.separator"));
        textArea.append("锁定区域: ALT+L" + System.getProperty("line.separator"));
        textArea.append("取消锁定区域: ALT+U" + System.getProperty("line.separator"));
        add(textArea);

        // 键盘检测事件
        final InputMap inputMap = colorPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("alt C"), "record.color");
        inputMap.put(KeyStroke.getKeyStroke("alt L"), "lock.position");
        inputMap.put(KeyStroke.getKeyStroke("alt U"), "unlock.position");

        final Action recordColorAction = new RecordColorAction(); // '记录'动作
        final Action lockPositionAction = new LockPositionAction();
        final Action unlockPositionAction = new UnlockPositionAction();

        final ActionMap actionMap = colorPanel.getActionMap();
        actionMap.put("record.color", recordColorAction);
        actionMap.put("lock.position", lockPositionAction);
        actionMap.put("unlock.position", unlockPositionAction);

        // 鼠标监听
        mouseListener();
    }

    /**
     * 按键动作
     * alt+c 触发此动作，用来记录颜色值
     */
    public class RecordColorAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (robot == null) {
                logger.error("robot is null");
                return;
            }
            // 记录当前的颜色
            Color c = robot.getPixelColor(mousePoint.x, mousePoint.y);
            currentColor = c;

            // 头进尾出，就可以满足绘制到顶部的记录是最新的
            colorQueue.offerFirst(c);
            if (colorQueue.size() > colorRecordMax) {
                colorQueue.pollLast(); // 删除尾
            }

            // 显示颜色值到文本框
            showField.setText(getColorText(c));
            repaint();
        }
    }

    class LockPositionAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            isLocked = true;
        }
    }

    class UnlockPositionAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            isLocked = false;
        }
    }

    /**
     * 重置窗口大小
     */
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    /**
     * 鼠标监听，利用 timer
     */
    public void mouseListener() {
        try {
            robot = new Robot();
        } catch (final AWTException e) {
            logger.error("Create Rebot instance failed");
        }

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mouseAction();
            }
        }, 100, 100);
    }

    /**
     * 鼠标事件
     * 获取位置和颜色，并显示位置和颜色信息
     */
    private void mouseAction() {
        // 获取光标位置之后，与上次比对，避免重复运行
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        if (pointerInfo == null) {
            logger.warn("pointerInfo is null");
            return;
        }
        mousePoint = pointerInfo.getLocation();

        if (mousePoint.equals(prevPoint)) {
            return;
        } else {
            prevPoint = mousePoint;
        }
        if (robot == null) {
            logger.error("robot is null");
            return;
        }
        final Color pixel = robot.getPixelColor(mousePoint.x, mousePoint.y);
        colorPanel.setBackground(pixel);

        coordsLabel.setText(String.format("[%d, %d]", mousePoint.x, mousePoint.y));
        colorLabel.setText(getColorText(pixel));

        if (!isLocked) {
            // 获取区域
            getMouseArea();
        }
    }

    /**
     * 根据当前颜色模式显示颜色值
     */
    private String getColorText(final Color c) {
        if (c == null) {
            return "";
        }
        String s = "";
        switch (currentColorMode) {
            case RGB:
                s = String.format("%d, %d, %d", c.getRed(), c.getGreen(), c.getBlue());
                break;
            case HTML:
                s = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
                break;
            case HEX:
                s = String.format("0x%02X%02X%02X", c.getBlue(), c.getGreen(), c.getRed());
                break;
            case HSB:
                hsbArr = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                s = String.format("%3.0f%% %3.0f%% %3.0f%%", hsbArr[0] * 100, hsbArr[1] * 100, hsbArr[2] * 100);
                break;
            default:
                break;
        }
        return s;

    }

    /**
     * 获取鼠标周围区域图片
     */
    protected void getMouseArea() {

        final int x = mousePoint.x;
        final int y = mousePoint.y;

        final Rectangle r = new Rectangle(x - sideLength / 2, y - sideLength / 2, sideLength, sideLength);
        if (robot != null) {
            areaImage = robot.createScreenCapture(r);
        }
        repaint(); // 重绘，调用 paintComponent
    }

    /**
     * 绘制界面
     */
    public void paintComponent(final Graphics g) {
        // 父类的paitComponent需要绘制其他默认的组件，比如左侧颜色框panel以及坐标和颜色值label。如果不执行，则绘制的区域会重叠
        super.paintComponent(g);

        // 中间放大镜
        final Graphics2D g2 = (Graphics2D) g;
        //g2.drawImage(areaImage,10,300,null); // 原大小
        g2.drawImage(areaImage, LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER, TOP_PADDING, DEFAULT_WIDTH, ZOOM_RECT_HEIGHT, null);
        // 放大镜的十字线
        g2.setPaint(Color.RED);
        g2.draw(crossHorizontal);
        g2.draw(crossVertical);

        // 右侧颜色历史记录
        paintColorRecord(g2);

        // 绘制各组件的边框
        paintBorder(g2);

        // 颜色值重新赋值，因为可能有模式的改变
        showField.setText(getColorText(currentColor));

    }

    private void paintColorRecord(final Graphics2D g2) {
        int i = 0;

        for (Color c : colorQueue) {
            Color penC = new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue()); // 反色
            // 字体颜色设置
            colorRecordValue[i].setForeground(penC);
            colorRecordValue[i].setText(getColorText(c));

            colorRecordValue[i].setBackground(c);

            if (++i > colorRecordMax)
                break;
        }
    }

    private void paintBorder(Graphics2D g2) {
        g2.setPaint(Color.BLACK);
        colorRect.setFrameFromDiagonal(
                (double)LEFT_PADDING - 1,
                (double)TOP_PADDING - 1,
                (double)LEFT_PADDING + DEFAULT_WIDTH,
                (double)TOP_PADDING + COLOR_RECT_HEIGHT);
        zoomRect.setFrameFromDiagonal(
                (double)LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER - 1,
                (double)TOP_PADDING - 1,
                (double)LEFT_PADDING + DEFAULT_WIDTH * 2 + Constants.DEFAULT_X_BORDER,
                (double)TOP_PADDING + ZOOM_RECT_HEIGHT);
        recordRect.setFrameFromDiagonal(
                (double)LEFT_PADDING + DEFAULT_WIDTH * 2 + Constants.DEFAULT_X_BORDER * 2 - 1,
                (double)TOP_PADDING - 1,
                (double)LEFT_PADDING + DEFAULT_WIDTH * 3 + Constants.DEFAULT_X_BORDER * 2,
                (double)TOP_PADDING + RECORD_RECT_HEIGHT);
        g2.draw(colorRect);
        g2.draw(zoomRect);
        g2.draw(recordRect);
    }

}
