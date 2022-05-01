package edu.jiangxin.apktoolbox.file.crack.dictionary.mock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.jiangxin.apktoolbox.file.crack.dictionary.TargetApplicationProxy;

public class TargetApplication implements TargetApplicationProxy {

	Map<String, String> userTopasswordMap = new ConcurrentHashMap<>();

	public static TargetApplicationProxy getTargetApplication(String userName, String password) {
		TargetApplication targetApplication = new TargetApplication();
		targetApplication.setPassword(userName, password);
		return targetApplication;
	}

	private void setPassword(String username, String password) {
		userTopasswordMap.put(username, password);
	}

	@Override
	public boolean login(String username, String password) {
		String storedPassword = userTopasswordMap.get(username);
		if (storedPassword != null && storedPassword.equals(password)) {
			return true;
		}
		return false;
	}
}
