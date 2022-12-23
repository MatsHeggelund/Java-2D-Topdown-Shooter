import java.awt.MouseInfo;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.awt.image.BufferedImage;
import java.awt.Point;

public class Cursor {
    int mx, my;
    int width, height;
    int pos;
    GamePanel game;
    BufferedImage cursorImage;
    Cursor(GamePanel game){
        this.game = game;
        this.width = game.tileSize*2/3;
        this.height = game.tileSize*2/3;
        getCursorImage();
    }

    public void update(){
        //get mouse position
        Point pt = new Point(this.game.getLocation());
        SwingUtilities.convertPointToScreen(pt, game);
        this.mx = MouseInfo.getPointerInfo().getLocation().x - pt.x;
        this.my = MouseInfo.getPointerInfo().getLocation().y - pt.y;

        //Flip player sprite based on mouse position
        if(this.mx < game.player.rect.x){
            game.player.direction = -1;
            game.player.flipImage = 1;
        } else{
            game.player.direction = 1;
            game.player.flipImage = 0;
        }
    }

    public void draw(Graphics2D g2){
        g2.drawImage(cursorImage, this.mx-this.width/2, this.my-this.height/2, this.width, this.height, null);
    }

    public void getCursorImage(){
        try{
            cursorImage = ImageIO.read(new File("sprites/cursor.png"));
        } catch(IOException e){
            e.printStackTrace(); //prints stack trace. helpful for debug
        }
    }
}
