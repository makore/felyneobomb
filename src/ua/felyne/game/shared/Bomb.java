package ua.felyne.game.shared;

import ua.felyne.game.shared.Block.BlockType;
import ua.felyne.game.shared.agwtapi.IController;

public class Bomb extends Sprite {
	private static final int BOMB_TIME = 3000;
	private static final int BOMB_FRAME_TIME = 100;
	private static final int BOMB_FRAMES = 3;
	private long _frameTimeBomb;
	private long _dropTimeStamp;
	private long _lastTimeStamp;
	private boolean _exploded;
	private int _currentBombFrame;
	private int _currentBombFrameOffset;
	private long _thrownBy;
	private static final int EXPLOSION_TIME = 500;
	private static final int EXPLOSION_FRAMES = 5;
	private int _explosionLength;
	private long _frameTimeFlame;
	private long _explosionTime;
	private int _currentFlameFrame;
	private boolean _setedBombs;
	private boolean _animationFlameEnded;
	private boolean[] _blockFoundDuringExplosion;

	public Bomb(int x, int y, int size, int explosionLength, long bomberId) {
		super(x, y, size, size);
		_explosionLength = explosionLength;
		_frameTimeBomb = 0;
		_frameTimeFlame = 0;
		_explosionTime = 0;
		//_dropTimeStamp = System.currentTimeMillis();
		_dropTimeStamp = 0;
		_lastTimeStamp = System.currentTimeMillis();
		_exploded = false;
		_currentBombFrame = 0;
		_currentBombFrameOffset = 0;
		_currentFlameFrame = 0;
		_setedBombs = false;
		_animationFlameEnded = false;
		_thrownBy = bomberId;
		_blockFoundDuringExplosion = new boolean[4];
	}

	public long getLastTimeStamp() {
		return _lastTimeStamp;
	}
	
	public void setLastTimeStamp(long timestamp) {
		_lastTimeStamp = timestamp;
	}
	
	public boolean isAnimationFlameEnded() {
		return _animationFlameEnded;
	}

	public long thrownBy() {
		return _thrownBy;
	}
	
	public long getFrameTimeBomb() {
		return _frameTimeBomb;
	}

	public void setFrameTimeBomb(long frameTimeBomb) {
		this._frameTimeBomb = frameTimeBomb;
	}

	public long getDropTimeStamp() {
		return _dropTimeStamp;
	}

	public void setDropTimeStamp(long dropTimeStamp) {
		this._dropTimeStamp = dropTimeStamp;
	}

	public boolean isExploded() {
		return _exploded;
	}

	public void setExploded(boolean exploded) {
		this._exploded = exploded;
	}

	public int getCurrentBombFrame() {
		return _currentBombFrame;
	}

	public void setCurrentBombFrame(int currentBombFrame) {
		this._currentBombFrame = currentBombFrame;
	}

	public int getCurrentBombFrameOffset() {
		return _currentBombFrameOffset;
	}

	public void setCurrentBombFrameOffset(int currentBombFrameOffset) {
		this._currentBombFrameOffset = currentBombFrameOffset;
	}

	public void setThrownBy(long thrownBy) {
		this._thrownBy = thrownBy;
	}

	public int getExplosionLength() {
		return _explosionLength;
	}

	public void setExplosionLength(int explosionLength) {
		this._explosionLength = explosionLength;
	}

	public long getFrameTimeFlame() {
		return _frameTimeFlame;
	}

	public void setFrameTimeFlame(long frameTimeFlame) {
		this._frameTimeFlame = frameTimeFlame;
	}

	public long getExplosionTime() {
		return _explosionTime;
	}

	public void setExplosionTime(long explosionTime) {
		this._explosionTime = explosionTime;
	}

	public int getCurrentFlameFrame() {
		return _currentFlameFrame;
	}

	public void setCurrentFlameFrame(int currentFlameFrame) {
		this._currentFlameFrame = currentFlameFrame;
	}

	public boolean isSetedBombs() {
		return _setedBombs;
	}

	public void setSetedBombs(boolean setedBombs) {
		this._setedBombs = setedBombs;
	}

	public void setAnimationFlameEnded(boolean animationFlameEnded) {
		this._animationFlameEnded = animationFlameEnded;
	}

	public boolean mustExplode() {
		long tm = System.currentTimeMillis();
		long now = tm - _lastTimeStamp;
		_lastTimeStamp = tm;
		_dropTimeStamp += now;
		//return _exploded || (_exploded = (now - _dropTimeStamp >= BOMB_TIME));
		return _exploded || (_exploded = (_dropTimeStamp  >= BOMB_TIME));
	}

