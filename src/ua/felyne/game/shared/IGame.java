package ua.felyne.game.shared;

import ua.felyne.game.shared.agwtapi.event.IEvents;

public interface IGame {
	public void startGame(int players, int id);
	public void loop();
	public void gameUpdate();
	public void gameRender();
	public void stopGame();
	public boolean isPaused();
	public void pauseGame();
	public void resumeGame();
	public IEvents getEvents();
}
