package edu.jiangxin.apktoolbox.file.password.recovery.category.dictionary.singlethread;

import edu.jiangxin.apktoolbox.file.password.recovery.Synchronizer;
import edu.jiangxin.apktoolbox.file.password.recovery.category.ICategory;
import edu.jiangxin.apktoolbox.file.password.recovery.checker.IChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public String startAndGet(File file, IChecker checker, String charsSet, Synchronizer synchronizer) {
        isCancelled = false;
        Predicate<String> isRecoveringPredicate = password -> (!isCancelled);
        Function<String, Stream<String>> generator = password -> {
            synchronizer.setCurrentPassword(password);
            synchronizer.increaseProgressBarValue();
            return Stream.of(password.toLowerCase(), password.toUpperCase());
        };
        Predicate<String> verifier = checker::checkPassword;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsSet))) {
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
    public void cancel() {
        isCancelled = true;
    }
}
