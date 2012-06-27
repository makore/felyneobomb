package ua.felyne.game.shared;

import ua.felyne.game.shared.agwtapi.IController;

public abstract class Sprite {
	protected int _x, _y, _width, _height;

	public Sprite(int x, int y, int w, int h) {
		this._x = x;
		this._y = y;
		this._width = w;
		this._height = h;
	}

	public int getX() {
		return _x;
	}

	public void setX(int x) {
		this._x = x;
	}

	public int getY() {
		return _y;
	}

	public void setY(int y) {
		this._y = y;
	}

	public int getWidth() {
		return _width;
	}

	public void setWidth(int width) {
		this._width = width;
	}

	public int getHeight() {
		return _height;
	}

	public void setHeight(int height) {
		this._height = height;
	}
	
	public void draw(IController controller, Integer buffer, int image, int sx, int sy, int sw, int sh) {
		controller.draw(buffer, image, _x, _y, _width, _height, sx, sy, sw, sh);
	}
}