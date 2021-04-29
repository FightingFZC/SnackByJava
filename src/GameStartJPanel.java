import util.DBUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ResourceBundle;

/*
 *@author 帅帅付
 *2021/2/10
 *IntelliJ IDEA
 **/
public class GameStartJPanel extends JPanel {
    String username;

    public GameStartJPanel() {
        //初始化username
        try {
            FileReader fileReader = new FileReader("username.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            username = bufferedReader.readLine();
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
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

        //添加数据库相关按钮
        JButton passwordChange = new JButton("修改密码");
        passwordChange.setBounds(750, 41, 90, 20);
        passwordChange.setBackground(Color.GRAY);
        add(passwordChange);
        JButton changeID = new JButton("切换账户");
        changeID.setBounds(750, 61, 90, 20);
        changeID.setBackground(Color.GRAY);
        add(changeID);

        //修改密码
        passwordChange.addActionListener(e -> {
            JFrame pwChangeFrame = new JFrame();

            JPanel oldPwPanel = new JPanel();
            JPanel newPwPanel = new JPanel();
            JPanel executePanel = new JPanel();

            JLabel oldPwLabel = new JLabel("旧密码：");
            JLabel newPwLabel = new JLabel("新密码：");

            JButton executeButton = new JButton("确认");

            JPasswordField oldPwTextField = new JPasswordField(10);
            JPasswordField newPwTextField = new JPasswordField(10);


            pwChangeFrame.add(oldPwPanel);
            pwChangeFrame.add(newPwPanel);
            pwChangeFrame.add(executePanel);

            oldPwPanel.add(oldPwLabel);
            oldPwPanel.add(oldPwTextField);

            newPwPanel.add(newPwLabel);
            newPwPanel.add(newPwTextField);

            executePanel.add(executeButton);

            pwChangeFrame.setLayout(new GridLayout(3, 1));
            pwChangeFrame.setBounds(500, 200, 411, 200);
            pwChangeFrame.setVisible(true);

            //按下修改密码
            executeButton.addActionListener(e1 -> {
                String oldPw = String.valueOf(oldPwTextField.getPassword());
                String newPw = String.valueOf(newPwTextField.getPassword());
                Connection connection = null;
                Statement statement = null;
                ResultSet resultSet = null;
                try {
                    //注册驱动, 获取连接
                    connection = DBUtil.getConnection();
                    //获取数据库操作对象
                    statement = connection.createStatement();
                    //执行SQL语句
                    //在这里判断密码是否正确
                    resultSet = statement.executeQuery("select password from users " +
                            "where username = '" + username + "'"
                    );
                    if (resultSet.next()) {
                        String truePassword = resultSet.getString("password");
                        if (oldPw.equals(truePassword)) {
                            int count = statement.executeUpdate("update users set password = '" +
                                    newPw + "' where password = '" + oldPw + "' and username = '"
                                    + username + "'"
                            );
                            if (count == 1) {
                                //提示密码修改成功
                                JDialog error = new JDialog();
                                error.add(new JLabel("密码修改成功"));
                                error.pack();
                                error.setLocation(620, 250);
                                error.setVisible(true);
                                pwChangeFrame.dispose();
                            }

                        } else {
                            //提示密码错误，重新输入
                            JDialog error = new JDialog();
                            error.add(new JLabel("旧密码错误"));
                            error.pack();
                            error.setLocation(620, 250);
                            error.setVisible(true);
                        }
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                } finally {
                    DBUtil.close(resultSet);
                    DBUtil.close(statement);
                    DBUtil.close(connection);
                }
            });
        });

        //切换账户
        changeID.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new LoginScreen();
        });


        //接下来实现按钮的功能。都用Lambda实现，达到练习的效果.
        //游戏说明
        b1.addActionListener(e -> {
            JDialog jDialog = new JDialog();
            jDialog.setTitle(((JButton) (e.getSource())).getText());
            jDialog.setVisible(true);
            jDialog.setBounds(570, 290, 400, 300);
            //用html来换行
            JLabel jl = new JLabel("<html>WSAD来对小蛇进行上下左右的操作，吃到一个食物加10分，只有撞到墙会游戏失败，撞到身子会吃掉身子并扣分<html>");
            jl.setFont(new Font("宋体", Font.BOLD, 20));
            jDialog.add(jl);
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

        //历史分数
        b4.addActionListener(e -> {
            JFrame frameCount = new JFrame();
            JTextArea textAreaCount = new JTextArea();
            textAreaCount.setLineWrap(true);
            textAreaCount.setEditable(false);
            JScrollPane scrollPaneCount = new JScrollPane(
                    textAreaCount,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
            );
            frameCount.add(scrollPaneCount);
            frameCount.setVisible(true);
            frameCount.setBounds(600, 400, 300, 200);

            ResourceBundle rb = ResourceBundle.getBundle("JDBC");
            Connection connection = null;
            Statement statement = null;
            ResultSet resultSet = null;


            try {
                Class.forName(rb.getString("driver"));
                connection = DriverManager.getConnection(rb.getString("url"),
                        rb.getString("username"), rb.getString("password"));
                statement = connection.createStatement();
                resultSet = statement.executeQuery("select * from data " +
                        "where username = " + username);
                while (resultSet.next()) {
                    textAreaCount.append(resultSet.getString("username") +
                            " " + resultSet.getString("time") +
                            " " + resultSet.getString("score") + "\n");
                }
            } catch (ClassNotFoundException | SQLException exception) {
                exception.printStackTrace();
            } finally {
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                try {
                    if (statement != null) {
                        statement.close();
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
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
        //画当前的账户名
        g.setFont(new Font("微软雅黑", Font.BOLD, 14));
        g.drawString("当前账户：" + username, 750, 40);

    }
}
