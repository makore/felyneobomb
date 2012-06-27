package ua.felyne.game.shared;

public class Block extends Sprite {
	private BlockType _type;

	public enum BlockType {
		TERRAIN, WALL, SOLID_BLOCK, SOFT_BLOCK
	}

	public Block(int x, int y, BlockType type, int width, int height) {
		super(x, y, width, height);
		this._type = type;
	}

	public BlockType getType() {
		return _type;
	}
	
	public void setType(BlockType _type) {
		this._type = _type;
	}	

	@Override
	public String toString() {
		return "Block [_type=" + _type + ", _x=" + _x + ", _y=" + _y + "]";
	}
}