package com.edv.game.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.edv.game.managers.GameStateManager;
import com.edv.game.managers.ResourceManager;
import com.edv.game.util.GameCamera;

/**
 * Game container class.
 * 
 * @author Edvinas
 *
 */
public class Game implements ApplicationListener {

	public static String TITLE = "PIZZA TIME";

	public static int HEIGHT = 540; // 540
	public static int WIDTH = 960; // 960

	public static int OFFSET = 100;

	public static boolean FULLSCREEN = false;
	public static boolean RESIZABLE = false;

	public static int HUD_SCALE = 1;

	public static ResourceManager resources;

	private GameStateManager gsm;

	private SpriteBatch sb;
	private GameCamera camera;
	private OrthographicCamera hudCamera;

	private boolean debug;

	private String currentLevel;

	public static InputMultiplexer inputs;

	@Override
	public void create() {

		debug = false;

		sb = new SpriteBatch();

		camera = new GameCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);

		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, WIDTH, HEIGHT);

		inputs = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputs);

		resources = new ResourceManager();

		loadAssets();

		gsm = new GameStateManager(this);

		currentLevel = "";
	}

	@Override
	public void render() {

		Gdx.graphics.setTitle(TITLE + " | FPS: "
				+ Gdx.graphics.getFramesPerSecond());

		Gdx.gl.glClearColor(0.1f, 0.5f, 0.7f, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

		resources.removeAll();
	}

	@Override
	public void resize(int arg0, int arg1) {

	}

	public SpriteBatch getSpriteBatch() {

		return sb;
	}

	public GameCamera getCamera() {

		return camera;
	}

	public OrthographicCamera getHudCamera() {

		return hudCamera;
	}

	/**
	 * Get avaliable level list from the levels.txt file.
	 * 
	 * @return level names as array list.
	 */
	public ArrayList<String> getLevelList() {

		ArrayList<String> levels = new ArrayList<>();

		BufferedReader br;

		try {

			br = new BufferedReader(new FileReader(new File(
					".\\res\\levels\\levels.txt")));

			String line;

			while ((line = br.readLine()) != null) {

				levels.add(line);
			}
			br.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

		return levels;
	}

	/**
	 * Set debug mode.
	 * 
	 * @param debug
	 *            - debug mode value.
	 */
	public void setDebug(boolean debug) {

		this.debug = debug;
	}

	/**
	 * @return is debug mode enabled.
	 */
	public boolean isDebug() {

		return debug;
	}

	public String getCurrentLevel() {

		return currentLevel;
	}

	public void setCurrentLevel(String currentLevel) {

		this.currentLevel = currentLevel;
	}

	public void loadAssets() {

		resources.loadTexture("test", "./res/test.png");
		resources.loadTexture("grid", "./res/grid.png");

		// resources.loadAtlas("buttons", "./res/ui/buttons.atlas");
		resources.loadAtlas("ui", "./res/ui/ui_items.atlas");

		resources.loadTexture("ui_overlay", "./res/ui/ui_overlay.png");

		resources.loadSound("click_1", "./res/sounds/click_1.wav");
		resources.loadSound("click_2", "./res/sounds/click_2.wav");
		resources.loadSound("click_3", "./res/sounds/click_3.wav");
	}
}
