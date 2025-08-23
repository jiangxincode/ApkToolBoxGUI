package edu.jiangxin.apktoolbox.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Cross-platform helper for revealing a file or folder in the native file-manager.
 */
public final class RevealFileUtils {
    private static final Logger logger = LogManager.getLogger(RevealFileUtils.class.getSimpleName());

    private RevealFileUtils() {}

    /**
     * Opens the system file-manager and highlights the supplied file or folder.
     *
     * @param file the file or folder to reveal
     * @throws IOException if the file does not exist or the platform command fails
     */
    public static void revealFile(File file) {
        if (file == null) {
            logger.error("revealFile failed: file is null");
            return;
        }
        if (!file.exists()) {
            logger.error("revealFile failed: file does not exist: {}", file.getPath());
            return;
        }

        String os   = System.getProperty("os.name").toLowerCase();
        String path = file.getAbsolutePath();

        List<String> cmd;

        //https://stackoverflow.com/questions/32314645/java-processbuilder-command-arguments-with-spaces-and-double-quotes-fails
        //TODO: test with spaces and special characters in path

        if (os.contains("win")) {
            // Pass the whole flag as one token, already quoted.
            cmd = Arrays.asList("explorer.exe", "/select", path);

        } else if (os.contains("mac")) {
            cmd = Arrays.asList("open", "-R", path);   // open handles quoting internally

        } else {
            // Linux – DBus interface wants file:// URI, properly encoded
            String uri = "file://" + file.toURI().getRawPath();
            if (file.isDirectory()) {
                // Fallback: simply open the directory
                cmd = Arrays.asList("xdg-open", path);
            } else {
                cmd = Arrays.asList(
                        "dbus-send",
                        "--session",
                        "--dest=org.freedesktop.FileManager1",
                        "--type=method_call",
                        "/org/freedesktop/FileManager1",
                        "org.freedesktop.FileManager1.ShowItems",
                        "array:string:\"" + uri + "\"",
                        "string:\"\""
                );
            }
        }
        logger.info("revealFile cmd: {}", cmd);

        try {
            new ProcessBuilder(cmd).start();
        } catch (IOException e) {
            logger.error("revealFile failed: {}", e.getMessage());
        }
    }

    public static void revealDirectory(File file) {
        if (file == null) {
            logger.error("revealDirectory failed: file is null");
            return;
        }
        if (!file.exists()) {
            logger.error("revealDirectory failed: file does not exist: {}", file.getPath());
            return;
        }
        if (file.isDirectory()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                logger.error("revealDirectory failed: {}", e.getMessage());
            }
        }
    }
}