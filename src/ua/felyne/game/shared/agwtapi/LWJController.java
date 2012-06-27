package ua.felyne.game.shared.agwtapi;

import ua.felyne.game.shared.agwtapi.audio.LWJGL.Sound;
import ua.felyne.game.shared.agwtapi.audio.LWJGL.SoundController;
import ua.felyne.game.shared.agwtapi.communication.JavaWS.WebSocket;
import ua.felyne.game.shared.agwtapi.graphics.LWJGL.Canvas;
import ua.felyne.game.shared.agwtapi.graphics.LWJGL.ImageJ;
import ua.felyne.game.shared.agwtapi.graphics.LWJGL.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LWJController implements IController {
	
	private Canvas canvas;
	private ImageLoader imageLoader;
	private HashMap<Integer, ImageJ> sprites;
	private int countImages = 0;
	
	private SoundController soundController;
	private HashMap<Integer, Sound> sounds;
	
	private WebSocket ws;
	
	@Override
	public void init(String name, int width, int height) {
		try {
			canvas = new Canvas(true);
			canvas.init(name, width, height);
			
			imageLoader = ImageLoader.getInstance();
			sprites = new HashMap<Integer, ImageJ>();
			
			soundController = SoundController.getInstance();
			sounds = new HashMap<Integer, Sound>();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void addImage(String src) {
		imageLoader.loadImage(src);
		ImageJ sprite = new ImageJ(src);
		sprites.put(countImages, sprite);
		countImages++;
		
	}

	@Override
	public void draw(Integer where, int image, int dx, int dy, int dw, int dh,
			int sx, int sy, int sw, int sh) {
		ImageJ sprite = sprites.get(image);
		sprite.draw(sprite.getId(), dx, dy, dw, dh, sx, sy, sw, sh);
	}

	@Override
	public void writeFPS(double fps) {
		canvas.writeFPS(fps);
		
	}

	@Override
	public void addSound(String src, String type) {
		// TODO Auto-generated method stub
		
	}

	//No es necesario en LWJGL
	@Override
	public int addBuffer() {
		return canvas.addBuffer();
	}

	//No es necesario en LWJGL
	@Override
	public void drawFromBuffer(int id) {

	}

}










