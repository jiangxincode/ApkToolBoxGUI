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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

import org.apache.commons.lang3.StringUtils;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.StreamHandler;
import edu.jiangxin.apktoolbox.utils.Utils;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class ScreenShotPanel extends EasyPanel {
    private static final long serialVersionUID = 1L;

    private static final int PANEL_WIDTH = Constants.DEFAULT_WIDTH - 50;

    private static final int PANEL_HIGHT = 110;

    private static final int CHILD_PANEL_HIGHT = 30;

    private static final int CHILD_PANEL_LEFT_WIDTH = 600;

    private static final int CHILD_PANEL_RIGHT_WIDTH = 130;

    private JPanel directoryPanel;

    private JTextField directoryTextField;

    private JButton directoryButton;

    private JPanel fileNamePanel;

    private JTextField fileNameTextField;

    private JButton fileNameButton;

    private JPanel sceenshotPanel;

    private JCheckBox openCheckBox;

    private JCheckBox copyCheckBox;

    private JButton sceenshotButton;

    private JButton getExistButton;

    public ScreenShotPanel() throws HeadlessException {
        super();

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        Utils.setJComponentSize(this, PANEL_WIDTH, PANEL_HIGHT);

        createDirectoryPanel();
        add(directoryPanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createFileNamePanel();
        add(fileNamePanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createScreenshotPanel();
        add(sceenshotPanel);
    }

    private void createScreenshotPanel() {
        sceenshotPanel = new JPanel();
        Utils.setJComponentSize(sceenshotPanel, PANEL_WIDTH, CHILD_PANEL_HIGHT);
        sceenshotPanel.setLayout(new BoxLayout(sceenshotPanel, BoxLayout.X_AXIS));

        openCheckBox = new JCheckBox("Open Dir");
        Utils.setJComponentSize(openCheckBox, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);
        openCheckBox.setSelected(false);

        copyCheckBox = new JCheckBox("Copy Pic");
        Utils.setJComponentSize(copyCheckBox, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);
        copyCheckBox.setSelected(false);

        sceenshotButton = new JButton("Sceenshot");
        Utils.setJComponentSize(sceenshotButton, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);
        sceenshotButton.addMouseListener(new ScreenshotButtonMouseAdapter());

        getExistButton = new JButton("Get Exist");
        Utils.setJComponentSize(getExistButton, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);
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
        sceenshotPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        sceenshotPanel.add(getExistButton);
    }

    private void createFileNamePanel() {
        fileNamePanel = new JPanel();
        Utils.setJComponentSize(fileNamePanel, PANEL_WIDTH, CHILD_PANEL_HIGHT);

        fileNameTextField = new JTextField();
        Utils.setJComponentSize(fileNameTextField, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HIGHT);
        fileNameTextField.setToolTipText("timestamp default(for example: 20180101122345.png)");

        fileNameButton = new JButton("File name");
        Utils.setJComponentSize(fileNameButton, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);

        fileNamePanel.setLayout(new BoxLayout(fileNamePanel, BoxLayout.X_AXIS));
        fileNamePanel.add(fileNameTextField);
        fileNamePanel.add(Box.createHorizontalGlue());
        fileNamePanel.add(fileNameButton);
    }

    private void createDirectoryPanel() {
        directoryPanel = new JPanel();
        Utils.setJComponentSize(directoryPanel, PANEL_WIDTH, CHILD_PANEL_HIGHT);

        directoryTextField = new JTextField();
        Utils.setJComponentSize(directoryTextField, CHILD_PANEL_LEFT_WIDTH, CHILD_PANEL_HIGHT);
        directoryTextField.setText(conf.getString("screenshot.save.dir", System.getenv("USERPROFILE")));
        directoryTextField.setTransferHandler(new TransferHandler() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
                    String filepath = o.toString();
                    filepath = filepath.substring(1, filepath.length() - 1);
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

        directoryButton = new JButton("Save Directory");
        Utils.setJComponentSize(directoryButton, CHILD_PANEL_RIGHT_WIDTH, CHILD_PANEL_HIGHT);
        directoryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                JFileChooser jfc = new JFileChooser(directoryTextField.getText());
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
        directoryPanel.add(Box.createHorizontalGlue());
        directoryPanel.add(directoryButton);
    }

    public static void setClipboardImage(JPanel frame, final Image image) {
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

    private final class ScreenshotButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            String title = Utils.getFrameTitle(ScreenShotPanel.this);
            Utils.setFrameTitle(ScreenShotPanel.this, title + "    [Processing]");
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
            try {
                Process process1 = Runtime.getRuntime()
                        .exec("adb shell /system/bin/screencap -p /sdcard/screenshot.png");
                new StreamHandler(process1.getInputStream(), 0).start();
                new StreamHandler(process1.getErrorStream(), 1).start();
                process1.waitFor();
                logger.info("screencap finish");
                Process process2 = Runtime.getRuntime()
                        .exec(new String[] { "adb", "pull", "/sdcard/screenshot.png", file.getCanonicalPath() });
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
                    setClipboardImage(ScreenShotPanel.this, image);
                    logger.info("copy finish");
                }
            } catch (IOException | InterruptedException e1) {
                logger.error("screenshot fail", e1);
            } finally {
                Utils.setFrameTitle(ScreenShotPanel.this, title);
            }
        }
    }
}
