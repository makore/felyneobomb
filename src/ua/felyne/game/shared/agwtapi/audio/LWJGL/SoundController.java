package ua.felyne.game.shared.agwtapi.audio.LWJGL;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.OpenALException;

import ua.felyne.game.shared.agwtapi.audio.ISound;
import ua.felyne.game.shared.agwtapi.audio.ISoundController;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;


public class SoundController implements ISoundController {
	
	private int current, sources;
	private IntBuffer source;
	private boolean on, soundWorks;
	private float volume;
	private final int maxSources = 64;
	private HashMap<String, Integer> soundStore = new HashMap<String, Integer>();

	private static SoundController controller = null;
	
	public static SoundController getInstance() {
		if(controller == null) {
			controller = new SoundController();
			return controller;
		}
		return controller;
	}
	
	private SoundController() {
		AccessController.doPrivileged(new PrivilegedAction() {
			
			public Object run() {
				try {
					AL.create();
					soundWorks = true;
					on = true;
					current = -1;
				} catch(Exception e) {
					soundWorks = false;
					on = false;
					System.out.println("peta");
				}
				
				return null;
			}
		});
		
		if(soundWorks) {
			sources = 0;
			source = BufferUtils.createIntBuffer(maxSources);
			while(AL10.alGetError() == AL10.AL_NO_ERROR) {
				IntBuffer temp = BufferUtils.createIntBuffer(1);
				
				try {
					AL10.alGenSources(temp);
				
					if (AL10.alGetError() == AL10.AL_NO_ERROR) {
						sources++;
						source.put(temp.get(0));
						if (sources > maxSources-1) {
							break;
						}
					} 
				} catch (OpenALException e) {
					break;
				}
			}
			
			if(AL10.alGetError() != AL10.AL_NO_ERROR) {
				on = false;
				soundWorks = false;
			}
			else {
				FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(
						new float[] { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f });
				FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(
						new float[] { 0.0f, 0.0f, 0.0f });
				FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(
						new float[] { 0.0f, 0.0f, 0.0f });
				listenerPos.flip();
				listenerVel.flip();
				listenerOri.flip();
				AL10.alListener(AL10.AL_POSITION, listenerPos);
				AL10.alListener(AL10.AL_VELOCITY, listenerVel);
				AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
			}
		}
	}

	@Override
	public ISound createSound(int type, String url) {
		if(type == Sound.OGG) {			
			return getOgg(url, new BufferedInputStream(SoundController.class.getClassLoader().getResourceAsStream(url)));
		}
		return null;
	}

	@Override
	public void setMusic(boolean music) {
		on=music;		
	}

	@Override
	public boolean isMusicOn() {
		return on;
	}

	@Override
	public void setMusicVolume(float volume) {
		this.volume = volume;		
	}

	@Override
	public boolean soundWorks() {		
		return soundWorks;
	}

	@Override
	public float getMusicVolume() {
		return volume;
	}

	@Override
	public int getSource(int index) {		
		return source.get(index);
	}

	@Override
	public void stopSource(int index) {
		AL10.alSourceStop(source.get(index));
	}

	@Override
	public int playAsSound(int buffer, boolean loop) {
		return playAsSoundAt(buffer, loop, 0, 0, 0);
	}
	
	private int findFreeSource() {
		for (int i=1;i<sources-1;i++) {
			int state = AL10.alGetSourcei(source.get(i), AL10.AL_SOURCE_STATE);
			
			if ((state != AL10.AL_PLAYING) && (state != AL10.AL_PAUSED)) {
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public int playAsSoundAt(int buffer, boolean loop, float x, float y, float z) {
		if (soundWorks) {
			int nextSource = findFreeSource();
			if (nextSource == -1) {
				return -1;
			}
			
			AL10.alSourceStop(source.get(nextSource));
			
			AL10.alSourcei(source.get(nextSource), AL10.AL_BUFFER, buffer);
		    AL10.alSourcei(source.get(nextSource), AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
		    
		    FloatBuffer posBuffer = BufferUtils.createFloatBuffer(3);
		    FloatBuffer velBuffer = BufferUtils.createFloatBuffer(3);
		    posBuffer.put(new float[] { x, y, z });
		    velBuffer.put(new float[] { 0, 0, 0 });
		    AL10.alSource(source.get(nextSource), AL10.AL_POSITION, posBuffer);
			AL10.alSource(source.get(nextSource), AL10.AL_VELOCITY, velBuffer);
		    
			AL10.alSourcePlay(source.get(nextSource)); 
			
			return nextSource;
		}
		
		return -1;
	}

	@Override
	public boolean isPlaying(int index) {
		int state = AL10.alGetSourcei(source.get(index), AL10.AL_SOURCE_STATE);
		return (state == AL10.AL_PLAYING);
	}

	@Override
	public void playAsMusic(int buffer, boolean loop) {
		if(soundWorks) {
			if(current != -1) {
				AL10.alSourceStop(current);
			}
			
			AL10.alSourcei(source.get(0), AL10.AL_BUFFER, buffer);
			AL10.alSourcei(source.get(0), AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
			
			current = source.get(0);
			
			AL10.alSourcePlay(source.get(0));
		}		
	}

	private ISound getOgg(String ref, InputStream in) {
		if(!soundWorks || !on) {
			return null;
		}
		
		int buffer = -1;
		if(soundStore.get(ref) != null) {
			buffer = ((Integer) soundStore.get(ref)).intValue();
		}
		else {
			IntBuffer buf = BufferUtils.createIntBuffer(1);
			
			OGGDecoder decoder = new OGGDecoder();
			OGGStruct ogg;
			try {
				ogg = decoder.getData(in);
			
				AL10.alGenBuffers(buf);
				AL10.alBufferData(buf.get(0), ogg.getChannel() > 1 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16, ogg.getData(), ogg.getRate());
			
				soundStore.put(ref,  new Integer(buf.get(0)));
			
				buffer = buf.get(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return new Sound(this,buffer);
	}
	
	@Override
	public void stopSoundEffect(int id) {
		AL10.alSourceStop(id);
	}

}
