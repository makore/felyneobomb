package ua.felyne.game.shared.agwtapi.communication.GWT;

import com.google.gwt.core.client.JavaScriptObject;

public class MessageEvent extends JavaScriptObject{

	protected MessageEvent() {
		
	}
	
	public final native String getData() /*-{
		return this.data;
	}-*/;
}
