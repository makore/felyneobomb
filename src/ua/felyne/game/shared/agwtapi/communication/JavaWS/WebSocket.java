package ua.felyne.game.shared.agwtapi.communication.JavaWS;

import org.java_websocket.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import ua.felyne.game.shared.agwtapi.communication.IWebSocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;


public class WebSocket implements IWebSocket {

	private WebSocketClient ws;
	private ArrayList<String> lastMessages;
	
	
	public WebSocket(String url) {
		URI uri;
		try {
			uri = new URI("ws://" + url + ":8887");
			lastMessages = new ArrayList<String>();
			ws = new WebSocketClient(uri) {
				
				@Override
				public void onOpen(ServerHandshake handshake) {
					//lastMessages.add("@You are connected@");
				}
				
				@Override
				public void onMessage(String message) {
					lastMessages.add(message);				
				}
				
				@Override
				public void onError(Exception e) {
					lastMessages.add( "Exception occured ...\n" + e + "\n" );
					e.printStackTrace();
				}
				
				@Override
				public void onClose(int code, String reason, boolean remote) {
					lastMessages.add("@You have been disconnected@");
				}
			};
		
			ws.connect();

		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}
	
	public WebSocket(String url, String protocol) {
		URI uri;
		try {
			uri = new URI("ws://" + url + ":" + protocol);
			lastMessages = new ArrayList<String>();
			ws = new WebSocketClient(uri) {
				
				@Override
				public void onOpen(ServerHandshake handshake) {
					//lastMessages.add("@You are connected@");
				}
				
				@Override
				public void onMessage(String message) {
					lastMessages.add(message);				
				}
				
				@Override
				public void onError(Exception e) {
					lastMessages.add( "Exception occured ...\n" + e + "\n" );
					e.printStackTrace();
				}
				
				@Override
				public void onClose(int code, String reason, boolean remote) {
					lastMessages.add("@You have been disconnected@");
				}
			};
		
			ws.connect();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}
	
	public String popMessages() {
		String msg = null;
		if(lastMessages.isEmpty() == false) {
			msg =lastMessages.get(0);
			lastMessages.remove(0);
		}
		return msg;
	}
	
	public int size() {
		return lastMessages.size();
	}
	
	public void clearMessages() {
		lastMessages.clear();
	}
	
	public void send(String message) {
		try {
			ws.send(message);
		} catch (NotYetConnectedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
