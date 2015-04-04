package com.edv.game.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Game launcher class.
 * 
 * @author Edvinas
 *
 */
public class GameMain {

	public static void main(String[] args) {

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

		// Initialize game configs.
		cfg.title = Game.TITLE;
		cfg.resizable = Game.RESIZABLE;
		cfg.fullscreen = Game.FULLSCREEN;
		cfg.height = Game.HEIGHT;
		cfg.width = Game.WIDTH;

		new LwjglApplication(new Game(), cfg);
	}
}
