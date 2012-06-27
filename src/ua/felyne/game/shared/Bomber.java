package ua.felyne.game.shared;

import ua.felyne.game.shared.Item.ItemType;
import ua.felyne.game.shared.agwtapi.IController;
import ua.felyne.game.shared.agwtapi.event.IEvents.GameAction;

public class Bomber extends Sprite implements IBomber {
	
	private int _allowedBombs;
	private int _currentBomberFrame;
	private int _prevBomberFrame;
	private int _currentBomberFrameOffset;
	private int _prevBomberFrameOffset;
	private int _speed;
	private float _unusedStep;
	private Level _level;
	private GameAction _actualAction;
	private GameAction _lastAction;
	private GameAction _lastMoveAction;
	private static final int FRAME_TIME = 6;
	private static final int SPEED = 250;
	private long _frameTime;
	private int _explosionLength;
	private boolean _killed;
	private static final int DEAD_TIME = 36;
	private static final int DEAD_FRAMES = 4;
	private long _deadTime;
	private long _frameTimeDead;
	private int _currentDeadFrame;
	private int _playerId; 

	public Bomber(int x, int y, int width, int height, Level level, int playerId) {
		super(x, y, width, height);
		this._currentBomberFrame = 0;
		this._prevBomberFrame = 0;
		this._prevBomberFrameOffset = 0;
		this._level = level;
		this._speed = SpriteSheet.SCREEN_HEIGHT / SPEED;
		this._unusedStep = 0f;
		this._lastAction = GameAction.NOTHING;
		this._lastMoveAction = GameAction.NOTHING;
		this._allowedBombs = 1;
		this._frameTime = 0;
		this._explosionLength = 2;
		this._killed = false;
		this._deadTime = 0;
		this._frameTimeDead = 0;
		this._currentDeadFrame = 0;
		this._actualAction = GameAction.NOTHING;
		this._playerId = playerId;
	}
	
	public void setActualAction(GameAction action) {
		_actualAction = action;
	}

	public GameAction getActualAction() {
		return _actualAction;
	}
	
	public int getPlayerId() {
		return _playerId;
	}

