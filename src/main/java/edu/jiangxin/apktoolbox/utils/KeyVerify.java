package edu.jiangxin.apktoolbox.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class KeyVerify {

	public static String verify(String keyStorePath, char[] password, String alias, char[] aliasPassword) {
		KeyStore keyStore;
		try {
			keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (KeyStoreException e) {
			return e.getMessage();
		}
		FileInputStream fis;
		try {
			fis = new FileInputStream(keyStorePath);
		} catch (FileNotFoundException e) {
			return e.getMessage();
		}
		try {
			keyStore.load(fis, password);
		} catch (NoSuchAlgorithmException e) {
			return e.getMessage();
		} catch (CertificateException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		}
		Key key;
		try {
			key = keyStore.getKey(alias, aliasPassword);
		} catch (UnrecoverableKeyException e) {
			return e.getMessage();
		} catch (KeyStoreException e) {
			return e.getMessage();
		} catch (NoSuchAlgorithmException e) {
			return e.getMessage();
		}
		if (key == null) {
			return "alias does not exist";
		}
		return "OK";
	}
}
