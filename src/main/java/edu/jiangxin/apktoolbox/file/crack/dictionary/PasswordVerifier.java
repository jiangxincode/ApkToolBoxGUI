package edu.jiangxin.apktoolbox.file.crack.dictionary;

/**
 * Verifies whether a password is valid.

 * @author Chandra Prakash.
 *
 */
public interface PasswordVerifier {
	boolean isValidPassword(String password);
}
