package edu.jiangxin.apktoolbox.android.screenshot;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;
import edu.jiangxin.apktoolbox.utils.DateUtils;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class ScreenShotPanel extends EasyPanel {
    private static final long serialVersionUID = 1L;

    private JPanel directoryPanel;

    private JTextField directoryTextField;

    private JButton directoryButton;

    private JPanel fileNamePanel;

    private JTextField fileNameTextField;

    private JButton fileNameButton;

    private JPanel screenshotPanel;

    private JCheckBox openCheckBox;

    private JCheckBox copyCheckBox;

    private JButton screenshotButton;

    private JButton getExistButton;

    public ScreenShotPanel() throws HeadlessException {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createDirectoryPanel();
        add(directoryPanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createFileNamePanel();
        add(fileNamePanel);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createScreenshotPanel();
        add(screenshotPanel);
    }

    private void createScreenshotPanel() {
        screenshotPanel = new JPanel();
        screenshotPanel.setLayout(new BoxLayout(screenshotPanel, BoxLayout.X_AXIS));

        openCheckBox = new JCheckBox("Open Dir");
        openCheckBox.setSelected(false);

        copyCheckBox = new JCheckBox("Copy Pic");
        copyCheckBox.setSelected(false);

        screenshotButton = new JButton("Sceenshot");
        screenshotButton.addMouseListener(new ScreenshotButtonMouseAdapter());

        getExistButton = new JButton("Get Exist");
        getExistButton.addMouseListener(new GetExistButtonMouseAdapter());

        screenshotPanel.add(openCheckBox);
        screenshotPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        screenshotPanel.add(copyCheckBox);
        screenshotPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        screenshotPanel.add(screenshotButton);
        screenshotPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        screenshotPanel.add(getExistButton);
    }

    private void createFileNamePanel() {
        fileNamePanel = new JPanel();

        fileNameTextField = new JTextField();
        fileNameTextField.setToolTipText("timestamp default(for example: 20180101122345.png)");

        fileNameButton = new JButton("File name");

        fileNamePanel.setLayout(new BoxLayout(fileNamePanel, BoxLayout.X_AXIS));
        fileNamePanel.add(fileNameTextField);
        fileNamePanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        fileNamePanel.add(fileNameButton);
    }

    private void createDirectoryPanel() {
        directoryPanel = new JPanel();

        directoryTextField = new JTextField();
        directoryTextField.setText(conf.getString("screenshot.save.dir", System.getenv("USERPROFILE")));
        directoryTextField.setTransferHandler(new DirectoryTextFieldTransferHandler());

        directoryButton = new JButton("Save Directory");
        directoryButton.addMouseListener(new DirectoryButtonMouseAdapter());

        directoryPanel.setLayout(new BoxLayout(directoryPanel, BoxLayout.X_AXIS));
        directoryPanel.add(directoryTextField);
        directoryPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        directoryPanel.add(directoryButton);
    }

    private final class DirectoryButtonMouseAdapter extends MouseAdapter {
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
    }

    private final class GetExistButtonMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            System.out.println("Get Exist");
        }
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
                fileName = DateUtils.getCurrentDateString() + ".png";
            }
            File file = new File(dirName, fileName);
            try {
                Utils.blockedExecutor("adb shell /system/bin/screencap -p /sdcard/screenshot.png");
                logger.info("screencap finish");
                Utils.blockedExecutor("adb pull /sdcard/screenshot.png " + file.getCanonicalPath());
                logger.info("pull finish");
                if (openCheckBox.isSelected()) {
                    Utils.blockedExecutor("explorer /e,/select, " + file.getCanonicalPath());
                    logger.info("open dir finish");
                }
                if (copyCheckBox.isSelected()) {
                    logger.info("copy the snapshot");
                    Image image = ImageIO.read(file);
                    setClipboardImage(ScreenShotPanel.this, image);
                    logger.info("copy finish");
                }
            } catch (IOException e1) {
                logger.error("screenshot fail", e1);
            } finally {
                Utils.setFrameTitle(ScreenShotPanel.this, title);
            }
        }
    }

    private final class DirectoryTextFieldTransferHandler extends TransferHandler {
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
    }

    private static void setClipboardImage(JPanel frame, final Image image) {
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
