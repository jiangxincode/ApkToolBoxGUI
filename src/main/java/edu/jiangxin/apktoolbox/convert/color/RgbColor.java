package edu.jiangxin.apktoolbox.convert.color;

public class RgbColor extends CommonColor {
	public RgbColor(String input) {
		super(input);
	}

	String parsedValue, r, g, b;

	@Override
	String convert() {
		String hexValue = "";
		if(!value.equals("")) {
			//parse the input value so that it becomes a well-formed RGB
			parse();
			//after parsing, convert the RGB to hexadecimal
			hexValue = String.format("0x%02X%02X%02X", Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b));
		}
		return hexValue;
	}

	@Override
	void parse() {
		parsedValue = value.trim();//trim leading and trailing whitespace
		parsedValue = parsedValue.replaceAll("\\s+", "");//ignore all other white-spaces
		parsedValue = parsedValue.replaceAll("[^\\d,]", "");//ignore all but number characters and commas

		if(parsedValue.length() - parsedValue.replace(",", "").length() == 2) {//if 2 commas
			r = parsedValue.split(",")[0];
			r = r.length() == 0 ? "0" : r;//treat null space as a 0 by default
			r = r.length() > 3 ? r.substring(0, 2) : r;//only take first 3 digits

			g = parsedValue.split(",")[1];
			g = g.length() == 0 ? "0" : g;
			g = g.length() > 3 ? g.substring(0, 2) : g;

			b = parsedValue.split(",")[2];
			b = b.length() == 0 ? "0" : b;
			b = b.length() > 3 ? b.substring(0, 2) : b;
		} else {
			r = "255";//if not 3 commas (3 color value parts), default to white
			g = "255";
			b = "255";
		}
	}

	@Override
	int getRed() {
		return Integer.parseInt(r);
	}

	@Override
	int getGreen() {
		return Integer.parseInt(g);
	}

	@Override
	int getBlue() {
		return Integer.parseInt(b);
	}

}
