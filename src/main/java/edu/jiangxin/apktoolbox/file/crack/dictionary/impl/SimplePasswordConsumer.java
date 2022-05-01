package edu.jiangxin.apktoolbox.file.crack.dictionary.impl;

import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordConsumer;

public class SimplePasswordConsumer implements PasswordConsumer {

	@Override
	public void consume(String password) {
		System.out.println("Found password:" + password);
	}

}
