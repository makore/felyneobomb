package ua.felyne.game.shared;

public interface IBomber {
	public void moveUp(int elapsedTime);
	public void moveDown(int elapsedTime);
	public void moveRight(int elapsedTime);
	public void moveLeft(int elapsedTime);
	public void doNothing(int elapsedTime);
	public void dropBomb();
}