	private int getNextFrameOffset(int elapsedTime, GameAction currentAction) {
		int frameOffset = 0;
		if (_lastAction == currentAction && currentAction != GameAction.NOTHING) {
			// si el bomber continua moviendose
			if (_frameTime < FRAME_TIME) {
				_frameTime += elapsedTime;
			} else {
				// pasamos al siguiente frame
				if (_currentBomberFrame == 1 || _currentBomberFrame == 2) {
					_prevBomberFrame = _currentBomberFrame;
					_currentBomberFrame = 0;
				} else {
					if (_prevBomberFrame == 1) {
						_currentBomberFrame = 2;
					} else if (_prevBomberFrame == 2) {
						_currentBomberFrame = 1;
					}
					_prevBomberFrame = 0;
				}
				_frameTime = 0;
			}

			if (currentAction == GameAction.UP) {
				if (_currentBomberFrame == 0) {
					frameOffset = SpriteSheet.Bomber1.BACKWARDS_QUIET_X;
				} else if (_currentBomberFrame == 1) {
					frameOffset = SpriteSheet.Bomber1.BACKWARDS_WALK1_X;
				} else if (_currentBomberFrame == 2) {
					frameOffset = SpriteSheet.Bomber1.BACKWARDS_WALK2_X;
				}
			} else if (currentAction == GameAction.DOWN) {
				if (_currentBomberFrame == 0) {
					frameOffset = SpriteSheet.Bomber1.FRONT_QUIET_X;
				} else if (_currentBomberFrame == 1) {
					frameOffset = SpriteSheet.Bomber1.FRONT_WALK1_X;
				} else if (_currentBomberFrame == 2) {
					frameOffset = SpriteSheet.Bomber1.FRONT_WALK2_X;
				}
			} else if (currentAction == GameAction.RIGHT) {
				if (_currentBomberFrame == 0) {
					frameOffset = SpriteSheet.Bomber1.SIDED_RIGHT_QUIET_X;
				} else if (_currentBomberFrame == 1) {
					frameOffset = SpriteSheet.Bomber1.SIDED_RIGHT_WALK1_X;
				} else if (_currentBomberFrame == 2) {
					frameOffset = SpriteSheet.Bomber1.SIDED_RIGHT_WALK2_X;
				}
			} else if (currentAction == GameAction.LEFT) {
				if (_currentBomberFrame == 0) {
					frameOffset = SpriteSheet.Bomber1.SIDED_LEFT_QUIET_X;
				} else if (_currentBomberFrame == 1) {
					frameOffset = SpriteSheet.Bomber1.SIDED_LEFT_WALK1_X;
				} else if (_currentBomberFrame == 2) {
					frameOffset = SpriteSheet.Bomber1.SIDED_LEFT_WALK2_X;
				}
			}
		} else {
			// si el bomber empieza a moverse
			if (currentAction == GameAction.UP) {
				frameOffset = SpriteSheet.Bomber1.BACKWARDS_WALK1_X;
				_currentBomberFrame = 1;
			} else if (currentAction == GameAction.DOWN) {
				frameOffset = SpriteSheet.Bomber1.FRONT_WALK1_X;
				_currentBomberFrame = 1;
			} else if (currentAction == GameAction.RIGHT) {
				frameOffset = SpriteSheet.Bomber1.SIDED_RIGHT_WALK1_X;
				_currentBomberFrame = 1;
			} else if (currentAction == GameAction.LEFT) {
				frameOffset = SpriteSheet.Bomber1.SIDED_LEFT_WALK1_X;
				_currentBomberFrame = 1;
			} else if (currentAction == GameAction.NOTHING) {
				// si el bomber se para
				_frameTime = 0;
				if (_lastAction == GameAction.UP) {
					frameOffset = _prevBomberFrameOffset = SpriteSheet.Bomber1.BACKWARDS_QUIET_X;
					_currentBomberFrame = _prevBomberFrame = 0;
				} else if (_lastAction == GameAction.DOWN) {
					frameOffset = _prevBomberFrameOffset = SpriteSheet.Bomber1.FRONT_QUIET_X;
					_currentBomberFrame = _prevBomberFrame = 0;
				} else if (_lastAction == GameAction.RIGHT) {
					frameOffset = _prevBomberFrameOffset = SpriteSheet.Bomber1.SIDED_RIGHT_QUIET_X;
					_currentBomberFrame = _prevBomberFrame = 0;
				} else if (_lastAction == GameAction.LEFT) {
					frameOffset = _prevBomberFrameOffset = SpriteSheet.Bomber1.SIDED_LEFT_QUIET_X;
					_currentBomberFrame = _prevBomberFrame = 0;
				} else if (_lastAction == GameAction.NOTHING) {
					frameOffset = _prevBomberFrameOffset;
				}
			}
			_lastAction = currentAction;
			if (_lastAction == GameAction.DOWN || _lastAction == GameAction.LEFT
					|| _lastAction == GameAction.RIGHT || _lastAction == GameAction.UP) {
				_lastMoveAction = _lastAction;
			}
		}
		return frameOffset;
	}

	public int getFrameOffset() {
		return _currentBomberFrameOffset;
	}

	@Override
	public void moveUp(int elapsedTime) {
		int step = getStep(elapsedTime);
		boolean[] canBeRounded = new boolean[1];
		int gap = _level.gap(this, 0, -step, canBeRounded);
		_y -= Math.min(step, gap);
		_currentBomberFrameOffset = getNextFrameOffset(elapsedTime, GameAction.UP);
		if (gap == 0 && canBeRounded[0]) {
			int[] roundedPosition = _level.roundTheCorner(this, 0, -step);
			_x = roundedPosition[0];
			_y = roundedPosition[1];
		}
	}

