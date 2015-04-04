package com.edv.game.managers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.edv.game.util.SpriteSheet;

public class ResourceManager {

	public static int TILE_SIZE = 16;
	public static int HALF_TILE = TILE_SIZE / 2;
	public static int SHEET_ROWS = 8;
	public static int SHEET_COLS = 16;

	private HashMap<String, Texture> textures;
	private HashMap<String, Sound> sounds;
	private HashMap<String, Music> music;
	private HashMap<String, TextureAtlas> atlases;
	private HashMap<String, SpriteSheet> spriteSheets;

	private BitmapFont gameFont;

	public ResourceManager() {

		textures = new HashMap<>();
		sounds = new HashMap<>();
		music = new HashMap<>();
		atlases = new HashMap<>();
		spriteSheets = new HashMap<>();

		gameFont = new BitmapFont();
	}

	/* Textures */

	public void loadTexture(String key, String dir) {

		textures.put(key, new Texture(Gdx.files.internal(dir)));
	}

	public Texture getTexture(String key) {

		return textures.get(key);
	}

	public void removeTexture(String key) {

		Texture texture = textures.get(key);

		if (texture != null) {

			textures.remove(key);
			texture.dispose();
		}
	}

	/* Music */

	public void loadMusic(String key, String dir) {

		music.put(key, Gdx.audio.newMusic(Gdx.files.internal(dir)));
	}

	public Music getMusic(String key) {

		return music.get(key);
	}

	public void removeMusic(String key) {

		Music music = this.music.get(key);

		if (music != null) {

			this.music.remove(key);
			music.dispose();
		}
	}

	/* Sound */

	public void loadSound(String key, String dir) {

		sounds.put(key, Gdx.audio.newSound(Gdx.files.internal(dir)));
	}

	public Sound getSound(String key) {

		return sounds.get(key);
	}

	public void removeSound(String key) {

		Sound sound = sounds.get(key);

		if (sound != null) {

			sounds.remove(key);
			sound.dispose();
		}
	}

	/* Atlases */

	public void loadAtlas(String key, String dir) {

		atlases.put(key, new TextureAtlas(dir));
	}

	public TextureAtlas getAltas(String key) {

		return atlases.get(key);
	}

	public void removeAtlas(String key) {

		TextureAtlas atlas = atlases.get(key);

		if (atlas != null) {

			textures.remove(key);
			atlas.dispose();
		}
	}

	/* Other */

	public void removeAll() {

		for (Texture texture : textures.values()) {

			texture.dispose();
		}
		textures.clear();

		for (Music music : music.values()) {

			music.dispose();
		}
		music.clear();

		for (Sound sound : sounds.values()) {

			sound.dispose();
		}
		sounds.clear();

		for (TextureAtlas atlas : atlases.values()) {

			atlas.dispose();
		}
		atlases.clear();

		for (SpriteSheet sheet : spriteSheets.values()) {

			sheet.dispose();
		}
		spriteSheets.clear();

		gameFont.dispose();
	}

	/* Fonts */

	public BitmapFont getGameFont() {

		return gameFont;
	}

	/* Sprite Sheets */

	public void loadSpriteSheet(String key, String dir) {

		spriteSheets.put(key, new SpriteSheet(dir));
	}

	public void removeSpriteSheet(String key) {

		SpriteSheet sheet = spriteSheets.get(key);

		if (sheet != null) {

			spriteSheets.remove(key);
			sheet.dispose();
		}
	}

	public SpriteSheet getSpriteSheet(String key) {

		return spriteSheets.get(key);
	}
}
