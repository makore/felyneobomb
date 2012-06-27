package ua.felyne.game.shared.agwtapi.communication.GWT;

import ua.felyne.game.shared.agwtapi.communication.IWebSocketHandler;

import java.util.ArrayList;

public class WebSocketClient {

	private WebSocket ws = null;
	private ArrayList<String> lastMessages;

	public WebSocketClient(String url, String protocol) {
		ws = WebSocket.create(url, protocol);//new WebSocket(url, protocol);
		lastMessages = new ArrayList<String>();
		
		ws.setHandler(new IWebSocketHandler() {
			
			@Override
			public void onOpen(WebSocket webSocket) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMessage(WebSocket webSocket, MessageEvent event) {
				lastMessages.add(event.getData());				
				
			}
			
			@Override
			public void onError(WebSocket webSocket) {
				lastMessages.add( "Exception occured ...\n" + webSocket + "\n" );
			}
			
			@Override
			public void onClose(WebSocket webSocket) {
				lastMessages.add("@You have been disconnected@");				
			}
		});
	}
	
	public WebSocketClient(String url) {
		ws = WebSocket.create(url);//new WebSocket(url);
		lastMessages = new ArrayList<String>();
		
		ws.setHandler(new IWebSocketHandler() {
			
			@Override
			public void onOpen(WebSocket webSocket) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMessage(WebSocket webSocket, MessageEvent event) {
				lastMessages.add(event.getData());				
				
			}
			
			@Override
			public void onError(WebSocket webSocket) {
				lastMessages.add( "Exception occured ...\n" + webSocket + "\n" );
			}
			
			@Override
			public void onClose(WebSocket webSocket) {
				lastMessages.add("@You have been disconnected@");				
			}
		});
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
		ws.send(message);
	}
	
}
