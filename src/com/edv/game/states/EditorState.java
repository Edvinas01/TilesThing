package com.edv.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.edv.game.level.GameMap;
import com.edv.game.main.Game;
import com.edv.game.managers.GameStateManager;
import com.edv.game.ui.EditorUI;
import com.edv.game.util.GameMath;

public class EditorState extends GameState {

	// Render for the grid.
	private ShapeRenderer shapeRenderer;

	// Current game map.
	private GameMap gameMap;

	// Box2D world.
	private World world;

	// Specialized UI for the Editor.
	private EditorUI UI;

	// Mouse buttons (justPressed for the mouse workaround).
	private int clickedButton = -1;

	// Should the collisions be regenerated this tick.
	private boolean regenPending;

	// Box2D debug rendering vars.
	private Box2DDebugRenderer debugRender;
	private Matrix4 debugMatrix;

	public EditorState(GameStateManager gsm) {

		super(gsm);

		shapeRenderer = new ShapeRenderer();

		world = new World(new Vector2(0, -9.8f), true);

		// TODO load selected map.
		gameMap = new GameMap("test01", gsm.getGame()); // TODO assign sprite
														// sheet to map
		gameMap.generateCollisions(world);

		setupInputs();

		// TODO load level specific sheet.
		Game.resources.loadSpriteSheet("test_sheet",
				"./res/sheets/test_sheet.atlas");
		UI = new EditorUI("test01");

		// Box2D debug rendering.
		debugRender = new Box2DDebugRenderer();
		debugMatrix = new Matrix4();
	}

	/**
	 * TEMP method for collision testing. TODO ENTITY SYSTEM.
	 */
	public void spawnBoxes() {

		if (clickedButton == Buttons.LEFT) {

			Vector2 point = GameMath.b2dMouse(camera);

			// TODO TESTING
			BodyDef def = new BodyDef();
			def.position.set(point.x, point.y);
			def.type = BodyType.DynamicBody;
			def.angle = 0;

			Body body = world.createBody(def);

			PolygonShape shape = new PolygonShape();
			shape.setAsBox(GameMath.toMeters(GameMap.TILE_SIZE / 2),
					GameMath.toMeters(GameMap.TILE_SIZE / 2));

			FixtureDef fixture = new FixtureDef();
			fixture.density = 1;
			fixture.friction = 1;
			fixture.restitution = 0.1f;
			fixture.shape = shape;

			body.createFixture(fixture);

			body.setUserData(new Sprite(Game.resources.getTexture("test")));
		}
	}

	/**
	 * TEMP method to work with collision boxes. TODO ENTITY SYSTEM.
	 */
	public void renderBoxes(SpriteBatch sb) {

		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);

		sb.begin();
		for (Body b : bodies) {

			if (b.getUserData() != null) {

				Sprite spr = (Sprite) b.getUserData();

				spr.setPosition(b.getPosition().x * GameMath.PPM
						- GameMap.TILE_SIZE / 2, b.getPosition().y
						* GameMath.PPM - GameMap.TILE_SIZE / 2);
				spr.setSize(GameMap.TILE_SIZE, GameMap.TILE_SIZE);

				spr.setOrigin(GameMap.TILE_SIZE / 2, GameMap.TILE_SIZE / 2);
				spr.setRotation(GameMath.toDegress(b.getAngle()));

				spr.draw(sb);
			}
		}
		sb.end();

		bodies.clear();
		bodies = null;
	}

	/**
	 * Render Box2D debug and the map grid using shapes.
	 */
	public void renderDebug() {

		shapeRenderer.setProjectionMatrix(camera.combined);

		shapeRenderer.begin(ShapeType.Line);

		shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.5f);

		for (int i = 0; i < gameMap.getWidth(); i++) {

			for (int j = 0; j < gameMap.getHeight(); j++) {

				if (GameMath.isVisible(i * GameMap.TILE_SIZE, j
						* GameMap.TILE_SIZE, camera)) {

					shapeRenderer.rect(i * GameMap.TILE_SIZE, j
							* GameMap.TILE_SIZE, GameMap.TILE_SIZE,
							GameMap.TILE_SIZE);
				}
			}
		}

		shapeRenderer.end();

		debugMatrix.set(camera.combined);
		debugMatrix.scale(GameMath.PPM, GameMath.PPM, 1);

		debugRender.render(world, debugMatrix);
	}

	/**
	 * TEMP, removes all box2d bodies.
	 */
	public void removeAllBodies() {

		Array<Body> arr = new Array<>();

		world.getBodies(arr);

		for (Body b : arr) {

			if (b != null) {

				world.destroyBody(b);
				b.setUserData(null);
				b = null;
			}
		}

		arr.clear();
		arr = null;
	}

	@Override
	public void render() {

		camera.update();

		// First use the world camera.
		sb.setProjectionMatrix(camera.combined);

		// Render all things here.
		sb.begin();

		sb.end();

		// Render test boxes. TEMP.
		renderBoxes(sb);

		gameMap.render(sb);

		if (game.isDebug()) {

			renderDebug();
		}

		// Render the UI camera.
		sb.setProjectionMatrix(game.getHudCamera().combined);

		UI.render(sb);
	}

	@Override
	public void update(float dt) {

		camera.update(dt);

		gameMap.update(dt);

		UI.update(dt);

		// Save the map to a chosen one.
		if (UI.getSaveMapString() != null) {

			gameMap.save(UI.getSaveMapString());

			System.err.println(UI.getSaveMapString() + " saved.");
		}

		// Set game debug accordingly to UI.
		game.setDebug(UI.getToggleGrid().isChecked());

		// If regenerate collisions button was pressed, do so.
		if (UI.getRegenCollisions().isPressed() && regenPending == false) {

			regenPending = true;

			removeAllBodies();
			gameMap.generateCollisions(world);

		} else if (UI.getRegenCollisions().isPressed() == false) {

			regenPending = false;
		}

		// Enable and disable the edit mode.
		if (UI.getToggleEdit().isChecked() && !UI.isOver()) {

			// Add tiles by stream.
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {

				gameMap.setTile((int) GameMath.worldMouse(camera).x,
						(int) GameMath.worldMouse(camera).y, UI.getClickedID());
			}

			// Remove tiles one by one.
			if (clickedButton == Buttons.RIGHT) {

				gameMap.setTile((int) GameMath.worldMouse(camera).x,
						(int) GameMath.worldMouse(camera).y, -1);
			}

		} else {

			// If the edit mode is disabled, do TEMP box spawning.
			spawnBoxes();
		}

		// Zoom zoom with buttons.
		if (UI.getZoomIn().isPressed()) {

			camera.setZoomTimes(-10);
		}
		if (UI.getZoomOut().isPressed()) {

			camera.setZoomTimes(10);
		}

		world.step(dt, 6, 2);

		// Renew the mouse button values.
		clickedButton = -1;
	}

	@Override
	public void dispose() {

		Game.resources.removeSpriteSheet("test_sheet");

		UI.dispose();
		world.dispose();
		gameMap.dispose();
	}

	/**
	 * Do input setup for the editor mode, it should be simillar in the main
	 * game as well.
	 * 
	 * Currently handles zooming in and out - adjusts the camera.
	 * 
	 * Handles left and right mouse clicks aka mouseJustClicked (really
	 * hackish).
	 */
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

				if (UI.isOver()) {

					return false;
				}
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

				if (UI.isOver()) {

					return false;
				}
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

				if (UI.isOver()) {

					return false;
				}
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
