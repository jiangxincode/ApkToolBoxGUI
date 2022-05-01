package edu.jiangxin.apktoolbox.file.crack.dictionary.impl;

import java.io.FileNotFoundException;

import edu.jiangxin.apktoolbox.file.crack.cracker.ICracker;
import edu.jiangxin.apktoolbox.file.crack.dictionary.DictionaryPasswordCracker;
import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordCombinationGenerator;
import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordConsumer;
import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordDataSource;

public class SimpleDictionaryPasswordCracker implements DictionaryPasswordCracker{

	public void crackPassword(PasswordDataSource ds, PasswordCombinationGenerator generator, ICracker cracker, PasswordConsumer passwordConsumer) throws FileNotFoundException, Exception {
		ds
		.getPasswords()
		.flatMap(generator::getPasswordCombinations)
		.filter(cracker::checkPassword)
		.findFirst()
		.ifPresent(passwordConsumer::consume);
		ds.close();
	}
}
