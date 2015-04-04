package com.edv.game.states;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.edv.game.main.Game;
import com.edv.game.managers.GameStateManager;
import com.edv.game.map.GameMap;
import com.edv.game.util.GameMath;

public class TestState extends GameState {

	@SuppressWarnings("unused")
	private int clickedButton = -1;

	private GameMap map;

	public TestState(GameStateManager gsm) {

		super(gsm);

		setupInputs();

		// Create a new map.
		/*
		map = new GameMap("test2", 32, 8, camera);
		map.dispose();
		*/
		
		// Load existing map.
		map = new GameMap("test2", camera);
	}

	@Override
	public void render() {

		camera.update();

		sb.setProjectionMatrix(camera.combined);

		sb.begin();

		map.render(sb);

		sb.end();

		map.renderGrid();
	}

	@Override
	public void update(float dt) {

		camera.update(dt);

		map.update(dt, GameMath.worldMouse(camera).x, GameMath.worldMouse(camera).y);

		clickedButton = -1;
	}

	@Override
	public void dispose() {

		map.dispose();
	}

	private void setupInputs() {

		Game.inputs.addProcessor(new InputProcessor() {

			@Override
			public boolean keyDown(int arg0) {

				return false;
			}

			@Override
			public boolean keyTyped(char arg0) {

				return false;
			}

			@Override
			public boolean keyUp(int arg0) {

				return false;
			}

			@Override
			public boolean mouseMoved(int arg0, int arg1) {

				return false;
			}

			@Override
			public boolean scrolled(int amount) {

				if (amount > 0 && camera.zoom < camera.getMaxZoom()) {

					camera.setZoomTimes(10);
					return true;
				}

				if (amount < 0 && camera.zoom > camera.getMinZoom()) {

					camera.setZoomTimes(-10);
					return true;
				}
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {

				clickedButton = -1;

				if (button == Buttons.LEFT) {

					clickedButton = Buttons.LEFT;
					return true;
				}
				if (button == Buttons.RIGHT) {

					clickedButton = Buttons.RIGHT;
					return true;
				}
				return false;
			}

			@Override
			public boolean touchDragged(int arg0, int arg1, int arg2) {

				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {

				return false; // TODO FIX UP STUFF IN THE INPUT TINGIE
			}
		});
	}
}
