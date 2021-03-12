import javax.swing.*;
import java.awt.*;

/*
 *@author 帅帅付
 *2021/2/10
 *IntelliJ IDEA
 **/
public class GameStartJFrame extends JFrame {
    public GameStartJFrame() {
        init();
    }

    private void init() {
        setTitle("贪吃蛇-Version 1.0 ");
        setVisible(true);
        //这个大小也适合小蛇身体25*25
        setBounds(10, 10, 900, 720);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        Container c = getContentPane();
        //开始添加初始的面板
        c.add(new GameStartJPanel());

    }
}
