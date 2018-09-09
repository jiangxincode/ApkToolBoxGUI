package edu.jiangxin.apktoolbox.help;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;

import edu.jiangxin.apktoolbox.swing.extend.JEasyFrame;

public class AboutFrame extends JEasyFrame {

	private static final long serialVersionUID = 1L;

	public AboutFrame() throws HeadlessException {
		super();
		setTitle("About");
		setSize(600, 400);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		BoxLayout boxLayout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
		contentPane.setLayout(boxLayout);
		setContentPane(contentPane);

		InputStream inputStream = AboutFrame.class.getResourceAsStream("/README.md");
		BufferedReader in;
		StringBuffer buffer = new StringBuffer();
		try {
			in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
				buffer.append("\n");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		MutableDataSet options = new MutableDataSet();
		options.setFrom(ParserEmulationProfile.MARKDOWN);
		options.set(Parser.EXTENSIONS, Arrays.asList(new Extension[] { TablesExtension.create() }));
		Parser parser = Parser.builder(options).build();
		HtmlRenderer renderer = HtmlRenderer.builder(options).build();

		Node document = parser.parse(buffer.toString());
		String html = renderer.render(document);

		JEditorPane editorPane = new JEditorPane("text/html", html);

		JScrollPane scrollPane = new JScrollPane(editorPane);

		contentPane.add(scrollPane);
	}

}
