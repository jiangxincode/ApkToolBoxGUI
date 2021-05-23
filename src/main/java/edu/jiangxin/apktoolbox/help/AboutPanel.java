/**
 * 
 */
package edu.jiangxin.apktoolbox.help;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import edu.jiangxin.apktoolbox.Version;
import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;

/**
 * @author jiangxin
 * @author 2019-04-12
 *
 */
public class AboutPanel extends EasyPanel {

    private static final long serialVersionUID = 1L;

    public AboutPanel() {
        super();
        setBorder(new EmptyBorder(10, 10, 10, 10));
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            inputStream = AboutPanel.class.getResourceAsStream("/about.html");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
        } catch (IOException ex) {
            logger.error("processing file failed", ex);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception ex) {
                logger.error("close bufferedReader failed", ex);
            }

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception ex) {
                logger.error("close inputStream failed", ex);
            }

        }

        JEditorPane editorPane = new JEditorPane("text/html",
                stringBuffer.toString().replace("{VERSION}", Version.VERSION));
        editorPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(editorPane);

        add(scrollPane);
    }

}
