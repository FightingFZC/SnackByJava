import util.DBUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Random;

/*
 *@author 帅帅付
 *2021/2/10
 *IntelliJ IDEA
 **/
public class GamingJPanel extends JPanel implements ActionListener, KeyListener {
    int foodX, foodY, score, length;
    int[] snackX = new int[600];
    int[] snackY = new int[500];
    Timer timer = new Timer(100, this);
    Random rf = new Random();
    boolean isStart, isFail;
    String fx = null;
    String username;

    public GamingJPanel() {
        init();
        setFocusable(true);
        addKeyListener(this);
        timer.start();
    }

    public void init() {
        setLayout(null);
        isStart = false;
        isFail = false;
        fx = "r";
        length = 2;
        score = 0;
        snackX[0] = 100;
        snackY[0] = 100;
        snackX[1] = 75;
        snackY[1] = 100;

        //接下来初始化食物的位置，要显示出来还是需要paintComponent方法
        foodX = 50 + 25 * rf.nextInt(32);
        foodY = 100 + 25 * rf.nextInt(23);

    }

    @Override
    //画容器
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        //最顶上的图片
        Snack.header.paintIcon(this, g, 5, 0);
        //活动区域
        g.fillRect(25, 75, 850, 600);

        //初始化食物
        Snack.food.paintIcon(this, g, foodX, foodY);

        //画蛇头
        if ("r".equals(fx)) {
            Snack.right.paintIcon(this, g, snackX[0], snackY[0]);
        } else if ("l".equals(fx)) {
            Snack.left.paintIcon(this, g, snackX[0], snackY[0]);
        } else if ("u".equals(fx)) {
            Snack.up.paintIcon(this, g, snackX[0], snackY[0]);
        } else if ("d".equals(fx)) {
            Snack.down.paintIcon(this, g, snackX[0], snackY[0]);
        }
        //画身子
        for (int i = 1; i < length; i++) {
            Snack.body.paintIcon(this, g, snackX[i], snackY[i]);
        }

        //画计分板
        g.setColor(Color.BLUE);
        g.setFont(new Font("微软雅黑", Font.BOLD, 14));
        g.drawString("分数：" + score, 800, 40);


        //暂停的时候出现的字样（因为失败了会设置isStart为false，所以要多加个条件）
        if (!isStart && !isFail) {
            g.setColor(Color.white);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("按下空格开始游戏!", 300, 300);
        }
        //失败后出现的字样
        if (isFail) {
            g.setColor(Color.white);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("您已失败，是否重新开始(Y/N)", 150, 300);
        }

    }


    //定时发生的动作。
    @Override
    public void actionPerformed(ActionEvent e) {

        if (isStart) {
            //判断是否出界，出界导致失败
            if (snackX[0] > 850 || snackX[0] < 25 || snackY[0] > 650 || snackY[0] < 75) {
                isFail = true;
                isStart = false;

                //现在还需要记录时间和分数
                //先读取本地临时文件的用户名是什么
                try {
                    FileReader reader = new FileReader("username.txt");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    username = bufferedReader.readLine();

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                //开始JDBC往数据库写记录
                Connection connection = null;
                Statement statement = null;
                try {
                    connection = DBUtil.getConnection();
                    statement = connection.createStatement();
                    statement.executeUpdate("insert into data values(" +
                            username + ",now()" + "," + score + ")");
                } catch (SQLException exception) {
                    exception.printStackTrace();
                } finally {
                    DBUtil.close(statement);
                    DBUtil.close(connection);
                }

            }

            //判断吃东西
            if (snackX[0] == foodX && snackY[0] == foodY) {
                foodX = 50 + 25 * rf.nextInt(32);
                foodY = 100 + 25 * rf.nextInt(23);
                length++;
                score += 10;
            }
            //判断小蛇撞到自己的身体
            for (int i = 1; i < length; i++) {
                if (snackX[0] == snackX[i] && snackY[0] == snackY[i]) {
                    score -= 10 * (length - 1 - i);
                    length = i + 1;//蛇会把自己身体咬断
                    snackX[length] = snackX[i];
                    snackY[length] = snackY[i];
                    break;
                }
            }
            //此处的for循环设计的很巧妙，即使转弯也不会出错。并且，也达到了吃食物增加身体的效果！！！太TM绝了
            for (int i = length - 1; i > 0; i--) {
                snackX[i] = snackX[i - 1];
                snackY[i] = snackY[i - 1];
            }
            //让小蛇的头转起来
            if ("r".equals(fx)) {
                snackX[0] += 25;
            } else if ("l".equals(fx)) {
                snackX[0] -= 25;
            } else if ("u".equals(fx)) {
                snackY[0] -= 25;
            } else if ("d".equals(fx)) {
                snackY[0] += 25;
            }
            //相对于视频的代码身体的前进的代码是放在前面的
            //因为不晓得为什么我这里吃一个东西，长度++，然后就会在0,0处也画个身体，直到
            //这个前进代码再次执行才会把那个身体画到蛇身后
            //解决了
            repaint();
        }
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (isFail) {
            if (e.getKeyCode() == KeyEvent.VK_Y) {
                init();
                repaint();
            } else if (e.getKeyCode() == KeyEvent.VK_N) {
                Container c = getParent();
                c.remove(this);
                c.add(new GameStartJPanel());
                c.validate();
                c.repaint();

            }
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && !isFail) {
            isStart = !isStart;
            repaint();
        }

        //键盘控制转向
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (!Objects.equals(fx, "l")) {
                fx = "r";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (!Objects.equals(fx, "r")) {
                fx = "l";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (!Objects.equals(fx, "d")) {
                fx = "u";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (!Objects.equals(fx, "u")) {
                fx = "d";
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


}