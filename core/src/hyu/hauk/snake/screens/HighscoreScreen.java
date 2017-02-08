package hyu.hauk.snake.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import hyu.hauk.snake.assets.*;

/**
 * Created by hauk on 17. 1. 29.
 */
public class HighscoreScreen extends hyu.hauk.snake.assets.AbstractAssetsManagedScreen {
    private static final String NAME = HighscoreScreen.class.getSimpleName();
    private static final String TAG = "\\"+NAME;
    private static final String HIGHSCORE = "HIGHSCORE";
    private static final String HIGHSCORE_MARKUP = "[#22ccff]"+HIGHSCORE;
    private static final String SCORE_MARK = "[#1368ad]";

    private static final float VIEWPORT_WIDTH = 480;
    private static final float VIEWPORT_HEIGHT = 320;
    private static final float HALF_VIEWPORT_WIDTH = VIEWPORT_WIDTH/2;
    private static final float HALF_VIEWPORT_HEIGHT = VIEWPORT_HEIGHT/2;

    private final Game game;
    private String lines[] = new String [5];
    private OrthographicCamera camera;
    private InputProcessor input;
    private SpriteBatch batch;
    private Sprite background;
    private Sprite buttonLeft;
    private BitmapFont font;
    private GlyphLayout highscore;
    private Vector2 highscorePosition;
    private GlyphLayout score;
    private Vector2 scorePosition;
    private float scoreGap;

    public HighscoreScreen(Game game) {
        this.game = game;
        for(int i=0; i<5; i++) {
            lines[i] = SCORE_MARK+(i+1)+ ". " + Settings.highscrores[i];
        }
    }

    @Override
    public void showManaged() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(VIEWPORT_WIDTH,VIEWPORT_HEIGHT);
        batch.setProjectionMatrix(camera.combined);

        background = new Sprite(Assets.INSTANCE.getTexture(Assets.BACKGROUND));
        background.setPosition(-background.getWidth()/2,
                -background.getHeight()/2);

        buttonLeft = new Sprite(Assets.INSTANCE.getTexture(Assets.BUTTONS),64,64,64,64);
        buttonLeft.setPosition(-HALF_VIEWPORT_WIDTH,
                -HALF_VIEWPORT_HEIGHT);

        font = Assets.INSTANCE.get(Assets.SILVER_CURSIVE_32, BitmapFont.class);
        font.getData().markupEnabled = true;

        highscore = new GlyphLayout(font, HIGHSCORE_MARKUP);
        highscorePosition = new Vector2(-highscore.width/2, HALF_VIEWPORT_HEIGHT- highscore.height/2);

        score = new GlyphLayout(font, lines[0]);
        scorePosition = new Vector2(-score.width/2, highscorePosition.y- score.height*2);
        scoreGap = score.height*2f;
        input = new InputAdapter(){
            Vector3 coords = new Vector3();

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                coords.set(screenX, screenY, 0);
                camera.unproject(coords);

                if(buttonLeft.getBoundingRectangle().contains(coords.x, coords.y)) {
                    close();
                    return true;
                }
                return false;
            }
            @Override
            public boolean keyUp(int keycode) {
                if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.LEFT) {
                    close();
                    return true;
                }
                return false;
            }

            private void close() {
                Gdx.app.log(TAG,"close highscore");
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
        font.draw(batch, highscore, highscorePosition.x, highscorePosition.y);
        buttonLeft.draw(batch);
        float y=scorePosition.y;
        for(int i=0; i<5; i++) {
            score.setText(font, lines[i]);
            font.draw(batch, lines[i], scorePosition.x, y);
            y-= scoreGap;
        }
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
