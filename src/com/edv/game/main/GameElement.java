package com.edv.game.main;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameElement {

	public void render(SpriteBatch sb);

	public void update(float dt);

	public void dispose();
}
