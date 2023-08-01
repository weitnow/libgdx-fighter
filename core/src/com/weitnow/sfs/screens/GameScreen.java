package com.weitnow.sfs.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.weitnow.sfs.SFS;
import com.weitnow.sfs.ressources.Assets;
import com.weitnow.sfs.ressources.GlobalVariables;

public class GameScreen implements Screen {
    private final SFS game;
    private final ExtendViewport viewport;

    // background/ring
    private Texture backgroundTexture;
    private Texture frontRopesTexture;

    // fighters
    private static final float PLAYER_START_POSITION_X = 16f;
    private static final float OPPONENT_START_POSITION_X = 51f;
    private static final float FIGHTER_START_POSITION_Y = 15f;

    public GameScreen(SFS game) {
        this.game = game;

        //set up the viewport
        viewport = new ExtendViewport(GlobalVariables.WORLD_WIDTH, GlobalVariables.MIN_WORLD_HEIGHT,
                GlobalVariables.WORLD_WIDTH, 0);

        // create the game area
        createGameArea();

        // get the fighters ready
        game.player.getReady(PLAYER_START_POSITION_X, FIGHTER_START_POSITION_Y);
        game.opponent.getReady(OPPONENT_START_POSITION_X, FIGHTER_START_POSITION_Y);
    }

    private void createGameArea() {
        // get the ring textures from the asset manager
        backgroundTexture = game.assets.manager.get(Assets.BACKGROUND_TEXTURE);
        frontRopesTexture = game.assets.manager.get(Assets.FRONT_ROPES_TEXTURE);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // update the game
        update(delta);

        // set the sprite batch to use the camera
        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        // begin drawing
        game.batch.begin();

        // draw the background
        game.batch.draw(backgroundTexture, 0, 0, backgroundTexture.getWidth() * GlobalVariables.WORLD_SCALE, backgroundTexture.getHeight() * GlobalVariables.WORLD_SCALE);

        // draw the fighters
        renderFighters();

        // end drawing
        game.batch.end();
    }

    private void renderFighters() {
        // draw player
        game.player.render(game.batch);

        //draw opponent
        game.opponent.render(game.batch);
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
}
