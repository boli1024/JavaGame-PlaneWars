package shoot;

public class Bullet extends FlyingObject {
    private int speed = 3;

    public Bullet(int x,int y){
        image = Game.bullet;
        width = image.getWidth();
        height = image.getHeight();
        this.x = x;
        this.y = y;
    }

    @Override
    public void step(){
        y -= speed;
    }

    @Override
    public boolean outOfBounds() {
        return this.y < -Game.HEIGHT;
    }
}
