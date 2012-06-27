package ua.felyne.game.shared.agwtapi.graphics.LWJGL;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import ua.felyne.game.shared.agwtapi.graphics.ICanvas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Canvas implements ICanvas{
	private static int width;
	private static int height;
	private boolean fullscreen;
	private ImageLoader textureloader;
	private HashMap<Integer, ArrayList<ImageJ>> buffers;
	private int countBuffer;


	public Canvas(boolean full) throws IOException {
		fullscreen = false;
		buffers = new HashMap<Integer, ArrayList<ImageJ>>();
		countBuffer = 0;
	}
	
	public void loadTexture(String name) {
		textureloader.loadImage(name);
	}
	  
	private boolean setDisplayMode() {
		  
		try
		{
			DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(width, height, -1, -1, -1, -1, 60, 60);
			  
			org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
					"width=" + width,
					"height=" + height,
					"freq=" + 60,
					"bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()
			});
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Imposible entrar en modo Pantalla completa");
		}
		return false;
	}


	@Override
	public void init(String TITLE, int w, int h) {

		try {
			width = w;
			height = h;
			setDisplayMode();
			Display.setTitle(TITLE);
			Display.setFullscreen(fullscreen);
			Display.create();
			  
			//NO RATON
			  
			glEnable(GL_TEXTURE_2D);
			  
			//2D desactivo todo el 3D
			glDisable(GL_DEPTH_TEST);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			  
			glOrtho(0, width, height, 0, -1, 1);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glViewport(0, 0, width, height);
			  
			textureloader = ImageLoader.getInstance();  
		  
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void writeFPS(double fps) {
		Display.setTitle("(FPS: "+fps+")");
		
	}

	@Override
	public int addBuffer() {
		ArrayList<ImageJ> buffer = new ArrayList<ImageJ>();
		buffers.put(countBuffer, buffer);
		countBuffer++;
		
		return countBuffer-1;
	}


	public void drawInBuffer(int id) {
		// TODO Auto-generated method stub
		
	}


	public ArrayList<ImageJ> drawFromBuffer(int n) {
		 return buffers.get(n);

	}
}
