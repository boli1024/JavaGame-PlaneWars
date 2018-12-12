package shoot;

import java.awt.image.BufferedImage;

public abstract class FlyingObject {
    public int x;
    public int y;
    public BufferedImage image;
    public int width;
    public int height;

    public abstract void step();

    public boolean shootBy(Bullet bullet){
        int bulletX = bullet.x;
        int bulletY = bullet.y;

        int enemyMinX = this.x;
        int enemyMaxX = this.x + this.width;

        int enemyMinY = this.y;
        int enemyMaxY = this.y + this.height;

        return bulletX > enemyMinX && bulletX < enemyMaxX && bulletY > enemyMinY && bulletY < enemyMaxY;
    }

    public abstract boolean outOfBounds();
}
