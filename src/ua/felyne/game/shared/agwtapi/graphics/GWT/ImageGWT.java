package ua.felyne.game.shared.agwtapi.graphics.GWT;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;

import ua.felyne.game.shared.agwtapi.graphics.IImage;
import ua.felyne.game.shared.agwtapi.graphics.GWT.ImageLoader;

//import agwtapi.util.GWT.Vector2d;

public class ImageGWT implements IImage{
	
	private String idImage;
	private double x, y;
	
	public ImageGWT(String id)
	{
		this.idImage = id;
		x=1.0;
		y=1.0;
	}
	
	public ImageGWT(String id, double x, double y)
	{
		this.idImage = id;
	}

	@Override
	public void setSize(double x, double y) {
		this.x=x;
		this.y=y;
	}

	@Override
	public void alphaChannel(Object context, double alpha) {
		((Context2d) context).setGlobalAlpha(alpha);		
	}

	@Override
	public void draw(Object context, int sx, int sy) {
		ImageLoader load = ImageLoader.getInstance();
		ImageElement img = ((ImageResource) load.getImage(idImage)).getImage();
		((Context2d) context).drawImage(img, sx, sy, img.getWidth(), img.getHeight(), 0, 0, x*img.getWidth(), y*img.getHeight());
		
	}

	@Override
	public void draw(Object context, int dx, int dy, int dw, int dh, int sx,
			int sy) {
		ImageLoader load = ImageLoader.getInstance();
		ImageElement img = ((ImageResource) load.getImage(idImage)).getImage();
		((Context2d) context).drawImage(img, sx, sy, dw, dh, dx, dy, x*dw, y*dh);
		
	}

	@Override
	public void draw(Object context, int dx, int dy, int dw, int dh, int sx,
			int sy, int sw, int sh) {
		ImageLoader load = ImageLoader.getInstance();
		ImageElement img = ((ImageResource) load.getImage(idImage)).getImage();
		((Context2d) context).drawImage(img, sx, sy, sw, sh, dx, dy, x*dw, x*dh);
	}

}