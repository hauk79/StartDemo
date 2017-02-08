package hyu.hauk.snake.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import hyu.hauk.snake.assets.*;
import hyu.hauk.snake.world.Snake;
import hyu.hauk.snake.world.SnakePart;
import hyu.hauk.snake.world.Stain;
import hyu.hauk.snake.world.World;

/**
 * Created by hauk on 17. 1. 29.
 */
public class GameScreen extends hyu.hauk.snake.assets.AbstractAssetsManagedScreen {
    private static final String NAME = GameScreen.class.getSimpleName();
    private static final String TAG = "\\"+NAME;
    private static final String SCORE_MARK = "[#6971f1]";
    private static final String READY = "READY ?";
    private static final String READY_MARKUP = "[#6f31e8]"+READY;
    private static final String TOUCH = "(TOUCH SCREEN)";
    private static final String TOUCH_MARKUP = "[#ef7898]"+TOUCH;
    private static final String GAMEOVER = "GAME OVER";
    private static final String GAMEOVER_MARKUP = "[#9f48a8]"+GAMEOVER;
    private static final String RESUME = "RESUME";
    private static final String RESUME_MARKUP = "[#cf4828]"+RESUME;
    private static final String QUIT = "QUIT";
    private static final String QUIT_MARKUP = "[#5f4878]"+QUIT;

    private int oldScore = 0;
    private String score ="0";

    private Sprite buttonLeft;
    private Sprite buttonLeftUp;
    private Sprite buttonRight;
    private Sprite buttonRightUp;
    private Sprite buttonPause;
    private Sprite buttonClose;
    private Sprite buttonLeftDown;
    private BitmapFont font;
    private GlyphLayout scoreText;
    private Vector2 scoreTextPosition;
    private GlyphLayout ready;
    private Vector2 readyPosition;
    private GlyphLayout touch;
    private Vector2 touchPosition;
    private GlyphLayout gameOver;
    private Vector2 gameOverPosition;
    private GlyphLayout resume;
    private Vector2 resumePosition;
    private GlyphLayout quit;
    private Vector2 quitPosition;

    enum GameState {
        Ready,
        Running,
        Paused,
        GameOver,
    }
    private GameState state = GameState.Ready;

    private static final float VIEWPORT_WIDTH = 480;
    private static final float VIEWPORT_HEIGHT = 320;
    private static final float HALF_VIEWPORT_WIDTH = VIEWPORT_WIDTH/2;
    private static final float HALF_VIEWPORT_HEIGHT = VIEWPORT_HEIGHT/2;
    private static final float VIEWPORT_TRANSLATE_X = HALF_VIEWPORT_WIDTH;
    private static final float VIEWPORT_TRANSLATE_Y = HALF_VIEWPORT_HEIGHT-64f;
    private static final float VIEWPORT_LEFT = -HALF_VIEWPORT_WIDTH + VIEWPORT_TRANSLATE_X;
    private static final float VIEWPORT_DOWN = -HALF_VIEWPORT_HEIGHT + VIEWPORT_TRANSLATE_Y;
    private static final float VIEWPORT_RIGHT = VIEWPORT_LEFT +VIEWPORT_WIDTH;
    private static final float VIEWPORT_TOP = VIEWPORT_DOWN +VIEWPORT_HEIGHT;
    private static final float VIEWPORT_WORLD_CENTER_X = (VIEWPORT_LEFT +VIEWPORT_RIGHT)/2f;
    private static final float VIEWPORT_WORLD_CENTER_Y = VIEWPORT_TOP/2f;

    private final World world;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private final Game game;
    private InputProcessor input;
    private ShapeRenderer shapeRenderer;

    public GameScreen(Game game) {
        this.game = game;
        world = new World();
    }

