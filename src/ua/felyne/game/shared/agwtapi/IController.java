package ua.felyne.game.shared.agwtapi;

public interface IController {

	public void init(String name, int width, int height);
	
	public void addImage(String src);
	public void draw(Integer where, int image, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh);
	public void writeFPS(double fps);
	public int addBuffer();	
	public void drawFromBuffer(int id);
	
	public void addSound(String src, String type);
	
}
