package ua.felyne.game.shared.agwtapi.event.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;

import ua.felyne.game.shared.agwtapi.event.IEvents;

public class Events implements IEvents {

	private HandlerRegistration _listener;
	public static final int KEY_DOWN = 40;
	public static final int KEY_LEFT = 37;
	public static final int KEY_UP = 38;
	public static final int KEY_RIGHT = 39;
	public static final int KEY_SPACE = 32;
	public static final int KEY_Z = 90;

	private GameAction _action;
	private boolean _bombRequest;

	public Events() {
		_listener = null;
		_action = GameAction.NOTHING;
		_bombRequest = false;

		_listener = Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
			@Override
			public void onPreviewNativeEvent(NativePreviewEvent event) {
				int keyCode = event.getNativeEvent().getKeyCode();
				if (event.getTypeInt() == Event.ONKEYDOWN) {
					onKeyDownHandler(keyCode);
				} else if (event.getTypeInt() == Event.ONKEYUP) {
					onKeyUpHandler(keyCode);
				}
				// event.getNativeEvent().preventDefault();
			}			
		});
	}

	@Override
	public GameAction getAction() {
		return _action; 
	}

	private GameAction key2Action(int keyCode) {
		GameAction action = null;
		if (keyCode == KEY_UP) {
			action = GameAction.UP;
		}
		if (keyCode == KEY_DOWN) {
			action = GameAction.DOWN;
		}
		if (keyCode == KEY_LEFT) {
			action = GameAction.LEFT;
		}
		if (keyCode == KEY_RIGHT) {
			action = GameAction.RIGHT;
		}
		if (keyCode == KEY_SPACE) {
			//action = GameAction.BOMB;
			_bombRequest = true;
			action = _action;
		}
		if (keyCode == KEY_Z) {
			action = GameAction.PAUSE;
		}
		return action;
	}

	private void onKeyDownHandler(int keyCode) {
		_action = key2Action(keyCode);
	}

	private void onKeyUpHandler(int keyCode) {
		if (keyCode == KEY_UP || keyCode == KEY_DOWN || keyCode == KEY_LEFT || keyCode == KEY_RIGHT
				|| /*keyCode == KEY_SPACE ||*/ keyCode == KEY_Z) {
			_action = GameAction.NOTHING;
		}
	}

	public boolean isBombRequest() {
		boolean request = _bombRequest;
		_bombRequest = false;
		return request;
	}	
	
	@Override
	public void removeHandler() {
		_listener.removeHandler();
	}
}