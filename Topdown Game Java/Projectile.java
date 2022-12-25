import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.lang.Math;

public class Projectile {
    GamePanel game;
    Rectangle rect;
    int x, y, width, height, speed, centerx, centery;
    int animationTick, animationIndex;
    double angle, rotAngle, dx, dy;
    Boolean destroy;

    String[] projectileSpritePaths = {
        "sprites/projectileImpact/projectileImpact1.png",
        "sprites/projectileImpact/projectileImpact2.png",
        "sprites/projectileImpact/projectileImpact3.png",
        "sprites/projectileImpact/projectileImpact4.png",
        "sprites/projectileImpact/projectileImpact5.png",
        "sprites/projectileImpact/projectileImpact6.png",
        "sprites/projectileImpact/projectileImpact7.png",
        "sprites/projectileImpact/projectileImpact8.png"
    };
    BufferedImage projectileImage, projectileImpact1, projectileImpact2, projectileImpact3, projectileImpact4,
                  projectileImpact5, projectileImpact6, projectileImpact7, projectileImpact8;
    BufferedImage[] projectileAnimation = {
        projectileImpact1, projectileImpact2, projectileImpact3, projectileImpact4,
        projectileImpact5, projectileImpact6, projectileImpact7, projectileImpact8
    };

    Projectile(GamePanel game){
        this.game = game;
        this.width = game.tileSize/2;
        this.height = game.tileSize/2;
        this.speed = 15;
        this.destroy = false;

        this.rect = new Rectangle();
        this.rect.x = game.weapon.x;
        this.rect.y = game.weapon.y;
        this.rect.width = this.width;
        this.rect.height = this.height;
        this.centerx = this.rect.x + this.width/2;
        this.centery = this.rect.y + this.height/2;

        this.dx = game.cursor.mx - this.rect.x;
        this.dy = game.cursor.my - this.rect.y;
        this.rotAngle = game.weapon.angle;

        for(int i = 0; i < projectileAnimation.length; i++){
            getProjectileImage(i);
        }

        try{
            this.projectileImage = ImageIO.read(new File("sprites/projectile.png"));
        } catch(IOException e){
            e.printStackTrace(); //prints stack trace. helpful for debug
        }
    }

    public void update(){
        if(this.speed > 0){
            this.angle = Math.atan2(this.dy, this.dx);
            this.rect.x += this.speed * Math.cos(this.angle);
            this.rect.y += this.speed * Math.sin(this.angle);
            this.projectileCollisionDetection();
        } else{
            if(this.animationTick == 3){
                if(this.animationIndex < 8){
                    this.projectileImage = this.projectileAnimation[this.animationIndex];
                } else{
                    this.destroy = true;
                }
                this.animationTick = 0;
                this.animationIndex += 1;
            }
            this.animationTick += 1;
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage rotImage = game.weapon.rotateImageByDegrees(this.projectileImage, this.rotAngle);
        g2.drawImage(rotImage, this.rect.x, this.rect.y, this.rect.width, this.rect.height, null);
    }

    public void projectileCollisionDetection(){
        //If projectile collides with wall
        for(int i = 0; i < game.walls.length; i++){
            if(this.rect.intersects(game.walls[i])){
                this.speed = 0;
            }

        }
        //if projectile collides with enemy
        for(int i = 0; i < game.enemies.length; i++){
            if(game.enemies[i] != null){
                if(this.rect.intersects(game.enemies[i].rect)){
                    this.speed = 0;
                    game.enemies[i].health--;
                    if(game.enemies[i].health <= 0){
                        game.enemies[i].destroy = true;
                        game.numberOfEnemies--;
                    }
                }
            }
        }
    }

    public void getProjectileImage(int animationIndex){
        try{
            projectileAnimation[animationIndex] = ImageIO.read(new File(projectileSpritePaths[animationIndex]));
        } catch(IOException e){
            e.printStackTrace(); //prints stack trace. helpful for debug
        }
    }
}
