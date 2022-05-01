package edu.jiangxin.apktoolbox.file.crack.dictionary;

/**
 * A consumer for passwords. e.g write to a file.
 * 
 * @author Chandra Prakash
 *
 */
public interface PasswordConsumer {
	void consume(String password);
}
