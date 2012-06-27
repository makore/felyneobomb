package ua.felyne.game.shared;

public class Item extends Sprite {
	private ItemType _type;
	private boolean _show;
	
	public enum ItemType {
		NONE, BOMB, SPEED, FLAME
	}
	
	public Item(int x, int y, ItemType type, int width, int height) {
		super(x, y, width, height);
		this._type = type;
		this._show = false;
	}
	
	public ItemType getType() {
		return _type;
	}
	
	public void setType(ItemType type) {
		_type = type;
	}
	
	public void show() {
		if (_type != ItemType.NONE) {
			_show = true;			
		}
	}

	public void hide() {
		_show = false;
		_type = ItemType.NONE;
	}

	public boolean isVisible() {
		return _show;
	}
	
	@Override
	public String toString() {
		return "Item [_type=" + _type + ", _x=" + _x + ", _y=" + _y + "]";
	}	
}