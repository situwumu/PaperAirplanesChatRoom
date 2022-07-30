package component;

import javax.swing.*;
import java.awt.*;

/**
 * 背景面板
 */

public class BackGroundPanel extends JPanel {
    //声明图片
    private Image backIcon;
    public BackGroundPanel(Image backIcon){
        this.backIcon = backIcon;
    }
    @Override
    public void paintComponent(Graphics g){
        //绘制背景,注意是paintComponent,用顶级容器绘制背景图片
        super.paintComponent(g);
        g.drawImage(backIcon,0,0,this.getWidth(),this.getHeight(),null);

    }


}
