package hyu.hauk.snake.world;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by hauk on 17. 1. 29.
 */
public class Snake {
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;

    public List<SnakePart> parts = new ArrayList<SnakePart>();
    public int direction;

    public Snake() {
        direction = UP;
        parts.add(new SnakePart(World.WORLD_WIDTH/2,World.WORLD_HEIGHT/2+1));
        parts.add(new SnakePart(World.WORLD_WIDTH/2,World.WORLD_HEIGHT/2));
        parts.add(new SnakePart(World.WORLD_WIDTH/2,World.WORLD_HEIGHT/2-1));
    }

    public void turnLeft() {
        if(direction == DOWN) {
            direction = LEFT;
            return;
        }
        direction+=1;
        if(direction>RIGHT) direction=UP;
    }
    public void turnRight() {
        if(direction == DOWN) {
            direction = RIGHT;
            return;
        }
        direction-=1;
        if(direction<UP) direction=RIGHT;
    }
    public void eat() {
        SnakePart end = parts.get(parts.size()-1);
        parts.add(new SnakePart(end.x, end.y));
    }
    public void advance() {
        SnakePart head = parts.get(0);

        int len = parts.size()-1;
        for(int i=len; i>0; i--) {
            SnakePart before = parts.get(i-1);
            SnakePart part = parts.get(i);
            part.x = before.x;
            part.y = before.y;
        }
        switch(direction) {
            case UP:
                head.y+=1;
                break;
            case LEFT:
                head.x-=1;
                break;
            case DOWN:
                head.y-=1;
                break;
            case RIGHT:
                head.x+=1;
                break;
        }

        if(head.x<0) head.x=World.WORLD_WIDTH-1;
        else if(head.x>=World.WORLD_WIDTH) head.x=0;
        if(head.y<0) head.y=World.WORLD_HEIGHT-1;
        else if(head.y>=World.WORLD_HEIGHT) head.y=0;
    }
    public boolean checkBitten() {
        int len = parts.size();
        SnakePart head = parts.get(0);
        for(int i=1; i<len; i++) {
            SnakePart part = parts.get(i);
            if(part.x == head.x && part.y == head.y) return true;
        }
        return false;
    }
}
