package ua.felyne.game.shared.agwtapi.graphics;

public interface ICanvas {

	public void init(String name, int width, int height);
	
	public void writeFPS(double fps);

	public int addBuffer();
}
