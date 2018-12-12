package shoot;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject {
    private int life;
    private BufferedImage images[];
    private int index;

    public Hero(){
        image = Game.hero0;
        width = image.getWidth();
        height = image.getHeight();
        x = 150;
        y = 400;
        life = 3;
        images = new BufferedImage[]{Game.hero0,Game.hero1};
    }

    @Override
    public void step() {
        index++;
        int number = index % 2;
        image = images[number];
    }

    @Override
    public boolean outOfBounds() {
        return false;
    }

    public Bullet[] shoot(){
        Bullet[] bullets = new Bullet[1];
        bullets[0] = new Bullet(x+width/2,y-20);
        return bullets;
    }

    public void addLife(){
        life++;
    }

    public void subtractLife(){
        life--;
    }

    public int getLife(){
        return life;
    }

    public boolean hit(FlyingObject flyingObject){
        int minX = flyingObject.x - this.width/2;
        int maxX = flyingObject.x + flyingObject.width + this.width/2;

        int minY = flyingObject.y - this.height/2;
        int maxY = flyingObject.y + flyingObject.height + this.height/2;

        int heroCenterX = this.x + this.width/2;
        int heroCenterY = this.y + this.height/2;

        return heroCenterX < maxX && heroCenterX > minX && heroCenterY < maxY && heroCenterY > minY;
    }
}
