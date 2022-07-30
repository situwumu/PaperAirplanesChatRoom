package UI;

import com.mysql.cj.protocol.a.MysqlBinaryValueDecoder;
import com.sun.org.apache.bcel.internal.generic.SWITCH;
import component.BackGroundPanel;
import component.Users;
import component.UsersDao;
import jdk.nashorn.internal.scripts.JO;
import util.DatabaseUtils;
import util.ScreenUtils;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;

public class ChatRoomInterface {
    //设置当前窗口登录人的信息
     public static String username = null;
     private static String IP = "127.0.0.1";
     private static int PORT = 5000;
     private boolean isConnect = false;

    Socket socket = null;
    DataInputStream is = null;
    DataOutputStream os = null;
    //消息窗口
    JTextArea jTextAreaRight = null;

    JFrame jf = new JFrame("聊天室");

    BackGroundPanel bgPanel;
    final int WIDTH = 700;//700
    final int HEIGHT = 432;//432
    //菜单JMenuBar---顶部菜单集合    为了实现刷新组件，定义在这里
    JMenuBar jmb = new JMenuBar();
    //主题设置


    public void init() {
        //设置窗口相关属性
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false);
        jf.setTitle("聊天室： " + username + "，欢迎您");
        jf.setLayout(new BorderLayout(20, 5));
        //设置图标
        try {
            jf.setIconImage(ImageIO.read(new File("images/emi纸飞机(red).png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置窗口的内容
        try {
            //设置背景
            bgPanel = new BackGroundPanel(ImageIO.read(new File("images/emi纸飞机(yellow).png")));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //设置菜单栏

        JMenu jMenu = new JMenu("设置");
        jMenu.setIcon(new ImageIcon("images/菜单.png"));
        jMenu.setSize(5, 5);
        JMenuItem m1 = new JMenuItem("切换账号");
        JMenuItem m2 = new JMenuItem("退出程序");
        JMenuItem m3 = new JMenuItem("更改密码");
        m1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new MainInterface().init();
                    jf.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        m2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //退出程序
                System.exit(0);
            }
        });
        m3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangePassword.username = username;
                new ChangePassword().init();
                jf.dispose();
            }
        });
        jMenu.add(m1);
        jMenu.addSeparator();
        jMenu.add(m2);
        jMenu.add(m3);
        jmb.add(jMenu);

        //设置主题栏
        JMenu jMenu1 = new JMenu("主题");
        jMenu1.setIcon(new ImageIcon("images/菜单2.png"));
        ButtonGroup bg = new ButtonGroup();

        JRadioButtonMenuItem metalItem = new JRadioButtonMenuItem("Metal 风格");
        JRadioButtonMenuItem nimbusItem = new JRadioButtonMenuItem("Nimbus 风格");
        JRadioButtonMenuItem windowsItem = new JRadioButtonMenuItem("Windows 风格");
        JRadioButtonMenuItem windowsClassicItem = new JRadioButtonMenuItem("Windows 今典风格");
        JRadioButtonMenuItem motifitem = new JRadioButtonMenuItem("Motif 风格");

        bg.add(metalItem);
        bg.add(nimbusItem);
        bg.add(motifitem);
        bg.add(windowsItem);
        bg.add(windowsClassicItem);
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //当前是哪一种风格
                String actionCommand = e.getActionCommand();
                try {
                    changeFlavor(actionCommand);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
        //监听器注册给按钮
        metalItem.addActionListener(actionListener);
        nimbusItem.addActionListener(actionListener);
        motifitem.addActionListener(actionListener);
        windowsItem.addActionListener(actionListener);
        windowsClassicItem.addActionListener(actionListener);


        jMenu1.add(metalItem);
        jMenu1.add(nimbusItem);
        jMenu1.add(motifitem);
        jMenu1.add(metalItem);
        jMenu1.add(windowsItem);
        jMenu1.add(windowsClassicItem);
        jmb.add(jMenu1);


        //声明文本域
//        JTextArea jTextAreaLeft = new JTextArea(20,31);
        jTextAreaRight = new JTextArea(20, 35);
        //自动换行
        jTextAreaRight.setLineWrap(true);
        //文本域不可以编辑
        jTextAreaRight.setEditable(false);
        // 创建滚动面板, 指定滚动显示的视图组件(showarea), 垂直滚动条一直显示, 水平滚动条从不显示
        JScrollPane TextScollAreaPanel = new JScrollPane(
                jTextAreaRight,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );


        //组装两个文本框
//        jf.add(jTextAreaLeft,BorderLayout.WEST);
        jf.add(TextScollAreaPanel, BorderLayout.EAST);
        //声明选择相关组件
        JLabel stLabel = new JLabel("状态选择");
        JComboBox<String> statusChoose = new JComboBox<String>();
        statusChoose.addItem("在线");
        statusChoose.addItem("隐身");
        statusChoose.addItem("离线");
        statusChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 0 在线 1 隐身 2 离线
                int s = statusChoose.getSelectedIndex();
                try {
                    Connection connection = DatabaseUtils.getConnection();
                    UsersDao.setSatus(connection, username, s);
                    tellServerStatus(s);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JLabel ipLable = new JLabel("服务器iP:");
        JTextField ipTextField = new JTextField(10);
        ipTextField.setText(IP);
        JLabel portLable = new JLabel("端口号  :");
        JTextField portTextField = new JTextField(10);
        portTextField.setText(String.valueOf(PORT));

        JButton coonBtn = new JButton("连接服务器");
        JButton colseConnBtn = new JButton("断开连接");

        coonBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IP = ipTextField.getText().trim();
                PORT = Integer.valueOf(portTextField.getText().trim());
                if (!isConnect){
                    StartSocketConnect();
                }else {
                    JLabel jl = new JLabel("已和服务器建立连接，如果想要更换服务器，请先断开连接");
                    jf.setFont(new Font("微软雅黑",Font.PLAIN,15));
                    JOptionPane.showMessageDialog(jf,jl,"请勿反复点击连接按钮",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        colseConnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isConnect){
                    closeScoketConnect();
                }else {
                    JLabel jl = new JLabel("已和服务器断开，无需重复点击");
                    jf.setFont(new Font("微软雅黑",Font.PLAIN,15));
                    JOptionPane.showMessageDialog(jf,jl,"请勿反复点击连接",JOptionPane.WARNING_MESSAGE);
                }


                System.out.println("断开连接按钮已生效");
            }
        });

        //组装选择相关组件
        JPanel selectPanel = new JPanel();
        selectPanel.add(stLabel);
        selectPanel.add(statusChoose);
        selectPanel.add(ipLable);
        selectPanel.add(ipTextField);
        selectPanel.add(portLable);
        selectPanel.add(portTextField);
        selectPanel.add(coonBtn);
        selectPanel.add(colseConnBtn);
        jf.add(selectPanel, BorderLayout.NORTH);

        //声明发送框 和 发送按钮
        JTextField sendField = new JTextField(25);
        JButton sendBtn = new JButton("发送");
        //为发送按钮添加监听
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //不允许发送空消息
                if(sendField.getText().isEmpty()) {return;}
                //获取输入框当前的内容,并加上用户姓名
                String text = username + ">: " + sendField.getText();
                //写出数据
                try {
                    os.writeUTF(text);
                    os.flush();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                sendField.setText("");//清空信息编辑框

            }
        });

        //组装底部
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(sendField);
        bottomPanel.add((sendBtn));
        jf.add(bottomPanel, BorderLayout.SOUTH);

        //添加背景
        jf.add(bgPanel);
        //窗口添加菜单
        jf.setJMenuBar(jmb);
        //设置窗口可见
        jf.setVisible(true);


    }

    /**
     * 新建socket，建立Client
     */
      private void StartSocketConnect(){
            try {
                socket = new Socket(IP, PORT);
                is = new DataInputStream(socket.getInputStream());
                os = new DataOutputStream(socket.getOutputStream());

                new Thread(new Read(socket)).start();//创建从服务器读数据的线程
                String s = "@"+username +" 已上线";

                os.writeUTF(s);//向服务器发送信息
                os.flush();//刷新

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isConnect = true;
        }


    /**
     *
     * 发送消息给服务器说在线状态
     *
     */
        private  void tellServerStatus(int status){
            if(status == 2  ){
                String message = username + " 已下线";
                try {
                    os.writeUTF(message);//向服务器发送信息
                    os.flush();//刷新
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if (status == 0){
                String message = username + " 已上线";
                try {
                    os.writeUTF(message);//向服务器发送信息
                    os.flush();//刷新
                }catch (Exception e){
                    e.printStackTrace(); }
            }else {
                return;
            }
            isConnect = false;
        }

    /**
     * 告诉服务器状态
     */
    private void closeScoketConnect(){
//            String message = username + " 已离开聊天室";
//            try {
//                os.writeUTF(message);//向服务器发送信息
//                os.flush();//刷新
//                os.close();
//                socket.close();
//
//            }catch (Exception e){
//                e.printStackTrace(); }
            ChatRoomInterface chatRoomInterface = new ChatRoomInterface();
            ChatRoomInterface.username = username;
            chatRoomInterface.init();
            jf.dispose();


        }


    //读取线程
    class Read implements Runnable
    {
        Socket socket;
        public Read() {}
        public Read(Socket t)
        {
            this.socket = t;
        }
        @Override
        public void run()
        {
            try
            {
                while(true)
                {
                    String str="";
                    str=is.readUTF();
                    jTextAreaRight.append(str+"\n");
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

    }//Read


















































    //定义一个方法，用于改变界面方格
    private void changeFlavor(String cmmand) throws Exception{
        switch (cmmand){
            case "Metal 风格":
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                break;
            case "Nimbus 风格":
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                break;
            case "Windows 风格":
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                break;
            case "Windows 经典风格":
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");

                break;
            case "Motif 风格":
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");

                break;
            default:break;

        }
        //刷新组件的外观
        SwingUtilities.updateComponentTreeUI(jf.getContentPane());
        SwingUtilities.updateComponentTreeUI(jf);
        SwingUtilities.updateComponentTreeUI(jmb);
    }

    //聊天室测试程序入口
    public static void main(String[] args) {
        new ChatRoomInterface().init();
    }
}
