import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Tilemap {
    GamePanel game;
    int[][] map = {
        {10,2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,11},
        {4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5},
        {4, 0, 0, 0, 0, 6, 6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5},
        {4, 0, 0, 0, 6, 5,10,13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5},
        {4, 0, 0, 0,12, 2,13, 1, 0, 0, 0, 0, 0, 0, 6, 6, 0, 0, 0, 5},
        {4, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 6, 5, 4, 0, 0, 0, 5},
        {4, 0, 0, 0, 6, 6, 6, 6, 0, 0, 0, 0, 0, 5, 3, 4, 0, 0, 0, 5},
        {4, 0, 0, 0, 5, 3, 3, 4, 0, 0, 0, 0, 0,12, 2,13, 0, 0, 0, 5},
        {4, 0, 0, 0,12, 2, 2,13, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 5},
        {4, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5},
        {4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5},
        {8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 9}
    };
    int width, height;
    BufferedImage tileImage0, tileImage1, tileImage2, tileImage3, tileImage4, tileImage5,
                  tileImage6, tileImage7, tileImage8, tileImage9, tileImage10, tileImage11,
                  tileImage12, tileImage13;
    BufferedImage tileImages[] = {
        tileImage0, tileImage1, tileImage2, tileImage3, tileImage4, tileImage5,
        tileImage6, tileImage7, tileImage8, tileImage9, tileImage10, tileImage11,
        tileImage12, tileImage13
    };


    Tilemap(GamePanel game){
        this.game = game;
        this.width = game.tileSize;
        this.height = game.tileSize;

        //Get the image for different tile types (type, xpos, ypos)

        getTileImage(1, 1, 1);
        getTileImage(2, 0, 0);
        getTileImage(3, 4, 6);
        getTileImage(4, 16, 2);
        getTileImage(5, 18, 2);
        getTileImage(6, 1, 0);
        getTileImage(7, 1, 1);
        getTileImage(8, 16, 3);
        getTileImage(9, 18, 3);
        getTileImage(10, 16, 0);
        getTileImage(11, 18, 0);
        getTileImage(12, 19, 2);
        getTileImage(13, 20, 2);
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
