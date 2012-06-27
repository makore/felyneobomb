package ua.felyne.game.shared;

import java.util.LinkedList;
import java.util.List;
import ua.felyne.game.client.GameGWT;
import ua.felyne.game.shared.Block.BlockType;
import ua.felyne.game.shared.Item.ItemType;
import ua.felyne.game.shared.agwtapi.event.IEvents.GameAction;

public class GameState extends State {
	private Bomber[] _bombers;
	private Level _level;
	private int _background;
	private int _playerId;

	public GameState(IGame game) {
		super(game);
		initResources();
	}

	private void initResources() {
		_controller.addImage("sprites/bomber1.png");
		_controller.addImage("sprites/bombs.png");
		_controller.addImage("sprites/flames.png");
		_controller.addImage("sprites/items.png");
		_controller.addImage("sprites/dead.png");
		_controller.addImage("sprites/bg1.png");
	}

	private void drawWorld(int numberOfPlayers) {
		_level = ObjectManager.createLevel();
		_background = _controller.addBuffer();
		redrawWorld();
		_controller.drawFromBuffer(_background);
		_bombers = new Bomber[numberOfPlayers];
		for (int i = 0; i < numberOfPlayers; i++) {
			_bombers[i] = ObjectManager.createBomber(i);
		}
	}

	private void drawBlock(int i, int j) {
		Block block = _level.getBlocks()[i][j];
		if (block.getType() == BlockType.TERRAIN) {
			block.draw(_controller, _background, SpriteSheet.IMG_BG1,
					SpriteSheet.Bg1.TERRAIN_X, SpriteSheet.Bg1.TERRAIN_Y,
					SpriteSheet.BLOCK_SIZE, SpriteSheet.BLOCK_SIZE);
		} else if (block.getType() == BlockType.WALL) {
			block.draw(_controller, _background, SpriteSheet.IMG_BG1,
					SpriteSheet.Bg1.WALL_X, SpriteSheet.Bg1.WALL_Y,
					SpriteSheet.BLOCK_SIZE, SpriteSheet.BLOCK_SIZE);
		} else if (block.getType() == BlockType.SOFT_BLOCK) {
			block.draw(_controller, _background, SpriteSheet.IMG_BG1,
					SpriteSheet.Bg1.SOFT_BLOCK_X, SpriteSheet.Bg1.SOFT_BLOCK_Y,
					SpriteSheet.BLOCK_SIZE, SpriteSheet.BLOCK_SIZE);
		} else if (block.getType() == BlockType.SOLID_BLOCK) {
			block.draw(_controller, _background, SpriteSheet.IMG_BG1,
					SpriteSheet.Bg1.SOLID_BLOCK_X,
					SpriteSheet.Bg1.SOLID_BLOCK_Y, SpriteSheet.BLOCK_SIZE,
					SpriteSheet.BLOCK_SIZE);
		}
	}

	private void drawItem(int i, int j) {
		Item item = _level.getItems()[i][j];
		if (item.getType() == ItemType.BOMB) {
			item.draw(_controller, _background, SpriteSheet.IMG_ITEMS,
					SpriteSheet.Items.BOMB_X, SpriteSheet.Items.BOMB_Y,
					SpriteSheet.Items.W, SpriteSheet.Items.H);
		} else if (item.getType() == ItemType.FLAME) {
			item.draw(_controller, _background, SpriteSheet.IMG_ITEMS,
					SpriteSheet.Items.FLAME_X, SpriteSheet.Items.FLAME_Y,
					SpriteSheet.Items.W, SpriteSheet.Items.H);
		} else if (item.getType() == ItemType.SPEED) {
			item.draw(_controller, _background, SpriteSheet.IMG_ITEMS,
					SpriteSheet.Items.SPEED_X, SpriteSheet.Items.SPEED_Y,
					SpriteSheet.Items.W, SpriteSheet.Items.H);
		}
	}

