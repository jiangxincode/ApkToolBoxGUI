package edu.jiangxin.apktoolbox.file.crack.dictionary.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordVerifier;
import edu.jiangxin.apktoolbox.file.crack.dictionary.TargetApplicationProxy;
import edu.jiangxin.apktoolbox.file.crack.dictionary.impl.SimplePasswordVerifier;
import edu.jiangxin.apktoolbox.file.crack.dictionary.mock.TargetApplication;

public class TestPasswordVerifier {
	
	String userName = "Test User";
	String password = "ZYBALA";
	TargetApplicationProxy targetApplicationProxy = TargetApplication.getTargetApplication(userName, password);
	
	@Test
	public void testValidPassowrd() {
		PasswordVerifier passwordVerifier = new SimplePasswordVerifier(targetApplicationProxy, userName);
		boolean isPasswordValid = passwordVerifier.isValidPassword(password);
		assertTrue(isPasswordValid);
	}
	
	@Test
	public void testInvalidPassword() {
		String invalidPassword = "hello";
		PasswordVerifier passwordVerifier = new SimplePasswordVerifier(targetApplicationProxy, userName);
		boolean isPasswordValid = passwordVerifier.isValidPassword(invalidPassword);
		assertFalse(isPasswordValid);
	}
	
	@Test
	public void testInvalidUser() {
		String invalidUserName = "Invalid User";
		PasswordVerifier passwordVerifier = new SimplePasswordVerifier(targetApplicationProxy, invalidUserName);
		boolean isPasswordValid = passwordVerifier.isValidPassword(password);
		assertFalse(isPasswordValid);
	}	

}
