package edu.jiangxin.apktoolbox.config;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ConfigPanel extends EasyPanel {

    private static final long serialVersionUID = 1L;


    public ConfigPanel() throws HeadlessException {
        super();
        initUI();
    }

    private void initUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createPathPanel("Path of ApkTool(e.g.\"D:/Portable/tools/apktool_2.5.0.jar\")", "https://github.com/iBotPeaches/Apktool", Constants.APKTOOL_PATH_KEY);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createPathPanel("Path of ApkSigner(e.g.\"D:/Portable/tools/apksigner.jar\")", "http://apk.aq.163.com/apkpack.do#download", Constants.APKSIGNER_PATH_KEY);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createPathPanel("Path of JD-GUI(e.g.\"D:/Portable/tools/jd-gui-1.6.6.jar\")", "http://jd.benow.ca", Constants.JD_GUI_PATH_KEY);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createPathPanel("Path of Luyten(e.g.\"D:/Portable/tools/luyten-0.5.4.jar\")", "https://github.com/deathmarine/Luyten", Constants.LUYTEN_PATH_KEY);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createPathPanel("Path of JADX(e.g.\"D:/Portable/tools/jadx-1.2.0/bin/jadx-gui.bat\")", "https://github.com/skylot/jadx", Constants.JADX_PATH_KEY);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createPathPanel("Path of AXMLPrinter(e.g.\"D:/Portable/tools/AXMLPrinter3.jar\")", "https://github.com/jiangxincode/AXMLPrinter3", Constants.AXMLPRINTER_PATH_KEY);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createPathPanel("Path of 7ZIP(e.g.\"C:/Program Files/7-Zip/7z.exe\")", "https://www.7-zip.org/", Constants.SEVEN_ZIP_PATH_KEY);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createPathPanel("Path of RAR(e.g.\"C:/Program Files/WinRAR/Rar.exe\")", "https://www.win-rar.com/", Constants.RAR_PATH_KEY);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createPathPanel("Path of RAR(e.g.\"C:/Program Files/WinRAR/WinRAR.exe\")", "https://www.win-rar.com/", Constants.WIN_RAR_PATH_KEY);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
    }

    private void createPathPanel(String label, String website, String confKey) {
        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new BoxLayout(pathPanel, BoxLayout.Y_AXIS));
        add(pathPanel);

        JPanel firstLinePanel = new JPanel();
        firstLinePanel.setLayout(new BoxLayout(firstLinePanel, BoxLayout.X_AXIS));

        JLabel pathLabel = new JLabel(label);

        JButton visitWebsiteButton = new JButton(bundle.getString("download.button"));

        firstLinePanel.add(pathLabel);
        firstLinePanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        firstLinePanel.add(visitWebsiteButton);
        firstLinePanel.add(Box.createHorizontalGlue());

        JPanel secondLinePanel = new JPanel();
        secondLinePanel.setLayout(new BoxLayout(secondLinePanel, BoxLayout.X_AXIS));

        JTextField pathTextField = new JTextField();
        pathTextField.setText(conf.getString(confKey));

        JButton pathButton = new JButton(bundle.getString("choose.file.button"));

        secondLinePanel.add(pathTextField);
        secondLinePanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        secondLinePanel.add(pathButton);

        pathPanel.add(firstLinePanel);
        pathPanel.add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        pathPanel.add(secondLinePanel);


        visitWebsiteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                URI uri;
                try {
                    uri = new URI(website);
                    Desktop.getDesktop().browse(uri);
                } catch (URISyntaxException ex) {
                    logger.error("URISyntaxException", ex);
                } catch (IOException ex) {
                    logger.error("IOException", ex);
                }
            }
        });

        pathButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setDialogTitle("select a file");
                int ret = jfc.showDialog(new JLabel(), null);
                switch (ret) {
                    case JFileChooser.APPROVE_OPTION:
                        File file = jfc.getSelectedFile();
                        pathTextField.setText(file.getAbsolutePath());
                        conf.setProperty(confKey, pathTextField.getText());
                        break;
                    default:
                        break;
                }
            }
        });
    }
}

