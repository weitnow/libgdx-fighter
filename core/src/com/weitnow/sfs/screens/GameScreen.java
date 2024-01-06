package com.weitnow.sfs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.weitnow.sfs.SFS;
import com.weitnow.sfs.objects.Fighter;
import com.weitnow.sfs.ressources.Assets;
import com.weitnow.sfs.ressources.GlobalVariables;

import java.util.Locale;

public class GameScreen implements Screen, InputProcessor {
    private final SFS game;
    private final ExtendViewport viewport;

    // game

    private GlobalVariables.Difficutly difficulty = GlobalVariables.Difficutly.EASY;

    // rounds

    private int roundsWon = 0, roundsLost = 0;

    private static final float MAX_ROUND_TIME = 99.99f;
    private float roundTimer = MAX_ROUND_TIME;

    // fonts
    private BitmapFont smallFont, mediumFont, largeFont;
    private static final Color DEFAULT_FONT_COLOR = Color.WHITE;

    // HUD
    private static final Color HEALTH_BAR_COLOR = Color.RED;
    private static final Color HEALTH_BAR_BACKGROUND_COLOR = GlobalVariables.GOLD;

    // background/ring
    private Texture backgroundTexture;
    private Texture frontRopesTexture;

    private static final float RING_MIN_X = 7f;
    private static final float RING_MAX_X = 60f;
    private static final float RING_MIN_Y = 4f;
    private static final float RING_MAX_Y = 22f;
    private static final float RING_SLOPE = 3.16f;

    // fighters
    private static final float PLAYER_START_POSITION_X = 16f;
    private static final float OPPONENT_START_POSITION_X = 51f;
    private static final float FIGHTER_START_POSITION_Y = 15f;
    private static final float FIGHTER_CONTACT_DISTANCE_X = 7.5f;
    private static final float FIGHTER_CONTACT_DISTANCE_Y = 1.5f;

    public GameScreen(SFS game) {
        this.game = game;

        //set up the viewport
        viewport = new ExtendViewport(GlobalVariables.WORLD_WIDTH, GlobalVariables.MIN_WORLD_HEIGHT,
                GlobalVariables.WORLD_WIDTH, 0);

        // create the game area
        createGameArea();

        // set up the fonts
        setUpFonts();

        // get the fighters ready
        game.player.getReady(PLAYER_START_POSITION_X, FIGHTER_START_POSITION_Y);
        game.opponent.getReady(OPPONENT_START_POSITION_X, FIGHTER_START_POSITION_Y);
    }

    private void createGameArea() {
        // get the ring textures from the asset manager
        backgroundTexture = game.assets.manager.get(Assets.BACKGROUND_TEXTURE);
        frontRopesTexture = game.assets.manager.get(Assets.FRONT_ROPES_TEXTURE);
    }

    private void setUpFonts() {
        smallFont = game.assets.manager.get(Assets.SMALL_FONT);
        smallFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        smallFont.setColor(DEFAULT_FONT_COLOR);
        smallFont.setUseIntegerPositions(false);


        mediumFont = game.assets.manager.get(Assets.MEDIUM_FONT);
        mediumFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        mediumFont.setColor(DEFAULT_FONT_COLOR);
        mediumFont.setUseIntegerPositions(false);

        largeFont = game.assets.manager.get(Assets.LARGE_FONT);
        largeFont.getData().setScale(GlobalVariables.WORLD_SCALE);
        largeFont.setColor(DEFAULT_FONT_COLOR);
        largeFont.setUseIntegerPositions(false);

    }

