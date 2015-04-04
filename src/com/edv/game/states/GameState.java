package com.edv.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.edv.game.main.Game;
import com.edv.game.managers.GameStateManager;
import com.edv.game.util.GameCamera;

/**
 * Main game state class
 * 
 * @author Edvinas
 *
 */
public abstract class GameState {

	protected GameStateManager gsm;

	protected Game game;

	protected SpriteBatch sb;

	protected GameCamera camera;
	protected OrthographicCamera hudCamera;

	protected GameState(GameStateManager gsm) {

		this.gsm = gsm;

		game = gsm.getGame();

		sb = game.getSpriteBatch();
		camera = game.getCamera();
		hudCamera = game.getHudCamera();
	}

	public abstract void render();

	public abstract void update(float dt);

	public abstract void dispose();
}