    @Override
    public void showManaged() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(VIEWPORT_WIDTH,VIEWPORT_HEIGHT);
        camera.translate(VIEWPORT_TRANSLATE_X, VIEWPORT_TRANSLATE_Y);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        font = Assets.INSTANCE.get(Assets.SILVER_CURSIVE_32, BitmapFont.class);
        font.getData().markupEnabled = true;
        scoreText = new GlyphLayout(font, SCORE_MARK+score);
        scoreTextPosition = new Vector2(VIEWPORT_RIGHT-scoreText.width, VIEWPORT_TOP);

        touch = new GlyphLayout(font, TOUCH_MARKUP);
        touchPosition = new Vector2(VIEWPORT_WORLD_CENTER_X -touch.width/2, VIEWPORT_WORLD_CENTER_Y + touch.height*2f);
        ready = new GlyphLayout(font, READY_MARKUP);
        readyPosition = new Vector2(VIEWPORT_WORLD_CENTER_X -ready.width/2, touchPosition.y + ready.height*2f);

        resume = new GlyphLayout(font, RESUME_MARKUP);
        quit = new GlyphLayout(font, QUIT_MARKUP);
        resumePosition = new Vector2(VIEWPORT_WORLD_CENTER_X -resume.width/2, VIEWPORT_WORLD_CENTER_Y + resume.height*2f);
        quitPosition = new Vector2(VIEWPORT_WORLD_CENTER_X -quit.width/2, VIEWPORT_WORLD_CENTER_Y);

        gameOver = new GlyphLayout(font, GAMEOVER_MARKUP);
        gameOverPosition = new Vector2(VIEWPORT_WORLD_CENTER_X -gameOver.width/2f, VIEWPORT_WORLD_CENTER_Y +gameOver.height/2f);

        buttonPause = new Sprite(Assets.INSTANCE.getTexture(Assets.BUTTONS),64,128,64,64);
        buttonClose = new Sprite(Assets.INSTANCE.getTexture(Assets.BUTTONS),0,128,64,64);
        buttonLeft = new Sprite(Assets.INSTANCE.getTexture(Assets.BUTTONS),64,64,64,64);
        buttonRight = new Sprite(Assets.INSTANCE.getTexture(Assets.BUTTONS),0,64,64,64);

        buttonPause.setPosition(0,VIEWPORT_TOP-64);
        buttonClose.setPosition(gameOverPosition.x+(gameOver.width - buttonClose.getWidth())/2,
                gameOverPosition.y-gameOver.height - buttonClose.getHeight());

        buttonLeft.setPosition(VIEWPORT_LEFT,VIEWPORT_DOWN);
        buttonLeftUp = new Sprite(buttonLeft);
        buttonLeftUp.setOrigin(buttonLeftUp.getWidth()/2, buttonLeftUp.getHeight()/2);
        buttonLeftDown = new Sprite(buttonLeftUp);
        buttonLeftUp.translate(buttonLeft.getWidth()*1.5f,0);
        buttonLeftUp.rotate90(true);

        buttonLeftDown.setPosition(VIEWPORT_WORLD_CENTER_X-buttonLeftDown.getWidth()/2,VIEWPORT_DOWN);
        buttonLeftDown.rotate90(false);

        buttonRight.setPosition(VIEWPORT_RIGHT-buttonRight.getWidth(),-64);
        buttonRightUp = new Sprite(buttonRight);
        buttonRightUp.translate(-buttonRight.getWidth()*1.5f,0);
        buttonRightUp.setOrigin(buttonRightUp.getWidth()/2, buttonRightUp.getHeight()/2);
        buttonRightUp.rotate90(false);

