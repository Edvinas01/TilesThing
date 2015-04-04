package com.edv.game.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.edv.game.main.Game;
import com.edv.game.util.GameMath;
import com.edv.game.util.Vector2i;

// OLD
public class GameMap {

	public static String SAVE_TEXT = "";

	public static int OVERHEAD = 5;

	public static int TILE_SIZE = 32;
	public static int HALF_TILE = TILE_SIZE / 2;

	private String name;

	private int height;
	private int width;

	private OrthographicCamera camera;

	private byte[][] tiles;

	/**
	 * Call when loading a existing game map.
	 * 
	 * @param name
	 *            - name of the game map to be loaded.
	 * @param game
	 *            - game container.
	 */
	public GameMap(String name, Game game) {

		this.name = name;
		this.camera = game.getCamera();

		load(name);
	}

	/**
	 * Call when creating a new gameMap.
	 * 
	 * @param name
	 *            - new map name.
	 * @param height
	 *            - new map height in tiles.
	 * @param width
	 *            - new map widht in tiles.
	 * @param game
	 *            - game container.
	 */
	public GameMap(String name, int height, int width, Game game) {

		this.name = name;
		this.height = height;
		this.width = width;

		this.camera = game.getCamera();

		tiles = new byte[height][width];

		for (int i = 0; i < height; i++) {

			for (int j = 0; j < width; j++) {

				tiles[i][j] = -1;
			}
		}
	}

	public void render(SpriteBatch sb) {

		Vector2i start = startRender();
		Vector2i end = endRender();

		sb.begin();

		for (int y = start.y; y < end.y; y++) {

			for (int x = start.x; x < end.x; x++) {

				if (tiles[y][x] != -1) {

					Sprite sprite = Game.resources.getSpriteSheet("test_sheet")
							.getSprite(tiles[y][x]);

					sb.draw(sprite, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE,
							TILE_SIZE);
				}
			}
		}

		sb.draw(Game.resources.getTexture("grid"), -32, -32);

		sb.end();

		start = null;
		end = null;
	}

	public void update(float dt) {

	}

	public void setTile(int xWorld, int yWorld, int id) {

		int y = yWorld / TILE_SIZE;
		int x = xWorld / TILE_SIZE;

		if (y >= 0 && y < height && x >= 0 && x < width && id > -128
				&& id < 128) {

			tiles[y][x] = (byte) id;
		}
	}

	public void generateMapLimits(World world) {

		Vector2[] verts = new Vector2[4];

		verts[0] = new Vector2(0, 0);
		verts[1] = new Vector2(0, GameMath.toMeters(height * TILE_SIZE));
		verts[2] = new Vector2(GameMath.toMeters(width * TILE_SIZE),
				GameMath.toMeters(height * TILE_SIZE));
		verts[3] = new Vector2(GameMath.toMeters(width * TILE_SIZE), 0);

		ChainShape shape = new ChainShape();

		shape.createLoop(verts);

		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;

		Body body = world.createBody(def);

		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;

		body.createFixture(fixture);
	}

	public void generateCollisions(World world) {

		generateMapLimits(world);

		for (int y = 0; y < height; y++) {

			for (int x = 0; x < width; x++) {

				if (tiles[y][x] != -1) {

					BodyDef def = new BodyDef();
					def.position.set(
							GameMath.toMeters(x * TILE_SIZE + TILE_SIZE / 2),
							GameMath.toMeters(y * TILE_SIZE + TILE_SIZE / 2));
					def.type = BodyType.StaticBody;

					Body body = world.createBody(def);

					PolygonShape shape = new PolygonShape();
					shape.setAsBox(GameMath.toMeters(TILE_SIZE / 2),
							GameMath.toMeters(TILE_SIZE / 2));

					FixtureDef fixture = new FixtureDef();
					fixture.shape = shape;

					body.createFixture(fixture);
				}
			}
		}
	}

	public void save(String name) {

		Map<String, Object> data = new HashMap<>();

		data.put("name", this.name);
		data.put("height", this.height);
		data.put("width", this.width);
		data.put("tiles", this.tiles);

		String dir = "./res/levels/" + name + "/";

		new File(dir).mkdirs();

		try {

			ObjectOutputStream stream = new ObjectOutputStream(
					new FileOutputStream(dir + name + ".map"));

			stream.writeObject(data);

			stream.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

		data.clear();
	}

	@SuppressWarnings("unchecked")
	public void load(String name) {

		Map<String, Object> data = new HashMap<>();

		String dir = "./res/levels/" + name + "/";

		try {

			ObjectInputStream stream = new ObjectInputStream(
					new FileInputStream(dir + name + ".map"));

			data = (HashMap<String, Object>) stream.readObject();

			stream.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		this.name = (String) data.get("name");
		this.height = (int) data.get("height");
		this.width = (int) data.get("width");
		this.tiles = (byte[][]) data.get("tiles");

		data.clear();
	}

	public void dispose() {

		Game.resources.removeSpriteSheet("test_sheet");
		tiles = null;
	}

	@Override
	public String toString() {

		String map = "";

		for (int i = 0; i < height; i++) {

			for (int j = 0; j < width; j++) {

				map += String.format("[%2d]", tiles[i][j]);
			}
			map += "\n";
		}

		return map;
	}

	public int getHeight() {

		return height;
	}

	public int getWidth() {

		return width;
	}

	private Vector2i startRender() {

		int x = (int) (camera.position.x + (-Game.WIDTH / 2) * camera.zoom)
				/ TILE_SIZE;
		int y = (int) (camera.position.y + (-Game.HEIGHT / 2) * camera.zoom)
				/ TILE_SIZE;

		if (x < 0) {

			x = 0;
		}
		if (y < 0) {

			y = 0;
		}
		return new Vector2i(x, y);
	}

	private Vector2i endRender() {

		int x = (int) (camera.position.x + (+Game.WIDTH / 2) * camera.zoom)
				/ TILE_SIZE;
		int y = (int) (camera.position.y + (+Game.HEIGHT / 2) * camera.zoom)
				/ TILE_SIZE;

		if (x < 0) {

			x = 0;
		}
		if (y < 0) {

			y = 0;
		}
		if (x + 1 <= width) {

			x++;
		}
		if (y + 1 <= height) {

			y++;
		}
		if (x >= width || x + 1 == width) {

			x = width;
		}
		if (y >= height || x + 1 == width) {

			y = height;
		}

		return new Vector2i(x, y);
	}
}
