package edu.jiangxin.apktoolbox.file.crack.dictionary.impl;

import java.util.stream.Stream;

import edu.jiangxin.apktoolbox.file.crack.dictionary.PasswordCombinationGenerator;

public class SimplePasswordCombinationGenerator implements PasswordCombinationGenerator {

	@Override
	public Stream<String> getPasswordCombinations(String word) {
		return Stream.of(word.toLowerCase(), word.toUpperCase());
	}
	

}
