package edu.jiangxin.apktoolbox.swing.extend.plugin.download;

import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class DownloadProcessDialog extends JDialog {

    @Serial
    private static final long serialVersionUID = 1L;
    public final JProgressBar progressBar = new JProgressBar();
    public final JLabel progressLabel = new JLabel("");

    public DownloadProcessDialog(String title) {
        setTitle(title);
        setSize(400, 100);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        //Swing Worker can't update GUI components in modal jdialog
        //https://stackoverflow.com/questions/54496606/swing-worker-cant-update-gui-components-in-modal-jdialog
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setLocationRelativeTo(null);

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
        add(contentPanel);

        contentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setPreferredSize(new Dimension(300, 20));
        contentPanel.add(progressBar);

        contentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        contentPanel.add(progressLabel);

        contentPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));

        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
    }
}

