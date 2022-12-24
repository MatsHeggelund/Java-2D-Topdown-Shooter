import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Player {
    int x, y, width, height, speed, attackspeed, direction, flipImage;
    GamePanel game;
    BufferedImage playerImage;
    Rectangle rect;

    Player(GamePanel gamePanel, int x, int y){
        this.game = gamePanel;
        this.speed = 6;
        this.attackspeed = 15;
        //Sprite flipping variables based on mouse position
        this.direction = 1;
        this.flipImage = 0;

        this.rect = new Rectangle();
        this.rect.x = x;
        this.rect.y = y;
        this.rect.width = game.tileSize;
        this.rect.height = game.tileSize;

        getPlayerImage();
    }

    public void update(){
        //Change players location based on key input
        if(game.keyH.upPressed == true){
            this.move(0, -this.speed);
        }
        if(game.keyH.downPressed == true){
            this.move(0, this.speed);
        }
        if(game.keyH.leftPressed == true){
            this.move(-this.speed, 0);
        }
        if(game.keyH.rightPressed == true){
            this.move(this.speed, 0);
        }
    }

    public void move(int dx, int dy){
        if(dx!=0){
            this.collisionDetection(this.rect, dx, 0);
        }
        if(dy!=0){
            this.collisionDetection(this.rect, 0, dy);
        }
    }

    public void collisionDetection(Rectangle rectangle, int dx, int dy){
        rectangle.x += dx;
        rectangle.y += dy;

        for(int i = 0; i < game.walls.length; i++){
            if(rectangle.intersects(game.walls[i])){
                if(dx>0){
                    rectangle.x = game.walls[i].x - rectangle.width;
                }
                if(dx<0){
                    rectangle.x = game.walls[i].x + game.walls[i].width;
                }
                if(dy>0){
                    rectangle.y = game.walls[i].y - rectangle.height;
                }
                if(dy<0){
                    rectangle.y = game.walls[i].y + game.walls[i].height;
                }
            }
        }
    }

    public void draw(Graphics2D g2){
        //Draw the player based on position
        g2.drawImage(playerImage, this.rect.x + this.flipImage*this.rect.width, this.rect.y, this.rect.width*this.direction, this.rect.height, null);
        
    }

    public void getPlayerImage(){
        int tileSize = game.originalTileSize;
        try{
            playerImage = ImageIO.read(new File("sprites/spritesheet.png"));
            playerImage = playerImage.getSubimage(6*tileSize, 15*tileSize, tileSize, tileSize);
        } catch(IOException e){
            e.printStackTrace(); //prints stack trace. helpful for debug
        }
    }
}
