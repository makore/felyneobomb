package ua.felyne.game.shared.agwtapi.audio.LWJGL;

import java.nio.ByteBuffer;

public class OGGStruct {
	private ByteBuffer data;

	private int channel;
	
	private int rate;

	public ByteBuffer getData() {
		return data;
	}

	public void setData(ByteBuffer data) {
		this.data = data;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
}
