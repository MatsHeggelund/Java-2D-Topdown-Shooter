import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy {
    GamePanel game;
    Rectangle rect;
    Boolean destroy;

    BufferedImage enemyImage;
    String[] enemyImages = {
        "sprites/enemies/ghost1.png", "sprites/enemies/ghost2.png", "sprites/enemies/ghost3.png"
    };

    double angle;
    int health, speed, dx, dy, spriteIndex, direction, flipImage;
    Enemy(GamePanel game, int x, int y){
        this.game = game;
        this.speed = 2;
        this.direction = 1;
        this.flipImage = 0;
        this.destroy = false;
        this.health = 3;

        this.rect = new Rectangle();
        this.rect.x = x;
        this.rect.y = y;
        this.rect.width = game.tileSize*2/3;
        this.rect.height = game.tileSize*2/3;

        this.spriteIndex = ThreadLocalRandom.current().nextInt(0, this.enemyImages.length);
        getEnemyImage(spriteIndex);
    }

    public void update(){
        if(this.inRange(game.player.rect, game.tileSize*6)){
            //Change enemies facing direction based on position relative to player
            if(game.player.rect.x > this.rect.x){
                this.direction = -1;
                this.flipImage = 1;
            } else{
                this.direction = 1;
                this.flipImage = 0;
            }

            //Move the enemy towards the player
            this.dx = game.player.rect.x - this.rect.x;
            this.dy = game.player.rect.y - this.rect.y;
            this.angle = Math.atan2(dy, dx);

            this.rect.x += this.speed * Math.cos(this.angle);
            this.rect.y += this.speed * Math.sin(this.angle);
        }
    }

    public void draw(Graphics2D g2){
        g2.drawImage(enemyImage, this.rect.x + this.flipImage*this.rect.width, this.rect.y, this.rect.width*this.direction, this.rect.height, null);
    }

    //Check if the enemy is within range of the player
    public Boolean inRange(Rectangle rect, int distance){
        double distanceFromRect = Math.sqrt(Math.pow((this.rect.x - rect.x), 2) + Math.pow((this.rect.y - rect.y), 2));
        if(distanceFromRect < distance){
            return true;
        } else{
            return false;
        }
    }
    
    public void getEnemyImage(int spriteIndex){
        try{
            enemyImage = ImageIO.read(new File(enemyImages[spriteIndex]));
            
        } catch(IOException e){
            e.printStackTrace(); //prints stack trace. helpful for debug
        }
    }
}
