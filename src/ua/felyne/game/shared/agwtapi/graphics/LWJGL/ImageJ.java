package ua.felyne.game.shared.agwtapi.graphics.LWJGL;

import ua.felyne.game.shared.agwtapi.graphics.IImage;
//import agwtapi.util.GWT.Vector2d;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

public class ImageJ implements IImage{
	
	private String idImage;
	private double alpha, x, y;
	private ImageResource img;
	

	public ImageJ(String idImage) {
		super();
		this.idImage = idImage;
		x=1.0d;
		y=1.0d;
		alpha =1.0d;
	}
	
	public String getId() {
		return idImage;
	}

	@Override
	public void setSize(double x, double y) {
		this.x=x; this.y=y;	
	}

	@Override
	public void draw(Object context, int sx, int sy) {
		ImageResource texture = (ImageResource) context;
		
		glPushMatrix();
		texture.bind();
		glTranslatef(sx, sy, 0);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor4d(1.0d, 1.0d, 1.0d, alpha);
		
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			
			glTexCoord2f(0, texture.getHeightRatio());
			glVertex2f(0, texture.getHeight());
			
			glTexCoord2f(texture.getWidthRatio(), texture.getHeightRatio());
			glVertex2f(texture.getWidth(), texture.getHeight());
			
			glTexCoord2f(texture.getWidthRatio(), 0);
			glVertex2f(texture.getWidth(), 0);
			
		}
		glEnd();

		glDisable(GL_BLEND);
		glPopMatrix();
		
	}

	public void draw(String name, int sx, int sy) {
		ImageLoader load = ImageLoader.getInstance();
		draw(load.getImage(name), sx, sy);
	}
	/**
	 * @dx: Posicion inicial de textura
	 * @dy: Posicion inicial de textura
	 * @dw: Ancho de textura a truncar
	 * @dh: Alto de textura a truncar
	 * @sx: Position x
	 * @sy: Position y
	 * 
	 */
	@Override
	public void draw(Object context, int dx, int dy, int dw, int dh, int sx,
			int sy) {
		ImageLoader loader = ImageLoader.getInstance();
		ImageResource tex = (ImageResource) context;
		ImageResource texture = (ImageResource) loader.getImage(tex.getName()+"_"+sx+"_"+sy+"_"+ Math.abs(tex.getHeight() - sy)+"_"+Math.abs(tex.getWidth() - sx));

		glPushMatrix();
		
		texture.bind();
		
		glTranslatef(dx, dy, 0);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor4d(1.0d, 1.0d, 1.0d, alpha);
		
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			
			glTexCoord2f(0, texture.getHeightRatio());
			glVertex2f(0, dh);
			
			glTexCoord2f(texture.getWidthRatio(), texture.getHeightRatio());
			glVertex2f(dw, dh);
			
			glTexCoord2f(texture.getWidthRatio(), 0);
			glVertex2f(dw, 0);
			
		}
		glEnd();

		glDisable(GL_BLEND);
		glPopMatrix();
	}

	public void draw(String name, int dx, int dy, int dw, int dh, int sx,
			int sy) {
		ImageLoader loader = ImageLoader.getInstance();
		 draw(loader.getImage(name), dx, dy, dw, dh, sx, sy);
	}
	/**
	 * @dx: Posicion inicial de textura
	 * @dy: Posicion inicial de textura
	 * @dw: Ancho de textura a truncar
	 * @dh: Alto de textura a truncar
	 * @sx: Position x
	 * @sy: Position y
	 * @sw: Ancho de textura total (Parallax)
	 * @sh: Alto de textura total (Parallax)
	 * 
	 */
	@Override
	public void draw(Object context, int dx, int dy, int dw, int dh, int sx,
			int sy, int sw, int sh) {
		ImageLoader loader = ImageLoader.getInstance();
		ImageResource texture = (ImageResource) loader.getImage(((ImageResource) context).getName());
		
		texture = (ImageResource) loader.getImage(texture.getName()+"_"+sx+"_"+sy+"_"+sh+"_"+sw );
		if(texture == null) {
			loader.loadPartialImage(((ImageResource) context).getName(), sx, sy, sh, sw);
			texture = (ImageResource) loader.getImage(((ImageResource) context).getName()+"_"+sx+"_"+sy+"_"+sh+"_"+sw );
		}
		glPushMatrix();
		
		texture.bind();
		
		glTranslatef(dx, dy, 0);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor4d(1.0d, 1.0d, 1.0d, alpha);
		
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			
			glTexCoord2f(0, texture.getHeightRatio());
			glVertex2f(0, dh);
			
			glTexCoord2f(texture.getWidthRatio(), texture.getHeightRatio());
			glVertex2f(dw, dh);
			
			glTexCoord2f(texture.getWidthRatio(), 0);
			glVertex2f(dw, 0);
			
		}
		glEnd();

		glDisable(GL_BLEND);
		glPopMatrix();
	}
	
	public void draw(String name, int dx, int dy, int dw, int dh, int sx,
			int sy, int sw, int sh) {
		ImageLoader loader = ImageLoader.getInstance();
		 draw(loader.getImage(name), dx, dy, dw, dh, sx, sy, sw, sh);
	}
	
	@Override
	public void alphaChannel(Object context, double alpha) {
		this.alpha = alpha;		
	}

}
