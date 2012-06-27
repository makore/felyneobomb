package ua.felyne.game.shared.agwtapi.audio;

public interface ISound {
	
	public void stop();

	public int getID();
	
	public boolean isPlaying();

	public int playAsSoundEffect(boolean loop);

	public int playAsSoundEffect(boolean loop, float x, float y, float z);

	public int playAsMusic(boolean loop);
}
