package UI;

import component.BackGroundPanel;
import component.Users;
import component.UsersDao;
import javafx.scene.layout.Background;
import util.DatabaseUtils;
import util.ScreenUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;

/**
 * 登录页面
 *
 */

public class MainInterface{
    JFrame jf = new JFrame("这是一个有关JDBC和Swing的系统");
    BackGroundPanel bgPanel;
    final int WIDTH = 500;
    final int HEIGHT = 300;
    public void init(){
        //设置窗口相关属性
        jf.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        jf.setResizable(false);
        //设置图标
        try {
            jf.setIconImage(ImageIO.read(new File("images/emi纸飞机(red).png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置窗口的内容
        try {
            //设置背景
            bgPanel = new BackGroundPanel(ImageIO.read(new File("images/emi纸飞机(blue).png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //组装其它相关元素,垂直vBox 用于最后添加三个uBox
        Box vBox = Box.createVerticalBox();

        //组装用户名
        Box uBox = Box.createHorizontalBox();
        JLabel  uLable = new JLabel("用户名：");
        JTextField uFiled = new JTextField(15);
        //把组件放进uBox, ubox.add(Box.createHorizontalStrut(20)),用于添加水平间隔
        uBox.add(uLable);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(uFiled);

        //组装密码
        Box pBox = Box.createHorizontalBox();
        JLabel  pLable = new JLabel("密    码：");
        JPasswordField  pFiled = new JPasswordField (15);

        pBox.add(pLable);
        pBox.add(Box.createHorizontalStrut(20));
        pBox.add(pFiled);

        //组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton loginBtn = new JButton("登录");

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取用户输入的数据
                String username = uFiled.getText().trim();
                String password = String.valueOf(pFiled.getPassword());

                //验证密码及访问登录界面

                Users user = new Users(username,password);
                Connection connection = null;
                try {
                    connection = DatabaseUtils.getConnection();
                    Users currentUser = UsersDao.Login(connection,user);
                    if (username.isEmpty() || password.isEmpty()){
                        JLabel jl = new JLabel("用户名或密码不能为空，请重新登录");
                        jf.setFont(new Font("微软雅黑",Font.PLAIN,15));
                        JOptionPane.showMessageDialog(jf,jl,"登录失败",JOptionPane.ERROR_MESSAGE);
                    } else if(currentUser != null){
                        JLabel jl = new JLabel("欢迎登录聊天室");
                        jf.setFont(new Font("微软雅黑",Font.PLAIN,15));
                        JOptionPane.showMessageDialog(jf,jl,"登录成功",JOptionPane.PLAIN_MESSAGE);
                        //实现窗口名称和 登录名称相同
                        ChatRoomInterface.username = username;
                        new ChatRoomInterface().init();
                        jf.dispose();
                    }else {
                        JLabel jl = new JLabel("用户名或密码错误，请重新登录");
                        jf.setFont(new Font("微软雅黑",Font.PLAIN,15));
                        JOptionPane.showMessageDialog(jf,jl,"登录失败",JOptionPane.WARNING_MESSAGE);

                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }finally {
                    try {
                        DatabaseUtils.closeConnection(connection);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });


        JButton registBtn = new JButton("注册");

        registBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //跳转到注册界面
//                JOptionPane.showMessageDialog(jf,"准备跳转到登录界面！！！");
                try {
                    new RegistInterface().init();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                //当前登录页面消失
                jf.dispose();

            }
        });

        btnBox.add(loginBtn);
        btnBox.add(Box.createHorizontalStrut(100));
        btnBox.add(registBtn);

        //垂直vBox组装三个水平box，依然要添加垂直间隔vBox.add(Box.createVerticalStrut(50));
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pBox);
        vBox.add(Box.createVerticalStrut(30));
        vBox.add(btnBox);

        //把背景面板添加到窗口，vBox添加到背景面板
        bgPanel.add(vBox);
        jf.add(bgPanel);

        //设置窗口可见
        jf.setVisible(true);




   }

}

