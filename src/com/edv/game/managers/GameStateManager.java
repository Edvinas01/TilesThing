package com.edv.game.managers;

import java.util.Stack;

import com.edv.game.main.Game;
import com.edv.game.states.EditorState;
import com.edv.game.states.GameState;
import com.edv.game.states.PlayState;
import com.edv.game.states.TestState;

/**
 * Manages all the game states and transistions.
 * 
 * @author Edvinas
 *
 */
public class GameStateManager {

	// Avaliable game state list.
	public static final int TEST_STATE = -2;
	public static final int EDITOR_STATE = -1;
	public static final int MENU_STATE = 0;
	public static final int PLAY_STATE = 1;

	// Game container.
	private Game game;

	// Stack of current game states.
	private Stack<GameState> gameStates;

	/**
	 * Initialize the manager and load first game state.
	 * 
	 * @param game
	 *            - game container.
	 */
	public GameStateManager(Game game) {

		this.game = game;

		gameStates = new Stack<>();

		pushState(TEST_STATE);
	}

	public void update(float dt) {

		gameStates.peek().update(dt);
	}

	public void render() {

		gameStates.peek().render();
	}

	/**
	 * Get a new game state.
	 * 
	 * @param state
	 *            - which state to load.
	 * @return loaded game state.
	 */
	private GameState getState(int state) {

		if (state == TEST_STATE) {

			return new TestState(this);
		}
		if (state == EDITOR_STATE) {

			return new EditorState(this);
		}
		if (state == MENU_STATE) {

			// return new MenuState(this);
		}

		if (state == PLAY_STATE) {

			return new PlayState(this);
		}
		return null;
	}

	/**
	 * Set a new state and pop the current one.
	 * 
	 * @param state
	 *            - new state to set.
	 */
	public void setState(int state) {

		popState();
		pushState(state);
	}

	/**
	 * Add a new game state to the stack.
	 * 
	 * @param state
	 *            - new game state to be added.
	 */
	public void pushState(int state) {

		gameStates.push(getState(state));
	}

	/**
	 * Remove the current game state.
	 */
	public void popState() {

		GameState state = gameStates.pop();
		state.dispose();
	}

	/**
	 * @return game container.
	 */
	public Game getGame() {

		return game;
	}
}
