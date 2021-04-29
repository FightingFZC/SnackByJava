import util.DBUtil;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginScreen extends JFrame {
    JPanel usernamePanel, passwordPanel, loginButtonPanel;
    JTextField usernameTextField;
    JPasswordField passwordTextField;
    JButton loginButton;
    JLabel usernameLabel, passwordLabel;

    public LoginScreen() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        usernamePanel = new JPanel();
        passwordPanel = new JPanel();
        loginButtonPanel = new JPanel();


        usernameLabel = new JLabel("用户名：");
        passwordLabel = new JLabel("密码：");

        loginButton = new JButton("登陆");

        usernameTextField = new JTextField(10);
        passwordTextField = new JPasswordField(10);


        this.add(usernamePanel);
        this.add(passwordPanel);
        this.add(loginButtonPanel);

        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTextField);

        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordTextField);

        loginButtonPanel.add(loginButton);

        this.setLayout(new GridLayout(3, 1));
        this.setBounds(500, 200, 411, 200);
        this.setVisible(true);

        //添加按钮监听器
        loginButton.addActionListener(e -> {

            //ResourceBundle绑定配置文件
            Connection connection = null;
            Statement statement = null;
            ResultSet resultSet = null;
            String username = usernameTextField.getText();
            String password = String.valueOf(passwordTextField.getPassword());
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
                    if (password.equals(truePassword)) {
                        setVisible(false);
                        new GameStartJFrame();
                        createTempFile(username);
                    } else {
                        //提示密码错误，重新输入s
                        JDialog error = new JDialog();
                        error.add(new JLabel("密码错误"));
                        error.pack();
                        error.setLocation(620, 250);
                        error.setVisible(true);
                    }
                } else {
                    statement.executeUpdate("insert into users(username, password)" +
                            "values(" + username + "," + password + ")");
                    createTempFile(username);
                    this.dispose();
                    new GameStartJFrame();
                }

            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                DBUtil.close(resultSet);
                DBUtil.close(statement);
                DBUtil.close(connection);
            }
        });
    }

    private void createTempFile(String username) {
        try {
            FileWriter fileWriter = new FileWriter("username.txt");
            fileWriter.write(username);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




