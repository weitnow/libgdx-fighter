package com.weitnow.sfs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.weitnow.sfs.objects.Fighter;
import com.weitnow.sfs.ressources.Assets;
import com.weitnow.sfs.screens.GameScreen;

public class SFS extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public Assets assets;

    // screens
    public GameScreen gameScreen;

    //fighters
    public Fighter player, opponent;


    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        assets = new Assets();

        // load all assets
        assets.load();
        assets.manager.finishLoading();

        // initialize the fighters
        player = new Fighter(this, "Slim Stallone", new Color(1f, 0.2f, 0.2f, 1f));
        opponent = new Fighter(this, "Thin Diesel", new Color(0.25f, 0.7f, 1f, 1f));

        // initialize the game screen and switch to it
        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        assets.dispose();

    }
}
