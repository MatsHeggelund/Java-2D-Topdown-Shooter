import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class Coin {
    GamePanel game;
    Rectangle rect;
    BufferedImage coinImage;
    double dx, dy, angle;
    int pullPower, pullRadius, collectRadius;
    Boolean destroy;
    
    public Coin(GamePanel game, int x, int y){
        this.game = game;
        this.pullPower = 15;
        this.pullRadius = game.tileSize;
        this.collectRadius = game.tileSize/2;
        this.destroy = false;

        getCoinImage();

        this.rect = new Rectangle();
        this.rect.x = x;
        this.rect.y = y;
        this.rect.height = 16*game.scale/2;
        this.rect.width = this.rect.height/2;
    }

    public void update(){
        //Move towards the player if within range
        if(game.inRange(this.rect, game.player.rect, this.pullRadius)){
            this.dx = game.player.rect.x - this.rect.x;
            this.dy = game.player.rect.y - this.rect.y;
            this.angle = Math.atan2(dy, dx);

            this.rect.x += this.pullPower * Math.cos(this.angle);
            this.rect.y += this.pullPower * Math.sin(this.angle);
        }
        if(game.inRange(this.rect, game.player.rect, game.player.rect.width)){
            this.destroy = true;
        }
    }

    public void draw(Graphics2D g2){
        g2.drawImage(coinImage, this.rect.x, this.rect.y, this.rect.width, this.rect.height, null);
    }

    public void getCoinImage(){
        try{
            coinImage = ImageIO.read(new File("sprites/coin.png"));
        } catch(IOException e){
            e.printStackTrace(); //prints stack trace. helpful for debug
        }
    }
}
