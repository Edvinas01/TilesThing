package com.edv.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.edv.game.main.Game;
import com.edv.game.util.GameMath;

public class EditorUI {

	// Image button sizes.
	public static int IMG_BUTTON_SIZE = 40;

	// Size of the scroll bar sprites.
	public static int SPRITE_SIZE = 40;

	// Vars for table adjustment.
	public static int RIGHT_WIDTH = 70;
	public static int BOTTOM_HEIGHT = 40;

	// Bottom and right layout tables.
	private Table bottomTable;
	private Table rightTable;

	private Stage stage;
	private Skin uiSkin;

	// TODO fix up the button positions.
	private ImageButton toggleGrid;
	private ImageButton toggleEdit;
	private ImageButton regenCollisions;
	private ImageButton saveMap;
	private ImageButton mainMenu;
	private ImageButton resize;
	private ImageButton zoomIn;
	private ImageButton zoomOut;
	private ImageButton info;

	// Is the mouse over UI.
	private boolean over;

	// Clicked tiles ID on the scroll pane.
	private int clickedID;

	// Current map which is loaded.
	@SuppressWarnings("unused")
	private String currentMap;

	private InputBox saveBox;

	private String saveMapString;

	public EditorUI(String currentMap) {

		this.currentMap = currentMap;

		stage = new Stage(new ExtendViewport(Game.WIDTH, Game.HEIGHT));

		Game.inputs.addProcessor(stage);

		// Skin for the buttons. TODO add more skins for other UI elements.
		uiSkin = new Skin(Gdx.files.internal("./res/ui/ui_items.json"),
				Game.resources.getAltas("ui"));

		// Bottom layout table setup.
		bottomTable = new Table();
		bottomTable.setBackground(uiSkin.getDrawable("table"));
		bottomTable.setHeight(BOTTOM_HEIGHT);
		bottomTable.setWidth(Game.WIDTH - RIGHT_WIDTH);

		// Right layout table setup.
		rightTable = new Table();
		rightTable.setBackground(uiSkin.getDrawable("table"));
		rightTable.setX(Game.WIDTH - RIGHT_WIDTH);
		rightTable.setHeight(Game.HEIGHT);
		rightTable.setWidth(RIGHT_WIDTH);

		// Extra setuping.
		setupButtons();
		setubScrollBar();

		// Current buggy table.
		saveBox = new InputBox(uiSkin, Game.WIDTH / 2 - 80,
				Game.HEIGHT / 2 - 32, 160, 64, "default");
		saveBox.setBackground(uiSkin.getDrawable("table"));
		saveBox.setVisible(false);
		saveBox.setText(currentMap);

		// Finalizing setups.
		stage.addActor(saveBox.getTable());
		stage.addActor(rightTable);
		stage.addActor(bottomTable);
	}

	public void render(SpriteBatch sb) {

		sb.setProjectionMatrix(sb.getProjectionMatrix());

		sb.begin();

		sb.end();

		// stage.setDebugAll(true);
		stage.draw();
	}

	public void update(float dt) {

		stage.act(dt);

		float x = GameMath.screenMouse().x;
		float y = GameMath.screenMouse().y;

		// Set the save map string;
		saveMapString = saveBox.getSaveTextOnce();

		// Check if the mouse is over any UI element.
		over = false;

		for (Actor actor : stage.getActors()) {

			if (isOver(actor, x, y)) {

				over = true;
				break;
			}
		}
	}

	public void dispose() {

		stage.dispose();
		uiSkin.dispose();
	}

