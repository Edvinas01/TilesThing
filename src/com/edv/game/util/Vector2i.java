package com.edv.game.util;

/**
 * Lightweight integer vector class.
 * 
 * @author Edvinas
 *
 */
public class Vector2i {

	public int x;
	public int y;

	public Vector2i(int x, int y) {

		this.x = x;
		this.y = y;
	}

	public Vector2i(float x, float y) {

		this.x = (int) x;
		this.y = (int) y;
	}

	public Vector2i() {

		this.x = 0;
		this.y = 0;
	}

	public String toString() {

		return "[" + x + " " + y + "]";
	}
}
