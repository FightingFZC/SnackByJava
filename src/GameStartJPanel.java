import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
 *@author 帅帅付
 *2021/2/10
 *IntelliJ IDEA
 **/
public class GameStartJPanel extends JPanel {

    public GameStartJPanel() {
        init();
    }

    //初始化界面
    private void init() {

        setLayout(null);


        JButton b1 = initJButton("游戏说明", 135);
        JButton b2 = initJButton("开始游戏", 250);
        JButton b3 = initJButton("结束游戏", 365);
        JButton b4 = initJButton("历史分数", 480);
        add(b1);
        add(b2);
        add(b3);
        add(b4);

        //接下来实现按钮的功能。都用Lambda实现，达到练习的效果.
        //游戏说明（待完善）
        b1.addActionListener(e -> {
            JDialog jDialog = new JDialog();
            jDialog.setTitle(((JButton) (e.getSource())).getText());
            jDialog.setVisible(true);
            jDialog.setBounds(570, 290, 400, 300);
        });

        //游戏开始
        b2.addActionListener(e -> {
            Container parent = this.getParent();
            parent.remove(this);
            GamingJPanel gamingJPanel = new GamingJPanel();
            parent.add(gamingJPanel);
            //这个代码验证JFrame，达到刷新的效果。
            parent.validate();
            //这个代码让gamingJPanel重新获得焦点，省得还得最小化再打开键盘监听器才有用
            gamingJPanel.requestFocus();
            parent.repaint();

        });

        //游戏结束
        b3.addActionListener(e -> {
            JDialog jDialog = new JDialog();
            jDialog.setTitle(((JButton) (e.getSource())).getText());
            jDialog.setVisible(true);
            jDialog.setBounds(670, 365, 200, 150);
            jDialog.setLayout(null);

            JLabel jLabel = new JLabel("你的小可爱已离你远去");
            jLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
            jLabel.setBounds(10, 5, 180, 35);
            jLabel.setHorizontalAlignment(SwingConstants.CENTER);
            jDialog.add(jLabel);

            //以下设置点击确定时和退出弹窗时都退出JVM虚拟机
            JButton end = new JButton("确定");
            end.setBounds(65, 70, 60, 40);
            end.addActionListener(e2 -> System.exit(0));
            jDialog.add(end);
            jDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        });

        //历史分数（待完善）
        b4.addActionListener(e -> {

        });

    }

    //创建按钮,初始化位置
    public JButton initJButton(String name, int y) {
        JButton b = new JButton(name);
        b.setBounds(300, y, 300, 70);
        setFont(new Font("微软雅黑", Font.BOLD, 40));
        return b;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Snack.header.paintIcon(this, g, 0, 10);

    }
}
