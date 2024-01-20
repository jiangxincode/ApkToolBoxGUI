package edu.jiangxin.apktoolbox.convert.protobuf.supervised;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/**
 * Desktop application offering the functionality of the {@link ProtoToJson}-API.
 * <p>
 * The user has the option to select a binary protobuf message which the application will then attempt to decode into
 * readable JSON. The result will then be displayed in an advanced text editor window for further processing.
 * <p>
 * Message decoding requires protobuf descriptors that are put into the {@link #CACHE_DIRECTORY} in advance by the user.
 * Such descriptors can be obtained by applying a {@code protoc} command on the protobuf schema {@code .proto}, for
 * example:
 * <pre>{@code
 * protoc --descriptor_set_out foo.desc foo.proto
 * }</pre>
 *
 * @author Daniel Tischner {@literal <zabuza.dev@gmail.com>}
 */
public enum AppMain {
	;
	/**
	 * The directory to use for the descriptor cache.
	 */
	@SuppressWarnings({ "WeakerAccess", "CallToSystemGetenv" })
	public static final Path CACHE_DIRECTORY = Path.of(System.getenv("LOCALAPPDATA"), "ProtoToJson", "descriptorCache");
	/**
	 * Preferred width for the GUI.
	 */
	private static final int WIDTH = 700;
	/**
	 * Preferred height for the GUI.
	 */
	private static final int HEIGHT = 700;

	/**
	 * Starts the desktop application.
	 * <p>
	 * Prior to using this, the user has to populate the {@link #CACHE_DIRECTORY} with protobuf descriptor files. Such
	 * descriptors can be obtained by applying a {@code protoc} command on the protobuf schema {@code .proto}, for
	 * example:
	 * <pre>{@code
	 * protoc --descriptor_set_out foo.desc foo.proto
	 * }</pre>
	 * The user has the option to select a binary protobuf message which the application will then attempt to decode
	 * into readable JSON. The result will then be displayed in an advanced text editor window for further processing.
	 *
	 * @param args Null or empty if the user should be given the option to select a binary protobuf message. Otherwise
	 *             one argument which is the path to the binary protobuf message to convert, the application will then
	 *             directly proceed to decoding this message instead of asking the user for a message first.
	 */
	@SuppressWarnings("OverlyBroadCatchBlock")
	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			// Establish API
			final DescriptorCache cache = AppMain.createCache();
			if (cache.isEmpty()) {
				//noinspection HardcodedLineSeparator
				AppMain.displayShortMessage(
						"The descriptor cache is empty, could not load any descriptor, check the cache directory:\n"
								+ AppMain.CACHE_DIRECTORY.toAbsolutePath(), "Empty descriptor cache",
						MessageType.ERROR);
				return;
			}
			final ProtoToJson protoToJson = ProtoToJson.fromCache(cache);

			// Pick which file to decode
			final Optional<Path> protoFile = AppMain.chooseProtoFile(args);
			if (protoFile.isEmpty()) {
				return;
			}

			// Decode to JSON
			final String protoJson;
			//noinspection NestedTryStatement
			try {
				protoJson = protoToJson.toJson(protoFile.orElseThrow());
			} catch (final NoDescriptorFoundException e) {
				//noinspection HardcodedLineSeparator
				AppMain.displayShortMessage(
						"Unable to find a descriptor matching the given JSON message, check the cache directory:\n"
								+ AppMain.CACHE_DIRECTORY.toAbsolutePath(), "No matching descriptor found",
						MessageType.ERROR);
				return;
			}

