package edu.jiangxin.apktoolbox.file.crack.dictionary.impl;

import edu.jiangxin.apktoolbox.file.crack.dictionary.DictionaryPasswordCracker;
import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordCombinationGenerator;
import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordConsumer;
import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordDataSource;
import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordVerifier;
import edu.jiangxin.apktoolbox.file.crack.dictionary.TargetApplicationProxy;
import edu.jiangxin.apktoolbox.file.crack.dictionary.mock.TargetApplication;

public class DictionaryAttack {

	public static void main(String[] args) throws Exception {
		String userName = "Test User";
		String password = "ZYBALA";
		TargetApplicationProxy targetApplicationProxy = getTargetApplication(userName, password);		
		PasswordVerifier verifier = new SimplePasswordVerifier(targetApplicationProxy, userName);
		
		PasswordCombinationGenerator generator = new SimplePasswordCombinationGenerator();
		PasswordConsumer passwordConsumer = new SimplePasswordConsumer();
		DictionaryPasswordCracker  dictionaryPasswordCracker = new SimpleDictionaryPasswordCracker();
		PasswordDataSource ds = new PasswordFileDataSource("words.txt");

		dictionaryPasswordCracker.crackPassword(ds, generator, verifier, passwordConsumer);
	}


	private static TargetApplicationProxy getTargetApplication(String userName, String password) {
		TargetApplicationProxy targetApplicationProxy = TargetApplication.getTargetApplication(userName, password);
		return targetApplicationProxy;
	}

}
