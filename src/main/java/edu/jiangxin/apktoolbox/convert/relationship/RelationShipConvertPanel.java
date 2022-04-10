package edu.jiangxin.apktoolbox.convert.relationship;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;

/*
 * 介绍：关于“小”这个功能，在称呼存在年龄大小不同时，按钮会改变颜色，默认为比你大，点击后选择比你小。
 * 退格功能，就是返回上一步功能还没做，也不想做了。有兴趣自己看着加。
 * 当你能够确定关系输入过程中不存在年龄比较问题时，可以连续输入，不需要点“的”按钮。
 * 例如直接按“兄”->“姐”->“=”就可以得到答案 【姐姐】
 * 在这个函数initData()里面修改你的数据文件的位置。
 * 数据文件为一行一条数据（注意使用utf-8编码）。
 * 格式如下：[]{(父-)(母-)(兄-)(弟-)(姐-)(妹-)(夫-)(妻-)(儿-)(女-)}
 * 存在年龄比较时<称谓1><称谓2>，比你大的称谓在前面。
 */
public class RelationShipConvertPanel extends EasyPanel {
    static Hashtable<String, String> dataHashtable = new Hashtable<String, String>();
    static String now = "你";
    static JTextPane inpuTextArea;
    static JTextArea resultTextArea;
    static JButton olderButton;
    static boolean isOlder = true;

