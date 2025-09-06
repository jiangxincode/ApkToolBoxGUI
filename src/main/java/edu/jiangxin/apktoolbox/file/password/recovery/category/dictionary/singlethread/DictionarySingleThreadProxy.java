package edu.jiangxin.apktoolbox.file.password.recovery.category.dictionary.singlethread;

import edu.jiangxin.apktoolbox.file.core.EncoderDetector;
import edu.jiangxin.apktoolbox.file.password.recovery.RecoveryPanel;
import edu.jiangxin.apktoolbox.file.password.recovery.category.ICategory;
import edu.jiangxin.apktoolbox.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DictionarySingleThreadProxy implements ICategory {
    private static final Logger logger = LogManager.getLogger(DictionarySingleThreadProxy.class.getSimpleName());

    private boolean isCancelled;

    private static class DictionarySingleThreadProxyHolder {
        private static final DictionarySingleThreadProxy instance = new DictionarySingleThreadProxy();
    }

    private DictionarySingleThreadProxy() {
    }

    public static DictionarySingleThreadProxy getInstance() {
        return DictionarySingleThreadProxy.DictionarySingleThreadProxyHolder.instance;
    }

    private String startAndGet(String charsetName, RecoveryPanel panel) {
        isCancelled = false;
        Predicate<String> isRecoveringPredicate = password -> !isCancelled;
        Function<String, Stream<String>> generator = password -> {
            panel.setCurrentPassword(password);
            panel.increaseProgressBarValue();
            return Stream.of(password.toLowerCase(), password.toUpperCase());
        };
        Predicate<String> verifier = panel.getCurrentFileChecker()::checkPassword;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(panel.getDictionaryFile()), charsetName))) {
            // java.util.stream.BaseStream.parallel can not increase the speed in test
            Optional<String> password = br.lines().takeWhile(isRecoveringPredicate).flatMap(generator).filter(verifier).findFirst();
            return password.orElse(null);
        } catch (FileNotFoundException e) {
            logger.info("FileNotFoundException");
        } catch (IOException e) {
            logger.info("IOException");
        }
        return null;
    }

    @Override
    public void start(RecoveryPanel panel) {
        File dictionaryFile = panel.getDictionaryFile();
        if (!dictionaryFile.isFile()) {
            JOptionPane.showMessageDialog(panel, "Invalid dictionary file");
            return;
        }
        String charsetName = EncoderDetector.judgeFile(dictionaryFile.getAbsolutePath());
        logger.info("dictionary file: {}, charset: {}", dictionaryFile.getAbsolutePath(), charsetName);
        if (charsetName == null) {
            JOptionPane.showMessageDialog(panel, "Invalid charsetName");
            return;
        }
        panel.resetProgressMaxValue(Utils.getFileLineCount(dictionaryFile));
        String password = startAndGet(charsetName, panel);
        panel.showResultWithDialog(password);
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }
}