	@Override
	public void moveDown(int elapsedTime) {
		int step = getStep(elapsedTime);
		boolean[] canBeRounded = new boolean[1];
		int gap = _level.gap(this, 0, step, canBeRounded);
		_y += Math.min(step, gap);
		_currentBomberFrameOffset = getNextFrameOffset(elapsedTime, GameAction.DOWN);
		if (gap == 0 && canBeRounded[0]) {
			int[] roundedPosition = _level.roundTheCorner(this, 0, step);
			_x = roundedPosition[0];
			_y = roundedPosition[1];
		}
	}

	@Override
	public void moveRight(int elapsedTime) {
		int step = getStep(elapsedTime);
		boolean[] canBeRounded = new boolean[1];
		int gap = _level.gap(this, step, 0, canBeRounded);
		_x += Math.min(step, gap);
		_currentBomberFrameOffset = getNextFrameOffset(elapsedTime, GameAction.RIGHT);
		if (gap == 0 && canBeRounded[0]) {
			int[] roundedPosition = _level.roundTheCorner(this, step, 0);
			_x = roundedPosition[0];
			_y = roundedPosition[1];
		}
	}

	@Override
	public void moveLeft(int elapsedTime) {
		int step = getStep(elapsedTime);
		boolean[] canBeRounded = new boolean[1];
		int gap = _level.gap(this, -step, 0, canBeRounded);
		_x -= Math.min(step, gap);
		_currentBomberFrameOffset = getNextFrameOffset(elapsedTime, GameAction.LEFT);
		if (gap == 0 && canBeRounded[0]) {
			int[] roundedPosition = _level.roundTheCorner(this, -step, 0);
			_x = roundedPosition[0];
			_y = roundedPosition[1];
		}
	}

	@Override
	public void doNothing(int elapsedTime) {
		_currentBomberFrameOffset = getNextFrameOffset(elapsedTime, GameAction.NOTHING);
	}

	@Override
	public void dropBomb() {
		if (_level.getUsedBombsBy(this) < _allowedBombs) {
			int[] cellCoords = _level.getMapCell(this);
			Block cell = _level.getBlock(cellCoords[0], cellCoords[1]);
			if (!_level.thereIsABomb(cellCoords[0], cellCoords[1])) {
				Bomb bomb = ObjectManager.createBomb(cell.getX(), cell.getY(), _explosionLength,
						_playerId);
				_level.setBomb(bomb);
			}
		}
	}

	public int getStep(long elapsedTime) {
		float step = elapsedTime * _speed + _unusedStep;
		int trueStep = (int) step;
		_unusedStep = step - trueStep;
		return trueStep;
	}

	public boolean isKilled() {
		return _killed;
	}

	public void die() {
		_killed = true;
	}

	private void increaseSpeed() {
		_speed *= 1.2;
	}

	private void increaseAllowedBombs() {
		_allowedBombs++;
	}

	private void increaseExplosionLength() {
		_explosionLength++;
	}

	public void increaseWeapons(Item catched) {
		if (catched.getType() == ItemType.BOMB) {
			increaseAllowedBombs();
		} else if (catched.getType() == ItemType.FLAME) {
			increaseExplosionLength();
		} else if (catched.getType() == ItemType.SPEED) {
			increaseSpeed();
		}
	}

	public int getXBoundingBox() {
		return _x;
	}

	public int getYBoundingBox() {
		return _y + _height - SpriteSheet.BLOCK_SIZE;
	}

	public int getWBoundingBox() {
		return 20;
	}

	public int getHBoundingBox() {
		return SpriteSheet.BLOCK_SIZE - 15;
	}

	public GameAction getLastMoveAction() {
		return _lastMoveAction;
	}
	
	@Override
	public String toString() {
		return "Bomber [_x=" + _x + ", _y=" + _y + "]";
	}

	public void draw(IController controller, int image) {
		controller.draw(null, image, _x, _y, _width, _height, getFrameOffset(), 0,
				SpriteSheet.Bomber1.W, SpriteSheet.Bomber1.H);
	}

