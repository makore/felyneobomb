package ua.felyne.game.client;

import com.google.gwt.core.client.EntryPoint;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FelyneOBomb implements EntryPoint {
	public void onModuleLoad() {
		GameFactory.create("GWT");
	}
	
	public static void main(String argv[]) {
		GameFactory.create("Java");
	}
}
