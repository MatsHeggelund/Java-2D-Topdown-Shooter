import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.Math;

public class Weapon {
    GamePanel game;
    BufferedImage weaponImage;
    int x, y, width, height, centerx, centery;
    double angle;

    Weapon(GamePanel game){
        this.game = game;
        this.width = game.tileSize;
        this.height = game.tileSize;
        this.x = game.player.rect.x;
        this.y = game.player.rect.y;
        this.angle = 0.0;
        this.centerx = this.x + this.width/2;
        this.centery = this.y + this.height/2;

        getWeaponImage();
    }

    public void update(){
        this.x = game.player.rect.x;
        this.y = game.player.rect.y + this.height/3;
        this.centerx = this.x + this.width/2;
        this.centery = this.y + this.height/2;
    }

    public void draw(Graphics2D g2){
        int correctionAngle = -45;
        int dx = game.cursor.mx - this.centerx;
        int dy = game.cursor.my - this.centery;
        this.angle = Math.toDegrees(-Math.atan2(-dy, dx)) - correctionAngle;

        BufferedImage rotImage = rotateImageByDegrees(weaponImage, this.angle);

        g2.drawImage(rotImage, this.x, this.y, this.width, this.height, null);
    }

    //Get rotated copy of weapon image based on angle
    public static BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
        int w = img.getWidth();    
        int h = img.getHeight();
    
        BufferedImage rotated = new BufferedImage(w, h, img.getType());  
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w/2, h/2);
        graphic.drawImage(img, null, 0, 0);
        graphic.dispose();
        return rotated;
    }

    public void getWeaponImage(){
        try{
            weaponImage = ImageIO.read(new File("sprites/weapon.png"));
        } catch(IOException e){
            e.printStackTrace(); //prints stack trace. helpful for debug
        }
    }
}
