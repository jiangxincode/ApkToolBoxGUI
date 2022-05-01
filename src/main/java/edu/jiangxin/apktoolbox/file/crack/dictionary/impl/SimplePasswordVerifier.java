package edu.jiangxin.apktoolbox.file.crack.dictionary.impl;

import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordVerifier;
import edu.jiangxin.apktoolbox.file.crack.dictionary.TargetApplicationProxy;

public class SimplePasswordVerifier implements  PasswordVerifier{
	
	private TargetApplicationProxy targetApplicationProxy;
	private String userName;
	
	public SimplePasswordVerifier(TargetApplicationProxy targetApplicationProxy, String userName) {
		this.targetApplicationProxy = targetApplicationProxy;
		this.userName = userName;
		
	}

	@Override
	public boolean isValidPassword(String password) {
		return targetApplicationProxy.login(userName, password);
	}

}
