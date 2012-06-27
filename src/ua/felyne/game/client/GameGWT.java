package ua.felyne.game.client;

import ua.felyne.game.shared.GameState;
import ua.felyne.game.shared.IGame;
import ua.felyne.game.shared.State;
import ua.felyne.game.shared.StateContext;
import ua.felyne.game.shared.agwtapi.GWTController;
import ua.felyne.game.shared.agwtapi.communication.GWT.WebSocketClient;
import ua.felyne.game.shared.agwtapi.event.IEvents.GameAction;
import ua.felyne.game.shared.agwtapi.event.GWT.Events;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class GameGWT implements IGame {
	private Timer _timer;
	private Events _events;
	private StateContext _stateContext;
	private State _state;
	private GameAction _lastAction;
	private WebSocketClient _wsClient;
	private int _numberOfPlayers;
	private int _playerId;
	private final String _location = "ws://localhost:8887";

	private boolean _paused = false;
	private boolean _selected = false;
	private boolean _time;

	public GameGWT() {
		menu();
		Timer time = new Timer() {

			@Override
			public void run() {
				if (_selected) {
					_selected = false;
					cancel();
					init();
					Util.showGame();
				}
			}
		};

		time.scheduleRepeating(100);
	}

	public void menu() {
		final Anchor anchorCreate = new Anchor("01. Crear partida");
		final Anchor anchorJoin = new Anchor("02. Unirse a partida");
		anchorCreate.setTitle("Crear partida");
		anchorJoin.setTitle("Unirse a partida");
		RootPanel.get("enlace1").add(anchorCreate);
		RootPanel.get("enlace2").add(anchorJoin);

		anchorCreate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				_numberOfPlayers = 1;
				_playerId = 0;
				_selected = true;
			}
		});

		anchorJoin.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				_wsClient = new WebSocketClient(_location);
				_time = false;
				Timer time1 = new Timer() {

					@Override
					public void run() {
						if (_wsClient.size() != 0) {
							cancel();
							String[] msg1 = _wsClient.popMessages().split(":");
							if (msg1[0].equals("@yourId")) {
								_playerId = Integer.parseInt(msg1[1]) - 1;
								_time = true;
							}
						}

					}
				};

				time1.scheduleRepeating(100);

				Timer time2 = new Timer() {

					@Override
					public void run() {
						if (_time && _wsClient.size() != 0) {
							cancel();
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
								_numberOfPlayers = Integer.parseInt(msg2[i + 1]);
							}
							_selected = true;
						}

					}
				};

				time2.scheduleRepeating(100);
				
			}
		});

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				Util.enhancedLinks();
			}
		});
	}

	public void init() {
		_stateContext = new StateContext();
		GameState gS = new GameState(this);
		_stateContext.enter(gS);
		_state = _stateContext.getState();
		_timer = null;
		_events = new Events();
		_lastAction = GameAction.NOTHING;
		startGame(_numberOfPlayers, _playerId);
	}

	@Override
	public void startGame(int numberOfPlayers, int playerId) {
		final int numer_of_players = numberOfPlayers;
		final int player_id = playerId;
		String src = "sprites/bg1.png";
		Image img = new Image(src);
		img.setVisible(false);
		((GWTController) _state.getController()).addImage(src, img);

		img.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {

				_state.createWorld(numer_of_players, player_id);

				_timer = new Timer() {
					@Override
					public void run() {
						loop();
					}
				};
				sheduleNextFrame();
			}
		});
		RootPanel.get().add(img);
	}

	private void sheduleNextFrame() {
		_timer.schedule(_state.getWaitTime());
	}

	@Override
	public void loop() {
		gameUpdate();
		gameRender();

		sheduleNextFrame();
		if (checkExit()) {
			stopGame();
		}
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
		if (!_paused) {
			_state.update();
			if (_wsClient != null) {
				String message = _wsClient.popMessages();
				while (message != null) {
					_state.desearializeAll(message);
					message = _wsClient.popMessages();
				}
				_wsClient.send(_state.serializePlayer());
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