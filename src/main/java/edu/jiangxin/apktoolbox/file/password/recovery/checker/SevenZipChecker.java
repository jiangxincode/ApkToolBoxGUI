package edu.jiangxin.apktoolbox.file.password.recovery.checker;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.utils.MultiReadOnlySeekableByteChannel;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.List;

public final class SevenZipChecker extends FileChecker {
    private static final boolean DEBUG = true;

    private boolean isSplitFile = false;

    private File[] splitFiles;

    public SevenZipChecker() {
        super();
    }

    @Override
    public String[] getFileExtensions() {
        return new String[]{"7z", "001"};
    }

    @Override
    public String getFileDescription() {
        return "*.7z";
    }

    @Override
    public String getDescription() {
        return "7Zip Checker";
    }

    @Override
    public boolean prepareChecker() {
        return true;
    }

    @Override
    public void attachFile(File file) {
        super.attachFile(file);
        isSplitFile = isSplitFile();
        splitFiles = getSplitFiles();
    }

    @Override
    public boolean checkPassword(String password) {
        if (DEBUG) {
            logger.info("checkPassword: {}", password);
        }
        if (isSplitFile) {
            SevenZFile.Builder builder = new SevenZFile.Builder();
            SeekableByteChannel channel = null;
            SevenZFile sevenZFile = null;
            try {
                channel = MultiReadOnlySeekableByteChannel.forFiles(splitFiles);
                builder.setSeekableByteChannel(channel).setPassword(password.toCharArray());
                sevenZFile = builder.get();
                tryToRead(sevenZFile);
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                IOUtils.closeQuietly(channel);
                IOUtils.closeQuietly(sevenZFile);
            }

        } else {
            SevenZFile sevenZFile = null;
            try {
                SevenZFile.Builder builder = new SevenZFile.Builder().setFile(file).setPassword(password.toCharArray());
                builder.setFile(file).setPassword(password.toCharArray());
                sevenZFile = builder.get();
                tryToRead(sevenZFile);
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                IOUtils.closeQuietly(sevenZFile);
            }
        }
    }

    private void tryToRead(SevenZFile sevenZFile) throws IOException {
        SevenZArchiveEntry entry = sevenZFile.getNextEntry();
        while (entry != null) {
            try (BufferedInputStream bis = new BufferedInputStream(sevenZFile.getInputStream(entry))) {
                byte[] buffer = new byte[1024];
                while (bis.read(buffer) != -1) {
                    // do nothing
                }
            }
            entry = sevenZFile.getNextEntry();
        }
    }

    private boolean isSplitFile() {
        String extension = FilenameUtils.getExtension(file.getName());
        logger.info("isSplitFile extension: {}", extension);
        return "001".equals(extension);
    }

    private File[] getSplitFiles() {
        List<File> fileList = new ArrayList<>();
        String baseName = FilenameUtils.getBaseName(file.getName());
        String parent = file.getParent();
        logger.info("getSplitFiles baseName: {}", baseName);
        for (int i = 1; i < 1000; i++) {
            File file = new File(parent, baseName + "." + String.format("%03d", i));
            if (file.exists()) {
                fileList.add(file);
            } else {
                break;
            }
        }
        logger.info("getSplitFiles: {}", fileList);
        return fileList.toArray(new File[0]);
    }
}
