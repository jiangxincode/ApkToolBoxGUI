package edu.jiangxin.apktoolbox.convert.color;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import org.apache.commons.lang3.StringUtils;

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

    private static final int TEXT_AREA_WIDTH = 200;

    private static final int DEFAULT_HEIGHT_SMALL = 20;

    private static final int DEFAULT_HEIGHT_BIG = 100;

    private static final int DEFAULT_WIDTH = 100;

    private static final int WIDTH = LEFT_PADDING + DEFAULT_WIDTH * 3 + Constants.DEFAULT_X_BORDER * 3 + TEXT_AREA_WIDTH + RIGHT_PADDING;

    private static final int HEIGHT = TOP_PADDING + DEFAULT_HEIGHT_SMALL * 3 + DEFAULT_HEIGHT_BIG + Constants.DEFAULT_Y_BORDER * 2 + BOTTOM_PADDING;

    private JPanel colorPanel;
    private JLabel coordinateLabel;
    private JLabel colorLabel;

    private transient Robot robot;
    private Point mousePoint; // 光标点
    private transient Image areaImage; // 待放大的图片

    private int zoomFactor = 2;

    private Point prevPoint = null; // 上一次的光标位置

    private Color currentColor; // 当前的颜色，按键记录才记录163, 184, 204

    private static ColorMode currentColorMode = ColorMode.RGB; // 当前颜色模式

    private transient Line2D crossHorizontal; // 交叉线
    private transient Line2D crossVertical;

    private int colorRecordMax = 5; // 记录的color record个数
    private LinkedList<Color> colorQueue = new LinkedList<>();

    private JTextField colorCopyTextField;

    // 边框
    private transient final Rectangle2D colorRect = new Rectangle2D.Double();
    private transient final Rectangle2D zoomRect = new Rectangle2D.Double();
    private transient final Rectangle2D recordRect = new Rectangle2D.Double();
    // 颜色记录JLabel数组
    private JLabel colorRecordValue[] = new JLabel[colorRecordMax];
    // hsb 数组
    float[] hsbArr;

    private boolean isLocked = false;

    @Override
    public void initUI() {
        setLayout(null);

        initFirstColumn();

        initSecondColumn();

        initThirdColumn();

        initFourthColumn();

        final InputMap inputMap = colorPanel.getInputMap(WHEN_IN_FOCUSED_WINDOW);
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

        mouseListener();
    }

    private void initFirstColumn() {
        // 左侧的颜色框和显示的坐标，颜色值
        // 添加 panel 组件用来做颜色框
        colorPanel = new JPanel();
        colorPanel.setBounds(LEFT_PADDING, TOP_PADDING, DEFAULT_WIDTH, DEFAULT_HEIGHT_BIG);
        add(colorPanel);

        coordinateLabel = new JLabel();
        coordinateLabel.setBounds(LEFT_PADDING, TOP_PADDING + DEFAULT_HEIGHT_BIG + Constants.DEFAULT_Y_BORDER, DEFAULT_WIDTH, DEFAULT_HEIGHT_SMALL);
        add(coordinateLabel);

        colorLabel = new JLabel();
        colorLabel.setBounds(LEFT_PADDING, TOP_PADDING + DEFAULT_HEIGHT_BIG + DEFAULT_HEIGHT_SMALL + Constants.DEFAULT_Y_BORDER * 2, DEFAULT_WIDTH, DEFAULT_HEIGHT_SMALL);
        add(colorLabel);

        JComboBox<String> colorModeCombo = new JComboBox<>();
        for (ColorMode t : ColorMode.values()) {
            colorModeCombo.addItem(t.toString());
        }

        colorModeCombo.addActionListener(event -> {
            currentColorMode = ColorMode.valueOf((String) colorModeCombo.getSelectedItem());
        });
        colorModeCombo.setBounds(LEFT_PADDING, TOP_PADDING + DEFAULT_HEIGHT_BIG + DEFAULT_HEIGHT_SMALL * 2 + Constants.DEFAULT_Y_BORDER * 3, DEFAULT_WIDTH, DEFAULT_HEIGHT_SMALL);
        add(colorModeCombo);
    }

    private void initSecondColumn() {
        crossHorizontal = new Line2D.Double(
                (double) LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER,
                (double) TOP_PADDING + DEFAULT_HEIGHT_BIG / 2.0,
                (double) LEFT_PADDING + DEFAULT_WIDTH * 2 + Constants.DEFAULT_X_BORDER,
                (double) TOP_PADDING + DEFAULT_HEIGHT_BIG / 2.0);
        crossVertical = new Line2D.Double(
                (double) LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER + DEFAULT_WIDTH / 2.0,
                TOP_PADDING,
                (double) LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER + DEFAULT_WIDTH / 2.0,
                (double) TOP_PADDING + DEFAULT_HEIGHT_BIG);

        colorCopyTextField = new JTextField();
        colorCopyTextField.setEditable(false);
        colorCopyTextField.setBounds(LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER, TOP_PADDING + DEFAULT_HEIGHT_BIG + Constants.DEFAULT_Y_BORDER, DEFAULT_WIDTH, DEFAULT_HEIGHT_SMALL);
        add(colorCopyTextField);

        JButton colorCopyButton = new JButton("Copy");
        colorCopyButton.addActionListener(event -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable trans = new StringSelection(getColorText(currentColor));
            clipboard.setContents(trans, null);
        });
        colorCopyButton.setBounds(LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER, TOP_PADDING + DEFAULT_HEIGHT_BIG + Constants.DEFAULT_Y_BORDER * 2 + DEFAULT_HEIGHT_SMALL, DEFAULT_WIDTH, DEFAULT_HEIGHT_SMALL);
        add(colorCopyButton);
    }

    private void initThirdColumn() {
        for (int i = 0; i < colorRecordMax; i++) {
            colorRecordValue[i] = new JLabel();
            colorRecordValue[i].setOpaque(true); // 背景不透明
            colorRecordValue[i].setBounds(LEFT_PADDING + DEFAULT_WIDTH * 2 + Constants.DEFAULT_X_BORDER * 2,
                    TOP_PADDING + i * DEFAULT_HEIGHT_SMALL, DEFAULT_WIDTH, DEFAULT_HEIGHT_SMALL);
            add(colorRecordValue[i]);
        }
        JComboBox<String> magnificationModeCombo = new JComboBox<>();
        magnificationModeCombo.addItem("Zoom: 100%");
        magnificationModeCombo.addItem("Zoom: 200%");
        magnificationModeCombo.addItem("Zoom: 300%");
        magnificationModeCombo.addItem("Zoom: 400%");
        magnificationModeCombo.addItem("Zoom: 500%");
        magnificationModeCombo.addItem("Zoom: 1200%");
        magnificationModeCombo.addItem("Zoom: 2800%");
        magnificationModeCombo.setSelectedItem("Zoom: 200%");
        magnificationModeCombo.addActionListener(event -> {
            String selectedItem = (String) magnificationModeCombo.getSelectedItem();
            String tmp = StringUtils.substringBetween(selectedItem, "Zoom: ", "%");
            zoomFactor = Integer.valueOf(tmp) / 100;
            logger.info("zoomFactor: " + zoomFactor);
        });
        magnificationModeCombo.setBounds(LEFT_PADDING + Constants.DEFAULT_X_BORDER * 2 + DEFAULT_WIDTH * 2, TOP_PADDING + DEFAULT_HEIGHT_BIG + Constants.DEFAULT_Y_BORDER, DEFAULT_WIDTH, DEFAULT_HEIGHT_SMALL);
        add(magnificationModeCombo);
    }

    private void initFourthColumn() {
        JTextArea textArea = new JTextArea();
        textArea.setBounds(LEFT_PADDING + DEFAULT_WIDTH * 3 + Constants.DEFAULT_X_BORDER * 3, TOP_PADDING, TEXT_AREA_WIDTH, DEFAULT_HEIGHT_BIG);
        textArea.append("记录颜色: ALT+C" + System.getProperty("line.separator"));
        textArea.append("锁定区域: ALT+L" + System.getProperty("line.separator"));
        textArea.append("取消锁定区域: ALT+U" + System.getProperty("line.separator"));
        add(textArea);
    }

    class RecordColorAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            if (robot == null) {
                logger.error("robot is null");
                return;
            }
            Color color = robot.getPixelColor(mousePoint.x, mousePoint.y);
            currentColor = color;

            // 头进尾出，就可以满足绘制到顶部的记录是最新的
            colorQueue.offerFirst(color);
            if (colorQueue.size() > colorRecordMax) {
                colorQueue.pollLast(); // 删除尾
            }

            // 显示颜色值到文本框
            colorCopyTextField.setText(getColorText(color));
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

    private void mouseListener() {
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

        coordinateLabel.setText(String.format("[%d, %d]", mousePoint.x, mousePoint.y));
        colorLabel.setText(getColorText(pixel));

        if (!isLocked) {
            getMouseArea();
        }
    }

    private String getColorText(final Color color) {
        if (color == null) {
            return "";
        }
        String s = "";
        switch (currentColorMode) {
            case RGB:
                s = String.format("%d, %d, %d", color.getRed(), color.getGreen(), color.getBlue());
                break;
            case HTML:
                s = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                break;
            case HEX:
                s = String.format("0x%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
                break;
            case HSB:
                hsbArr = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                s = String.format("%3.0f%% %3.0f%% %3.0f%%", hsbArr[0] * 100, hsbArr[1] * 100, hsbArr[2] * 100);
                break;
            default:
                break;
        }
        return s;

    }

    protected void getMouseArea() {

        final int x = mousePoint.x;
        final int y = mousePoint.y;

        int length = 100 / zoomFactor;
        final Rectangle r = new Rectangle(x - length / 2, y - length / 2, length, length);
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
        g2.drawImage(areaImage, LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER, TOP_PADDING, DEFAULT_WIDTH, DEFAULT_HEIGHT_BIG, null);
        // 放大镜的十字线
        g2.setPaint(Color.RED);
        g2.draw(crossHorizontal);
        g2.draw(crossVertical);

        // 右侧颜色历史记录
        paintColorRecord();

        // 绘制各组件的边框
        paintBorder(g2);

        // 颜色值重新赋值，因为可能有模式的改变
        colorCopyTextField.setText(getColorText(currentColor));

    }

    private void paintColorRecord() {
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
                (double) LEFT_PADDING - 1,
                (double) TOP_PADDING - 1,
                (double) LEFT_PADDING + DEFAULT_WIDTH,
                (double) TOP_PADDING + DEFAULT_HEIGHT_BIG);
        zoomRect.setFrameFromDiagonal(
                (double) LEFT_PADDING + DEFAULT_WIDTH + Constants.DEFAULT_X_BORDER - 1,
                (double) TOP_PADDING - 1,
                (double) LEFT_PADDING + DEFAULT_WIDTH * 2 + Constants.DEFAULT_X_BORDER,
                (double) TOP_PADDING + DEFAULT_HEIGHT_BIG);
        recordRect.setFrameFromDiagonal(
                (double) LEFT_PADDING + DEFAULT_WIDTH * 2 + Constants.DEFAULT_X_BORDER * 2 - 1,
                (double) TOP_PADDING - 1,
                (double) LEFT_PADDING + DEFAULT_WIDTH * 3 + Constants.DEFAULT_X_BORDER * 2,
                (double) TOP_PADDING + DEFAULT_HEIGHT_BIG);
        g2.draw(colorRect);
        g2.draw(zoomRect);
        g2.draw(recordRect);
    }
}
