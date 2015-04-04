package com.edv.game.map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.edv.game.main.Game;
import com.edv.game.util.GameCamera;

public class GameMap {

	public static final int EXTRA_DRAW_WIDTH = 1;
	public static final int EXTRA_DRAW_HEIGHT = 1;
	
	private String name;

	private int width;
	private int height;

	private Manipulator manipulator;

	private GameCamera camera;
	private ShapeRenderer sr;

	private ArrayList<Chunk> chunksToDraw;

	/**
	 * Create a new GameMap.
	 * 
	 * @param name - name of the map.
	 * @param width - width in chunks.
	 * @param height - height in chunks.
	 * @param camera - game camera.
	 */
	public GameMap(String name, int width, int height, GameCamera camera) {
		
		this.name = name;
		
		this.width = width;
		this.height = height;
		
		this.camera = camera;
		
		sr = new ShapeRenderer();
		
		chunksToDraw = new ArrayList<>();
		
		Header header = new Header(0f, width, height);
		
		manipulator = new Manipulator(name, header);
		
		manipulator.open(name);
		manipulator.write(emptyWorld());
	}	
	/**
	 * Load an existing GameMap.
	 * 
	 * @param name - name of the map.
	 * @param camera - game camera.
	 */
	public GameMap(String name, GameCamera camera) {

		this.name = name;
		
		this.camera = camera;
		
		sr = new ShapeRenderer();
		
		chunksToDraw = new ArrayList<>();
		
		manipulator = new Manipulator(name);
		
		this.width = manipulator.getHeader().getWidth();
		this.height = manipulator.getHeader().getHeight();
		
		manipulator.open(name);
	}

	public void render(SpriteBatch sb) {

		for (Chunk chunk : chunksToDraw) {

			chunk.render(sb);
		}

		sb.draw(Game.resources.getTexture("grid"), -32, -32);
		
		Game.resources.getGameFont().draw(sb, name + ".map", 0, -32);
	}

	public void update(float dt) {

	}

	public void update(float dt, float entX, float entY) {

		int x = (int) (entX / Chunk.WIDTH / Tile.SIZE);
		int y = (int) (entY / Chunk.HEIGHT / Tile.SIZE);

		chunksToDraw.clear();
		for (int i = x - EXTRA_DRAW_WIDTH; i < x + EXTRA_DRAW_WIDTH + 1; i++) {

			for (int j = y - EXTRA_DRAW_HEIGHT; j < y + EXTRA_DRAW_HEIGHT + 1; j++) {

				if (isInBounds(i, j)) {

					chunksToDraw.add(manipulator.read(i, j));
				}
			}
		}
	}

	private Chunk[][] emptyWorld() {

		Chunk[][] chunks = new Chunk[width][height];

		for (int x = 0; x < width; x++) {

			for (int y = 0; y < height; y++) {

				chunks[x][y] = new Chunk(x * Chunk.WIDTH * Tile.SIZE, y	* Chunk.HEIGHT * Tile.SIZE);
			}
		}		
		return chunks;
	}

	public boolean isInBounds(int x, int y) {

		return 0 <= x && x < width && 0 <= y && y < height;
	}

	public void dispose() {

		chunksToDraw.clear();
		sr.dispose();
		manipulator.dispose();
	}

	public void renderGrid() {

		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeType.Line);

		// Draw chunk grid.
		sr.setColor(0.8f, 0.8f, 0.8f, 1f);
		for (int x = 0; x < width; x++) {

			for (int y = 0; y < height; y++) {

				sr.rect(x * Chunk.WIDTH * Tile.SIZE, 
						y * Chunk.HEIGHT * Tile.SIZE, 
						Chunk.WIDTH * Tile.SIZE,
						Chunk.HEIGHT * Tile.SIZE);
			}
		}

		// Draw bounds.
		sr.setColor(0.8f, 0f, 0f, 1f);

		sr.rect(0, 0, width * Chunk.WIDTH * Tile.SIZE, height * Chunk.HEIGHT
				* Tile.SIZE);

		sr.end();
	}
}
