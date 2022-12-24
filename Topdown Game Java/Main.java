import java.awt.Color;
import javax.swing.JFrame;

public class Main{
    public static void main(String[] args){
        JFrame window = new JFrame();
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Java Game");

        GamePanel gamePanel = new GamePanel();
        Color backgroundColor = new Color(66, 40, 53);
        gamePanel.setBackground(backgroundColor);

        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);


        gamePanel.startGameThread();
    }
}