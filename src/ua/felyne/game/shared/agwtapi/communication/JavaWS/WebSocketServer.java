package ua.felyne.game.shared.agwtapi.communication.JavaWS;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

public class WebSocketServer extends org.java_websocket.WebSocketServer {

	private int number;
	private int count;
	private ArrayList<WebSocket> players = new ArrayList<WebSocket>();
	private ArrayList<String> lastmoves = new ArrayList<String>();
	private boolean started;
	
	public WebSocketServer(int number, int port) throws UnknownHostException {
		super(new InetSocketAddress(InetAddress.getByAddress(new byte[] {0,0,0,0}), port));
		this.number = number;
		count = 1;
		started = false;
	}

	public WebSocketServer(InetSocketAddress address, int number) throws UnknownHostException {
		super(address);
		this.number = number;
		count = 1;
		started = false;
	}
	
	@Override
	public void onClose(WebSocket ws, int code, String reason, boolean remote) {
		int id = 1;
		for (WebSocket con : connections()) {
			if(con == ws) {
				break;
			}
			id++;
		}
		count--;
		sendToAll("@left:" + id);
		System.out.println( ws + " has left the room!" );	
	}

	@Override
	public void onError(WebSocket ws, Exception ex) {
		sendToAll("@error:");
	}

	@Override
	public void onMessage(WebSocket ws, String message) {
		lastmoves.add(message);
		//System.out.println( ws + ": " + message );		
	}

	@Override
	public void onOpen(WebSocket ws, ClientHandshake cl) {

		try {
			if(started) {
				ws.send("@gameStarted:");
			}
			else {
				count++;
				if(count > number) {
					ws.send("@serverFull:");
					count--;
				}
				else {
					ws.send("@yourId:" + count);
					System.out.println( ws + " entered the room!" );
				}
			}
		} catch (NotYetConnectedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean complete() {
		if(count == number) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void startGame() {
		sendToAll("@startGame:"+number);
		started = true;
	}
	
	public void endGame() {
		sendToAll("@endGame:");
	}
	
	public void sendToAll( String text ) {
		Set<WebSocket> con = connections();
		synchronized ( con ) {
			for( WebSocket c : con ) {
				try {
					c.send( text );
				} catch (NotYetConnectedException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}	
	
	public ArrayList<String> getMoves() {
		ArrayList<String> copymoves = new ArrayList<String>(lastmoves);
		lastmoves.clear();
		return copymoves;
	}
}
