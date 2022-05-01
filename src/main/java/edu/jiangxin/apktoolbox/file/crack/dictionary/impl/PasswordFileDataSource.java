package edu.jiangxin.apktoolbox.file.crack.dictionary.impl;

import java.io.*;
import java.util.stream.Stream;

import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordDataSource;

public class PasswordFileDataSource implements PasswordDataSource {

	BufferedReader br = null;

	public PasswordFileDataSource(File file) throws FileNotFoundException {
		br = new BufferedReader(new FileReader(file));
	}

	@Override
	public Stream<String> getPasswords() {
		if (br == null) {
			throw new IllegalArgumentException("No file reader to read.");
		}
		Stream<String> words = br.lines();
		return words;
	}

	@Override
	public void close() {
		if (br != null) {
			try {
				br.close();
				br = null;
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}

}
