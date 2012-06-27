package ua.felyne.game.shared.agwtapi.graphics.LWJGL;

import com.google.gwt.dom.client.ImageElement;


import org.lwjgl.BufferUtils;

import ua.felyne.game.shared.agwtapi.graphics.IImageLoader;
import ua.felyne.game.shared.agwtapi.graphics.IImageResource;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.ImageIcon;

public class ImageLoader implements IImageLoader { 
	
	//Lista de texturas
	private static HashMap<String, IImageResource> images = new HashMap<String, IImageResource>();
	private static HashMap<String, BufferedImage> buffImg = new HashMap<String, BufferedImage>();
	//ID LIST
	private static IntBuffer textureID = BufferUtils.createIntBuffer(1);
	private ColorModel alphaColorModel;
	private static ImageLoader instance = null;
	
	private ImageLoader()
	{
		alphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
				new int[] { 8,8,8,8}, 
				true, 
				false, 
				ComponentColorModel.TRANSLUCENT,
				DataBuffer.TYPE_BYTE);		
	}
	
	public static ImageLoader getInstance() {
		if(instance == null) {
			instance = new ImageLoader();
			return instance;
		}
		return instance;
	}
	
	@Override
	public IImageResource getImage(String image) {
		return images.get(image);
	}
	
	@Override
	public int getImageWidth(String name) {
		return images.get(name).getWidth();
	}
	
	@Override
	public int getImageHeight(String name) {
		return images.get(name).getHeight();
	}
	
	@Override
	public void loadImage(String resourceName)
	{
		try 
		{
			IImageResource tex = images.get(resourceName);
			
			if(tex != null)
			{
				throw new IOException();
			}
			
			tex = getTexture(resourceName, GL_TEXTURE_2D, GL_RGBA, GL_LINEAR, GL_LINEAR);
			
			images.put(resourceName, tex);
			
			
		} catch(IOException e) {
			
		}
		
	}
	
	public void loadPartialImage(String resourceName, int x, int y, int h, int w) {
		
		try 
		{
			String name = resourceName+"_"+x+"_"+y+"_"+h+"_"+w;
			IImageResource tex = images.get(name);
			
			if(tex != null)
			{
				throw new IOException();
			}
			
			tex = getTexturePartial(resourceName, GL_TEXTURE_2D, GL_RGBA, GL_LINEAR, GL_LINEAR, x, y, h, w);
			
			images.put(name, tex);
			
			
		} catch(IOException e) {
			
		}
	}

	public ImageResource getTexturePartial(String resourceName, int target, int pixelformat, int minFilter, int magFilter, 
			int x, int y, int h, int w) throws IOException
	{
		int src;
		int tID = newTextureID();
		ImageResource tex = new ImageResource(target, tID, resourceName);
		glBindTexture(target, tID);
		
		
		BufferedImage buffImage = buffImg.get(resourceName);
		buffImage = buffImage.getSubimage(x, y, w, h);
		tex.setWidth(buffImage.getWidth());
		tex.setHeight(buffImage.getHeight());
		
		src = GL_RGBA;
		
		ByteBuffer buffer = convertIData(buffImage, tex);
		if(target == GL_TEXTURE_2D)
		{
			glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
			glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
		}
		
		glTexImage2D(target, 0, 
				pixelformat, 
				get2Fold(buffImage.getWidth()), 
				get2Fold(buffImage.getHeight()), 
				0, 
				src, 
				GL_UNSIGNED_BYTE, 
				buffer);
		return tex;
		
	}
	
	private int newTextureID()
	{
		glGenTextures(textureID);
		return textureID.get(0);
	}
	
	private ImageResource getTexture(String resourceName, int target, int pixelformat, int minFilter, int magFilter) throws IOException
	{
		int src;
		int tID = newTextureID();
		
		ImageResource tex = new ImageResource(target, tID, resourceName);
		glBindTexture(target, tID);		
		
		BufferedImage buffImage = readImage(resourceName);
		buffImg.put(resourceName, buffImage);
		tex.setWidth(buffImage.getWidth());
		tex.setHeight(buffImage.getHeight());
		
		src = GL_RGBA;
		
		ByteBuffer buffer = convertIData(buffImage, tex);
		if(target == GL_TEXTURE_2D)
		{
			glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
			glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
		}
		
		glTexImage2D(target, 0, 
				pixelformat, 
				get2Fold(buffImage.getWidth()), 
				get2Fold(buffImage.getHeight()), 
				0, 
				src, 
				GL_UNSIGNED_BYTE, 
				buffer);
		return tex;
		
	}
	
	private ByteBuffer convertIData(BufferedImage buffImage, ImageResource tex)
	{
		ByteBuffer buffer;
		WritableRaster raster;
		BufferedImage tximage;
		
		tex.setTextureWidth(2);
		tex.setTextureHeight(2);
		
		
		while(tex.getTexWidth() < buffImage.getWidth()) tex.setTextureWidth(tex.getTexWidth() * 2);
		while(tex.getTexHeight() < buffImage.getHeight()) tex.setTextureHeight(tex.getTexHeight() * 2);

		tex.setTextureHeight(tex.getTexHeight());
		tex.setTextureWidth(tex.getTexWidth());

		raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, 
				tex.getTexWidth(), tex.getTexHeight(), 4, null);
		tximage = new BufferedImage(alphaColorModel, raster, false, new Hashtable());

		
		Graphics g = tximage.getGraphics();
		g.setColor(new Color(0f,0f,0f,0f));
		g.fillRect(0, 0, tex.getTexWidth(), tex.getTexHeight());
		g.drawImage(buffImage,0,0,null);
		
		byte[] data = ((DataBufferByte) tximage.getRaster().getDataBuffer()).getData();
		
		buffer = ByteBuffer.allocateDirect(data.length);
		buffer.order(ByteOrder.nativeOrder());
		buffer.put(data, 0, data.length);
		buffer.flip();
		
		
		return buffer;
	}
	
	private static int get2Fold(int fold) {
		int ret = 2;
		while (ret < fold) {
			ret *= 2;
		}
		return ret;
	}
	
	private BufferedImage readImage(String ref) throws IOException
	{
		URL url = ImageLoader.class.getClassLoader().getResource(ref);
		if(url == null)
		{
			throw new IOException("No se encuentra: " + ref);
		}
		
		Image img = new ImageIcon(url).getImage();
		
		BufferedImage buffImage;
		buffImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = buffImage.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();

		return buffImage;
		
	}
	
	public boolean hasAlpha(Image image) {

	    if (image instanceof BufferedImage) {
	        BufferedImage bimage = (BufferedImage)image;
	        return bimage.getColorModel().hasAlpha();
	    }
	    return false;
	}
		
}
