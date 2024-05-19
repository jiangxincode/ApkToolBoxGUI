package edu.jiangxin.apktoolbox.help.settings;

import edu.jiangxin.apktoolbox.swing.extend.EasyChildTabbedPanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DependencyPathPanel extends EasyChildTabbedPanel {

    private static final long serialVersionUID = 1L;

    @Override
    public void createUI() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        createPathPanel(this, "Path of 7ZIP(e.g.\"C:/Program Files/7-Zip/7z.exe\")", "https://www.7-zip.org/", Constants.SEVEN_ZIP_PATH_KEY);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createPathPanel(this, "Path of RAR(e.g.\"C:/Program Files/WinRAR/Rar.exe\")", "https://www.win-rar.com/", Constants.RAR_PATH_KEY);
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        createPathPanel(this, "Path of RAR(e.g.\"C:/Program Files/WinRAR/WinRAR.exe\")", "https://www.win-rar.com/", Constants.WIN_RAR_PATH_KEY);
    }

    private void createPathPanel(JPanel panel, String label, String website, String confKey) {
        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new BoxLayout(pathPanel, BoxLayout.Y_AXIS));
        panel.add(pathPanel);

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


        visitWebsiteButton.addActionListener(e -> {
            URI uri;
            try {
                uri = new URI(website);
                Desktop.getDesktop().browse(uri);
            } catch (URISyntaxException ex) {
                logger.error("URISyntaxException", ex);
            } catch (IOException ex) {
                logger.error("IOException", ex);
            }
        });

        pathButton.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setDialogTitle("select a file");
            int ret = jfc.showDialog(new JLabel(), null);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                pathTextField.setText(file.getAbsolutePath());
                conf.setProperty(confKey, pathTextField.getText());
            }
        });
    }
}

