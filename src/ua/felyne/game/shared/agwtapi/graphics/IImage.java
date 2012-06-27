package ua.felyne.game.shared.agwtapi.graphics;

//import agwtapi.util.GWT.Vector2d;

public interface IImage {
	
	//public Vector2d getSize();	
	public void setSize(double x, double y);
	
	void draw(Object context, int sx, int sy);
	void draw(Object context, int dx, int dy, int dw, int dh, int sx, int sy);
	void draw(Object context, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh);	
	void alphaChannel(Object context, double alpha);
	
}
