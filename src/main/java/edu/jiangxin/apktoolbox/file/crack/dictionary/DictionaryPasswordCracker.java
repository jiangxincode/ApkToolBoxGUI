package edu.jiangxin.apktoolbox.file.crack.dictionary;

/**
 * Driver interface to crack password using dictionary.
 * @author cprak
 *
 */
public interface DictionaryPasswordCracker {
	
	void crackPassword(PasswordDataSource ds, PasswordCombinationGenerator generator, PasswordVerifier verifier, PasswordConsumer passwordConsumer) throws Exception;

}