	public boolean explosionDrawn() {
		//long now = System.currentTimeMillis();
		//return (now - _dropTimeStamp) > (BOMB_TIME + EXPLOSION_TIME);
		return _animationFlameEnded;
	}

	public void doBomb(int elapsedTime) {
		_currentBombFrameOffset = getBombNextFrameOffset(elapsedTime);
	}

	private int getBombNextFrameOffset(int elapsedTime) {
		if (_frameTimeBomb < BOMB_FRAME_TIME) {
			_frameTimeBomb += elapsedTime;
		} else {
			// pasamos al siguiente frame
			_currentBombFrame++;
			_currentBombFrame = _currentBombFrame % BOMB_FRAMES;
			_frameTimeBomb = 0;
		}
		return _currentBombFrame * SpriteSheet.Bombs.W;
	}

	public int getFrameOffset() {
		return _currentBombFrameOffset;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bomb other = (Bomb) obj;
		if (_dropTimeStamp != other._dropTimeStamp)
			return false;
		return true;
	}

	public void doExplosion(int elapsedTime, Level level, Bomber bomber) {
		int[] coordsBomb = level.getMapCell(this);

		if (_explosionTime < (EXPLOSION_TIME / 2)) {
			if (_frameTimeFlame < (EXPLOSION_TIME / (2 * EXPLOSION_FRAMES))) {
				_frameTimeFlame += elapsedTime;
				if (!_setedBombs) {
					level.setFlame(coordsBomb[0], coordsBomb[1]);
					for (int i = 0; i <= _explosionLength; i++) {
						// Derecha
						Block block = level.getBlock(coordsBomb[0] + i, coordsBomb[1]);
						if (block != null && (block.getType() == BlockType.TERRAIN || 
								block.getType() == BlockType.SOFT_BLOCK)
								&& level.thereIsAFlame(coordsBomb[0] + i - 1, coordsBomb[1])
								&& !_blockFoundDuringExplosion[0]) {
							if (block.getType() == BlockType.TERRAIN) {
								level.setFlame(coordsBomb[0] + i, coordsBomb[1]);								
							}
							if (block.getType() == BlockType.SOFT_BLOCK) {
								_blockFoundDuringExplosion[0] = true;
							}
							block.setType(BlockType.TERRAIN);							
							level.setRedraw(true);									
							level.getItem(coordsBomb[0] + i, coordsBomb[1]).show();
						}
						// Izquierda
						block = level.getBlock(coordsBomb[0] - i, coordsBomb[1]);
						if (block != null && (block.getType() == BlockType.TERRAIN || 
								block.getType() == BlockType.SOFT_BLOCK)
								&& level.thereIsAFlame(coordsBomb[0] - i + 1, coordsBomb[1])
								&& !_blockFoundDuringExplosion[1]) {
							if (block.getType() == BlockType.TERRAIN) {
								level.setFlame(coordsBomb[0] - i, coordsBomb[1]);
							}
							if (block.getType() == BlockType.SOFT_BLOCK) {
								_blockFoundDuringExplosion[1] = true;
							}
							block.setType(BlockType.TERRAIN);
							level.setRedraw(true);											
							level.getItem(coordsBomb[0] - i, coordsBomb[1]).show();
						}
						// Abajo
						block = level.getBlock(coordsBomb[0], coordsBomb[1] + i);
						if (block != null && (block.getType() == BlockType.TERRAIN || 
								block.getType() == BlockType.SOFT_BLOCK)
								&& level.thereIsAFlame(coordsBomb[0], coordsBomb[1] + i - 1)
								&& !_blockFoundDuringExplosion[2]) {
							if (block.getType() == BlockType.TERRAIN) {
								level.setFlame(coordsBomb[0], coordsBomb[1] + i);
							}
							if (block.getType() == BlockType.SOFT_BLOCK) {
								_blockFoundDuringExplosion[2] = true;
							}
							block.setType(BlockType.TERRAIN);
							level.setRedraw(true);
							level.getItem(coordsBomb[0], coordsBomb[1] + i).show();
						}
						// Arriba
						block = level.getBlock(coordsBomb[0], coordsBomb[1] - i);
						if (block != null && (block.getType() == BlockType.TERRAIN || 
								block.getType() == BlockType.SOFT_BLOCK)
								&& level.thereIsAFlame(coordsBomb[0], coordsBomb[1] - i + 1)
								&& !_blockFoundDuringExplosion[3]) {
							if (block.getType() == BlockType.TERRAIN) {
								level.setFlame(coordsBomb[0], coordsBomb[1] - i);
							}
							if (block.getType() == BlockType.SOFT_BLOCK) {
								_blockFoundDuringExplosion[3] = true;
							}
							block.setType(BlockType.TERRAIN);
							level.setRedraw(true);															
							level.getItem(coordsBomb[0], coordsBomb[1] - i).show();
						}
					}
					_setedBombs = true;
				}
			} else {
				_currentFlameFrame++;
				_frameTimeFlame = 0;
			}
		} else if (_explosionTime >= (EXPLOSION_TIME / 2) && _explosionTime < EXPLOSION_TIME) {	
			
			level.overlapWithExplodedBomb(bomber);
			
			if (_frameTimeFlame < (EXPLOSION_TIME / (2 * EXPLOSION_FRAMES))) {
				_frameTimeFlame += elapsedTime;
			} else {
				_currentFlameFrame--;
				_frameTimeFlame = 0;
			}
		} else if (_explosionTime >= EXPLOSION_TIME) {
			for (int i = 0; i <= _explosionLength; i++) {
				level.unsetFlame(coordsBomb[0] + i, coordsBomb[1]);
				level.unsetFlame(coordsBomb[0] - i, coordsBomb[1]);
				level.unsetFlame(coordsBomb[0], coordsBomb[1] + i);
				level.unsetFlame(coordsBomb[0], coordsBomb[1] - i);
			}
			_animationFlameEnded = true;
		}
		_explosionTime += elapsedTime;
	}