        input = new InputAdapter(){
            Vector3 coords = new Vector3();
            Rectangle quit = new Rectangle(quitPosition.x, quitPosition.y-GameScreen.this.quit.height,
                    GameScreen.this.quit.width, GameScreen.this.quit.height);
            Rectangle resume = new Rectangle(resumePosition.x, resumePosition.y-GameScreen.this.resume.height,
                    GameScreen.this.resume.width, GameScreen.this.resume.height);

            @Override
            public boolean keyDown(int keycode) {
                switch(state) {
                    case Ready:
                        if(keycode != Input.Keys.ESCAPE && keycode !=Input.Keys.SPACE)
                            state = GameState.Running;
                        break;
                    case Running:
                        if (keycode == Input.Keys.LEFT) world.snake.turnLeft();
                        if (keycode == Input.Keys.RIGHT) world.snake.turnRight();
                        if (keycode == Input.Keys.UP) {
                            if(world.snake.direction == world.snake.LEFT)world.snake.turnRight();
                            if(world.snake.direction == world.snake.RIGHT)world.snake.turnLeft();
                        }
                        if (keycode == Input.Keys.DOWN) {
                            justDown();
                        }
                        break;
                }
                return true;
            }

            private void justDown() {
                if(world.snake.direction == world.snake.LEFT)world.snake.turnLeft();
                if(world.snake.direction == world.snake.RIGHT)world.snake.turnRight();
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                switch(state) {
                    case Ready:
                        state = GameState.Running;
                        break;
                    case Running:
                        coords.set(screenX, screenY, 0);
                        camera.unproject(coords);
                        if (buttonLeft.getBoundingRectangle().contains(coords.x, coords.y)) world.snake.turnLeft();
                        if (buttonLeftUp.getBoundingRectangle().contains(coords.x, coords.y)) world.snake.turnRight();
                        if (buttonRight.getBoundingRectangle().contains(coords.x, coords.y)) world.snake.turnRight();
                        if (buttonRightUp.getBoundingRectangle().contains(coords.x, coords.y)) world.snake.turnLeft();
                        if (buttonLeftDown.getBoundingRectangle().contains(coords.x, coords.y)) {
                            justDown();
                        }
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                if(keycode == Input.Keys.ESCAPE) {
                    close();
                    return true;
                }
                switch(state) {
                    case Ready:
                        if(keycode == Input.Keys.SPACE) {
                            state = GameState.Running;
                            return true;
                        }
                        break;
                    case Running:
                        if(keycode == Input.Keys.SPACE || keycode == Input.Keys.P ) {
                            clicked();
                            state = GameState.Paused;
                            return true;
                        }
                        break;
                    case Paused:
                        if(keycode == Input.Keys.SPACE || keycode == Input.Keys.R) {
                            clicked();
                            state = GameState.Running;
                            return true;
                        }
                        if(keycode == Input.Keys.Q) {
                            close();
                            return true;
                        }
                        break;
                    case GameOver:
                        if(keycode == Input.Keys.X) {
                            close();
                            return true;
                        }
                        break;
                }
                return false;
            }

            private void close() {
                clicked();
                game.setScreen(new MainMenuScreen(game));
            }

            private void clicked() {
                if(Settings.soundEnabled) Assets.INSTANCE.getSound(Assets.CLICK).play(1);
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                coords.set(screenX, screenY, 0);
                camera.unproject(coords);

                switch(state) {
                    case Running:
                        if(buttonPause.getBoundingRectangle().contains(coords.x, coords.y)) {
                            clicked();
                            state = GameState.Paused;
                            return true;
                        }
                        break;
                    case Paused:
                        if(resume.contains(coords.x, coords.y)) {
                            clicked();
                            state = GameState.Running;
                            return true;
                        }
                        if(quit.contains(coords.x, coords.y)) {
                            close();
                            return true;
                        }
                        break;
                    case GameOver:
                        if(buttonClose.getBoundingRectangle().contains(coords.x, coords.y)) {
                            close();
                            return true;
                        }
                        break;
                }
                return true;
            }
        };
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta) {
        switch(state) {
            case Running:
                updateRunning(delta);
                break;
        }
        batch.begin();
        batch.draw(Assets.INSTANCE.getTexture(Assets.BACKGROUND), VIEWPORT_LEFT, VIEWPORT_DOWN);
        drawWorld(world, batch);
        switch (state) {
            case Ready:
                drawReadyUI(batch);
                break;
            case Running:
                drawRunningUI(batch);
                break;
            case Paused:
                drawPausedUI(batch);
                break;
            case GameOver:
                drawGameOverUI(batch);
                break;
        }
        font.draw(batch, scoreText, scoreTextPosition.x, scoreTextPosition.y);
        batch.end();
        drawLine();
    }

