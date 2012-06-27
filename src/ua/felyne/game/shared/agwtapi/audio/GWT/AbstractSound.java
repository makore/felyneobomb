package ua.felyne.game.shared.agwtapi.audio.GWT;

import com.google.gwt.dom.client.Element;

import ua.felyne.game.shared.agwtapi.audio.ISound;


public abstract class AbstractSound implements ISound {

	private final String url;
	private int volume; 
	private int balance;
	public AbstractSound(String url, int v, int b)
	{
		this.url = url;
		this.volume = v;
		this.balance = b;
	}

}
