package edu.jiangxin.apktoolbox.help;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author jiangxin
 * @author 2019-03-31
 *
 */
public class FeedbackActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        URI uri;
        try {
            uri = new URI("https://github.com/jiangxincode/ApkToolBoxGUI/issues/new");
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