			// Display the result
			AppMain.displayJson(protoJson);
		} catch (final Throwable t) {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			AppMain.displayLongMessage(sw.toString(), "Error", MessageType.ERROR);
		}
	}

	/**
	 * Decides which binary protobuf message file to choose for decoding. Either given by {code args} or by asking the
	 * user to choose a file.
	 *
	 * @param args Null or empty if the user should be given the option to select a binary protobuf message. Otherwise
	 *             one argument which is the path to the binary protobuf message to convert, the method will then
	 *             directly choose this file instead of asking the user for a file first.
	 *
	 * @return The selected binary protobuf message or empty if the user cancelled the process.
	 */
	private static Optional<Path> chooseProtoFile(final String[] args) {
		if (args != null && args.length > 0) {
			return Optional.of(Path.of(args[0]));
		}

		@SuppressWarnings("AccessOfSystemProperties") final File workingDirectory =
				new File(System.getProperty("user.dir"));
		final JFileChooser fileChooser = new JFileChooser(workingDirectory);
		fileChooser.setDialogTitle("Choose a protobuf message file");

		if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return Optional.empty();
		}

		return Optional.of(fileChooser.getSelectedFile()
				.toPath());
	}

	private static DescriptorCache createCache() throws IOException {
		Files.createDirectories(AppMain.CACHE_DIRECTORY);
		return DescriptorCache.fromDirectory(AppMain.CACHE_DIRECTORY);
	}

	/**
	 * Displays the given JSON text in an advanced text editor window.
	 *
	 * @param json The JSON to display, not null
	 */
	private static void displayJson(final String json) {
		Objects.requireNonNull(json);

		AppMain.displayLongMessage(json, "Decoded JSON message", MessageType.PLAIN);
	}

	/**
	 * Displays a long text message in an advanced text editor window.
	 *
	 * @param message     The message to display, not null
	 * @param title       The title of the window, not null
	 * @param messageType The type of the message, not null
	 */
	private static void displayLongMessage(final String message, final String title, final MessageType messageType) {
		Objects.requireNonNull(message);
		Objects.requireNonNull(title);
		Objects.requireNonNull(messageType);

		final RSyntaxTextArea textArea = new RSyntaxTextArea(message);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
		textArea.setCodeFoldingEnabled(true);
		textArea.setEditable(false);

		final RTextScrollPane scrollPane = new RTextScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(AppMain.WIDTH, AppMain.HEIGHT));

		//noinspection MagicConstant
		JOptionPane.showMessageDialog(null, scrollPane, title, messageType.getCode());
	}

	/**
	 * Displays a short text message in small popup window.
	 *
	 * @param message     The message to display, not null
	 * @param title       The title of the window, not null
	 * @param messageType The type of the message, not null
	 */
	@SuppressWarnings({ "SameParameterValue", "MagicConstant" })
	private static void displayShortMessage(final String message, final String title, final MessageType messageType) {
		Objects.requireNonNull(message);
		Objects.requireNonNull(title);
		Objects.requireNonNull(messageType);

		JOptionPane.showMessageDialog(null, message, title, messageType.getCode());
	}

	/**
	 * Dialog message types, with codes supported by {@link JOptionPane}.
	 */
	private enum MessageType {
		/**
		 * A plain message, no additional indicators or decoration.
		 */
		PLAIN(JOptionPane.PLAIN_MESSAGE),
		/**
		 * An error message.
		 */
		ERROR(JOptionPane.ERROR_MESSAGE),
		/**
		 * An info message.
		 */
		INFO(JOptionPane.INFORMATION_MESSAGE),
		/**
		 * A warning message.
		 */
		WARNING(JOptionPane.WARNING_MESSAGE),
		/**
		 * A question message.
		 */
		QUESTION(JOptionPane.QUESTION_MESSAGE);

		/**
		 * The corresponding code as supported by {@link JOptionPane}.
		 */
		private final int code;

		/**
		 * Creates a message type.
		 *
		 * @param code The corresponding code as supported by {@link JOptionPane}
		 */
		MessageType(final int code) {
			this.code = code;
		}

		/**
		 * Gets the code of the message type, as supported by {@link JOptionPane}.
		 *
		 * @return The code of the message type
		 */
		@SuppressWarnings("WeakerAccess")
		public int getCode() {
			return code;
		}
	}
}
