package ua.felyne.game.shared;

import java.util.Stack;

/**
 * Estadisticas, Fin, Juego, Menu, Opciones, Pausa, UnirJugador
 * @author makore
 *
 */
public class StateContext {
	private Stack<State> _states;
	
	public StateContext() {
		_states = new Stack<State>();
	}
		
	public void enter(State state) {
		state.setStateContext(this);
		_states.add(state);		
	}

	public void leave() {
		_states.pop();
	}
	
	public State getState() {
		return _states.peek();
	}
}
