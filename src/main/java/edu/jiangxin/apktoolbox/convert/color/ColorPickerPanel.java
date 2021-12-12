package edu.jiangxin.apktoolbox.convert.color;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.MouseInfo;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.AbstractAction;

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
    private static final int WIDTH = 340;
    private static final int HEIGHT = 150;
    private Robot robot;

    private JPanel colorPanel; // 颜色展示面板
    //private final JPanel recordPanel; // 颜色记录框
    private JLabel coordsJlabel; // 坐标信息
    private JLabel colorJlabel; // 颜色信息
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
        // 窗体居中
        final Dimension centre = this.getScreenCentre();
        setLocation(centre.width - WIDTH / 2, centre.height - HEIGHT / 2);
        System.out.println((centre.width - WIDTH / 2) + " " + (centre.height - HEIGHT / 2));
        // 无布局方式
        setLayout(null);

        // 左侧的颜色框和显示的坐标，颜色值
        // 添加 panel 组件用来做颜色框
        colorPanel = new JPanel();
        colorPanel.setBounds(10, 10, 100, 60);
        add(colorPanel);
        // 坐标
        coordsJlabel = new JLabel();
        coordsJlabel.setBounds(10, 70, 100, 20);
        add(coordsJlabel);
        // 颜色值
        colorJlabel = new JLabel();
        colorJlabel.setBounds(10, 90, 100, 20);
        add(colorJlabel);

        // 放大镜的十字线（位置10,120 大小100,100）
        crossHorizontal = new Line2D.Double(120 + 50, 10, 120 + 50, 10 + 100);
        crossVertical = new Line2D.Double(120, 10 + 50, 120 + 100, 10 + 50);

        // 右侧的颜色背景和颜色值label。位置和大小230, 10, 100, 100
        for (int i = 0; i < colorRecordMax; i++) {
            colorRecordValue[i] = new JLabel();
            colorRecordValue[i].setOpaque(true); // 背景不透明
            colorRecordValue[i].setBounds(230, 10 + i * 20, 100, 20);
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
        colorModeCombo.setBounds(10, 120, 100, 20);
        add(colorModeCombo);

        // 颜色值框体
        showField = new JTextField();
        showField.setEditable(false);
        showField.setBounds(120, 120, 100, 20);
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
        copyButton.setBounds(230, 120, 100, 20);
        add(copyButton);

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
     * 获取屏幕中心点
     *
     * @return Dimension
     */
    public Dimension getScreenCentre() {
        // 获取屏幕分辨率
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension(screenSize.width / 2, screenSize.height / 2);
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
            e.printStackTrace();
        }

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isLocked) {
                    mouseAction();
                }
            }
        }, 100, 100);
    }

    /**
     * 鼠标事件
     * 获取位置和颜色，并显示位置和颜色信息
     */
    private void mouseAction() {
        // 获取光标位置之后，与上次比对，避免重复运行
        mousePoint = MouseInfo.getPointerInfo().getLocation();

        if (mousePoint.equals(prevPoint)) {
            return;
        } else {
            prevPoint = mousePoint;
        }

        final Color pixel = robot.getPixelColor(mousePoint.x, mousePoint.y);
        colorPanel.setBackground(pixel);

        coordsJlabel.setText(String.format("[%d, %d]", mousePoint.x, mousePoint.y));
        colorJlabel.setText(getColorText(pixel));

        // 获取区域
        getMouseArea();
    }

    /**
     * 根据当前颜色模式显示颜色值
     *
     * @param Color c
     * @return
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
        areaImage = robot.createScreenCapture(r);

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
        g2.drawImage(areaImage, 120, 10, 100, 100, null);
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

    /**
     * @param Graphics2D 绘制历史记录
     */
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

    /**
     * @param Graphics2D 绘制边框
     */
    private void paintBorder(Graphics2D g2) {
        g2.setPaint(Color.BLACK);
        colorRect.setFrameFromDiagonal(10 - 1, 10 - 1, 110, 70);
        zoomRect.setFrameFromDiagonal(120 - 1, 10 - 1, 120 + 100, 110);
        recordRect.setFrameFromDiagonal(230 - 1, 10 - 1, 230 + 100, 110);
        g2.draw(colorRect);
        g2.draw(zoomRect);
        g2.draw(recordRect);
    }

}
