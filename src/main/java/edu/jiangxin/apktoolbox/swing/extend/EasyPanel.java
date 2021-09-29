package edu.jiangxin.apktoolbox.swing.extend;

import java.awt.HeadlessException;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.jiangxin.apktoolbox.utils.Utils;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class EasyPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    protected Logger logger;
    protected Configuration conf;
    protected ResourceBundle bundle;

    private EasyFrame rootFrame;
    
    public EasyPanel() throws HeadlessException {
        super();
        logger = LogManager.getLogger(this.getClass().getSimpleName());
        conf = Utils.getConfiguration();
        bundle = ResourceBundle.getBundle("apktoolbox");
        logger.info("Panel start: " + this.getClass().getSimpleName());
    }

    protected EasyFrame getFrame() {
        return rootFrame;
    }

    public void setFrame(EasyFrame frame) {
        rootFrame = frame;
    }
}
