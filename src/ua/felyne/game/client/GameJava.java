package ua.felyne.game.client;

import org.lwjgl.opengl.Display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;

import ua.felyne.game.shared.GameState;
import ua.felyne.game.shared.IGame;
import ua.felyne.game.shared.State;
import ua.felyne.game.shared.StateContext;
import ua.felyne.game.shared.agwtapi.communication.JavaWS.WebSocket;
import ua.felyne.game.shared.agwtapi.communication.JavaWS.WebSocketServer;
import ua.felyne.game.shared.agwtapi.event.IEvents.GameAction;
import ua.felyne.game.shared.agwtapi.event.LWJ.Events;

public class GameJava implements IGame {
	private Timer _timer;
	private Events _events;
	private StateContext _stateContext;
	private State _state;
	private GameAction _lastAction;
	private WebSocket _wsClient;
	private WebSocketServer _wsServer;
	private boolean _client;
	private int _players;
	private int _id;
	private boolean _selected = false;

	private boolean _gameOver = false;
	private boolean _paused = false;

	public GameJava() {
		menu();
		_stateContext = new StateContext();
		GameState gS = new GameState(this);
		_stateContext.enter(gS);
		_state = _stateContext.getState();
		_timer = null;
		_events = new Events();
		_lastAction = GameAction.NOTHING;
		startGame(_players, _id);
	}

	private void menu() {
		final MenuJava menu = new MenuJava();

		menu.addListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == menu.getJoin()) {
					_wsClient = new WebSocket("localhost");
					while (_wsClient.size() == 0) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					String[] msg1 = _wsClient.popMessages().split(":");
					if (msg1[0].equals("@yourId")) {
						_id = Integer.parseInt(msg1[1]) - 1;
						_client = true;
						while (_wsClient.size() == 0) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
						String[] msg2 = _wsClient.popMessages().split(":");
						boolean find = false;
						int i = 0;
						for (; i < msg2.length; i++) {
							if (msg2[i].equals("@startGame")) {
								find = true;
								break;
							}
						}
						if (find) {
							_players = Integer.parseInt(msg2[i + 1]);
							_selected = true;
						} else {
							System.err.println("Connection error");
							System.exit(-1);
						}
					} else {
						System.err.println("Connection error");
						System.exit(-1);
					}
				} else {
					if (e.getSource() == menu.getCreate()) {
						try {
							_wsServer = new WebSocketServer(menu.getPlayers(),
									8887);
							_wsServer.start();
							while (_wsServer.complete() == false) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
							_wsServer.startGame();
							_client = false;
							menu.setVisible(false);
							_players = menu.getPlayers();
							_id = 0;
							_selected = true;
						} catch (UnknownHostException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		while (!_selected) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		menu.dispose();
	}

	@Override
	public void startGame(int players, int id) {
		_state.createWorld(players, id);

		loop();
	}

	@Override
	public void loop() {
		boolean game = true;
		while (game) {
			Display.sync(30);
			gameUpdate();
			gameRender();
			Display.update();
			if (Display.isCloseRequested()) {
				game = false;
			}
		}
		Display.destroy();
	}

	@Override
	public void gameUpdate() {
		if (_events.getAction() == GameAction.PAUSE
				&& _lastAction != GameAction.PAUSE) {
			if (isPaused()) {
				resumeGame();
			} else {
				// TODO mostrar mensaje de pausa
				pauseGame();
			}
		}

		_lastAction = _events.getAction();

		if (!_paused && !_gameOver) {
			_state.update();
			if (_client) {
				String message = _wsClient.popMessages();
				while (message != null) {
					_state.desearializeAll(message);
					message = _wsClient.popMessages();
				}
				_wsClient.send(_state.serializePlayer());
			} else {
				ArrayList<String> messages = _wsServer.getMoves();
				for (String message : messages) {
					_state.desearializePlayer(message);
				}
				_wsServer.sendToAll(_state.serializeAll());
			}
		}
	}

	@Override
	public void gameRender() {
		_state.render();
	}

	@Override
	public void stopGame() {
		if (_timer != null) {
			_timer.cancel();
			_timer = null;
		}
	}

	@Override
	public boolean isPaused() {
		return _paused;
	}

	@Override
	public void pauseGame() {
		_paused = true;
	}

	@Override
	public void resumeGame() {
		_paused = false;
	}

	public boolean checkExit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Events getEvents() {
		return _events;
	}
}
