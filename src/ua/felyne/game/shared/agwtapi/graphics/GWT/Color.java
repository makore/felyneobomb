package ua.felyne.game.shared.agwtapi.graphics.GWT;

import ua.felyne.game.shared.agwtapi.graphics.IColor;

public class Color implements IColor{

	private String id;
	private int red, green, blue;
	private double alpha;
	
	public Color(int red, int green, int blue, double alpha) {
		super();
		this.id = "rgb("+red+","+green+","+blue+")";
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Color) ? equals ((Color) obj) : false;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public int getHexValue(int r, int g, int b) {
		return ((r << 16) & 0xFF0000) | ((g << 8) & 0xFF00) | (b & 0xFF);
	}

}

