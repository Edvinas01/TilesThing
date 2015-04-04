package com.edv.game.map;

import java.nio.ByteBuffer;

public class Tile {

	public static final int SIZE = 16;
	public static final int BYTE_COUNT = 5;

	private short id;
	private short health;
	private boolean solid;

	public Tile() {

		this.id = 0;
		this.health = 100;
		this.solid = false;
	}

	public Tile(short id, short health, boolean solid) {

		this.id = id;
		this.health = health;
		this.solid = solid;
	}

	public short getId() {

		return id;
	}

	public void setId(short id) {

		this.id = id;
	}

	public short getHealth() {

		return health;
	}

	public void setHealth(short health) {

		this.health = health;
	}

	public boolean isSolid() {

		return solid;
	}

	public void setSolid(boolean solid) {

		this.solid = solid;
	}

	public void save(ByteBuffer buffer) {

		buffer.putShort(id);
		buffer.putShort(health);
		buffer.put(solid ? (byte) 1 : (byte) 0);
	}

	public static Tile load(ByteBuffer buffer) {

		Tile tile = new Tile();

		tile.id = buffer.getShort();
		tile.health = buffer.getShort();
		tile.solid = buffer.get() == 1;

		return tile;
	}
}
