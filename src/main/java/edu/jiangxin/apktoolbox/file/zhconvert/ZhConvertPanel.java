package edu.jiangxin.apktoolbox.file.zhconvert;

import com.github.houbb.opencc4j.util.ZhConverterUtil;
import edu.jiangxin.apktoolbox.swing.extend.FileListPanel;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.List;
import java.util.*;

public class ZhConvertPanel extends EasyPanel {
    @Serial
    private static final long serialVersionUID = 1L;

    private JPanel northPanel;

    private JSplitPane centerPanel;

    private FileListPanel fileListPanel;

    private JTextField suffixTextField;

    private JCheckBox recursiveCheckBox;

    private JComboBox comboBox;

    private JTextField keyText;

    private JTextField valueText;

    private JTextArea textArea;

    private JList transformList;

    private static ZHConverterUtils myZHConverterUtils = new ZHConverterUtils();

    public ZhConvertPanel() throws HeadlessException {
        super();
    }

    @Override
    public void initUI() {
        // JSplitPanel只能用BorderLayout，JFrame默认是BorderLayout，JPanel默认是BoxLayout
        BorderLayout boxLayout = new BorderLayout();
        setLayout(boxLayout);

        createNorthPanel();
        add(northPanel, BorderLayout.NORTH);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
    }

    private void createNorthPanel() {
        northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

        fileListPanel = new FileListPanel();
        northPanel.add(fileListPanel);
        northPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));
        northPanel.add(optionPanel);

        JLabel suffixLabel = new JLabel("Suffix:");
        suffixTextField = new JTextField();
        suffixTextField.setToolTipText("an array of extensions, ex. {\"java\",\"xml\"}. If this parameter is empty, all files are returned.");
        suffixTextField.setText(conf.getString("osconvert.suffix"));
        optionPanel.add(suffixLabel);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(suffixTextField);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        recursiveCheckBox = new JCheckBox("Recursive");
        recursiveCheckBox.setSelected(true);
        optionPanel.add(recursiveCheckBox);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        comboBox = new JComboBox();
        comboBox.addItem(Constants.zhSimple2zhTw);
        comboBox.addItem(Constants.zhTw2zhSimple);
        optionPanel.add(comboBox);

        JButton convertBtn = new JButton("确认转换");
        convertBtn.addActionListener(new ConvertBtnActionListener());
        northPanel.add(convertBtn);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    private void createCenterPanel() {
        JPanel centerLeftTopPanel = new JPanel();
        centerLeftTopPanel.setLayout(new BoxLayout(centerLeftTopPanel, BoxLayout.Y_AXIS));

        JPanel keyValuePanel = new JPanel();
        keyValuePanel.setLayout(new BoxLayout(keyValuePanel, BoxLayout.X_AXIS));
        centerLeftTopPanel.add(keyValuePanel);

        keyText = new JTextField(10);
        keyValuePanel.add(keyText);

        valueText = new JTextField(10);
        keyValuePanel.add(valueText);

        JButton saveBtn = new JButton("添加词组定义");
        saveBtn.addActionListener(new SaveBtnActionListener());
        centerLeftTopPanel.add(saveBtn);

        JScrollPane centerLeftBottomPanel = new JScrollPane();
        textArea = new JTextArea();
        textArea.setMargin(new Insets(10, 10, 10, 10));
        //自动换行
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        centerLeftBottomPanel.add(textArea);

        //垂直滚动条自动出现
        centerLeftBottomPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JSplitPane centerLeftSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerLeftTopPanel, centerLeftBottomPanel);

        transformList = new JList();
        refreshListData();
        transformList.setFont(new Font("Dialog", 1, 18));
        transformList.setBorder(BorderFactory.createTitledBorder("词组转换定义"));
        JScrollPane centerRightScrollPanel = new JScrollPane(transformList);

        centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerLeftSplitPanel, centerRightScrollPanel);
        centerPanel.setDividerLocation(0.7f);
    }

    private final class ConvertBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                String converType = comboBox.getSelectedItem().toString();
                System.out.println(converType);

                List<File> fileList = new ArrayList<>();
                for (File file : fileListPanel.getFileList()) {
                    String[] extensions = null;
                    if (StringUtils.isNotEmpty(suffixTextField.getText())) {
                        extensions = suffixTextField.getText().split(",");
                    }
                    fileList.addAll(FileUtils.listFiles(file, extensions, recursiveCheckBox.isSelected()));
                }
                Set<File> fileSet = new TreeSet<>(fileList);
                fileList.clear();
                fileList.addAll(fileSet);

                textArea.setCaretPosition(textArea.getText().length());
                try {
                    scanFolderAndConver(fileList, converType, textArea);
                    JOptionPane.showMessageDialog(getFrame().getFrames()[0], "转换成功" , "提示",JOptionPane.WARNING_MESSAGE);
                    textArea.append("done..." + "\n");
                    textArea.setCaretPosition(textArea.getText().length());
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(getFrame().getFrames()[0], "异常：" + e1.getMessage(), "异常",JOptionPane.ERROR_MESSAGE);
                    textArea.append("转换异常..." + "\n");
                    textArea.setCaretPosition(textArea.getText().length());
                }
            }).start();
        }
    }

    private final class SaveBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String key = keyText.getText();
            String value = valueText.getText();

            if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)){
                myZHConverterUtils.storeDataToProperties(key,value);
                JOptionPane.showMessageDialog(ZhConvertPanel.this, "成功插入一条词组对应信息", "提示",JOptionPane.WARNING_MESSAGE);
                refreshListData();
                textArea.append("成功插入一条词组对应信息：" + key + " <===> " + value + "\n");
            }else{
                JOptionPane.showMessageDialog(ZhConvertPanel.this, "键值对不能为空", "提示",JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void refreshListData(){
        List<String> listModel = new ArrayList<>();
        Properties properties = myZHConverterUtils.getCharMap();
        for (String key2 : properties.stringPropertyNames()) {
            listModel.add(key2 + " <===> " + properties.getProperty(key2));
        }
        transformList.setListData(listModel.toArray());
    }

    private static void scanFolderAndConver(List<File> fileList, String converType, JTextArea jTextArea) throws IOException {
        jTextArea.append("文件转换开始：\n");
        for (File file : fileList){
            jTextArea.append("开始转换："+file + "\n");
            String content = org.apache.commons.io.FileUtils.readFileToString(file, "utf-8");

            if (converType.equals(Constants.zhSimple2zhTw)){
                String str = myZHConverterUtils.myConvertToTW(content);
                String result = ZhConverterUtil.toTraditional(str);
                org.apache.commons.io.FileUtils.write(file,result,"UTF-8");
            }else{
                String str = myZHConverterUtils.myConvertToSimple(content);
                String result = ZhConverterUtil.toSimple(str);
                org.apache.commons.io.FileUtils.write(file,result,"UTF-8");
            }
            jTextArea.append("转换完成："+file + "\n");
            jTextArea.setCaretPosition(jTextArea.getText().length());
        }
        jTextArea.append("文件转换结束：\n");
        jTextArea.append("==========================================================================：\n");
    }
}
