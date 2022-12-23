import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    // Screen Settings
    final int scale = 6;
    final int originalTileSize = 16;
    final int tileSize = originalTileSize*scale;

    final int maxScreenCol = 20;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize*maxScreenCol;
    final int screenHeight = tileSize*maxScreenRow;

    final int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Player player = new Player(this, tileSize*3, tileSize*3);
    Tilemap tilemap = new Tilemap(this);
    Thread gameThread;
    Rectangle[] walls = {};
    Cursor cursor = new Cursor(this);

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        for(int row = 0; row < tilemap.map.length; row++){
            for(int col = 0; col < tilemap.map[0].length; col++){
                if(tilemap.map[row][col] != 0 && tilemap.map[row][col] != 1 && tilemap.map[row][col] != 6){
                    walls = AppendArray(walls, new Rectangle(col*tileSize, row*tileSize, tileSize, tileSize));
                }
            }
        }

    }

    public Rectangle[] AppendArray(Rectangle[] walls, Rectangle wall){
        walls = Arrays.copyOf(walls, walls.length + 1);
        walls[walls.length-1] = wall;
        return walls;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    //Game loop
    @Override
    public void run() {
        //Define time variables
        double drawInterval = 10e8/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1){
                //Update function
                update();
                //Draw function
                repaint();
                delta--;
            }
        }
    }

    public void update(){
        player.update();
        cursor.update();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        for(int row = 0; row < maxScreenRow; row++){
            for(int col = 0; col < maxScreenCol; col++){
                tilemap.draw(g2, row, col, tilemap.map[row][col]);
            }
        }
        player.draw(g2);
        cursor.draw(g2);

        g2.dispose();
    }
}
