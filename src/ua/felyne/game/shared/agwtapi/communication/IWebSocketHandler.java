package ua.felyne.game.shared.agwtapi.communication;

import ua.felyne.game.shared.agwtapi.communication.GWT.MessageEvent;
import ua.felyne.game.shared.agwtapi.communication.GWT.WebSocket;

public interface IWebSocketHandler {

	  public void onOpen(WebSocket webSocket);

	  public void onClose(WebSocket webSocket);

	  public void onError(WebSocket webSocket);

	  public void onMessage(WebSocket webSocket, MessageEvent event);
}