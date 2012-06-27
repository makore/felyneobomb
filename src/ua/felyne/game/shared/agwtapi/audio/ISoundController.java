package ua.felyne.game.shared.agwtapi.audio;

public interface ISoundController {
	
	public ISound createSound(int type, String url);
	
	public void setMusic(boolean music) ;

	public boolean isMusicOn();

	public void setMusicVolume(float volume);

	public boolean soundWorks();
	
	public float getMusicVolume();

	public int getSource(int index);	
	
	public void stopSource(int index);
	
	public int playAsSound(int buffer, boolean loop);
	
	public int playAsSoundAt(int buffer, boolean loop,float x, float y, float z);
	
	public boolean isPlaying(int index);
	
	public void playAsMusic(int buffer, boolean loop);

	public void stopSoundEffect(int id);

}
