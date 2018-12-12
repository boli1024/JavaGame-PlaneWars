package shoot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends JPanel {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;

    public static BufferedImage background;
    public static BufferedImage hero0;
    public static BufferedImage hero1;
    public static BufferedImage enemyPlane;
    public static BufferedImage bullet;

    FlyingObject[] flyings = {};
    Bullet[] bullets = {};
    Hero hero = new Hero();

    public static int flyEnterIndex = 0;
    public static int shootIndex = 0;

    int score=0;

    private java.util.Timer timer;

    static {
        try{
            hero0 = ImageIO.read(Game.class.getResource("hero0.png"));
            hero1 = ImageIO.read(Game.class.getResource("hero1.png"));
            background = ImageIO.read((Game.class.getResource("background.png")));
            enemyPlane = ImageIO.read((Game.class.getResource("enemyPlane.png")));
            bullet = ImageIO.read(Game.class.getResource("bullet.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public Game(){
        flyings = new FlyingObject[]{new EnemyPlane(),new EnemyPlane(),new EnemyPlane()};
        bullets = new Bullet[]{new Bullet(200,200)};
    }

    public void printHero(Graphics g){
        g.drawImage(hero.image,hero.x,hero.y,null);
    }

    public void printBullets(Graphics g){
        for (Bullet bullet : bullets) {
            g.drawImage(bullet.image, bullet.x, bullet.y, null);
        }
    }

    public void printFlyObjects(Graphics g){
        for (FlyingObject flyingObject : flyings) {
            g.drawImage(flyingObject.image, flyingObject.x, flyingObject.y, null);
        }
    }

    public void printScore(Graphics g){
        Font font = new Font(Font.SANS_SERIF,Font.BOLD,20);
        g.setFont(font);
        g.setColor(new Color(255, 0, 0));
        g.drawString("SCORE:"+score,10,20);
        g.drawString("LIFE:"+hero.getLife(),10,40);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background,0,0,null);
        printHero(g);
        printBullets(g);
        printFlyObjects(g);
        printScore(g);
    }

    public void stepAction(){
        hero.step();
        for(FlyingObject flyingObject : flyings){
            flyingObject.step();
        }
        for(Bullet bullet : bullets){
            bullet.step();
        }
    }

    public void enterAction(){
        flyEnterIndex++;
        if(flyEnterIndex % 40 == 0){
            FlyingObject flyingObject = nextOne();
            flyings = Arrays.copyOf(flyings,flyings.length + 1);
            flyings[flyings.length - 1] = flyingObject;
        }
    }

    public void action(){
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                enterAction();
                stepAction();
                shootAction();
                bangAction();
                outOfBoundsAction();
//                System.out.println("敌人数量:"+flyings.length+ "  "+"子弹数量:"+bullets.length);
                checkGameOver();
                repaint();
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,10);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                hero.x = x-hero.width/2;
                hero.y = y-hero.height/2;
            }
        };
        this.addMouseMotionListener(mouseAdapter);
    }

    public static FlyingObject nextOne(){
        return new EnemyPlane();
    }

    public void shootAction(){
        shootIndex++;
        if(shootIndex % 30 == 0){
            Bullet[] bullet = hero.shoot();
            bullets = Arrays.copyOf(bullets,bullets.length+bullet.length);
            System.arraycopy(bullet,0,bullets,bullets.length-bullet.length,bullet.length);
        }
    }

    public void bangAction(){
        for(Bullet bullet : bullets){
            bang(bullet);
        }
    }

    private void bang(Bullet bullet){
        int index = -1;
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject flyingObject = flyings[i];
            boolean flag = flyingObject.shootBy(bullet);
            if (flag) {
                index = i;
                break;
            }
        }
        if(index != -1){
            FlyingObject flyingObject = flyings[index];
            if(flyingObject instanceof Enemy){
                Enemy enemy = (Enemy)flyingObject;
                score += enemy.getScore();
            }
            FlyingObject deleteObject = flyings[index];
            flyings[index] = flyings[flyings.length-1];
            flyings[flyings.length-1] = deleteObject;
            flyings = Arrays.copyOf(flyings,flyings.length-1);
        }
    }

    public void outOfBoundsAction(){
        FlyingObject[] flyingLives = new FlyingObject[flyings.length];
        int index=0;

        for(FlyingObject flyingObject : flyings){
            if(!flyingObject.outOfBounds()){
                flyingLives[index++] = flyingObject;
            }
        }
        flyings = Arrays.copyOf(flyingLives,index);

        index=0;

        Bullet[] bulletLives = new Bullet[bullets.length];
        for(Bullet bullet : bullets){
            if(!bullet.outOfBounds()){
                bulletLives[index++] = bullet;
            }
        }
        bullets = Arrays.copyOf(bulletLives,index);

    }

    public boolean isGameOver(){
        for(int count=0;count<flyings.length;count++){
            FlyingObject flyingObject = flyings[count];
            int index = -1;
            if(hero.hit(flyingObject)){
                hero.subtractLife();
                index = count;
            }
            if(index != -1){
                FlyingObject flyingObject1 = flyings[index];
                flyings[index] = flyings[flyings.length-1];
                flyings[flyings.length-1] = flyingObject1;
                flyings = Arrays.copyOf(flyings,flyings.length-1);
            }
        }
        return hero.getLife() <= 0;
    }

    public void checkGameOver(){
        if(isGameOver()){
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("飞机大战");
        Game gamePanel = new Game();
        frame.add(gamePanel);
        frame.setSize(WIDTH,HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        gamePanel.paint(frame.getGraphics());
        gamePanel.action();
    }
}
