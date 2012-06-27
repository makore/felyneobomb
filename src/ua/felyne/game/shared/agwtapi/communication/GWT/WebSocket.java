package ua.felyne.game.shared.agwtapi.communication.GWT;

import com.google.gwt.core.client.JavaScriptObject;

import ua.felyne.game.shared.agwtapi.communication.IWebSocketHandler;

public class WebSocket extends JavaScriptObject {

	public static final int CONNECTING = 0;
	public static final int OPEN = 1;
	public static final int CLOSING = 2;
	public static final int CLOSED = 3;
	
	protected static native WebSocket create(String url) /*-{
		return new $wnd.WebSocket(url);
	}-*/;
	
	protected static native WebSocket create(String url, String protocol) /*-{
		return new $wnd.WebSocket(url, protocol);
	}-*/;
	
	protected WebSocket() {
		
	}
	
	public final native int getBufferedAmount() /*-{
		return this.bufferedAmount;
	}-*/;
	
	public final native int getReadyState() /*-{
		return this.readyState;
	}-*/;
	
	public final native void send(String data) /*-{
		this.send(data);
	}-*/;
	
	public final native void close() /*-{
		this.close();
	}-*/;
	
	
	public final native void setHandler(IWebSocketHandler handler) /*-{
		
		var _this = this;
		this.onopen = $entry(function() {
			handler.@ua.felyne.game.shared.agwtapi.communication.IWebSocketHandler::onOpen(Lua/felyne/game/shared/agwtapi/communication/GWT/WebSocket;)(_this);
		});
		
		this.onclose = $entry(function() {
			handler.@ua.felyne.game.shared.agwtapi.communication.IWebSocketHandler::onClose(Lua/felyne/game/shared/agwtapi/communication/GWT/WebSocket;)(_this);
		});
				
		this.onerror = $entry(function() {
			handler.@ua.felyne.game.shared.agwtapi.communication.IWebSocketHandler::onError(Lua/felyne/game/shared/agwtapi/communication/GWT/WebSocket;)(_this);
		});	
			
		this.onmessage = $entry(function(event) {
			handler.@ua.felyne.game.shared.agwtapi.communication.IWebSocketHandler::onMessage(Lua/felyne/game/shared/agwtapi/communication/GWT/WebSocket;Lua/felyne/game/shared/agwtapi/communication/GWT/MessageEvent;)(_this,event);
		});
	}-*/;
}
