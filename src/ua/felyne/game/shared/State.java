package ua.felyne.game.shared;

import ua.felyne.game.client.GameGWT;
import ua.felyne.game.shared.agwtapi.GWTController;
import ua.felyne.game.shared.agwtapi.IController;
import ua.felyne.game.shared.agwtapi.event.IEvents.GameAction;

public abstract class State {
	protected StateContext _stateContext;
	protected IGame _game;
	protected IController _controller;
	protected long _prevStatsTime;
	protected long _elapsedTime;
	protected long _totalElapsedTime;
	protected long _frameCount;
	protected long _framesLastSecond;
	protected double _actualFPS;
	protected int _waitTime;
	protected float _unusedSteps;
	protected static final int FPS = 60;
	protected GameAction _action;
	protected boolean _bombRequest;

	public State(IGame game) {
		this._game = game;
		_prevStatsTime = 0L;
		_elapsedTime = 0L;
		_totalElapsedTime = 0L;
		_frameCount = 0L;
		_framesLastSecond = 0L;
		_actualFPS = 0.0;
		_waitTime = 1;
		_unusedSteps = 0.0f;
		_action = GameAction.NOTHING;
		_bombRequest = false;
		if (game.getClass().equals(GameGWT.class)) {
			_controller = new GWTController();
		} else {	
			/*
			 * XXX Para poder compilar en GWT, comentar la siguiente linea
			 */
			//_controller = new ua.felyne.game.shared.agwtapi.LWJController();			
		}
		_controller.init("game", SpriteSheet.SCREEN_WIDTH, SpriteSheet.SCREEN_HEIGHT);
	}

	public abstract void createWorld(int numberOfPlayers, int playerId);

	protected void readInputData() {
		_action = _game.getEvents().getAction();
		_bombRequest = _game.getEvents().isBombRequest();
	}

	protected abstract void updateObjects();

	protected abstract void refreshScreen();

	public int getWaitTime() {
		_frameCount++;
		_framesLastSecond++;
		long timeNow = System.currentTimeMillis();
		_elapsedTime = _prevStatsTime != 0L ? timeNow - _prevStatsTime : 0L;

		/*
		 * FPS 
		 * 
		 * _totalElapsedTime += _elapsedTime; 
		 * if (_totalElapsedTime >= 1000L) {
		 * 	_totalElapsedTime = _totalElapsedTime - 1000L; 
		 * 	_actualFPS = _framesLastSecond; 
		 * 	_framesLastSecond = 0L; 
		 * }
		 * _controller.writeFPS(_actualFPS);
		 */
		
		_waitTime = Math.max(1, (1000 / FPS) - (int) _elapsedTime);
		_prevStatsTime = timeNow;
		return _waitTime;
	}

	public long getElapsedTime() {
		return _elapsedTime;
	}

	public abstract void update();

	public abstract void render();
	
	public abstract String serializeAll();
	
	public abstract String serializePlayer();
	
	public abstract void desearializeAll(String object);
	
	public abstract void desearializePlayer(String object);

	public void setStateContext(StateContext stateContext) {
		this._stateContext = stateContext;
	}

	public IGame getGame() {
		return _game;
	}

	public IController getController() {
		return _controller;
	}
}