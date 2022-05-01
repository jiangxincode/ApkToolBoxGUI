package edu.jiangxin.apktoolbox.file.crack.dictionary.impl;

import java.io.FileNotFoundException;

import edu.jiangxin.apktoolbox.file.crack.dictionary.DictionaryPasswordCracker;
import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordCombinationGenerator;
import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordConsumer;
import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordDataSource;
import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordVerifier;

public class SimpleDictionaryPasswordCracker implements DictionaryPasswordCracker{

	public void crackPassword(PasswordDataSource ds, PasswordCombinationGenerator generator, PasswordVerifier verifier, PasswordConsumer passwordConsumer) throws FileNotFoundException, Exception {
		ds
		.getPasswords()
		.flatMap(generator::getPasswordCombinations)
		.filter(verifier::isValidPassword)
		.findFirst()
		.ifPresent(passwordConsumer::consume);
		ds.close();
	}
}