	public void drawFlames(IController controller, int image, Level level) {		
		int[] coordsBomb = level.getMapCell(this);
		int sxCenter = 0, syCenter = 0, sxRightTop = 0, syRightTop = 0, sxLeftTop = 0, 
				syLeftTop = 0, sxUpTop = 0, syUpTop = 0, sxDownTop = 0, syDownTop = 0, 
				sxRightMiddle = 0, syRightMiddle = 0, sxLeftMiddle = 0, syLeftMiddle = 0, 
				sxUpMiddle = 0, syUpMiddle = 0, sxDownMiddle = 0, syDownMiddle = 0;
		switch (_currentFlameFrame) {
			case 0: {
				sxCenter = SpriteSheet.Flames.T5_CENTER_X;
				syCenter = SpriteSheet.Flames.T5_CENTER_Y;
				sxRightTop = SpriteSheet.Flames.T5_RIGHT_TOP_X;
				syRightTop = SpriteSheet.Flames.T5_RIGHT_TOP_Y;
				sxLeftTop = SpriteSheet.Flames.T5_LEFT_TOP_X;
				syLeftTop = SpriteSheet.Flames.T5_LEFT_TOP_Y;
				sxUpTop = SpriteSheet.Flames.T5_UP_TOP_X;
				syUpTop = SpriteSheet.Flames.T5_UP_TOP_Y;
				sxDownTop = SpriteSheet.Flames.T5_DOWN_TOP_X;
				syDownTop = SpriteSheet.Flames.T5_DOWN_TOP_Y;
				sxRightMiddle = SpriteSheet.Flames.T5_RIGHT_MIDDLE_X;
				syRightMiddle = SpriteSheet.Flames.T5_RIGHT_MIDDLE_Y;
				sxLeftMiddle = SpriteSheet.Flames.T5_LEFT_MIDDLE_X;
				syLeftMiddle = SpriteSheet.Flames.T5_LEFT_MIDDLE_Y;
				sxUpMiddle = SpriteSheet.Flames.T5_UP_MIDDLE_X;
				syUpMiddle = SpriteSheet.Flames.T5_UP_MIDDLE_Y;
				sxDownMiddle = SpriteSheet.Flames.T5_DOWN_MIDDLE_X;
				syDownMiddle = SpriteSheet.Flames.T5_DOWN_MIDDLE_Y;
			}
				break;
			case 1: {
				sxCenter = SpriteSheet.Flames.T4_CENTER_X;
				syCenter = SpriteSheet.Flames.T4_CENTER_Y;
				sxRightTop = SpriteSheet.Flames.T4_RIGHT_TOP_X;
				syRightTop = SpriteSheet.Flames.T4_RIGHT_TOP_Y;
				sxLeftTop = SpriteSheet.Flames.T4_LEFT_TOP_X;
				syLeftTop = SpriteSheet.Flames.T4_LEFT_TOP_Y;
				sxUpTop = SpriteSheet.Flames.T4_UP_TOP_X;
				syUpTop = SpriteSheet.Flames.T4_UP_TOP_Y;
				sxDownTop = SpriteSheet.Flames.T4_DOWN_TOP_X;
				syDownTop = SpriteSheet.Flames.T4_DOWN_TOP_Y;
				sxRightMiddle = SpriteSheet.Flames.T4_RIGHT_MIDDLE_X;
				syRightMiddle = SpriteSheet.Flames.T4_RIGHT_MIDDLE_Y;
				sxLeftMiddle = SpriteSheet.Flames.T4_LEFT_MIDDLE_X;
				syLeftMiddle = SpriteSheet.Flames.T4_LEFT_MIDDLE_Y;
				sxUpMiddle = SpriteSheet.Flames.T4_UP_MIDDLE_X;
				syUpMiddle = SpriteSheet.Flames.T4_UP_MIDDLE_Y;
				sxDownMiddle = SpriteSheet.Flames.T4_DOWN_MIDDLE_X;
				syDownMiddle = SpriteSheet.Flames.T4_DOWN_MIDDLE_Y;
			}
				break;
			case 2: {
				sxCenter = SpriteSheet.Flames.T3_CENTER_X;
				syCenter = SpriteSheet.Flames.T3_CENTER_Y;
				sxRightTop = SpriteSheet.Flames.T3_RIGHT_TOP_X;
				syRightTop = SpriteSheet.Flames.T3_RIGHT_TOP_Y;
				sxLeftTop = SpriteSheet.Flames.T3_LEFT_TOP_X;
				syLeftTop = SpriteSheet.Flames.T3_LEFT_TOP_Y;
				sxUpTop = SpriteSheet.Flames.T3_UP_TOP_X;
				syUpTop = SpriteSheet.Flames.T3_UP_TOP_Y;
				sxDownTop = SpriteSheet.Flames.T3_DOWN_TOP_X;
				syDownTop = SpriteSheet.Flames.T3_DOWN_TOP_Y;
				sxRightMiddle = SpriteSheet.Flames.T3_RIGHT_MIDDLE_X;
				syRightMiddle = SpriteSheet.Flames.T3_RIGHT_MIDDLE_Y;
				sxLeftMiddle = SpriteSheet.Flames.T3_LEFT_MIDDLE_X;
				syLeftMiddle = SpriteSheet.Flames.T3_LEFT_MIDDLE_Y;
				sxUpMiddle = SpriteSheet.Flames.T3_UP_MIDDLE_X;
				syUpMiddle = SpriteSheet.Flames.T3_UP_MIDDLE_Y;
				sxDownMiddle = SpriteSheet.Flames.T3_DOWN_MIDDLE_X;
				syDownMiddle = SpriteSheet.Flames.T3_DOWN_MIDDLE_Y;
			}
				break;
			case 3: {
				sxCenter = SpriteSheet.Flames.T2_CENTER_X;
				syCenter = SpriteSheet.Flames.T2_CENTER_Y;
				sxRightTop = SpriteSheet.Flames.T2_RIGHT_TOP_X;
				syRightTop = SpriteSheet.Flames.T2_RIGHT_TOP_Y;
				sxLeftTop = SpriteSheet.Flames.T2_LEFT_TOP_X;
				syLeftTop = SpriteSheet.Flames.T2_LEFT_TOP_Y;
				sxUpTop = SpriteSheet.Flames.T2_UP_TOP_X;
				syUpTop = SpriteSheet.Flames.T2_UP_TOP_Y;
				sxDownTop = SpriteSheet.Flames.T2_DOWN_TOP_X;
				syDownTop = SpriteSheet.Flames.T2_DOWN_TOP_Y;
				sxRightMiddle = SpriteSheet.Flames.T2_RIGHT_MIDDLE_X;
				syRightMiddle = SpriteSheet.Flames.T2_RIGHT_MIDDLE_Y;
				sxLeftMiddle = SpriteSheet.Flames.T2_LEFT_MIDDLE_X;
				syLeftMiddle = SpriteSheet.Flames.T2_LEFT_MIDDLE_Y;
				sxUpMiddle = SpriteSheet.Flames.T2_UP_MIDDLE_X;
				syUpMiddle = SpriteSheet.Flames.T2_UP_MIDDLE_Y;
				sxDownMiddle = SpriteSheet.Flames.T2_DOWN_MIDDLE_X;
				syDownMiddle = SpriteSheet.Flames.T2_DOWN_MIDDLE_Y;
			}
				break;
			case 4: {
				sxCenter = SpriteSheet.Flames.T1_CENTER_X;
				syCenter = SpriteSheet.Flames.T1_CENTER_Y;
				sxRightTop = SpriteSheet.Flames.T1_RIGHT_TOP_X;
				syRightTop = SpriteSheet.Flames.T1_RIGHT_TOP_Y;
				sxLeftTop = SpriteSheet.Flames.T1_LEFT_TOP_X;
				syLeftTop = SpriteSheet.Flames.T1_LEFT_TOP_Y;
				sxUpTop = SpriteSheet.Flames.T1_UP_TOP_X;
				syUpTop = SpriteSheet.Flames.T1_UP_TOP_Y;
				sxDownTop = SpriteSheet.Flames.T1_DOWN_TOP_X;
				syDownTop = SpriteSheet.Flames.T1_DOWN_TOP_Y;
				sxRightMiddle = SpriteSheet.Flames.T1_RIGHT_MIDDLE_X;
				syRightMiddle = SpriteSheet.Flames.T1_RIGHT_MIDDLE_Y;
				sxLeftMiddle = SpriteSheet.Flames.T1_LEFT_MIDDLE_X;
				syLeftMiddle = SpriteSheet.Flames.T1_LEFT_MIDDLE_Y;
				sxUpMiddle = SpriteSheet.Flames.T1_UP_MIDDLE_X;
				syUpMiddle = SpriteSheet.Flames.T1_UP_MIDDLE_Y;
				sxDownMiddle = SpriteSheet.Flames.T1_DOWN_MIDDLE_X;
				syDownMiddle = SpriteSheet.Flames.T1_DOWN_MIDDLE_Y;
			}
				break;
			default:
				return;
		}

		for (int i = 0; i <= _explosionLength; i++) {
			if (i == 0) {
				controller.draw(null, image, _x, _y, _width, _height, sxCenter, syCenter,
						SpriteSheet.Flames.W, SpriteSheet.Flames.H);
			} 
			else if (i == _explosionLength) {
				if (level.thereIsAFlame(coordsBomb[0] + i, coordsBomb[1])) {
					controller.draw(null, image, _x + SpriteSheet.BLOCK_SIZE * i, _y, _width,
							_height, sxRightTop, syRightTop, SpriteSheet.Flames.W,
							SpriteSheet.Flames.H);
				}
				if (level.thereIsAFlame(coordsBomb[0] - i, coordsBomb[1])) {
					controller.draw(null, image, _x - SpriteSheet.BLOCK_SIZE * i, _y, _width,
							_height, sxLeftTop, syLeftTop, SpriteSheet.Flames.W,
							SpriteSheet.Flames.H);
				}
				if (level.thereIsAFlame(coordsBomb[0], coordsBomb[1] + i)) {
					controller.draw(null, image, _x, _y + SpriteSheet.BLOCK_SIZE * i, _width,
							_height, sxDownTop, syDownTop, SpriteSheet.Flames.W,
							SpriteSheet.Flames.H);
				}
				if (level.thereIsAFlame(coordsBomb[0], coordsBomb[1] - i)) {
					controller.draw(null, image, _x, _y - SpriteSheet.BLOCK_SIZE * i, _width,
							_height, sxUpTop, syUpTop, SpriteSheet.Flames.W, SpriteSheet.Flames.H);
				}
			} else {
				if (level.thereIsAFlame(coordsBomb[0] + i, coordsBomb[1])) {
					controller.draw(null, image, _x + SpriteSheet.BLOCK_SIZE * i, _y, _width,
							_height, sxRightMiddle, syRightMiddle, SpriteSheet.Flames.W,
							SpriteSheet.Flames.H);
				}
				if (level.thereIsAFlame(coordsBomb[0] - i, coordsBomb[1])) {
					controller.draw(null, image, _x - SpriteSheet.BLOCK_SIZE * i, _y, _width,
							_height, sxLeftMiddle, syLeftMiddle, SpriteSheet.Flames.W,
							SpriteSheet.Flames.H);
				}
				if (level.thereIsAFlame(coordsBomb[0], coordsBomb[1] + i)) {
					controller.draw(null, image, _x, _y + SpriteSheet.BLOCK_SIZE * i, _width,
							_height, sxDownMiddle, syDownMiddle, SpriteSheet.Flames.W,
							SpriteSheet.Flames.H);
				}
				if (level.thereIsAFlame(coordsBomb[0], coordsBomb[1] - i)) {
					controller.draw(null, image, _x, _y - SpriteSheet.BLOCK_SIZE * i, _width,
							_height, sxUpMiddle, syUpMiddle, SpriteSheet.Flames.W,
							SpriteSheet.Flames.H);
				}
			}
		}
	}

	public void draw(IController controller, int image) {
		controller.draw(null, image, _x, _y, _width, _height, getFrameOffset(), 0,
				SpriteSheet.Bombs.W, SpriteSheet.Bombs.H);
	}
}