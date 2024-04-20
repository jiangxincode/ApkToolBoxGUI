package edu.jiangxin.apktoolbox.file.password.recovery.category;

import edu.jiangxin.apktoolbox.file.password.recovery.category.bruteforce.BruteForceProxy;
import edu.jiangxin.apktoolbox.file.password.recovery.category.dictionary.multithread.DictionaryMultiThreadProxy;
import edu.jiangxin.apktoolbox.file.password.recovery.category.dictionary.singlethread.DictionarySingleThreadProxy;

/**
 * CategoryFactory is a factory class to create different category instance.
 */
public class CategoryFactory {
    private CategoryFactory() {
    }

    public static ICategory getCategoryInstance(CategoryType categoryType) {
        return switch (categoryType) {
            case DICTIONARY_SINGLE_THREAD -> DictionarySingleThreadProxy.getInstance();
            case DICTIONARY_MULTI_THREAD -> DictionaryMultiThreadProxy.getInstance();
            default -> BruteForceProxy.getInstance();
        };
    }
}