	private void redrawWorld() {
		Block[][] map = _level.getBlocks();
		Item[][] items = _level.getItems();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				Item item = items[i][j];
				if (item.getType() != ItemType.NONE && item.isVisible()) {
					drawItem(i, j);
				} else {
					drawBlock(i, j);
				}
			}
		}
	}

	@Override
	public void createWorld(int numberOfplayers, int playerId) {
		this._playerId = playerId;
		drawWorld(numberOfplayers);
	}

	@Override
	protected void refreshScreen() {
		if (_game.getClass().equals(GameGWT.class)) {
			if (_level.needRedraw()) {
				redrawWorld();
				_controller.drawFromBuffer(_background);
				_level.setRedraw(false);
			} else {
				_controller.drawFromBuffer(_background);
			}
		} else {
			redrawWorld();
		}
		for (Bomb bomb : _level.getFiredBombs()) {
			if (bomb.mustExplode()) {
				bomb.drawFlames(_controller, SpriteSheet.IMG_FLAMES, _level);
			} else {
				bomb.draw(_controller, SpriteSheet.IMG_BOMBS);
			}
		}
		for (Bomber bomber : _bombers) {
			if (bomber.isKilled()) {
				if (!bomber.deathDrawn()) {
					bomber.drawDead(_controller, SpriteSheet.IMG_DEAD1);
				} else {
					gameOver();
				}
			} else {
				bomber.draw(_controller, SpriteSheet.IMG_BOMBER1);
			}
		}
	}

	@Override
	public void updateObjects() {
		int elapsed = super.getWaitTime();
		for (int i = 0; i < _bombers.length; i++) {
			updateBomber(_bombers[i], elapsed);
			updateBombs(_bombers[i]);
		}
	}

	private void updateBomber(Bomber bomber, int elapsed) {

		if (bomber.isKilled()) {
			bomber.doDeath(elapsed);
		} else {
			if (bomber.getPlayerId() == _playerId) {
				if (_action == GameAction.UP) {
					bomber.moveUp(elapsed);
				} else if (_action == GameAction.DOWN) {
					bomber.moveDown(elapsed);
				} else if (_action == GameAction.LEFT) {
					bomber.moveLeft(elapsed);
				} else if (_action == GameAction.RIGHT) {
					bomber.moveRight(elapsed);
				// } else if (_action == GameAction.BOMB) {
				// 	_bomber.dropBomb();
				} else {
					bomber.doNothing(elapsed);
				}

				if (_bombRequest) {
					bomber.dropBomb();
				}
			} else {
				if (bomber.getActualAction() == GameAction.UP) {
					bomber.moveUp(elapsed);
				} else if (bomber.getActualAction() == GameAction.DOWN) {
					bomber.moveDown(elapsed);
				} else if (bomber.getActualAction() == GameAction.LEFT) {
					bomber.moveLeft(elapsed);
				} else if (bomber.getActualAction() == GameAction.RIGHT) {
					bomber.moveRight(elapsed);
				// } else if (_action == GameAction.BOMB) {
				// _bomber.dropBomb();
				} else {
					bomber.doNothing(elapsed);
				}
			}
		}
	}

	private void updateBombs(Bomber bomber) {
		int elapsed = super.getWaitTime();
		List<Bomb> copy = new LinkedList<Bomb>(_level.getFiredBombs());
		for (Bomb bomb : copy) {
			if (bomb.explosionDrawn()) {
				_level.unsetBomb(bomb);
			} else if (bomb.mustExplode()) {
				bomb.doExplosion(elapsed, _level, bomber);
			} else {
				bomb.doBomb(elapsed);
			}
		}
	}

	@Override
	public void update() {
		readInputData();
		updateObjects();
	}

	@Override
	public void render() {
		refreshScreen();
	}

	public void gameOver() {
		if (_bombers[_playerId].isKilled()) {
			_game.getEvents().removeHandler();

			// _stateContext.leave();
			// stateContext.enter(new EndState());
		}
	}

	@Override
	public String serializeAll() {
		String msg = "";

		for (int i = 0; i < _bombers.length; i++) {
			msg += _bombers[i].getX();
			msg += ";";
			msg += _bombers[i].getY();
			msg += ";";
			msg += _bombers[i].getAllowedBombs();
			msg += ";";
			msg += _bombers[i].getCurrentBomberFrame();
			msg += ";";
			msg += _bombers[i].getCurrentBomberFrameOffset();
			msg += ";";
			msg += _bombers[i].getCurrentDeadFrame();
			msg += ";";
			msg += _bombers[i].getDeadTime();
			msg += ";";
			msg += _bombers[i].getExplosionLength();
			msg += ";";
			msg += _bombers[i].getFrameTime();
			msg += ";";
			msg += _bombers[i].getFrameTimeDead();
			msg += ";";
			msg += _bombers[i].getLastAction();
			msg += ";";
			msg += _bombers[i].getActualAction();
			msg += ";";
			msg += _bombers[i].getPrevBomberFrame();
			msg += ";";
			msg += _bombers[i].getPrevBomberFrameOffset();
			msg += ";";
			msg += _bombers[i].getSpeed();
			msg += ";";
			msg += _bombers[i].getUnusedStep();
			msg += ";";
			msg += _bombers[i].isKilled();
			msg += ";";
			msg += _bombers[i].getLastMoveAction();
			msg += "@";
		}
		msg += "&";
		for (Bomb bomb : _level.getFiredBombs()) {
			msg += bomb.getX();
			msg += ";";
			msg += bomb.getY();
			msg += ";";
			msg += bomb.thrownBy();
			msg += ";";
			msg += bomb.getCurrentBombFrame(); // 3
			msg += ";";
			msg += bomb.getCurrentBombFrameOffset(); // 4
			msg += ";";
			msg += bomb.getCurrentFlameFrame(); // 5
			msg += ";";
			msg += bomb.getDropTimeStamp(); // 6
			msg += ";";
			msg += bomb.getExplosionLength(); // 7
			msg += ";";
			msg += bomb.getExplosionTime(); // 8
			msg += ";";
			msg += bomb.getFrameTimeBomb(); // 9
			msg += ";";
			msg += bomb.getFrameTimeFlame(); // 10
			msg += ";";
			msg += bomb.isExploded(); // 11
			msg += ";";
			msg += bomb.isSetedBombs(); // 12
			msg += ";";
			msg += bomb.isAnimationFlameEnded(); // 13
			msg += "@";
		}
		msg += "&";
		for (Block[] blocks : _level.getBlocks()) {
			for (Block block : blocks) {
				msg += block.getType();
				msg += "@";
			}
		}
		msg += "&";
		for (Item[] items : _level.getItems()) {
			for (Item item : items) {
				msg += item.getX();
				msg += ";";
				msg += item.getY();
				msg += ";";
				msg += item.getType();
				msg += ";";
				msg += item.isVisible();
				msg += "@";
			}
		}
		msg += "&";
		boolean[][] flames = _level.getFlames();
		for (int y = 0; y < flames.length; y++) {
			for (int x = 0; x < flames[y].length; x++) {
				msg += flames[y][x];
				msg += "@";
			}
		}

		return msg;
	}

	@Override
	public String serializePlayer() {
		String msg = "";

		msg += "Player:" + _playerId;
		msg += ";";
		msg += (_action == null ? GameAction.NOTHING : _action);
		msg += ";";
		msg += _bombRequest;

		return msg;
	}

	@Override
	public void desearializePlayer(String object) {
		String[] msg = object.split(";");
		int player = Integer.parseInt(msg[0].split(":")[1]);
		_bombers[player].setActualAction(GameAction.valueOf(msg[1]));
		if (Boolean.parseBoolean(msg[2])) {
			_bombers[player].dropBomb();
		}
	}

	@Override
	public void desearializeAll(String object) {
		String[] msg1 = object.split("&");
		String bombersMsg = msg1[0];
		String bombsMsg = msg1[1];
		String blocksMsg = msg1[2];
		String itemsMsg = msg1[3];
		String flamesMsg = msg1[4];
		String[] msg;

		// Bombers
		String[] strArrBombers = bombersMsg.split("@");
		for (int i = 0; i < strArrBombers.length; i++) {
			msg = strArrBombers[i].split(";");
			_bombers[i]._x = Integer.parseInt(msg[0]);
			_bombers[i]._y = Integer.parseInt(msg[1]);
			_bombers[i].setAllowedBombs(Integer.parseInt(msg[2]));
			_bombers[i].setCurrentBomberFrame(Integer.parseInt(msg[3]));
			_bombers[i].setCurrentBomberFrameOffset(Integer.parseInt(msg[4]));
			_bombers[i].setCurrentDeadFrame(Integer.parseInt(msg[5]));
			_bombers[i].setDeadTime(Long.parseLong(msg[6]));
			_bombers[i].setExplosionLength(Integer.parseInt(msg[7]));
			_bombers[i].setFrameTime(Long.parseLong(msg[8]));
			_bombers[i].setFrameTimeDead(Integer.parseInt(msg[9]));
			_bombers[i].setLastAction(GameAction.valueOf(msg[10]));
			_bombers[i].setActualAction(GameAction.valueOf(msg[11]));
			_bombers[i].setPrevBomberFrame(Integer.parseInt(msg[12]));
			_bombers[i].setPrevBomberFrameOffset(Integer.parseInt(msg[13]));
			_bombers[i].setSpeed(Integer.parseInt(msg[14]));
			_bombers[i].setUnusedStep(Float.parseFloat(msg[15]));
			_bombers[i].setKilled(Boolean.parseBoolean(msg[16]));
			_bombers[i].setLastMove(GameAction.valueOf(msg[17]));
		}

		// Bombs
		boolean[][] placedBombs = new boolean[SpriteSheet.Y_TILES][SpriteSheet.X_TILES];
		LinkedList<Bomb> firedBombs = new LinkedList<Bomb>();
		int x, y;
		if (bombsMsg.isEmpty() == false) {
			String[] strArrBombs = bombsMsg.split("@");
			for (String strBomb : strArrBombs) {
				msg = strBomb.split(";");
				x = Integer.parseInt(msg[0]);
				y = Integer.parseInt(msg[1]);
				Bomb bomb = ObjectManager.createBomb(x, y,
						Integer.parseInt(msg[7]), Long.parseLong(msg[2]));
				bomb.setAnimationFlameEnded(Boolean.parseBoolean(msg[13]));
				bomb.setCurrentBombFrame(Integer.parseInt(msg[3]));
				bomb.setCurrentBombFrameOffset(Integer.parseInt(msg[4]));
				bomb.setCurrentFlameFrame(Integer.parseInt(msg[5]));
				bomb.setDropTimeStamp(Long.parseLong(msg[6]));
				bomb.setExploded(Boolean.parseBoolean(msg[11]));
				bomb.setExplosionTime(Long.parseLong(msg[8]));
				bomb.setFrameTimeBomb(Integer.parseInt(msg[9]));
				bomb.setFrameTimeFlame(Integer.parseInt(msg[10]));
				bomb.setSetedBombs(Boolean.parseBoolean(msg[12]));
				bomb.setLastTimeStamp(System.currentTimeMillis());
				firedBombs.add(bomb);
				placedBombs[y / 48][x / 48] = true;
			}
			_level.setFiredBombs(firedBombs, placedBombs);
		}
		// Block
		Block[][] blocks = _level.getBlocks();
		String[] strArrBlocks = blocksMsg.split("@");
		for (y = 0; y < blocks.length; y++) {
			for (x = 0; x < blocks[y].length; x++) {
				blocks[y][x].setType(BlockType.valueOf(strArrBlocks[blocks[y].length * y
						+ x]));
			}
		}
		_level.setBlocks(blocks);
		// Item
		Item[][] items = new Item[SpriteSheet.Y_TILES][SpriteSheet.X_TILES];
		String[] strArrItems = itemsMsg.split("@");
		for (String strItem : strArrItems) {
			msg = strItem.split(";");
			x = Integer.parseInt(msg[0]);
			y = Integer.parseInt(msg[1]);

			items[y / 48][x / 48] = new Item(x, y, ItemType.valueOf(msg[2]),
					SpriteSheet.BLOCK_SIZE, SpriteSheet.BLOCK_SIZE);

			if (Boolean.parseBoolean(msg[3])
					&& items[y / 48][x / 48].getType() != ItemType.NONE) {
				items[y / 48][x / 48].show();
			}
		}
		_level.setItems(items);

		boolean[][] flames = new boolean[SpriteSheet.Y_TILES][SpriteSheet.X_TILES];
		msg = flamesMsg.split("@");
		for (y = 0; y < flames.length; y++) {
			for (x = 0; x < flames[y].length; x++) {
				flames[y][x] = Boolean
						.parseBoolean(msg[flames[y].length * y + x]);
			}
		}
		_level.setFlames(flames);
	}
}