package ua.felyne.game.shared.agwtapi;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Image;

import ua.felyne.game.shared.agwtapi.audio.GWT.SoundController;
import ua.felyne.game.shared.agwtapi.audio.GWT.SoundHTML5;
import ua.felyne.game.shared.agwtapi.communication.GWT.WebSocket;
import ua.felyne.game.shared.agwtapi.graphics.GWT.Canvas;
import ua.felyne.game.shared.agwtapi.graphics.GWT.ImageLoader;
import ua.felyne.game.shared.agwtapi.graphics.GWT.ImageGWT;

public class GWTController implements IController {

	private Canvas canvas;
	private ImageLoader imageLoader;
	private HashMap<Integer, ImageGWT> sprites;
	private int countImages = 0;

	private SoundController soundController;
	private HashMap<Integer, SoundHTML5> sounds;

	private WebSocket ws;

	@Override
	public void init(String name, int width, int height) {
		canvas = new Canvas();
		canvas.init(name, width, height);

		imageLoader = ImageLoader.getInstance();
		soundController = SoundController.getInstance();

		sprites = new HashMap<Integer, ImageGWT>();
		sounds = new HashMap<Integer, SoundHTML5>();
	}

	@Override
	public void addImage(String src) {
		imageLoader.loadImage(src);
		ImageGWT sprite = new ImageGWT(src);
		sprites.put(countImages, sprite);
		countImages++;
	}

	public void addImage(String src, Image img) {
		imageLoader.loadImage(src, img);
		ImageGWT sprite = new ImageGWT(src);
		sprites.put(countImages, sprite);
		countImages++;
	}

	@Override
	public void addSound(String src, String type) {
		if (type.equals("HTML5")) {
			soundController.createSound(SoundController.HTML5, src);
		} else {

		}
	}

	@Override
	public void writeFPS(double fps) {
		canvas.writeFPS(fps);
	}

	// TODO El tema de buffers no se si subirlo de nivel o dejarlo abajo.

	public int addBuffer() {
		return canvas.addBuffer();
	}

	public void drawFromBuffer(int id) {
		canvas.drawFromBuffer(id);
	}

	@Override
	public void draw(Integer where, int image, int dx, int dy, int dw, int dh,
			int sx, int sy, int sw, int sh) {
		ImageGWT sprite = sprites.get(image);
		Object ctx = null;
		if (where == null) {
			ctx = canvas.getContext();
		} else {
			ctx = canvas.getContext2dCanvasBuffer(where);
		}

		sprite.draw(ctx, dx, dy, dw, dh, sx, sy, sw, sh);
	}

	public Canvas getCanvas() {
		return canvas;
	}

}