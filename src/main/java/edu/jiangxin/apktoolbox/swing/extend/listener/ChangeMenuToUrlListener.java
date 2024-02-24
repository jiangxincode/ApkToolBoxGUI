package edu.jiangxin.apktoolbox.swing.extend.listener;

import java.awt.Desktop;
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
public class ChangeMenuToUrlListener extends ChangeMenuListener {

    private static final Logger logger = LogManager.getLogger(ChangeMenuToUrlListener.class.getSimpleName());

    private String url;

    public ChangeMenuToUrlListener(String url) {
        super();
        this.url = url;
    }

    @Override
    public void onPreChangeMenu(IFinishCallBack callBack) {
        callBack.onFinish();
    }

    @Override
    public void onChangeMenu() {
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