	public void doDeath(int elapsed) {
		if (_deadTime < DEAD_TIME) {
			if (_frameTimeDead < (DEAD_TIME / DEAD_FRAMES)) {
				_frameTimeDead += elapsed;
			} else {
				_currentDeadFrame++;
				_frameTimeDead = 0;
			}
		}
		_deadTime += elapsed;		
	}
	
	public boolean deathDrawn() {
		return _deadTime > DEAD_TIME;
	}
	
	public void drawDead(IController controller, int image) {		
		int sx = 0, sy = 0, sw = 0, sh = 0;
		sy = SpriteSheet.Dead1.Y;
		sh = SpriteSheet.Dead1.H;
		switch (_currentDeadFrame) {
			case 0: {
				sx = SpriteSheet.Dead1.X1;
				sw = SpriteSheet.Dead1.W1;
			}
				break;
			case 1: {
				sx = SpriteSheet.Dead1.X2;
				sw = SpriteSheet.Dead1.W2;
			}
				break;
			case 2: {
				sx = SpriteSheet.Dead1.X3;
				sw = SpriteSheet.Dead1.W3;
			}
				break;
			case 3: {
				sx = SpriteSheet.Dead1.X4;
				sw = SpriteSheet.Dead1.W4;
			}
				break;
			default:
				break;
		}
		controller.draw(null, image, _x, _y, _width, _height, sx, sy, sw, sh);
	}
	
	
	public int getAllowedBombs() {
		return _allowedBombs;
	}

	public void setAllowedBombs(int allowedBombs) {
		this._allowedBombs = allowedBombs;
	}

	public int getCurrentBomberFrame() {
		return _currentBomberFrame;
	}

	public void setCurrentBomberFrame(int currentBomberFrame) {
		this._currentBomberFrame = currentBomberFrame;
	}

	public int getPrevBomberFrame() {
		return _prevBomberFrame;
	}

	public void setPrevBomberFrame(int prevBomberFrame) {
		this._prevBomberFrame = prevBomberFrame;
	}

	public int getCurrentBomberFrameOffset() {
		return _currentBomberFrameOffset;
	}

	public void setCurrentBomberFrameOffset(int currentBomberFrameOffset) {
		this._currentBomberFrameOffset = currentBomberFrameOffset;
	}

	public int getPrevBomberFrameOffset() {
		return _prevBomberFrameOffset;
	}

	public void setPrevBomberFrameOffset(int prevBomberFrameOffset) {
		this._prevBomberFrameOffset = prevBomberFrameOffset;
	}

	public int getSpeed() {
		return _speed;
	}

	public void setSpeed(int speed) {
		this._speed = speed;
	}

	public float getUnusedStep() {
		return _unusedStep;
	}

	public void setUnusedStep(float unusedStep) {
		this._unusedStep = unusedStep;
	}

	public GameAction getLastAction() {
		return _lastAction;
	}

	public void setLastAction(GameAction lastAction) {
		this._lastAction = lastAction;
	}
	
	public GameAction getLastMove() {
		return _lastMoveAction;
	}

	public void setLastMove(GameAction lastMoveAction) {
		this._lastMoveAction = lastMoveAction;
	}

	public long getFrameTime() {
		return _frameTime;
	}

	public void setFrameTime(long frameTime) {
		this._frameTime = frameTime;
	}

	public int getExplosionLength() {
		return _explosionLength;
	}

	public void setExplosionLength(int explosionLength) {
		this._explosionLength = explosionLength;
	}

	public void setKilled(boolean killed) {
		this._killed = killed;
	}

	public long getDeadTime() {
		return _deadTime;
	}

	public void setDeadTime(long deadTime) {
		this._deadTime = deadTime;
	}

	public long getFrameTimeDead() {
		return _frameTimeDead;
	}

	public void setFrameTimeDead(long frameTimeDead) {
		this._frameTimeDead = frameTimeDead;
	}

	public int getCurrentDeadFrame() {
		return _currentDeadFrame;
	}

	public void setCurrentDeadFrame(int currentDeadFrame) {
		this._currentDeadFrame = currentDeadFrame;
	}
	
	
}