package com.weitnow.sfs.ressources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class Assets {
    // asset manager
    public final AssetManager manager = new AssetManager();

    // gameplay assets
    public static final String BACKGROUND_TEXTURE = "textures/Background.png";
    public static final String FRONT_ROPES_TEXTURE = "textures/FrontRopes.png";
    public static final String IDLE_SPRITE_SHEET = "sprites/IdleSpriteSheet.png";
    public static final String WALK_SPRITE_SHEET = "sprites/WalkSpriteSheet.png";
    public static final String KICK_SPRITE_SHEET = "sprites/KickSpriteSheet.png";
    public static final String PUNCH_SPRITE_SHEET = "sprites/PunchSpriteSheet.png";
    public static final String HURT_SPRITE_SHEET = "sprites/HurtSpriteSheet.png";
    public static final String BLOCK_SPRITE_SHEET = "sprites/BlockSpriteSheet.png";
    public static final String WIN_SPRITE_SHEET = "sprites/WinSpriteSheet.png";
    public static final String LOSE_SPRITE_SHEET = "sprites/LoseSpriteSheet.png";
    public static final String GAMEPLAY_BUTTONS_ATLAS = "textures/GameplayButtons.atlas";
    public static final String BLOOD_ATLAS = "textures/Blood.atlas";

    // fonts
    public static final String ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
    public static final String SMALL_FONT = "smallFont.ttf";
    public static final String MEDIUM_FONT = "mediumFont.ttf";
    public static final String LARGE_FONT = "largeFont.ttf";

    // audio assets

    public static final String BLOCK_SOUND = "audio/block.mp3";
    public static final String BOO_SOUND = "audio/boo.mp3";
    public static final String CHEER_SOUND = "audio/cheer.mp3";
    public static final String CLICK_SOUND = "audio/click.mp3";
    public static final String HIT_SOUND = "audio/hit.mp3";
    public static final String MUSIC = "audio/music.ogg";

    // menu assets

    public static final String MENU_ITEMS_ATLAS = "textures/MenuItems.atlas";

    public void load() {
        // load all assets
        loadGameplayAssets();
        loadFonts();
    }

    private void loadGameplayAssets() {
        manager.load(BACKGROUND_TEXTURE, Texture.class);
        manager.load(FRONT_ROPES_TEXTURE, Texture.class);
        manager.load(IDLE_SPRITE_SHEET, Texture.class);
        manager.load(WALK_SPRITE_SHEET, Texture.class);
        manager.load(PUNCH_SPRITE_SHEET, Texture.class);
        manager.load(KICK_SPRITE_SHEET, Texture.class);
        manager.load(HURT_SPRITE_SHEET, Texture.class);
        manager.load(BLOCK_SPRITE_SHEET, Texture.class);
        manager.load(WIN_SPRITE_SHEET, Texture.class);
        manager.load(LOSE_SPRITE_SHEET, Texture.class);
        manager.load(GAMEPLAY_BUTTONS_ATLAS, TextureAtlas.class);
        manager.load(BLOOD_ATLAS, TextureAtlas.class);
    }

    private void loadFonts() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        // load the small font
        FreetypeFontLoader.FreeTypeFontLoaderParameter smallFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        smallFont.fontFileName = ROBOTO_REGULAR;
        smallFont.fontParameters.size = 32;
        manager.load(SMALL_FONT, BitmapFont.class, smallFont);

        // load the medium font
        FreetypeFontLoader.FreeTypeFontLoaderParameter mediumFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        mediumFont.fontFileName = ROBOTO_REGULAR;
        mediumFont.fontParameters.size = 106;
        mediumFont.fontParameters.borderWidth = 4;
        manager.load(MEDIUM_FONT, BitmapFont.class, mediumFont);

        // load the large font
        FreetypeFontLoader.FreeTypeFontLoaderParameter largeFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        largeFont.fontFileName = ROBOTO_REGULAR;
        largeFont.fontParameters.size = 132;
        largeFont.fontParameters.borderWidth = 4;
        manager.load(LARGE_FONT, BitmapFont.class, largeFont);

    }

    public void dispose(){
        manager.dispose();
    }
}