	private void setubScrollBar() {

		// Table inside the scroll pane.
		Table scrollTable = new Table();

		// Add the sprites from the level sheet. TODO use level specific sheet.
		for (int i = 0; i < Game.resources.getSpriteSheet("test_sheet").size(); i++) {

			ImageButtonStyle style = new ImageButtonStyle();

			style.up = uiSkin.getDrawable("button_basic_up");
			style.down = uiSkin.getDrawable("button_basic_down");

			style.imageUp = new SpriteDrawable(Game.resources.getSpriteSheet(
					"test_sheet").getSprite(i));
			style.imageUp.setMinHeight(SPRITE_SIZE);
			style.imageUp.setMinWidth(SPRITE_SIZE);

			ImageButton tile = new ImageButton(style);

			tile.setUserObject(i);
			tile.addListener(new ClickListener() {

				// Set the ID of the clicked tile.
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {

					Game.resources.getSound("click_2").play();
					clickedID = (int) tile.getUserObject();
					return true;
				}
			});

			scrollTable.add(tile).width(SPRITE_SIZE).height(SPRITE_SIZE).fill()
					.expand();
			scrollTable.row();
		}

		ScrollPane scroll = new ScrollPane(scrollTable, uiSkin);
		scroll.setFadeScrollBars(false);

		rightTable.setVisible(false);
		rightTable.add(scroll).expand().fill();
	}

