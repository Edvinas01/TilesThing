package com.edv.game.map;

import java.nio.ByteBuffer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.edv.game.main.Game;

public class Chunk {

	public static final int WIDTH = 4;
	public static final int HEIGHT = 4;

	public static final int SIZE = WIDTH * HEIGHT * Tile.BYTE_COUNT;

	private Tile[][] tiles;

	private int chunkX;
	private int chunkY;

	public Chunk() {

		tiles = new Tile[WIDTH][HEIGHT];
	}

	// Create empty chunk.
	public Chunk(int chunkX, int chunkY) {

		this.chunkX = chunkX;
		this.chunkY = chunkY;

		fill((short) 1, (short) 100, true);
	}

	public void render(SpriteBatch sb) {

		for (int x = 0; x < WIDTH; x++) {

			for (int y = 0; y < HEIGHT; y++) {

				switch (tiles[x][y].getId()) {

				case 0:

					break;
				default:

					sb.draw(Game.resources.getTexture("test"),
							chunkX + x * Tile.SIZE,
							chunkY + y * Tile.SIZE);
					break;
				}
			}
		}
	}

	public void fill(short id, short health, boolean solid) {

		tiles = new Tile[WIDTH][HEIGHT];

		for (int x = 0; x < WIDTH; x++) {

			for (int y = 0; y < HEIGHT; y++) {

				tiles[x][y] = new Tile(id, health, solid);
			}
		}
	}

	public Tile getTile(int x, int y) {

		return tiles[x][y];
	}

	public int getChunkX() {

		return chunkX;
	}

	public void setChunkX(int chunkX) {

		this.chunkX = chunkX;
	}

	public int getChunkY() {

		return chunkY;
	}

	public void setChunkY(int chunkY) {

		this.chunkY = chunkY;
	}

	public void save(ByteBuffer buffer) {

		for (int x = 0; x < WIDTH; x++) {

			for (int y = 0; y < HEIGHT; y++) {

				tiles[x][y].save(buffer);
			}
		}
	}

	public static Chunk load(ByteBuffer buffer) {

		Chunk chunk = new Chunk();

		for (int x = 0; x < WIDTH; x++) {

			for (int y = 0; y < HEIGHT; y++) {

				chunk.tiles[x][y] = Tile.load(buffer);
			}
		}
		return chunk;
	}
}
