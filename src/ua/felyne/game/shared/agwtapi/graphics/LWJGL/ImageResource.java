package ua.felyne.game.shared.agwtapi.graphics.LWJGL;

import static org.lwjgl.opengl.GL11.*;

import ua.felyne.game.shared.agwtapi.graphics.IImageResource;

public class ImageResource implements IImageResource{
	
	private int target;
	private int textureID;
	private int height;
	private int width;
	private int texWidth;
	private int texHeight;
	private float heightRatio;
	
	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getTextureID() {
		return textureID;
	}

	public void setTextureID(int textureID) {
		this.textureID = textureID;
	}

	public int getTexWidth() {
		return texWidth;
	}

	public void setTexWidth(int texWidth) {
		this.texWidth = texWidth;
	}

	public int getTexHeight() {
		return texHeight;
	}

	public void setTexHeight(int texHeight) {
		this.texHeight = texHeight;
	}

	public float getHeightRatio() {
		return heightRatio;
	}

	public void setHeightRatio(float heightRatio) {
		this.heightRatio = heightRatio;
	}

	public float getWidthRatio() {
		return widthRatio;
	}

	public void setWidthRatio(float widthRatio) {
		this.widthRatio = widthRatio;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private float widthRatio;
	private String name;
	
	public ImageResource(int target, int id, String name)
	{
		this.target=target;
		this.textureID=id;
		this.name=name;
	}
	
	public void bind()
	{
		glBindTexture(target, textureID);
	}
	
	public void setTextureHeight(int texHeight) {
		this.texHeight = texHeight;
		setHeight();
	}
	
	public void setTextureWidth(int texWidth) {
		this.texWidth = texWidth;
		setWidth();		
	}
	
	public void setWidth(int width) {
		this.width = width;
		setWidth();
	}
	
	public void setHeight(int height) {
		this.height = height;
		setHeight();
	}
	
	private void setHeight()
	{
		if(texHeight != 0)
		{
			heightRatio = ((float) height) / texHeight;
		}
	}

	private void setWidth()
	{
		if(texHeight != 0)
		{
			widthRatio = ((float) width) / texWidth;
		}
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}
}
