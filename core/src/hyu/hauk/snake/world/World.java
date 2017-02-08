package hyu.hauk.snake.world;

import java.util.Random;

/**
 * Created by hauk on 17. 1. 29.
 */
public class World {
    static final int WORLD_WIDTH = 15;
    static final int WORLD_HEIGHT = 8;
    static final int SCORE_INCREMENT = 10;
    static final float TICK_INITIAL = .5f;
    static final float TICK_DECREMENT = TICK_INITIAL/10;

    public Snake snake;
    public Stain stain;
    private float tickTime=0;
    private static float tick = TICK_INITIAL;
    private boolean[][] fields = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
    private Random random = new Random();

    public boolean gameOver = false;
    public int score = 0;

    public World() {
        snake = new Snake();
        placeStain();
    }

    private void placeStain() {
        for(int x=0; x<WORLD_WIDTH; x++) {
            for(int y=0; y<WORLD_HEIGHT; y++) {
                fields[x][y]=false;
            }
        }

        int len = snake.parts.size();
        for(int i=0; i<len; i++) {
            SnakePart part = snake.parts.get(i);
            fields[part.x][part.y] = true;
        }

        int stainX = random.nextInt(WORLD_WIDTH);
        int stainY = random.nextInt(WORLD_HEIGHT);
        while(true) {
            if(!fields[stainX][stainY]) break;
            stainX += 1;
            if(stainX>=WORLD_WIDTH) {
                stainX = 0;
                stainY += 1;
                if(stainY >=WORLD_HEIGHT) stainY = 0;
            }
        }
        stain = new Stain(stainX, stainY, random.nextInt(3));
    }

    public void update(float delta) {
        if(gameOver) return;

        tickTime += delta;
        while(tickTime>tick) {
            tickTime-=tick;
            snake.advance();
            if(snake.checkBitten()) {
                gameOver = true;
                return;
            }
            SnakePart head = snake.parts.get(0);
            if(head.x == stain.x && head.y == stain.y) {
                score += SCORE_INCREMENT;
                snake.eat();
                if(snake.parts.size() == WORLD_WIDTH*WORLD_HEIGHT) {
                    gameOver = true;
                    return;
                } else {
                    placeStain();
                }

                if(score%100 == 0 && tick - TICK_DECREMENT > 0) tick -= TICK_DECREMENT;
            }
        }
    }
}
