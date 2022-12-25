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
    Boolean destroy, hit;

    BufferedImage enemyImage;
    String[] enemyImages = {
        "sprites/enemies/ghost1.png", "sprites/enemies/ghost2.png", "sprites/enemies/ghost3.png"
    };
    String[] enemyHitImages = {
        "sprites/enemies/ghost1hit.png", "sprites/enemies/ghost2hit.png", "sprites/enemies/ghost3hit.png"
    };

    double angle;
    int health, speed, dx, dy, spriteIndex, direction, flipImage, hitTimer;
    Enemy(GamePanel game, int x, int y){
        this.game = game;
        this.speed = 2;
        this.direction = 1;
        this.flipImage = 0;
        this.destroy = false;
        this.health = 3;
        this.hit = false;

        this.rect = new Rectangle();
        this.rect.x = x;
        this.rect.y = y;
        this.rect.width = game.tileSize*2/3;
        this.rect.height = game.tileSize*2/3;

        this.spriteIndex = ThreadLocalRandom.current().nextInt(0, this.enemyImages.length);
        getEnemyImage(enemyImages, spriteIndex);
    }

    public void update(){
        //if enemy is in range of the player
        if(this.inRange(game.player.rect, game.tileSize*7)){
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

        //if enemy collides with projectile
        if(this.hit){ 
            //if this is the first iteration of update() while enemy is hit
            if(this.hitTimer == 0){
                //Change enemy sprite to hit sprite (white flash)
                getEnemyImage(enemyHitImages, spriteIndex);
                //Knock the enemy away from the player
                this.dx = game.player.rect.x - this.rect.x;
                this.dy = game.player.rect.y - this.rect.y;
                this.angle = Math.atan2(dy, dx);
    
                this.rect.x -= this.speed * Math.cos(this.angle)*15;
                this.rect.y -= this.speed * Math.sin(this.angle)*15;
            }
            this.hitTimer++;
            //change enemy sprite back 
            if(this.hitTimer > 10){
                this.hit = false;
                this.hitTimer = 0;
                getEnemyImage(enemyImages, spriteIndex);
            }
        }
    }

    //draw the enemy
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
    
    //get enemies sprite
    public void getEnemyImage(String[] spriteArray, int spriteIndex){
        try{
            enemyImage = ImageIO.read(new File(spriteArray[spriteIndex]));
            
        } catch(IOException e){
            e.printStackTrace(); //prints stack trace. helpful for debug
        }
    }
}
