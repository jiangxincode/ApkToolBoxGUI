package edu.jiangxin.apktoolbox.help;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author jiangxin
 * @author 2019-03-31
 *
 */
public class OpenWebsiteListener implements ActionListener {

    private static Logger logger = LogManager.getLogger(OpenWebsiteListener.class);

    private String url;

    public OpenWebsiteListener(String url) {
        super();
        this.url = url;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        URI uri;
        try {
            uri = new URI(url);
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException ex) {
            logger.error("URISyntaxException", ex);
        } catch (IOException ex) {
            logger.error("IOException", ex);
        }
    }

}
