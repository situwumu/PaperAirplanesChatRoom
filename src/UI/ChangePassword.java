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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.util.logging.ErrorManager;

/**
 * 注册页面
 *
 */

public class ChangePassword{
    public static String username;
    JFrame jf = new JFrame("修改密码页面");
    BackGroundPanel bgPanel;
    final int WIDTH = 500;
    final int HEIGHT = 300;
    public void init(){
        //设置窗口相关属性
        jf.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        jf.setResizable(false);
        //设置图标
        try {
            jf.setIconImage(ImageIO.read(new File("images/emi纸飞机(yellow).png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置窗口的内容
        try {
            //设置背景
            bgPanel = new BackGroundPanel(ImageIO.read(new File("images/emi纸飞机(blue).png")));
            bgPanel.setBounds(0,0,WIDTH,HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //组装其它相关元素,垂直vBox 用于最后添加三个uBox
        Box vBox = Box.createVerticalBox();

        //组装用户名
        Box uBox = Box.createHorizontalBox();
        JLabel  oldPassword = new JLabel("原密码：");
        JPasswordField  oldFiled = new JPasswordField (15);
        //把组件放进uBox, uBox.add(Box.createHorizontalStrut(20)),用于添加水平间隔
        uBox.add(oldPassword);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(oldFiled);

        //组装新密码
        Box pBox = Box.createHorizontalBox();
        JLabel  password_1 = new JLabel("新密码：");
        JPasswordField  pFiled = new JPasswordField (15);

        pBox.add(password_1);
        pBox.add(Box.createHorizontalStrut(20));
        pBox.add(pFiled);

        //组装确认密码
        Box tBox = Box.createHorizontalBox();
        JLabel  password_2 = new JLabel("确认密码：");
        JPasswordField  pFiled_1 = new JPasswordField (15);

        tBox.add(password_2);
        tBox.add(Box.createHorizontalStrut(15));
        tBox.add(pFiled_1);


        //组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton sureBtn = new JButton("确认修改");
        sureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // 获取用户录入的数据
                String oldPwd = String.valueOf(oldFiled.getPassword());
                String mewPwd_1 = String.valueOf(pFiled.getPassword());
                String newPwd_2 = String.valueOf(pFiled_1.getPassword());
//            //判断密码

                Connection connection = null;
                try {
                    connection = DatabaseUtils.getConnection();
                    if (oldPwd.isEmpty() || mewPwd_1.isEmpty() ||newPwd_2.isEmpty()){
                        JLabel jl = new JLabel("输入内容不能为空，请重新输入");
                        JOptionPane.showMessageDialog(jf,jl,"修改密码失败",JOptionPane.ERROR_MESSAGE);
                    } else if ( !newPwd_2.equals(mewPwd_1)){
                        JLabel jl = new JLabel("两次输入密码不同，请重新输入");
                        jf.setFont(new Font("微软雅黑",Font.PLAIN,15));
                        JOptionPane.showMessageDialog(jf,jl,"注册失败",JOptionPane.WARNING_MESSAGE);

                    }else if(UsersDao.ContainsByUserAndPassword(connection,username,oldPwd)){
                        UsersDao.ChangePassword(connection,username,newPwd_2);
                        JLabel jl = new JLabel("修改成功，将跳转到登录页面");
                        jf.setFont(new Font("微软雅黑",Font.PLAIN,15));
                        JOptionPane.showMessageDialog(jf,jl,"注册成功",JOptionPane.PLAIN_MESSAGE);
                        new MainInterface().init();
                        jf.dispose();

                    }else {
                        JLabel jl = new JLabel("原密码错误，重新输入");
                        jf.setFont(new Font("微软雅黑",Font.PLAIN,15));
                        JOptionPane.showMessageDialog(jf,jl,"注册失败",JOptionPane.WARNING_MESSAGE);
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


        JButton backtBtn = new JButton("返回登录页面");

        backtBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //跳转到登录界面
                try {
                    new MainInterface().init();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                //当前页面消失
                jf.dispose();
            }
        });

        btnBox.add(sureBtn);
        btnBox.add(Box.createHorizontalStrut(100));
        btnBox.add(backtBtn);

        //垂直vBox组装三个水平box，依然要添加垂直间隔vBox.add(Box.createVerticalStrut(50));
        vBox.add(Box.createVerticalStrut(50));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(tBox);
        vBox.add(Box.createVerticalStrut(20));;
        vBox.add(btnBox);

        //把背景面板添加到窗口，vBox添加到背景面板
        bgPanel.add(vBox);
        jf.add(bgPanel);

        //设置窗口可见
        jf.setVisible(true);




    }
    //注册页面测试程序端入口
    public static void main(String[] args) {
        new  ChangePassword().init();
    }

}

