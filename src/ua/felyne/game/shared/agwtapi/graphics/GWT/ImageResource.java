package ua.felyne.game.shared.agwtapi.graphics.GWT;

import com.google.gwt.dom.client.ImageElement;

import ua.felyne.game.shared.agwtapi.graphics.IImageResource;


public class ImageResource implements IImageResource{

	private ImageElement img;
	
	public ImageResource(ImageElement img)
	{
		this.img = img;
	}

	public ImageElement getImage() {
		return img;
	}

	public void setImage(ImageElement img) {
		this.img = img;		
	}

	@Override
	public int getHeight() {		
		return img.getHeight();
	}

	@Override
	public int getWidth() {		
		return img.getWidth();
	}
	
	
}