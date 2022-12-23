import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Tilemap {
    GamePanel game;
    int[][] map = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    int width, height;
    BufferedImage tileImage0, tileImage1;
    BufferedImage tileImages[] = {
        tileImage0, tileImage1
    };


    Tilemap(GamePanel game){
        this.game = game;
        this.width = game.tileSize;
        this.height = game.tileSize;

        //Get the image for different tile types (type, xpos, ypos)
        getTileImage(0, 2, 3);
        getTileImage(1, 1, 1);

    }

    public void draw(Graphics2D g2, int row, int col, int index){
        g2.drawImage(tileImages[index], col*game.tileSize, row*game.tileSize, width, height, null);
    }

    public void getTileImage(int tileImage, int col, int row){
        int tileSize = game.originalTileSize;
        try{
            tileImages[tileImage] = ImageIO.read(new File("sprites/spritesheet.png"));
            tileImages[tileImage] = tileImages[tileImage].getSubimage(col*tileSize, row*tileSize, tileSize, tileSize);

        }catch(IOException e){
            e.printStackTrace(); //prints stack trace. helpful for debug
        }
    }
}
