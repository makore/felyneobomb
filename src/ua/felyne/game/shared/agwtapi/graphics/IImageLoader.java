package ua.felyne.game.shared.agwtapi.graphics;

public interface IImageLoader {
	
	public void loadImage(String resourceName);
	
	public IImageResource getImage(String image);
	
	public int getImageWidth(String name);
	
	public int getImageHeight(String name);
}
