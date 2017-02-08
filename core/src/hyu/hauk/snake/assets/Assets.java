package hyu.hauk.snake.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by hauk on 17. 1. 27.
 */
public class Assets {
    public static final Assets INSTANCE = new Assets();
    public static final String TAG = "\\"+Assets.class.getSimpleName();
    private AssetManager manager;
    private Assets() {
        manager = new AssetManager();
    }

    public static final String SILVER_CURSIVE_32 = "fonts/silver_cursive_32.fnt";

    public static final String BACKGROUND = "background.png";
    public static final String BUTTONS = "buttons.png";
    public static final String HELP1 = "help1.png";
    public static final String HELP2 = "help2.png";
    public static final String HELP3 = "help3.png";
    public static final String HEAD_UP = "headup.png";
    public static final String HEAD_LEFT = "headleft.png";
    public static final String HEAD_DOWN = "headdown.png";
    public static final String HEAD_RIGHT = "headright.png";
    public static final String TAIL = "tail.png";
    public static final String STAIN1 = "stain1.png";
    public static final String STAIN2 = "stain2.png";
    public static final String STAIN3 = "stain3.png";

    public static final String CLICK = "click3.ogg";
    public static final String EAT = "switch16.ogg";
    public static final String BITTEN = "game_over.ogg";

    public void loadTexture(String fileName) {
        load(Texture.class, fileName);
    }
    public void loadTextures(String... fileNames) {
        for(String fileName:fileNames)
            loadTexture(fileName);
    }
    public void loadSound(String fileName) {
        load(Sound.class, fileName);
    }
    public void loadSounds(String... fileNames) {
        for(String fileName:fileNames)
            loadSound(fileName);
    }
    public <T> void load(Class<T> type, String fileName) {
        manager.load(fileName, type);
    }
    public <T> void load(Class<T> type, String... fileNames) {
        for(String fileName: fileNames)
            load(type, fileName);
    }

    public void unload(String fileName) {
        manager.unload(fileName);
    }
    public void unloads(String ...fileNames) {
        for(String fileName:fileNames)
            unload(fileName);
    }

    public Texture getTexture(String fileName) {
        return get(fileName, Texture.class);
    }
    public Sound getSound(String fileName) {
        return get(fileName, Sound.class);
    }
    public <T> T get(String fileName, Class<T> type) {
        return manager.get(fileName, type);
    }

    public void finishLoading() {
        manager.finishLoading();
    }
    public void dispose() {
        Gdx.app.log(TAG,"dispose()");
        manager.dispose();
    }

    public void clear() {
        Gdx.app.log(TAG,"clear()");
        manager.clear();
    }

}
