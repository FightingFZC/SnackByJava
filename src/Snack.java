import javax.swing.*;
import java.net.URL;

/*
 *@author 帅帅付
 *2021/2/10
 *IntelliJ IDEA
 **/
public class Snack {
    public static URL headerURL = Snack.class.getResource("/statics/header.png");
    public static URL bodyURL = Snack.class.getResource("/statics/body.png");
    public static URL upURL = Snack.class.getResource("/statics/up.png");
    public static URL downURL = Snack.class.getResource("/statics/down.png");
    public static URL leftURL = Snack.class.getResource("/statics/left.png");
    public static URL rightURL = Snack.class.getResource("/statics/right.png");
    public static URL foodURL = Snack.class.getResource("/statics/food.png");

    public static ImageIcon header = new ImageIcon(headerURL);
    public static ImageIcon body = new ImageIcon(bodyURL);
    public static ImageIcon up = new ImageIcon(upURL);
    public static ImageIcon down = new ImageIcon(downURL);
    public static ImageIcon left = new ImageIcon(leftURL);
    public static ImageIcon right = new ImageIcon(rightURL);
    public static ImageIcon food = new ImageIcon(foodURL);

}
