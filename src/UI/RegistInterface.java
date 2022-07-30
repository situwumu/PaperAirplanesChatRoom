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

public class RegistInterface{
    JFrame jf = new JFrame("注册页面");
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
        JLabel  uLable = new JLabel("用户名：");
        JTextField uFiled = new JTextField(15);
        //把组件放进uBox, uBox.add(Box.createHorizontalStrut(20)),用于添加水平间隔
        uBox.add(uLable);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(uFiled);

        //组装密码
        Box pBox = Box.createHorizontalBox();
        JLabel  pLable = new JLabel("密    码：");
        JTextField pFiled = new JTextField(15);

        pBox.add(pLable);
        pBox.add(Box.createHorizontalStrut(20));
        pBox.add(pFiled);

        //组装手机号
        Box tBox = Box.createHorizontalBox();
        JLabel  tLable = new JLabel("手机号：");
        JTextField tFiled = new JTextField(15);

        tBox.add(tLable);
        tBox.add(Box.createHorizontalStrut(20));
        tBox.add(tFiled);

        //组装性别，为了实现单选需要添加一个  ButtonGroup bg 把两个按钮添加到一起,默认单选男
        Box gBox = Box.createHorizontalBox();
        JLabel gLable = new JLabel("性    别");
        JRadioButton maleBtn = new JRadioButton("男",true);
        JRadioButton femaleBtn = new JRadioButton("女",false);

        //为了实现单选的效果
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(maleBtn);
        buttonGroup.add(femaleBtn);

        gBox.add(gLable);
        gBox.add(Box.createHorizontalStrut(20));
        gBox.add(maleBtn);
        gBox.add(femaleBtn);
        gBox.add(Box.createHorizontalStrut(120));

//        //组装验证码，未实现
//        Box cBox = Box.createHorizontalBox();
//        JLabel  cLable = new JLabel("验证码：");
//        JTextField cFiled = new JTextField(4);
//        JLabel cImge = new JLabel(new ImageIcon("images/emi纸飞机(yellow).png"));
//        //给某个组件设置提示信息
//        cImge.setToolTipText("点击刷新,此功能未实现");
//        cImge.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                cImge.setIcon(new ImageIcon("images/emi纸飞机(blue).png"));
//                cImge.updateUI();
//
//            }
//        });
//
//        cBox.add(cLable);
//        cBox.add(Box.createHorizontalStrut(20));
//        cBox.add(cFiled);
//        cBox.add(cImge);

        //组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton loginBtn = new JButton("注册");

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取用户录入的数据
                String username = uFiled.getText().trim();
                String password = pFiled.getText().trim();
                String telephone = tFiled.getText().trim();
                //判断哪一个按钮被选中,获取按钮上的文字信息 分为 男 或 女
               String gender = buttonGroup.isSelected(maleBtn.getModel())?maleBtn.getText():femaleBtn.getText();
                /*
                * 验证码功能未实现
                * */
//                String checkCode = cFiled.getText().trim();

                //验证密码，访问后台接口
                Users user = new Users(username,password,telephone,gender,0);
                Connection connection = null;
                try {
                    connection = DatabaseUtils.getConnection();
                    if (username.isEmpty() || password.isEmpty()){
                        JLabel jl = new JLabel("用户名或密码不能为空，请重新注册");
                        JOptionPane.showMessageDialog(jf,jl,"注册失败",JOptionPane.ERROR_MESSAGE);
                    } else if(UsersDao.ContainsUser(connection,username)){
                        JLabel jl = new JLabel("用户名已存在，请修改用户名后重新注册");
                        jf.setFont(new Font("微软雅黑",Font.PLAIN,15));
                        JOptionPane.showMessageDialog(jf,jl,"注册失败",JOptionPane.WARNING_MESSAGE);
                    }else {
                        UsersDao.Regist(connection,user);
                        JLabel jl = new JLabel("注册成功，将跳转到登录页面");
                        jf.setFont(new Font("微软雅黑",Font.PLAIN,15));
                        JOptionPane.showMessageDialog(jf,jl,"注册成功",JOptionPane.PLAIN_MESSAGE);
                        new MainInterface().init();
                        jf.dispose();

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

                //当前注册消失
                jf.dispose();
            }
        });

        btnBox.add(loginBtn);
        btnBox.add(Box.createHorizontalStrut(100));
        btnBox.add(backtBtn);

        //垂直vBox组装三个水平box，依然要添加垂直间隔vBox.add(Box.createVerticalStrut(50));
        vBox.add(Box.createVerticalStrut(50));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(tBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(gBox);
//        //验证码为实现
//        vBox.add(Box.createVerticalStrut(20));
//        vBox.add(cBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(btnBox);

        //把背景面板添加到窗口，vBox添加到背景面板
        bgPanel.add(vBox);
        jf.add(bgPanel);

        //设置窗口可见
        jf.setVisible(true);




    }
//    //注册页面测试程序端入口
//    public static void main(String[] args) {
//        new  RegistInterface().init();
//    }

}

