import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;
import java.awt.Point;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

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
    Player player = new Player(this, screenWidth/2, screenHeight/2);
    Weapon weapon = new Weapon(this);
    Tilemap tilemap = new Tilemap(this);
    Thread gameThread;
    Rectangle[] walls = {};
    Projectile[] projectiles = {};
    Enemy[] enemies = {};
    Coin[] coins = {};
    Cursor cursor = new Cursor(this);

    Boolean mouseClick = false;
    int mouseTimer;
    int enemySpawnTimer, numberOfEnemies;
    int maxEnemies = 7;
    int[] enemyPos;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        //Add collision for all wall tiles
        for(int row = 0; row < tilemap.map.length; row++){
            for(int col = 0; col < tilemap.map[0].length; col++){
                if(tilemap.map[row][col] != 0 && tilemap.map[row][col] != 1 && tilemap.map[row][col] != 6){
                    walls = AppendArray(walls, new Rectangle(col*tileSize, row*tileSize, tileSize, tileSize));
                }
            }
        }

        //set new cursor to an invisible one
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
        new ImageIcon("").getImage(),
        new Point(0,0),"custom cursor"));

        //check for mouse input. Toggle mouseClick variable (mouse button held down or not)
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent me){
                mouseClick = true;
            }
            public void mouseReleased(MouseEvent me){
                mouseClick = false;
            }
        });
    }

    //Append rectangle to an array
    public Rectangle[] AppendArray(Rectangle[] array, Rectangle element){
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length-1] = element;
        return array;
    }

    //Append projectile to projectile array
    public Projectile[] AppendProjectile(Projectile[] array, Projectile element){
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length-1] = element;
        return array;
    }

    //Append enemy to enemy array
    public Enemy[] AppendEnemy(Enemy[] array, Enemy element){
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length-1] = element;
        return array;
    }

    //Append coin to coin array
    public Coin[] AppendCoins(Coin[] array, Coin element){
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length-1] = element;
        return array;
    }
    
    //Check if rect is in range of another rect
    public Boolean inRange(Rectangle rect1, Rectangle rect2, int distance){
        double distanceFromRect = Math.sqrt(Math.pow(((rect1.x + rect1.width/2) - (rect2.x + rect2.width/2)), 2) + Math.pow(((rect1.y + rect1.height/2) - (rect2.y + rect2.height/2)), 2));
        if(distanceFromRect < distance){
            return true;
        } else{
            return false;
        }
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
        weapon.update();

        //Projectile creation
        mouseTimer++;
        //Check if mouse button is being held
        if(mouseClick){
            //If projectile cooldown is finished, create a new projectile
            if(mouseTimer > player.attackspeed){
                projectiles = AppendProjectile(projectiles, new Projectile(this));
                mouseTimer = 0;
            }
        }

        //enemy creation
        if(numberOfEnemies < maxEnemies){
            enemySpawnTimer++;
            //If projectile cooldown is finished, create a new projectile
            if(enemySpawnTimer > 30){
                //get position of enemy
                enemyPos = tilemap.getRandomFloorTile();
                //if enemy isnt inside of a wall
                if(enemyPos[0] != 0 && enemyPos[1] != 0){
                    //get distance between player and potential enemy
                    double distanceFromPlayer = Math.sqrt(Math.pow((enemyPos[0] - player.rect.x), 2) + Math.pow((enemyPos[1] - player.rect.y), 2));
                    //if potential enemy isnt too close to the player, create it
                    if(distanceFromPlayer > tileSize*3){
                        enemies = AppendEnemy(enemies, new Enemy(this, enemyPos[0], enemyPos[1]));
                        numberOfEnemies++;
                        enemySpawnTimer = 0;
                    }
                }
            }
        }

        //update enemies
        for(int i = 0; i < enemies.length; i++){
            //if enemy isnt deleted
            if(enemies[i] != null){
                //if enemy should be destroyed, set it to null (destroy it)
                if(enemies[i].destroy){
                    enemies[i] = null;
                } else{
                    enemies[i].update();
                }
            }
        }

        //update projectiles
        for(int i = 0; i < projectiles.length; i++){
            //if projectile isnt deleted
            if(projectiles[i] != null){
                //if projectile should be destroyed, set it to null (destroy it)
                if(projectiles[i].destroy){
                    projectiles[i] = null;
                } else{
                    projectiles[i].update();
                }
            }
        }

        //update coins
        for(int i = 0; i < coins.length; i++){
            //if coins isnt deleted
            if(coins[i] != null){
                //if coin should be destroyed, set it to null (destroy it)
                if(coins[i].destroy){
                    coins[i] = null;
                } else{
                    coins[i].update();
                }
            }
        }
    }

    //draw function
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        //draw tiles that are behind the player
        for(int row = 0; row < maxScreenRow; row++){
            for(int col = 0; col < maxScreenCol; col++){
                if(tilemap.map[row][col] == 1){
                    tilemap.draw(g2, row, col, tilemap.map[row][col]);
                }
            }
        }

        //draw player, draw weapon
        player.draw(g2);
        weapon.draw(g2);

        //draw tiles that are in front of the player
        for(int row = 0; row < maxScreenRow; row++){
            for(int col = 0; col < maxScreenCol; col++){
                if(tilemap.map[row][col] != 1){
                    tilemap.draw(g2, row, col, tilemap.map[row][col]);
                }
            }
        }

        //draw enemies
        for(int i = 0; i < enemies.length; i++){
            if(enemies[i] != null){
                enemies[i].draw(g2);
            }
        }

        //draw coins
        for(int i = 0; i < coins.length; i++){
            if(coins[i] != null){
                coins[i].draw(g2);
            }
        }

        //draw projectiles
        for(int i = 0; i < projectiles.length; i++){
            //If projectile isnt deleted, draw it.
            if(projectiles[i] != null){
                projectiles[i].draw(g2);
            }
        }

        //draw cursor
        cursor.draw(g2);

        g2.dispose();
    }
}