    @Override
    public void show() {
        // process user input
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // update the game
        update(delta);

        // set the sprite batch and the shape renderer to use the viewport's camera
        game.batch.setProjectionMatrix(viewport.getCamera().combined);
        game.shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        // begin drawing
        game.batch.begin();

        // draw the background
        game.batch.draw(backgroundTexture, 0, 0, backgroundTexture.getWidth() * GlobalVariables.WORLD_SCALE, backgroundTexture.getHeight() * GlobalVariables.WORLD_SCALE);

        // draw the fighters
        renderFighters();

        // draw the front ropes
        game.batch.draw(frontRopesTexture, 0, 0, frontRopesTexture.getWidth() * GlobalVariables.WORLD_SCALE,
                frontRopesTexture.getHeight() * GlobalVariables.WORLD_SCALE);


        // draw the HUD
        renderHUD();

        // end drawing
        game.batch.end();
    }

    private void renderFighters() {
        // use the y coordinates of the fighter's positions to determine which fighter to draw first
        if (game.player.getPosition().y > game.opponent.getPosition().y) {
            // draw player
            game.player.render(game.batch);

            //draw opponent
            game.opponent.render(game.batch);
        } else {
            //draw opponent
            game.opponent.render(game.batch);
            // draw player
            game.player.render(game.batch);
        }
    }

    private void renderHUD() {
        float HUDMargin = 1f;

        // draw the rounds won to lost
        smallFont.draw(game.batch, "WINS: " + roundsWon + " - " + roundsLost, HUDMargin, viewport.getWorldHeight() - HUDMargin);

        // draw the difficulty setting
        String text = "DIFFICULTY: ";
        switch (difficulty) {
            case EASY:
                text += "EASY";
                break;
            case MEDIUM:
                text += "MEDIUM";
                break;
            case HARD:
                text += "HARD";
                break;
        }
        smallFont.draw(game.batch, text, viewport.getWorldWidth() - HUDMargin, viewport.getWorldHeight() - HUDMargin, 0, Align.right, false);

        // set up the layout sizes and positions
        float healthBarPadding = 0.5f;
        float healthBarHeight = smallFont.getCapHeight() + healthBarPadding * 2f;
        float healthBarMaxWidth = 32f;
        float healthBarBackgroundPadding = 0.2f;
        float healthBarBackgroundHeight = healthBarHeight + healthBarBackgroundPadding * 2f;
        float healthBarBackgroundWidth = healthBarMaxWidth + healthBarBackgroundPadding * 2f;
        float healthBarBackgroundMarginTop = 0.8f;
        float healthBarBackgroundPositionY = viewport.getWorldHeight() - HUDMargin - smallFont.getCapHeight() - healthBarBackgroundMarginTop - healthBarBackgroundHeight;
        float healthBarPositionY = healthBarBackgroundPositionY + healthBarBackgroundPadding;
        float fighterNamePositionY = healthBarBackgroundPositionY + healthBarHeight - healthBarPadding;

        game.batch.end();
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // draw the player's health bar background
        game.shapeRenderer.setColor(HEALTH_BAR_BACKGROUND_COLOR);
        game.shapeRenderer.rect(HUDMargin, healthBarBackgroundPositionY, healthBarBackgroundWidth, healthBarBackgroundHeight);
        game.shapeRenderer.rect(viewport.getWorldWidth() - HUDMargin - healthBarBackgroundWidth, healthBarBackgroundPositionY, healthBarBackgroundWidth, healthBarBackgroundHeight);

        // draw the fighter health bar rectangles
        game.shapeRenderer.setColor(HEALTH_BAR_COLOR);
        float healthBarWidth = healthBarMaxWidth * game.player.getLife() / Fighter.MAX_LIFE;
        game.shapeRenderer.rect(HUDMargin + healthBarBackgroundPadding, healthBarPositionY, healthBarWidth, healthBarHeight);
        healthBarWidth = healthBarMaxWidth * game.opponent.getLife() / Fighter.MAX_LIFE;
        game.shapeRenderer.rect(viewport.getWorldWidth() - HUDMargin - healthBarBackgroundPadding - healthBarWidth,
                healthBarPositionY, healthBarWidth, healthBarHeight);

        game.shapeRenderer.end();
        game.batch.begin();

        // draw the fighter names
        smallFont.draw(game.batch, game.player.getName(), HUDMargin + healthBarBackgroundPadding + healthBarPadding,
                fighterNamePositionY);
        smallFont.draw(game.batch, game.opponent.getName(), viewport.getWorldWidth() - HUDMargin - healthBarBackgroundPadding - healthBarPadding,
                fighterNamePositionY, 0, Align.right, false);


        // draw the round timer
        mediumFont.draw(game.batch, String.format(Locale.getDefault(), "%02d", (int) roundTimer), viewport.getWorldWidth() / 2f, viewport.getWorldHeight() - HUDMargin, 0, Align.center, false);
    }

