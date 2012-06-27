package ua.felyne.game.shared.agwtapi.graphics.GWT;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Image;

import ua.felyne.game.shared.agwtapi.graphics.IImageLoader;
import ua.felyne.game.shared.agwtapi.graphics.IImageResource;

import java.io.IOException;
import java.util.HashMap;



public class ImageLoader implements IImageLoader{

	private static HashMap<String, IImageResource> images = new HashMap<String, IImageResource>();
	private static ImageLoader instance = null;
	
	private ImageLoader()
	{	
	}
	
	public static ImageLoader getInstance() {
		if(instance == null) {
			instance = new ImageLoader();
			return instance;
		}
		return instance;
	}
	
	
	@Override
	public void loadImage(String src)
	{
		try {
		
			if(getImage(src) != null) {
				throw new IOException();
			}
			Image img = new Image(src);

			ImageResource imgResource = new ImageResource(((ImageElement)img.getElement().cast()));
			images.put(src, (imgResource));
			
		} catch(IOException e) {
			
		}
	}
	
	public void loadImage(String src, Image img) {
		ImageResource imgResource = new ImageResource(((ImageElement)img.getElement().cast()));
		images.put(src, (imgResource));		
	}
	
	@Override
	public IImageResource getImage(String name) {
		return images.get(name);
	}
	
	@Override
	public int getImageWidth(String name) {
		return images.get(name).getWidth();
	}
	
	@Override
	public int getImageHeight(String name) {
		return images.get(name).getHeight();
	}
}