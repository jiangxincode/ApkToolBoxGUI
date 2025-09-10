package edu.jiangxin.apktoolbox.convert.encoding;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.Serial;

public class GarbledTextRecoveryPanel extends EasyPanel {
    @Serial
    private static final long serialVersionUID = 1L;

    private JPanel inputPanel;

    private JTextArea inputArea;

    private JPanel resultPanel;

    private DefaultTableModel tableModel;

    private JPanel operationPanel;

    private static final String[] CHARSETS = {
            "UTF-8", "GBK", "ISO-8859-1", "Big5", "Shift_JIS", "EUC-KR", "Windows-1252"
    };

    public GarbledTextRecoveryPanel() throws HeadlessException {
        super();
    }

    @Override
    public void initUI() {
        setPreferredSize(new Dimension(900, 600));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createInputPanel();
        add(inputPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createResultPanel();
        add(resultPanel);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createOperationPanel();
        add(operationPanel);
    }

    private void createInputPanel() {
        inputPanel = new JPanel();
        BoxLayout layout = new BoxLayout(inputPanel, BoxLayout.X_AXIS);
        inputPanel.setLayout(layout);

        inputArea = new JTextArea(4, 60);
        inputArea.setText("鐢变簬鍦ㄧ被璺\uE21A緞涓\uE15E彂鐜颁簡涓€涓\uE045垨澶氫釜澶勭悊绋嬪簭锛屽洜姝ゅ惎鐢ㄤ簡");
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(BorderFactory.createTitledBorder("Please enter the garbled text:"));
        inputScroll.setPreferredSize(new Dimension(700, 200));

        inputPanel.add(inputScroll);
    }

    private void createResultPanel() {
        resultPanel = new JPanel();
        BoxLayout layout = new BoxLayout(resultPanel, BoxLayout.Y_AXIS);
        resultPanel.setLayout(layout);


        String[] columns = {"Current Encoding", "Original Encoding", "Recovered Result"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable resultTable = new JTable(tableModel);
        resultTable.setRowHeight(30);
        resultTable.getColumnModel().getColumn(2).setPreferredWidth(400);
        JScrollPane tableScroll = new JScrollPane(resultTable);
        tableScroll.setPreferredSize(new Dimension(700, 300));

        resultPanel.add(tableScroll);
    }

    private void createOperationPanel() {
        operationPanel = new JPanel();
        BoxLayout layout = new BoxLayout(operationPanel, BoxLayout.X_AXIS);
        operationPanel.setLayout(layout);

        JButton recoverButton = new JButton("Try All Encoding Combinations");
        recoverButton.addActionListener(e -> recoverAll());

        operationPanel.add(recoverButton);
    }

    private void recoverAll() {
        tableModel.setRowCount(0);
        String input = inputArea.getText().trim();
        if (input.isEmpty()) return;

        for (String now : CHARSETS) {
            for (String orig : CHARSETS) {
                if (!now.equals(orig)) {
                    try {
                        byte[] bytes = input.getBytes(now);
                        String recovered = new String(bytes, orig);

                        tableModel.addRow(new Object[]{now, orig, recovered});
                    } catch (Exception ex) {
                        tableModel.addRow(new Object[]{now, orig, "Conversion Failed"});
                    }
                }

            }
        }
    }
}
