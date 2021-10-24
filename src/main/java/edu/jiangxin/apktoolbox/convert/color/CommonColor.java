package edu.jiangxin.apktoolbox.convert.color;

public abstract class CommonColor {

protected String value;
	
	public CommonColor(String input) {
		value = input;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String newValue) {
		value = newValue;
	}
	
	abstract String convert();
	abstract void parse();
	abstract int getRed();
	abstract int getGreen();
	abstract int getBlue();
}
