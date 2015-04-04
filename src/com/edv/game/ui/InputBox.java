package com.edv.game.ui;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.edv.game.main.Game;

/**
 * A table with text input.
 * 
 * @author Edvinas
 *
 */
public class InputBox {

	// Offset on the corners.
	public static float OFFSET = 20;

	// The table.
	private Table table;

	// Table input.
	private TextField inputField;

	// Ok and Close buttons.
	private TextButton okButton;
	private TextButton closeButton;

	// The output of the table.
	private String saveText;

	/**
	 * Create a table.
	 * 
	 * @param skin
	 *            - skin used for buttons and the textfield. (USES DEFAULT).
	 * @param x
	 *            - x coordinate of the table.
	 * @param y
	 *            - y coordinate of the table.
	 * @param width
	 *            - width of the table.
	 * @param height
	 *            - height of the table.
	 * @param style
	 *            - style of the skin.
	 */
	public InputBox(Skin skin, float x, float y, float width, float height,
			String style) {

		table = new Table();

		inputField = new TextField("", skin);

		okButton = new TextButton("Ok", skin, style);
		okButton.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (button != Buttons.LEFT) {

					return false;
				}

				Game.resources.getSound("click_2").play();
				table.setVisible(false);

				if (inputField.getText().equals("")) {

					saveText = null;
					return false;
				}

				okButton.setChecked(true);

				saveText = inputField.getText();
				return true;
			}
		});

		closeButton = new TextButton("Close", skin, style);

		closeButton.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (button != Buttons.LEFT) {

					return false;
				}

				Game.resources.getSound("click_2").play();

				saveText = null;

				closeButton.setChecked(true);
				table.setVisible(false);

				return true;
			}
		});

		table.setX(x);
		table.setY(y);

		table.setWidth(width);
		table.setHeight(height);

		table.add(inputField).colspan(2).center().width(width - OFFSET)
				.expand().padRight(2);
		table.row();
		table.add(okButton).width(width / 2 - OFFSET / 2).right();
		table.add(closeButton).width(width / 2 - OFFSET / 2).left();
		table.pad(5);
	}

	/**
	 * Set the starting text of the table.
	 * 
	 * @param text
	 */
	public void setText(String text) {

		inputField.setText(text);
	}

	/**
	 * Get the save text and set it to null after receiving the output.
	 * 
	 * @return inputField output.
	 */
	public String getSaveTextOnce() {

		String temp = saveText;

		saveText = null;

		return temp;
	}

	/**
	 * Set background of the table.
	 * 
	 * @param background
	 *            - table background.
	 */
	public void setBackground(Drawable background) {

		table.setBackground(background);
	}

	/**
	 * @return Is the table set to visible.
	 */
	public boolean isVisible() {

		return table.isVisible();
	}

	public void setVisible(boolean visible) {

		table.setVisible(visible);
	}

	public TextButton getOkButton() {

		return okButton;
	}

	public TextButton getCloseButton() {

		return closeButton;
	}

	/**
	 * Get the table component. Use when adding to stage.
	 * 
	 * @return Table.
	 */
	public Table getTable() {

		return table;
	}
}
