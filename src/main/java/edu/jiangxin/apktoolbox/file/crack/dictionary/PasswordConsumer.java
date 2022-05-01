package edu.jiangxin.apktoolbox.file.crack.dictionary;

/**
 * A consumer for passwords. e.g write to a file.
 * 
 */
public interface PasswordConsumer {
	void consume(String password);
}
