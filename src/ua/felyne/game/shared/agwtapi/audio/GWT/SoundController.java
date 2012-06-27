package ua.felyne.game.shared.agwtapi.audio.GWT;

import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

import ua.felyne.game.shared.agwtapi.audio.ISound;
import ua.felyne.game.shared.agwtapi.audio.ISoundController;


public class SoundController implements ISoundController {

	public static final int HTML5 = 0;
	
	public Element container;
	
	private static SoundController controller = null;
	
	private SoundController() {
		container = DOM.createDiv();
		RootPanel.getBodyElement().appendChild(container);
		Style style = container.getStyle();
	    style.setPosition(Position.ABSOLUTE);
	    style.setOverflow(Overflow.HIDDEN);
	    style.setLeft(-500, Unit.PX);
	    style.setTop(-500, Unit.PX);
	    style.setWidth(0, Unit.PX);
	    style.setHeight(0, Unit.PX);
	}
	
	public static SoundController getInstance() {
		if(controller == null) {
			controller = new SoundController();
			return controller;
		}
		return controller;
	}

	@Override
	public ISound createSound(int type, String url) {
		ISound sound;
		switch (type) {
			case HTML5 :
				sound = new SoundHTML5(url);
			break;
			default: 
				sound = null;
		}
		return sound;
	}

	public native boolean play(Element controller, Element elem)
	/*-{
	 	controller.appendChild(elem);
	 	return true;
	 }-*/;
	
	public boolean play(SoundHTML5 audio)
	{
		audio.playAsMusic(true);
		
		return true;
	}

	@Override
	public void setMusic(boolean music) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isMusicOn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMusicVolume(float volume) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean soundWorks() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getMusicVolume() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSource(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void stopSource(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int playAsSound(int buffer, boolean loop) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int playAsSoundAt(int buffer, boolean loop, float x, float y, float z) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isPlaying(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void playAsMusic(int buffer, boolean loop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopSoundEffect(int id) {
		// TODO Auto-generated method stub
		
	}

}
