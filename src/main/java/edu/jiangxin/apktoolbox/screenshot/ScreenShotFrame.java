package edu.jiangxin.apktoolbox.screenshot;

import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

import edu.jiangxin.apktoolbox.swing.extend.JEasyFrame;
import edu.jiangxin.apktoolbox.utils.StreamHandler;
import edu.jiangxin.apktoolbox.utils.Utils;

public class ScreenShotFrame extends JEasyFrame {
    private static final long serialVersionUID = 1L;

    public ScreenShotFrame() throws HeadlessException {
        super();
        setTitle("Screenshot");
        setSize(600, 130);
        setResizable(false);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        BoxLayout boxLayout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
        contentPane.setLayout(boxLayout);
        setContentPane(contentPane);

        JPanel directoryPanel = new JPanel();
        contentPane.add(directoryPanel);

        JTextField directoryTextField = new JTextField();
        directoryTextField.setText(conf.getString("screenshot.save.dir", System.getenv("USERPROFILE")));
        directoryTextField.setTransferHandler(new TransferHandler() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                    String filepath = o.toString();
                    if (filepath.startsWith("[")) {
                        filepath = filepath.substring(1);
                    }
                    if (filepath.endsWith("]")) {
                        filepath = filepath.substring(0, filepath.length() - 1);
                    }
                    directoryTextField.setText(filepath);
                    return true;
                } catch (Exception e) {
                    logger.error("import data excetion", e);
                }
                return false;
            }

            @Override
            public boolean canImport(JComponent jComponent, DataFlavor[] dataFlavors) {
                for (int i = 0; i < dataFlavors.length; i++) {
                    if (DataFlavor.javaFileListFlavor.equals(dataFlavors[i])) {
                        return true;
                    }
                }
                return false;
            }
        });

        JButton directoryButton = new JButton("Save Directory");
        directoryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.setDialogTitle("select a directory");
                int ret = jfc.showDialog(new JLabel(), null);
                switch (ret) {
                case JFileChooser.APPROVE_OPTION:
                    File file = jfc.getSelectedFile();
                    directoryTextField.setText(file.getAbsolutePath());
                    conf.setProperty("screenshot.save.dir", file.getAbsolutePath());
                    break;

                default:
                    break;
                }

            }
        });

        directoryPanel.setLayout(new BoxLayout(directoryPanel, BoxLayout.X_AXIS));
        directoryPanel.add(directoryTextField);
        directoryPanel.add(directoryButton);

        JPanel fileNamePanel = new JPanel();
        contentPane.add(fileNamePanel);

        JTextField fileNameTextField = new JTextField();
        fileNameTextField.setToolTipText("timestamp default(for example: 20180101122345.png)");

        JButton fileNameButton = new JButton("File name");

        fileNamePanel.setLayout(new BoxLayout(fileNamePanel, BoxLayout.X_AXIS));
        fileNamePanel.add(fileNameTextField);
        fileNamePanel.add(fileNameButton);

        JPanel sceenshotPanel = new JPanel();
        sceenshotPanel.setLayout(new BoxLayout(sceenshotPanel, BoxLayout.X_AXIS));
        contentPane.add(sceenshotPanel);

        JCheckBox openCheckBox = new JCheckBox("Open Dir");
        openCheckBox.setSelected(false);

        JCheckBox copyCheckBox = new JCheckBox("Copy Pic");
        copyCheckBox.setSelected(false);

        JButton sceenshotButton = new JButton("Sceenshot");
        sceenshotButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                String title = getTitle();
                setTitle(title + "    [Processing]");
                String dirName = fileNameTextField.getText();
                if (StringUtils.isEmpty(dirName)) {
                    String defaultDir = System.getenv("USERPROFILE");
                    dirName = conf.getString("screenshot.save.dir", defaultDir);
                    logger.info("dirName: " + dirName);
                }
                String fileName = fileNameTextField.getText();
                if (StringUtils.isEmpty(fileName)) {
                    fileName = Utils.getCurrentDateString() + ".png";
                }
                File file = new File(dirName, fileName);
                if (file.exists()) {

                }
                try {
                    Process process1 = Runtime.getRuntime()
                            .exec("adb shell /system/bin/screencap -p /sdcard/screenshot.png");
                    new StreamHandler(process1.getInputStream(), 0).start();
                    new StreamHandler(process1.getErrorStream(), 1).start();
                    process1.waitFor();
                    logger.info("screencap finish");
                    Process process2 = Runtime.getRuntime()
                            .exec("adb pull /sdcard/screenshot.png " + file.getCanonicalPath());
                    new StreamHandler(process2.getInputStream(), 0).start();
                    new StreamHandler(process2.getErrorStream(), 1).start();
                    process2.waitFor();
                    logger.info("pull finish");
                    if (openCheckBox.isSelected()) {
                        Process process3 = Runtime.getRuntime().exec("explorer /e,/select," + file.getCanonicalPath());
                        new StreamHandler(process3.getInputStream(), 0).start();
                        new StreamHandler(process3.getErrorStream(), 1).start();
                        process3.waitFor();
                        logger.info("open dir finish");
                    }
                    if (copyCheckBox.isSelected()) {
                        logger.info("copy the snapshot");
                        Image image = ImageIO.read(file);
                        setClipboardImage(ScreenShotFrame.this, image);
                        logger.info("copy finish");
                    }
                } catch (IOException | InterruptedException e1) {
                    logger.error("screenshot fail", e1);
                } finally {
                    setTitle(title);
                }
            }
        });

        JButton getExistButton = new JButton("Get Exist");
        getExistButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                System.out.println("Get Exist");
            }
        });

        sceenshotPanel.add(openCheckBox);
        sceenshotPanel.add(copyCheckBox);
        sceenshotPanel.add(sceenshotButton);
        sceenshotPanel.add(getExistButton);

    }

    public static void setClipboardImage(JFrame frame, final Image image) {
        Transferable trans = new Transferable() {
            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor)) {
                    return image;
                }
                throw new UnsupportedFlavorException(flavor);
            }

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] { DataFlavor.imageFlavor };
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }
        };

        frame.getToolkit().getSystemClipboard().setContents(trans, null);
    }
}
