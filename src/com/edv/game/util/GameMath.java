package com.edv.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.edv.game.main.Game;

/**
 * Helper class with game math stuff.
 * 
 * @author Edvinas
 * 
 */
public class GameMath {

	// Box2D pixels per meter ratio.
	public static int PPM = 100;

	/**
	 * Convert pixels to meters.
	 * 
	 * @param pixels
	 *            - pixel value.
	 * @return pixels as meters.
	 */
	public static float toMeters(float pixels) {

		return pixels / PPM;
	}

	/**
	 * Convert meters to pixels.
	 * 
	 * @param meters
	 *            - meter value.
	 * @return meters as pixels.
	 */
	public static float toPixels(float meters) {

		return meters * PPM;
	}

	/**
	 * Converts degrees to radians.
	 * 
	 * @param degrees
	 *            - degrees value.
	 * @return degress as radians.
	 */
	public static float toRadians(float degrees) {

		return degrees * (float) Math.PI / 180;
	}

	/**
	 * Converts radians to degrees.
	 * 
	 * @param radians
	 *            - radian value.
	 * @return radians as degrees.
	 */
	public static float toDegress(float radians) {

		return radians * 180 / (float) Math.PI;
	}

	/**
	 * Get the coordinates of the mouse on the world.
	 * 
	 * @param camera
	 *            - main game camera.
	 * @return mouse coordinates on the world.
	 */
	public static Vector2 worldMouse(OrthographicCamera camera) {

		float x = camera.position.x + (Gdx.input.getX() - Game.WIDTH / 2)
				* camera.zoom;
		float y = camera.position.y + (-Gdx.input.getY() + Game.HEIGHT / 2)
				* camera.zoom;

		return new Vector2(x, y);
	}

	/**
	 * Get the coordinates of the mouse on the box2d world.
	 * 
	 * @param camera
	 *            - main game camera.
	 * @return mosue coordinates on the Box2d world.
	 */
	public static Vector2 b2dMouse(OrthographicCamera camera) {

		float x = camera.position.x + (Gdx.input.getX() - Game.WIDTH / 2)
				* camera.zoom;
		float y = camera.position.y + (-Gdx.input.getY() + Game.HEIGHT / 2)
				* camera.zoom;

		return new Vector2(x / PPM, y / PPM);
	}

	/**
	 * Is an object visible within the camera.
	 * 
	 * @param x
	 *            - objects x coordinate.
	 * @param y
	 *            - objects y coordinate.
	 * @param camera
	 *            - camera of the game world.
	 * @return is the object visible.
	 */
	public static boolean isVisible(int x, int y, OrthographicCamera camera) {

		float upX = camera.position.x + (-Game.WIDTH / 2 - Game.OFFSET)
				* camera.zoom;
		float upY = camera.position.y + (+Game.HEIGHT / 2 + Game.OFFSET)
				* camera.zoom;

		float downX = camera.position.x + (+Game.WIDTH / 2 + Game.OFFSET)
				* camera.zoom;
		float downY = camera.position.y + (-Game.HEIGHT / 2 - Game.OFFSET)
				* camera.zoom;

		if (upX <= x && upY >= y && downX >= x && downY <= y) {

			return true;
		}
		return false;
	}

	/**
	 * @return mouse coordinates on the screen.
	 */
	public static Vector2 screenMouse() {

		return new Vector2(Gdx.input.getX(), -Gdx.input.getY() + Game.HEIGHT);
	}

	// https://keithmaggio.wordpress.com/2011/02/15/math-magician-lerp-slerp-and-nlerp/
	public static Vector2 lerp(Vector2 start, Vector2 end, float precentage) {

		return start.set(start.x + precentage * (end.x - start.x), start.y
				+ precentage * (end.y - start.y));
	}
}
