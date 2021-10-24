package edu.jiangxin.apktoolbox.convert.color;

import java.awt.Color;

public class HexColor extends CommonColor {

	public HexColor(String input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

	String parsedValue;
	Color color;

	@Override
	String convert() {

		String rgbValue = "";

		if(!value.equals("")) {
			//parse the input value so that it becomes a well-formed hexadecimal representation
			parse();
			//after parsing, convert the hex to RGB
			color = Color.decode(parsedValue);
			rgbValue = "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
		}
		return rgbValue;
	}

	@Override
	void parse() {
		value = value.replace("0x", "");//ignore leading 0x, 0X, or #
		value = value.replace("0X", "");
		value = value.replace("#", "");
		value = value.replaceAll("[^0-9a-fA-F]", "");//ignore char entry not within hex bounds
		if(value.length() > 6) {
			value = value.substring(0, 6);//ignore subsequent chars after the first 6
		}
		switch (value.length()) {
			case 0:
			case 1:
			case 2: parsedValue = "0xFFFFFF";
					break;
			case 3: parsedValue = "0x000" + value;
					break;
			case 4:
			case 5: parsedValue = "0x" + value.substring(0, 3);
					break;
			case 6: parsedValue = "0x" + value;
					break;
		}
	}

	@Override
	int getRed() {
		return color.getRed();
	}

	@Override
	int getGreen() {
		return color.getGreen();
	}

	@Override
	int getBlue() {
		return color.getBlue();
	}

}