    private void update(float deltaTime) {
        game.player.update(deltaTime);
        game.opponent.update(deltaTime);

        // make sure the fighters are fading each other
        if (game.player.getPosition().x <= game.opponent.getPosition().x) {
            game.player.faceRight();
            game.opponent.faceLeft();
        } else {
            game.player.faceLeft();
            game.opponent.faceRight();
        }

        // keep the fighters within the bounds of the ring
        keepWithinRingBounds(game.player.getPosition());
        keepWithinRingBounds(game.opponent.getPosition());

        // check if the fighters are within contact distance
        if (areWithinContactDistance(game.player.getPosition(), game.opponent.getPosition())) {
            if (game.player.isAttackActive()) {
                // if the fighters are within contact distance and player is actively attacking, opponent gets hit
                game.opponent.getHit(Fighter.HIT_STRENGTH);
                System.out.println("opponent's life: " + game.opponent.getLife());

                // deactivate player's attack
                game.player.makeContact();

                // check if opponent has lost
                if (game.opponent.hasLost()) {
                    // if opponent hast lost, player wins
                    game.player.win();
                }
            }
        }
    }

    private void keepWithinRingBounds(Vector2 position) {
        if (position.y < RING_MIN_Y) {
            position.y = RING_MIN_Y;
        } else if (position.y > RING_MAX_Y) {
            position.y = RING_MAX_Y;
        }
        if (position.x < position.y / RING_SLOPE + RING_MIN_X ) {
            position.x = position.y / RING_SLOPE + RING_MIN_X;
        } else if (position.x > position.y / -RING_SLOPE + RING_MAX_X) {
            position.x = position.y / -RING_SLOPE + RING_MAX_X;
        }
    }

    private boolean areWithinContactDistance(Vector2 position1, Vector2 position2) {
        // determine if the positions are within the distance in which contact is possible
        float xDistance = Math.abs(position1.x - position2.x);
        float yDistance = Math.abs(position1.y - position2.y);
        return xDistance <= FIGHTER_CONTACT_DISTANCE_X && yDistance <= FIGHTER_CONTACT_DISTANCE_Y;
    }

    @Override
    public void resize(int width, int height) {
        // update the viewport with the new screen size
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        // check if player has pressed a movement key
        if (keycode == Input.Keys.A) {
            game.player.moveLeft();
        } else if (keycode == Input.Keys.D) {
            game.player.moveRight();
        }
        if (keycode == Input.Keys.W) {
            game.player.moveUp();
        } else if (keycode == Input.Keys.S) {
            game.player.moveDown();
        }

        // check if the player has pressed a block or attack key
        if (keycode == Input.Keys.B) {
            game.player.block();
        } else if (keycode == Input.Keys.F) {
            game.player.punch();
        } else if (keycode == Input.Keys.V) {
            game.player.kick();
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // if player has released a movement key, stop moving in that direction
        if (keycode == Input.Keys.A) {
            game.player.stopMovingLeft();
        } else if (keycode == Input.Keys.D) {
            game.player.stopMovingRight();
        }
        if (keycode == Input.Keys.W) {
            game.player.stopMovingUp();
        } else if (keycode == Input.Keys.S) {
            game.player.stopMovingDown();
        }

        // if player has released the block key, stop blocking
        if (keycode == Input.Keys.B) {
            game.player.stopBlocking();
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
