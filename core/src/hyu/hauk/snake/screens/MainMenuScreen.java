package hyu.hauk.snake.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import hyu.hauk.snake.assets.*;

/**
 * Created by hauk on 17. 1. 28.
 */
public class MainMenuScreen extends hyu.hauk.snake.assets.AbstractAssetsManagedScreen {
    private static final String NAME = MainMenuScreen.class.getSimpleName();
    private static final String TAG = "\\"+NAME;
    private static final String LOGO = "Start Demo";
    private static final String LOGO_MARKUP = "[#9911ee]"+LOGO;
    private static final String PLAY = "PLAY";
    private static final String PLAY_MARKUP = "[#2f11ee]"+PLAY;
    private static final String HIGHSCORE = "HIGHSCORE";
    private static final String HIGHSCORE_MARKUP = "[#9955ee]"+HIGHSCORE;
    private static final String HELP = "HELP";
    private static final String HELP_MARKUP = "[#991175]"+HELP;
    private static final float VIEWPORT_WIDTH = 480;
    private static final float VIEWPORT_HEIGHT = 320;
    private static final float HALF_VIEWPORT_WIDTH = VIEWPORT_WIDTH/2;
    private static final float HALF_VIEWPORT_HEIGHT = VIEWPORT_HEIGHT/2;
    private final Game game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Sprite background;
    private Sprite buttonSoundEnabled;
    private Sprite buttonSoundDisabled;
    private InputProcessor input;
    private BitmapFont font;
    private GlyphLayout logo;
    private Vector2 logoPosition;
    private GlyphLayout play;
    private Vector2 playPosition;
    private GlyphLayout highscore;
    private Vector2 highscorePosition;
    private GlyphLayout help;
    private Vector2 helpPosition;


    public MainMenuScreen(Game game) {
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

        font = Assets.INSTANCE.get(Assets.SILVER_CURSIVE_32, BitmapFont.class);
        font.getData().markupEnabled = true;

        logo = new GlyphLayout();
        logo.setText(font, LOGO_MARKUP);
        logoPosition = new Vector2(0-logo.width/2,
                HALF_VIEWPORT_HEIGHT-logo.height);

        float menuGap = 2.5f;
        help = new GlyphLayout();
        help.setText(font, HELP_MARKUP);
        helpPosition = new Vector2(0-help.width/2,
                -HALF_VIEWPORT_HEIGHT+help.height*menuGap);

        highscore = new GlyphLayout();
        highscore.setText(font, HIGHSCORE_MARKUP);
        highscorePosition = new Vector2(0-highscore.width/2,
                helpPosition.y+highscore.height*menuGap);
        play = new GlyphLayout();
        play.setText(font, PLAY_MARKUP);
        playPosition = new Vector2(0-play.width/2,
                highscorePosition.y+play.height*menuGap);

        buttonSoundEnabled = new Sprite(Assets.INSTANCE.getTexture(Assets.BUTTONS),0,0,64,64);
        buttonSoundEnabled.setPosition(-HALF_VIEWPORT_WIDTH, -HALF_VIEWPORT_HEIGHT);
        buttonSoundDisabled= new Sprite(Assets.INSTANCE.getTexture(Assets.BUTTONS),64,0,64,64);
        buttonSoundDisabled.setPosition(buttonSoundEnabled.getX(), buttonSoundEnabled.getY());
        input = new InputAdapter(){
            Vector3 coords = new Vector3();

            Rectangle help = new Rectangle(MainMenuScreen.this.helpPosition.x,
                    MainMenuScreen.this.helpPosition.y-MainMenuScreen.this.help.height,
                    MainMenuScreen.this.help.width, MainMenuScreen.this.help.height);

            Rectangle highscores = new Rectangle(MainMenuScreen.this.highscorePosition.x,
                    MainMenuScreen.this.highscorePosition.y-MainMenuScreen.this.highscore.height,
                    MainMenuScreen.this.highscore.width, MainMenuScreen.this.highscore.height);

            Rectangle play = new Rectangle(MainMenuScreen.this.playPosition.x,
                    MainMenuScreen.this.playPosition.y-MainMenuScreen.this.play.height,
                    MainMenuScreen.this.play.width, MainMenuScreen.this.play.height);

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                coords.set(screenX, screenY, 0);
                camera.unproject(coords);
                if(buttonSoundEnabled.getBoundingRectangle().contains(coords.x, coords.y)) {
                    toggleSound();
                }
                if(play.contains(coords.x, coords.y)) {
                    play();
                    return true;
                }
                if(highscores.contains(coords.x, coords.y)) {
                    Gdx.app.log(TAG,"HIGHSCORES");
                    if(Settings.soundEnabled) Assets.INSTANCE.getSound(Assets.CLICK).play(1);
                    game.setScreen(new HighscoreScreen(game));
                    return true;
                }
                if(help.contains(coords.x, coords.y)) {
                    Gdx.app.log(TAG,"HELP");
                    if(Settings.soundEnabled) Assets.INSTANCE.getSound(Assets.CLICK).play(1);
                    game.setScreen(new HelpScreen(game));
                    return true;
                }
                return true;
            }

            private void play() {
                Gdx.app.log(TAG,"PLAY");
                if(Settings.soundEnabled) Assets.INSTANCE.getSound(Assets.CLICK).play(1);
                game.setScreen(new GameScreen(game));
            }

            private void toggleSound() {
                Settings.toggleSoundEnabled();
                if(Settings.soundEnabled) Assets.INSTANCE.getSound(Assets.CLICK).play(1);
            }

            @Override
            public boolean keyUp(int keycode) {
                if(keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                    return true;
                }
                if(keycode == Input.Keys.S) {
                    toggleSound();
                }
                if(keycode == Input.Keys.SPACE) {
                    play();
                    return true;
                }
                return true;
            }
        };
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        //draw scene
        background.draw(batch);
        font.draw(batch, logo, logoPosition.x, logoPosition.y);

        font.draw(batch, play, playPosition.x, playPosition.y);
        font.draw(batch, highscore, highscorePosition.x, highscorePosition.y);
        font.draw(batch, help, helpPosition.x, helpPosition.y);
        if(Settings.soundEnabled) buttonSoundEnabled.draw(batch);
        else buttonSoundDisabled.draw(batch);
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
        Settings.save(Gdx.files);
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