    private void updateRunning(float delta) {
        world.update(delta);
        if(world.gameOver) {
            if(Settings.soundEnabled) Assets.INSTANCE.getSound(Assets.BITTEN).play(1);
            state = GameState.GameOver;
        }
        if(oldScore != world.score) {
            oldScore = world.score;
            score = SCORE_MARK + oldScore;
            scoreText.setText(font,score);
            scoreTextPosition.set(VIEWPORT_RIGHT-scoreText.width, scoreTextPosition.y);
            if(Settings.soundEnabled) Assets.INSTANCE.getSound(Assets.EAT).play(1);
        }
    }

    private void drawReadyUI(SpriteBatch batch) {
          font.draw(batch, ready, readyPosition.x, readyPosition.y);
          font.draw(batch, touch, touchPosition.x, touchPosition.y);
    }
    private void drawRunningUI(SpriteBatch batch) {
        buttonPause.draw(batch);
        buttonLeft.draw(batch);
        buttonLeftUp.draw(batch);
        buttonRight.draw(batch);
        buttonRightUp.draw(batch);
        buttonLeftDown.draw(batch);

    }
    private void drawPausedUI(SpriteBatch batch) {
        font.draw(batch, resume, resumePosition.x, resumePosition.y);
        font.draw(batch, quit, quitPosition.x, quitPosition.y);
    }
    private void drawGameOverUI(SpriteBatch batch) {
        font.draw(batch, gameOver, gameOverPosition.x, gameOverPosition.y);
        buttonClose.draw(batch);
    }

    private void drawLine() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.line(0,0,VIEWPORT_WIDTH,0);
        shapeRenderer.end();
    }

    private void drawWorld(World world, SpriteBatch batch) {
        Snake snake = world.snake;
        SnakePart head = snake.parts.get(0);
        Stain stain = world.stain;

        Texture stainTexture = null;
        switch(stain.type) {
            case Stain.TYPE_1:
                stainTexture = Assets.INSTANCE.getTexture(Assets.STAIN1);
                break;
            case Stain.TYPE_2:
                stainTexture = Assets.INSTANCE.getTexture(Assets.STAIN2);
                break;
            case Stain.TYPE_3:
                stainTexture = Assets.INSTANCE.getTexture(Assets.STAIN3);
                break;

        }
        float x=stain.x * 32f;
        float y=stain.y * 32f;
        batch.draw(stainTexture, x, y);

        int len = snake.parts.size();
        for(int i=1; i<len; i++) {
            SnakePart part = snake.parts.get(i);
            x = part.x * 32f;
            y = part.y * 32f;
            batch.draw(Assets.INSTANCE.getTexture(Assets.TAIL), x, y);
        }
        Texture headTexture = null;
        switch (snake.direction) {
            case Snake.UP:
                headTexture = Assets.INSTANCE.getTexture(Assets.HEAD_UP);
                break;
            case Snake.LEFT:
                headTexture = Assets.INSTANCE.getTexture(Assets.HEAD_LEFT);
                break;
            case Snake.DOWN:
                headTexture = Assets.INSTANCE.getTexture(Assets.HEAD_DOWN);
                break;
            case Snake.RIGHT:
                headTexture = Assets.INSTANCE.getTexture(Assets.HEAD_RIGHT);
                break;
        }
        x=head.x*32+16;
        y=head.y*32+16;
        batch.draw(headTexture, x-headTexture.getWidth()/2, y-headTexture.getHeight()/2);
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
        if(state == GameState.Running) state = GameState.Paused;
        if(world.gameOver) {
            saveScore();
        }
    }

    @Override
    public void hideManaged() {
        Gdx.input.setInputProcessor(null);
        saveScore();
    }

    private void saveScore() {
        Settings.addScore(world.score);
        Settings.save(Gdx.files);
    }

    @Override
    public void disposeManaged() {
        batch.dispose();
    }
}
