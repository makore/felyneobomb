package ua.felyne.game.shared.agwtapi.audio.GWT;

import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.RootPanel;

import ua.felyne.game.shared.agwtapi.audio.ISound;


public class SoundHTML5 extends AbstractSound {

	private Audio sound;
	
	public SoundHTML5(String url)
	{
		super(url, 100, 100);
		
		sound = Audio.createIfSupported();
		AudioElement elem = sound.getAudioElement();
		
		RootPanel.get().add(sound);
		elem.setSrc(url);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int playAsSoundEffect(boolean loop) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int playAsSoundEffect(boolean loop, float x, float y, float z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int playAsMusic(boolean loop) {
		sound.getAudioElement().play();
		return 0;
	}


}
