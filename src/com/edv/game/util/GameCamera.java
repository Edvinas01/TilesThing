package com.edv.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Camera for the game world.
 * 
 * @author Edvinas
 *
 */
public class GameCamera extends OrthographicCamera {

	public static float LERP = 0.01f;

	public float targetZoom = 1f;
	public float t = 0.02f;

	// Maximum and minimum zoom values.
	private float maxZoom;
	private float minZoom;

	// Zoom amount per single tick.
	private float zoomAmount;

	// Camera movement speed.
	private float cameraSpeed;

	// How many times to zoom after mouse wheel has changed (simulates smooth
	// zoom).
	private int zoomTimes;

	public GameCamera() {

		// Defaults.
		maxZoom = 1.5f;
		minZoom = 0.5f;

		zoomAmount = 0.025f;

		zoomTimes = 0;

		cameraSpeed = 500;
	}

	public void update(float dt) {

		handleZoom(dt);
		handleMovement(dt);
	}

	public float getMaxZoom() {

		return maxZoom;
	}

	public void setMaxZoom(float maxZoom) {

		this.maxZoom = maxZoom;
	}

	public float getMinZoom() {

		return minZoom;
	}

	public void setMinZoom(float minZoom) {

		this.minZoom = minZoom;
	}

	public float getZoomAmount() {

		return zoomAmount;
	}

	public void setZoomAmount(float zoomAmount) {

		this.zoomAmount = zoomAmount;
	}

	public float getCameraSpeed() {

		return cameraSpeed;
	}

	public void setCameraSpeed(float cameraSpeed) {

		this.cameraSpeed = cameraSpeed;
	}

	public int getZoomTimes() {

		return zoomTimes;
	}

	public void setZoomTimes(int zoomTimes) {

		this.zoomTimes = zoomTimes;
	}

	/**
	 * Handle camera zooming.
	 * 
	 * @param dt
	 *            - delta time.
	 */
	private void handleZoom(float dt) {

		// Zoom in.
		if (zoomTimes < 0 && zoom > minZoom) {

			zoom -= zoomAmount;
			zoomTimes++;
		}
		// Zoom out.
		if (zoomTimes > 0 && zoom < maxZoom) {

			zoom += zoomAmount;
			zoomTimes--;
		}
	}

	/**
	 * Move the camere using arrow keys.
	 * 
	 * @param dt
	 *            - delta time.
	 */
	private void handleMovement(float dt) {

		if (Gdx.input.isKeyPressed(Keys.UP)) {

			position.y = (position.y + cameraSpeed * dt);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {

			position.y = position.y - cameraSpeed * dt;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {

			position.x = position.x + cameraSpeed * dt;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {

			position.x = position.x - cameraSpeed * dt;
		}
	}
}
