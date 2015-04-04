package com.edv.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

/**
 * Sprite sheet based on atlas and sprite indexes.
 * 
 * @author Edvinas
 *
 */
public class SpriteSheet {

	// Atlas for the sprites.
	private TextureAtlas atlas;

	// Extracted sprites from the atlas ready for drawing.
	private Sprite[] sprites;

	public SpriteSheet(String dir) {

		atlas = new TextureAtlas(Gdx.files.internal(dir));

		sprites = new Sprite[atlas.getRegions().size];

		// Create sprite array.
		int index = 0;
		for (AtlasRegion region : atlas.getRegions()) {

			sprites[index++] = new Sprite(region);
		}
	}

	public TextureAtlas getAtlas() {

		return atlas;
	}

	public Sprite[] getSprites() {

		return sprites;
	}

	public Sprite getSprite(int index) {

		return sprites[index];
	}

	public int size() {

		return sprites.length;
	}

	public void dispose() {

		sprites = null;
		atlas.dispose();
	}
}
