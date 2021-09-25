package edu.jiangxin.apktoolbox.file;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class DuplicateFindPanel extends EasyPanel
        implements ActionListener {
    private JList contentList;
    private DefaultListModel defaultListModel;
    private static final long serialVersionUID = 1L;
    static private final String newline = "\n";
    JButton browserButton;
    JTextArea textArea;
    JFileChooser fileChooser;

    public DuplicateFindPanel() {
        super();

        setPreferredSize(new Dimension(700, 500));
        setMaximumSize(new Dimension(700, 500));

        setLayout(new BorderLayout());

        defaultListModel = new DefaultListModel();
        contentList = new JList(defaultListModel);

        contentList.setCellRenderer(new ColorfulCellRenderer());

        contentList.setSize(100, 100);
        contentList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        textArea = new JTextArea(100, 100);
        textArea.setMargin(new Insets(5, 5, 5, 5));
        textArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(textArea);
        logScrollPane.add(contentList);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        browserButton = new JButton("Browse");
        browserButton.addActionListener(this);


        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(browserButton);


        //Add the buttons and the list to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(new JScrollPane(contentList), BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //Handle Browse button action.
        if (e.getSource() == browserButton) {

            int returnVal = fileChooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                // Hashmap to store the duplicate files
                Map<String, List<String>> duplicateFiles = new HashMap<String, List<String>>();
                find(duplicateFiles, file);

                //This is where a real application would open the file.
                defaultListModel.addElement("Looking for Duplicates in Folder Name: " + file.getName() + " at location " + file.getParent());
                defaultListModel.addElement("Double Click on File to open the Folder");

                for (List<String> list : duplicateFiles.values()) {
                    if (list.size() > 1) {
                        defaultListModel.addElement("--");
                        defaultListModel.addElement("Duplicates Found");
                        for (String file1 : list) {
                            defaultListModel.addElement(file1);
                        }
                    }
                }
                defaultListModel.addElement("--");

                contentList.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent mouseEvent) {
                        JList list = (JList) mouseEvent.getSource();
                        if (mouseEvent.getClickCount() == 2) {
                            int index = list.locationToIndex(mouseEvent.getPoint());
                            String str = defaultListModel.getElementAt(index).toString();

                            if (new File(str) != null)
                                try {
                                    Desktop.getDesktop().open(new File(str).getParentFile());
                                } catch (Exception e) {
                                    // Should not generate an exception!
                                }
                        } else if (mouseEvent.getClickCount() == 3) {   // Triple-click
                            int index = list.locationToIndex(mouseEvent.getPoint());
                            System.out.println(index);
                        }
                    }
                });
            } else {
                textArea.append("Browse command cancelled by user." + newline);
            }
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }

    public static void find(Map<String, List<String>> duplicateFiles, File dir) {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                find(duplicateFiles, f);
            } else {
                String hash = f.getName() + f.length();
                List<String> list = duplicateFiles.get(hash);
                if (list == null) {
                    // Create a linked list and add duplicate entries
                    list = new LinkedList<String>();
                    duplicateFiles.put(hash, list);
                }
                list.add(f.getAbsolutePath());
            }
        }
    }

    class ColorfulCellRenderer extends JLabel implements ListCellRenderer {
        private Color[] colors = new Color[]{Color.GREEN, Color.WHITE, Color.RED};
        public ColorfulCellRenderer() {
            setOpaque(true);
        }
        @Override
        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            setText(value.toString());
            Color background;
            Color foreground;
//当前Renderer是否是拖拽目标
            JList.DropLocation dropLocation = list.getDropLocation();
            if (dropLocation != null
                    && !dropLocation.isInsert()
                    && dropLocation.getIndex() == index) {
                background = Color.BLUE;
                foreground = Color.WHITE;
//当前Renderer是否被选中
            } else if (isSelected) {
                background = Color.BLUE;
                foreground = Color.WHITE;
            } else {
                background = colors[index%3];
                foreground = Color.BLACK;
            }
            setBackground(background);
            setForeground(foreground);
            return this;
        }
    }
}
