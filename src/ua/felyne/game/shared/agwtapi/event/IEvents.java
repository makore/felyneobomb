package ua.felyne.game.shared.agwtapi.event;

public interface IEvents {
	
	public enum GameAction {
		UP, DOWN, RIGHT, LEFT, NOTHING, PAUSE
	};	
	
	public GameAction getAction();

	public void removeHandler();

	public boolean isBombRequest();
	
}
