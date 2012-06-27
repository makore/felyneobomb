package ua.felyne.game.shared.agwtapi.event.LWJ;

import org.lwjgl.input.Keyboard;

import ua.felyne.game.shared.agwtapi.event.IEvents;

public class Events implements IEvents {

	private GameAction _action;	

	private boolean _bombRequest;
	
	public Events() {
		_action = GameAction.NOTHING;
	}
	
	@Override
	public GameAction getAction() {
		while(Keyboard.next()) {
			if(onKeyUpHandler()) {
				onKeyDownHandler();
			}
			else {
				_action = GameAction.NOTHING;
			}
		}
		return _action;
	}


	private void onKeyDownHandler() {
		if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
			_action = GameAction.UP;
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
			_action = GameAction.DOWN;
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
			_action = GameAction.LEFT;
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
			_action = GameAction.RIGHT;
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
			//_action = GameAction.SPACE;
			_bombRequest = true;
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_Z) {
			_action = GameAction.PAUSE;
		}
	}
	
	public boolean isBombRequest() {
		boolean request = _bombRequest;
		_bombRequest = false;
		return request;
	}	
	
	private boolean onKeyUpHandler() {
		return Keyboard.getEventKeyState();
	}	
	
	@Override
	public void removeHandler() {
	}

}


















