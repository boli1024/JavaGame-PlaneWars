package shoot;

import java.util.Random;

public class EnemyPlane extends FlyingObject implements Enemy {

    private int speed=2;

    public EnemyPlane(){
        image = Game.enemyPlane;
        width = image.getWidth();
        height = image.getHeight();

        Random random = new Random();
        x = random.nextInt(Game.WIDTH-width);
        y = -height;
    }

    @Override
    public int getScore() {
        return 5;
    }

    @Override
    public void step() {
        y += speed;
    }

    @Override
    public boolean outOfBounds() {
        return y>Game.HEIGHT;
    }
}