	private void setupButtons() {

		// Grid button.
		toggleGrid = new ImageButton(new ImageButtonStyle());
		toggleGrid.getStyle().imageUp = uiSkin.getDrawable("grid");
		toggleGrid.getStyle().up = uiSkin.getDrawable("button_flat_up");
		toggleGrid.getStyle().down = uiSkin.getDrawable("button_flat_down");
		toggleGrid.getStyle().checked = uiSkin.getDrawable("button_flat_down");

		toggleGrid.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (button != Buttons.LEFT) {

					return false;
				}

				Game.resources.getSound("click_3").play();
				return true;
			}
		});

		// Edit button.
		toggleEdit = new ImageButton(new ImageButtonStyle());
		toggleEdit.getStyle().imageUp = uiSkin.getDrawable("edit");
		toggleEdit.getStyle().up = uiSkin.getDrawable("button_flat_up");
		toggleEdit.getStyle().down = uiSkin.getDrawable("button_flat_down");
		toggleEdit.getStyle().checked = uiSkin.getDrawable("button_flat_down");

		toggleEdit.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				Game.resources.getSound("click_1").play();
				if (button != Buttons.LEFT) {

					return false;
				}

				if (rightTable.isVisible()) {

					rightTable.setVisible(false);
					return true;
				}

				rightTable.setVisible(true);
				return true;
			}
		});

		// Save map button.
		saveMap = new ImageButton(new ImageButtonStyle());
		saveMap.getStyle().imageUp = uiSkin.getDrawable("save");
		saveMap.getStyle().up = uiSkin.getDrawable("button_flat_up");
		saveMap.getStyle().down = uiSkin.getDrawable("button_flat_down");

		saveMap.addListener(new ClickListener() {

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (button != Buttons.LEFT) {

					return false;
				}

				Game.resources.getSound("click_2").play();
				if (saveBox.isVisible()) {

					saveBox.setVisible(false);
					return false;
				}

				saveBox.setVisible(true);
				return true;
			}
		});

		// Regenerate collisions button.
		regenCollisions = new ImageButton(new ImageButtonStyle());
		regenCollisions.getStyle().imageUp = uiSkin.getDrawable("calculate");
		regenCollisions.getStyle().up = uiSkin.getDrawable("button_flat_up");
		regenCollisions.getStyle().down = uiSkin
				.getDrawable("button_flat_down");

		regenCollisions.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (button != Buttons.LEFT) {

					return false;
				}

				Game.resources.getSound("click_2").play();
				System.err.println("REGEN PENDING");
				return true;
			}
		});

		// TODO Resize the map.
		resize = new ImageButton(new ImageButtonStyle());
		resize.getStyle().imageUp = uiSkin.getDrawable("resize");
		resize.getStyle().up = uiSkin.getDrawable("button_flat_up");
		resize.getStyle().down = uiSkin.getDrawable("button_flat_down");

		resize.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (button != Buttons.LEFT) {

					return false;
				}

				Game.resources.getSound("click_2").play();
				return true;
			}
		});

		// Zoom in camera button.
		zoomIn = new ImageButton(new ImageButtonStyle());
		zoomIn.getStyle().imageUp = uiSkin.getDrawable("zoom_in");
		zoomIn.getStyle().up = uiSkin.getDrawable("button_flat_up");
		zoomIn.getStyle().down = uiSkin.getDrawable("button_flat_down");

		zoomIn.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (button != Buttons.LEFT) {

					return false;
				}

				Game.resources.getSound("click_2").play();
				return true;
			}
		});

		// Zoom out camera button
		zoomOut = new ImageButton(new ImageButtonStyle());
		zoomOut.getStyle().imageUp = uiSkin.getDrawable("zoom_out");
		zoomOut.getStyle().up = uiSkin.getDrawable("button_flat_up");
		zoomOut.getStyle().down = uiSkin.getDrawable("button_flat_down");

		zoomOut.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (button != Buttons.LEFT) {

					return false;
				}

				Game.resources.getSound("click_2").play();
				return true;
			}
		});

		// TODO info
		info = new ImageButton(new ImageButtonStyle());
		info.getStyle().imageUp = uiSkin.getDrawable("info");
		info.getStyle().up = uiSkin.getDrawable("button_flat_up");
		info.getStyle().down = uiSkin.getDrawable("button_flat_down");

		info.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (button != Buttons.LEFT) {

					return false;
				}

				Game.resources.getSound("click_2").play();
				System.err.println("INFO");
				return true;
			}
		});

		// TODO Enter main menu button.
		mainMenu = new ImageButton(new ImageButtonStyle());
		mainMenu.getStyle().imageUp = uiSkin.getDrawable("menu");
		mainMenu.getStyle().up = uiSkin.getDrawable("button_flat_up");
		mainMenu.getStyle().down = uiSkin.getDrawable("button_flat_down");

		mainMenu.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (button != Buttons.LEFT) {

					return false;
				}

				Game.resources.getSound("click_2").play();
				System.err.println("ENTER MAIN MENU");

				Gdx.app.exit();
				return true;
			}
		});

		// Add the buttons to stage.
		bottomTable.add(toggleGrid).left().height(IMG_BUTTON_SIZE)
				.width(IMG_BUTTON_SIZE);
		bottomTable.add(toggleEdit).left().height(IMG_BUTTON_SIZE)
				.width(IMG_BUTTON_SIZE);
		bottomTable.add(saveMap).left().height(IMG_BUTTON_SIZE)
				.width(IMG_BUTTON_SIZE);
		bottomTable.add(regenCollisions).width(IMG_BUTTON_SIZE)
				.height(IMG_BUTTON_SIZE);
		bottomTable.add(resize).width(IMG_BUTTON_SIZE).height(IMG_BUTTON_SIZE);
		bottomTable.add(zoomIn).width(IMG_BUTTON_SIZE).height(IMG_BUTTON_SIZE);
		bottomTable.add(zoomOut).width(IMG_BUTTON_SIZE).height(IMG_BUTTON_SIZE);
		bottomTable.add(info).width(IMG_BUTTON_SIZE).height(IMG_BUTTON_SIZE);
		bottomTable.add(mainMenu).width(IMG_BUTTON_SIZE)
				.height(IMG_BUTTON_SIZE).expand().left();
	}

	public boolean isOver(Actor actor, float x, float y) {

		if (actor.isVisible() == false) {

			return false;
		}

		return actor.getX() <= x && actor.getY() <= y
				&& actor.getX() + actor.getWidth() >= x
				&& actor.getY() + actor.getHeight() >= y;
	}

	public int getClickedID() {

		return clickedID;
	}

	public ImageButton getRegenCollisions() {

		return regenCollisions;
	}

	public ImageButton getToggleGrid() {

		return toggleGrid;
	}

	public ImageButton getToggleEdit() {

		return toggleEdit;
	}

	public ImageButton getSaveMap() {

		return saveMap;
	}

	public ImageButton getResize() {

		return resize;
	}

	public ImageButton getZoomIn() {

		return zoomIn;
	}

	public ImageButton getZoomOut() {

		return zoomOut;
	}

	public ImageButton getInfo() {

		return saveMap;
	}

	public ImageButton getMainMenu() {

		return mainMenu;
	}

	public String getSaveMapString() {

		return saveMapString;
	}

	public boolean isOver() {

		return over;
	}
}
