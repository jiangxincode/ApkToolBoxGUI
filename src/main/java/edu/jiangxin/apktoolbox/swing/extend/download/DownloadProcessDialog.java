package edu.jiangxin.apktoolbox.swing.extend.download;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import java.io.Serial;

public class DownloadProcessDialog extends JDialog {

    @Serial
    private static final long serialVersionUID = 1L;
    public JProgressBar progressBar = new JProgressBar();
    public JLabel progressLabel = new JLabel("progress");

    public DownloadProcessDialog() {
        setTitle("Download");
        setBounds(100, 100, 447, 137);
        getContentPane().setLayout(null);

        progressBar.setBounds(37, 32, 259, 21);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        getContentPane().add(progressBar);

        progressLabel.setBounds(324, 32, 72, 18);
        getContentPane().add(progressLabel);
    }
}

