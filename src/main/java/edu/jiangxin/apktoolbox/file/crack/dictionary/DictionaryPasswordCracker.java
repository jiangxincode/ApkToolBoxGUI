package edu.jiangxin.apktoolbox.file.crack.dictionary;

import edu.jiangxin.apktoolbox.file.crack.cracker.ICracker;

/**
 * Driver interface to crack password using dictionary.
 *
 */
public interface DictionaryPasswordCracker {
	
	void crackPassword(PasswordDataSource ds, PasswordCombinationGenerator generator, ICracker cracker, PasswordConsumer passwordConsumer) throws Exception;

}
