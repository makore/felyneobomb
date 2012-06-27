package ua.felyne.game.client;

import ua.felyne.game.shared.IGame;

public class GameFactory {
	private static IGame _game = null;
	public static IGame create(String architecture) {
        if (architecture.compareTo("GWT") == 0) {
        	_game = new GameGWT();
        } else if (architecture.compareTo("Java") == 0) {
			/*
			 * XXX Para poder compilar en GWT, comentar la siguiente linea
			 */        	
        	//_game = new GameJava();
        }
        return _game;
    }
}
