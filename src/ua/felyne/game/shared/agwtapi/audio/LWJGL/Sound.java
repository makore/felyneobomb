package ua.felyne.game.shared.agwtapi.audio.LWJGL;

import ua.felyne.game.shared.agwtapi.audio.ISound;

public class Sound implements ISound {

	private int id;
	private int idstore;
	private SoundController controller;
	public static final int OGG = 0;
	
	public Sound(SoundController controller, int idstore) {
		this.controller = controller;
		this.idstore = idstore;
		
		
	}
	
	@Override
	public void stop() {
		if(id != -1) {
			controller.stopSource(id);
		}
	}

	@Override
	public int getID() {
		return idstore;
	}

	@Override
	public boolean isPlaying() {
		if(id != -1) {
			return controller.isPlaying(id);
		}
		return false;
	}

	@Override
	public int playAsSoundEffect(boolean loop) {
		id = controller.playAsSound(idstore, loop);
		return controller.getSource(id);
	}

	@Override
	public int playAsSoundEffect(boolean loop, float x, float y, float z) {
		id = controller.playAsSoundAt(idstore, loop, x, y, z);
		return controller.getSource(id);
	}

	@Override
	public int playAsMusic(boolean loop) {
		controller.playAsMusic(idstore, loop);
		id = 0;
		return controller.getSource(0);
	}

}