    public RelationShipConvertPanel() {
        super();
        setLayout(null);
        inpuTextArea = new JTextPane();
        inpuTextArea.setBounds(5, 10, 250, 50);
        inpuTextArea.setEditable(false);
        inpuTextArea.setBackground(new Color(153, 187, 170));
        inpuTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        resultTextArea = new JTextArea();
        resultTextArea.setBounds(5, 61, 250, 50);
        resultTextArea.setEditable(false);
        resultTextArea.setBackground(new Color(153, 187, 170));
        resultTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 35));
        resultTextArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        //******************下面是各种按钮***************************
        ButtonListener btnListener = new ButtonListener();
        //父 按钮
        JButton fatherButton = new JButton("父");
        fatherButton.setBounds(5, 120, 55, 55);
        fatherButton.setBackground(new Color(95, 95, 95));
        fatherButton.setForeground(Color.white);
        fatherButton.setFont(new Font("宋体", Font.BOLD, 20));
        fatherButton.addActionListener(btnListener);
        //母 按钮
        JButton motherButton = new JButton("母");
        motherButton.setBounds(70, 120, 55, 55);
        motherButton.setBackground(new Color(95, 95, 95));
        motherButton.setForeground(Color.white);
        motherButton.setFont(new Font("宋体", Font.BOLD, 20));
        motherButton.addActionListener(btnListener);
        // 退格
        JButton backButton = new JButton("←");
        backButton.setBounds(135, 120, 55, 55);
        backButton.setBackground(new Color(217, 2, 0));
        backButton.setForeground(Color.white);
        backButton.setFont(new Font("宋体", Font.BOLD, 20));
        backButton.addActionListener(btnListener);
        //清零
        JButton acButton = new JButton("AC");
        acButton.setBounds(200, 120, 55, 55);
        acButton.setBackground(new Color(217, 2, 0));
        acButton.setForeground(Color.white);
        acButton.setFont(new Font("宋体", Font.BOLD, 16));
        acButton.addActionListener(btnListener);
        //兄
        JButton bortherButton = new JButton("兄");
        bortherButton.setBounds(5, 185, 55, 55);
        bortherButton.setBackground(new Color(95, 95, 95));
        bortherButton.setForeground(Color.white);
        bortherButton.setFont(new Font("宋体", Font.BOLD, 20));
        bortherButton.addActionListener(btnListener);
        //弟
        JButton littleBortherButton = new JButton("弟");
        littleBortherButton.setBounds(70, 185, 55, 55);
        littleBortherButton.setBackground(new Color(95, 95, 95));
        littleBortherButton.setForeground(Color.white);
        littleBortherButton.setFont(new Font("宋体", Font.BOLD, 20));
        littleBortherButton.addActionListener(btnListener);
        //夫
        JButton husbandButton = new JButton("夫");
        husbandButton.setBounds(135, 185, 55, 55);
        husbandButton.setBackground(new Color(95, 95, 95));
        husbandButton.setForeground(Color.white);
        husbandButton.setFont(new Font("宋体", Font.BOLD, 20));
        husbandButton.addActionListener(btnListener);
        //是否年长
        olderButton = new JButton("小");
        olderButton.setBounds(200, 185, 55, 55);
        olderButton.setBackground(new Color(217, 2, 0));
        olderButton.setForeground(Color.white);
        olderButton.setFont(new Font("宋体", Font.BOLD, 20));
        olderButton.addActionListener(btnListener);
        //姐
        JButton sisterButton = new JButton("姐");
        sisterButton.setBounds(5, 250, 55, 55);
        sisterButton.setBackground(new Color(95, 95, 95));
        sisterButton.setForeground(Color.white);
        sisterButton.setFont(new Font("宋体", Font.BOLD, 20));
        sisterButton.addActionListener(btnListener);
        //妹
        JButton littleSisterButton = new JButton("妹");
        littleSisterButton.setBounds(70, 250, 55, 55);
        littleSisterButton.setBackground(new Color(95, 95, 95));
        littleSisterButton.setForeground(Color.white);
        littleSisterButton.setFont(new Font("宋体", Font.BOLD, 20));
        littleSisterButton.addActionListener(btnListener);
        //妻
        JButton wifeButton = new JButton("妻");
        wifeButton.setBounds(135, 250, 55, 55);
        wifeButton.setBackground(new Color(95, 95, 95));
        wifeButton.setForeground(Color.white);
        wifeButton.setFont(new Font("宋体", Font.BOLD, 20));
        wifeButton.addActionListener(btnListener);
        //的
        JButton deButton = new JButton("的");
        deButton.setBounds(200, 250, 55, 55);
        deButton.setBackground(new Color(212, 98, 2));
        deButton.setForeground(Color.white);
        deButton.setFont(new Font("宋体", Font.BOLD, 20));
        deButton.addActionListener(btnListener);
        //子
        JButton sonButton = new JButton("儿");
        sonButton.setBounds(5, 315, 55, 55);
        sonButton.setBackground(new Color(95, 95, 95));
        sonButton.setForeground(Color.white);
        sonButton.setFont(new Font("宋体", Font.BOLD, 20));
        sonButton.addActionListener(btnListener);
        //女
        JButton daughterbButton = new JButton("女");
        daughterbButton.setBounds(70, 315, 55, 55);
        daughterbButton.setBackground(new Color(95, 95, 95));
        daughterbButton.setForeground(Color.white);
        daughterbButton.setFont(new Font("宋体", Font.BOLD, 20));
        daughterbButton.addActionListener(btnListener);
        //等于
        JButton resultButton = new JButton("=");
        resultButton.setBounds(135, 315, 120, 55);
        resultButton.setBackground(new Color(212, 98, 2));
        resultButton.setForeground(Color.white);
        resultButton.setFont(new Font("宋体", Font.BOLD, 40));
        resultButton.addActionListener(btnListener);
        //*********************************************************

        add(fatherButton);
        add(motherButton);
        add(backButton);
        add(acButton);
        add(bortherButton);
        add(littleBortherButton);
        add(husbandButton);
        add(olderButton);
        add(sisterButton);
        add(littleSisterButton);
        add(wifeButton);
        add(deButton);
        add(sonButton);
        add(daughterbButton);
        add(resultButton);
        //**以上为按钮**
        add(inpuTextArea);
        add(resultTextArea);
        setBackground(new Color(51, 51, 51));
        initData();
    }

    public Dimension getPreferredSize() {
        return new Dimension(280, 420);
    }

    private void initData() {
        BufferedReader bfReader = null;
        bfReader = new BufferedReader(new StringReader(Constants.data));
        String tempString;
        try {
            tempString = bfReader.readLine();
            while (tempString.length() > 3) {
                dataHashtable.put(StringUtils.substringBetween(tempString, "[", "]"), StringUtils.substringBetween(tempString, "{", "}"));
                tempString = bfReader.readLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "数据载入失败!");
        } catch (NullPointerException e) {
            System.out.println("数据载入完成->共加载数据" + dataHashtable.size() + "条");
        }

    }

}

class ButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        String which = ((JButton) e.getSource()).getText();
        if (which.equals("AC")) {
            RelationShipConvertPanel.now = "你";
            RelationShipConvertPanel.isOlder = true;
            RelationShipConvertPanel.olderButton.setBackground(new Color(217, 2, 0));
            RelationShipConvertPanel.inpuTextArea.setText("");
            RelationShipConvertPanel.resultTextArea.setText("");
        } else if (which.equals("=")) {
            if (RelationShipConvertPanel.now.charAt(0) == '<') {
                String[] oldChooseString = StringUtils.substringsBetween(RelationShipConvertPanel.now, "<", ">");
                RelationShipConvertPanel.now = RelationShipConvertPanel.isOlder ? oldChooseString[0] : oldChooseString[1];
                RelationShipConvertPanel.inpuTextArea.setText(RelationShipConvertPanel.inpuTextArea.getText() + (RelationShipConvertPanel.isOlder ? "(比你年长)" : "(比你年轻)"));
            }
            RelationShipConvertPanel.olderButton.setBackground(new Color(217, 2, 0));
            RelationShipConvertPanel.isOlder = true;
            RelationShipConvertPanel.resultTextArea.setText(RelationShipConvertPanel.now);
            //RelationShipConvertPanel.now="你";
        } else if (which.equals("小")) {
            RelationShipConvertPanel.isOlder = false;
            RelationShipConvertPanel.olderButton.setBackground(new Color(217, 2, 0));
        } else if (which.equals("←")) {

        } else if (which.equals("的")) {
            String temp = "";
            RelationShipConvertPanel.olderButton.setBackground(new Color(217, 2, 0));
            if (RelationShipConvertPanel.now.charAt(0) == '<') {
                String[] oldChooseString = StringUtils.substringsBetween(RelationShipConvertPanel.now, "<", ">");
                RelationShipConvertPanel.now = RelationShipConvertPanel.isOlder ? oldChooseString[0] : oldChooseString[1];
                temp = RelationShipConvertPanel.isOlder ? "(比你年长)" : "(比你年轻)";
            }
            RelationShipConvertPanel.inpuTextArea.setText(RelationShipConvertPanel.inpuTextArea.getText() + temp + which);
            RelationShipConvertPanel.isOlder = true;
        } else {//按下关系按钮时
            RelationShipConvertPanel.olderButton.setBackground(new Color(217, 2, 0));
            RelationShipConvertPanel.inpuTextArea.setText(RelationShipConvertPanel.inpuTextArea.getText() + which);
            RelationShipConvertPanel.now = getNext(which).substring(2);
            if (RelationShipConvertPanel.now.charAt(0) == '<') {
                RelationShipConvertPanel.olderButton.setBackground(new Color(212, 98, 2));
            }
            RelationShipConvertPanel.isOlder = true;
        }

    }

    String getNext(String what) {
        String relationsString = RelationShipConvertPanel.dataHashtable.get(RelationShipConvertPanel.now);
        String[] relation = null;
        if (relationsString != null) {
            relation = StringUtils.substringsBetween(relationsString, "(", ")");
        } else {
            RelationShipConvertPanel.resultTextArea.setText("超出计算范围");
            return RelationShipConvertPanel.now;
        }
        if (what.equals("父")) {
            return relation[0];
        } else if (what.equals("母")) {
            return relation[1];
        } else if (what.equals("兄")) {
            return relation[2];
        } else if (what.equals("弟")) {
            return relation[3];
        } else if (what.equals("姐")) {
            return relation[4];
        } else if (what.equals("妹")) {
            return relation[5];
        } else if (what.equals("夫")) {
            return relation[6];
        } else if (what.equals("妻")) {
            return relation[7];
        } else if (what.equals("儿")) {
            return relation[8];
        } else if (what.equals("女")) {
            return relation[9];
        } else {
            return RelationShipConvertPanel.now;
        }

    }
}
