package hyu.hauk.snake.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import hyu.hauk.snake.assets.*;

/**
 * Created by hauk on 17. 1. 29.
 */
public class HelpScreen extends hyu.hauk.snake.assets.AbstractAssetsManagedScreen {
    private static final String NAME = HelpScreen.class.getSimpleName();
    private static final String TAG = "\\"+NAME;

    private static final float VIEWPORT_WIDTH = 480;
    private static final float VIEWPORT_HEIGHT = 320;
    private static final float HALF_VIEWPORT_WIDTH = VIEWPORT_WIDTH/2;
    private static final float HALF_VIEWPORT_HEIGHT = VIEWPORT_HEIGHT/2;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private final Game game;
    private Sprite background;
    private Sprite help;
    private Sprite buttonRight;
    private InputProcessor input;

    public HelpScreen(Game game) {
        this.game = game;
    }

    @Override
    public void showManaged() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(VIEWPORT_WIDTH,VIEWPORT_HEIGHT);
        batch.setProjectionMatrix(camera.combined);

        background = new Sprite(Assets.INSTANCE.getTexture(Assets.BACKGROUND));
        background.setPosition(-background.getWidth()/2,
                -background.getHeight()/2);

        help = new Sprite(Assets.INSTANCE.getTexture(Assets.HELP1));
        help.setPosition(-help.getWidth()/2,
                -help.getHeight()/2);

        buttonRight = new Sprite(Assets.INSTANCE.getTexture(Assets.BUTTONS),0,64,64,64);
        buttonRight.setPosition(HALF_VIEWPORT_WIDTH-64,
                -HALF_VIEWPORT_HEIGHT);
        input = new InputAdapter(){
            Vector3 coords = new Vector3();
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                coords.set(screenX, screenY, 0);
                camera.unproject(coords);
                if(buttonRight.getBoundingRectangle().contains(coords.x, coords.y)) {
                    next();
                    return true;
                }
                return false;
            }

            private void next() {
                Gdx.app.log(TAG,"Next->help2");
                if(Settings.soundEnabled) Assets.INSTANCE.getSound(Assets.CLICK).play(1);
                game.setScreen(new HelpScreen2(game));
            }

            @Override
            public boolean keyUp(int keycode) {
                if(keycode == Input.Keys.ESCAPE) {
                    close();
                    return true;
                }
                if(keycode == Input.Keys.RIGHT) {
                    next();
                    return true;
                }
                return false;
            }

            private void close() {
                Gdx.app.log(TAG,"close help1");
                if(Settings.soundEnabled) Assets.INSTANCE.getSound(Assets.CLICK).play(1);
                game.setScreen(new MainMenuScreen(game));
            }

        };
        Gdx.input.setInputProcessor(input);

    }

    @Override
    public void render(float delta) {
        batch.begin();
        background.draw(batch);
        help.draw(batch);
        buttonRight.draw(batch);
        batch.end();
    }

    @Override
    public void resume() {
        if(Gdx.input.getInputProcessor() == null) {
            Gdx.input.setInputProcessor(input);
        }
    }

    @Override
    public void pause() {
        Gdx.app.log(TAG,"pause()");
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void hideManaged() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void disposeManaged() {
        batch.dispose();
    }

}
