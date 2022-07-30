package UI;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SeverInterface {
    JFrame jf = new JFrame("服务器监视窗口");
    final int WIDTH = 370;//370
    final int HEIGHT = 600;//600
    ServerSocket server=null;
    JTextArea showrArea = null;
    //菜单JMenuBar---顶部菜单集合    为了实现刷新组件，定义在这里
    JMenuBar jmb = new JMenuBar();
    //主题设置
    //列表存全部连接的客户端
    public static ArrayList<Socket> list=new ArrayList<Socket>();

    public void init() {
        //设置窗口相关属性
        jf.setBounds(50,100, WIDTH, HEIGHT);
        jf.setResizable(false);
        //设置布局格式
        jf.setLayout(new BorderLayout());

        //设置图标
        try {
            jf.setIconImage(ImageIO.read(new File("images/emi纸飞机(blue).png")));

        } catch (IOException e) {
            e.printStackTrace();
        }

        JMenu jMenu = new JMenu("设置");
        jMenu.setIcon(new ImageIcon("images/菜单.png"));
        jMenu.setSize(5, 5);
        JMenuItem m2 = new JMenuItem("退出程序");
        m2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //退出程序
                System.exit(0);
            }
        });
        jMenu.add(m2);
        jmb.add(jMenu);

        //显示框
        showrArea = new JTextArea(20, 20);

        //自动换行
        showrArea.setLineWrap(true);
        //文本域不可以编辑
        showrArea.setEditable(false);
        // 创建滚动面板, 指定滚动显示的视图组件(showarea), 垂直滚动条一直显示, 水平滚动条从不显示
        JScrollPane TextScollAreaPanel = new JScrollPane(
                showrArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
//        JPanel jPanel = new JPanel();
//        jPanel.add(TextScollAreaPanel);
//        jf.setContentPane(jPanel);
        jf.add(TextScollAreaPanel,BorderLayout.CENTER);


        showrArea.setText("有内容了");
        //窗口添加菜单
        jf.setJMenuBar(jmb);
        //设置窗口可见
        jf.setVisible(true);

        try {
            server = new ServerSocket(5000);
            while (true) {
                Socket socket = server.accept();
                showrArea.setText("----------服务器已启动----------\n");
                list.add(socket);
                new Thread(new ServerThread(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
        /**
         *多线程监听
         */
        class ServerThread implements Runnable {
            Socket socket;

            //每个线程有自己的输入输出流！！
            DataInputStream is=null;
            DataOutputStream os=null;

            String name=null;

            public ServerThread(Socket t)
            {
                this.socket = t;
                try {
                    is = new DataInputStream(this.socket.getInputStream());
                    os = new DataOutputStream(this.socket.getOutputStream());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            @Override
            public void run()
            {
                try
                {
                    while(true)
                    {
                        String str=is.readUTF();




                            //接收信息，第一次处理线程名字
                            if(name==null) {
                                //截取名称
                                name=str.substring(0,str.indexOf("上"));
                            }
                            showrArea.append(str+"\n");
                            for(Socket socket2:list)
                            {
                                //创建输出流
                                DataOutputStream os2 = new DataOutputStream(socket2.getOutputStream());
                                //输出信息
                                os2.writeUTF("##F#"+str+"@#");
                                //刷新
                                os2.flush();
                            }
                    }
                }catch(IOException e) {
                    //客户下线就移除
                    list.remove(this.socket);
                    //循环输出信息
                    for(Socket socket2:list) {
                        //创建输出流
                        try {
                            DataOutputStream os2 = new DataOutputStream(socket2.getOutputStream());
                            //输出信息
                            os2.writeUTF((name+"已经离开聊天室"));
                            //刷新
                            os2.flush();
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }

    public static void main(String[] args) {
        new SeverInterface().init();
    }

    }




