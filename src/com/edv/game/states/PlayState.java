package com.edv.game.states;

import com.edv.game.managers.GameStateManager;

/**
 * Main game state which will load the level and initialize level resources.
 * 
 * @author Edvinas
 * 
 */
public class PlayState extends GameState {

	// Current game level.
	// private Level gameLevel;

	// Name of the level. TODO GET THE LEVEL FROM MENU
	private String levelName = "test01";

	public PlayState(GameStateManager gsm) {

		super(gsm);

		if (gsm.getGame().getLevelList().contains(levelName) == false) {

			// TODO RETURN TU MENU WITH AN ERROR
			System.err.println("Level: \"" + levelName + "\" does not exist.");
		}

		// gameLevel = new Level(gsm, levelName);
	}

	@Override
	public void render() {

		// gameLevel.render(sb);
	}

	@Override
	public void update(float dt) {

		// gameLevel.update(dt);
	}

	@Override
	public void dispose() {

		// gameLevel.dispose();
	}
}
