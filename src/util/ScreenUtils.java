package util;
import java.awt.*;
/*
* 获取当前屏幕的高度和宽度
* */


public class ScreenUtils {
    /**
     *宽度
     */
    public static int getScreenWidth(){ return Toolkit.getDefaultToolkit().getScreenSize().width;}
    /**
     *高度
     */
    public static int getScreenHeight(){return Toolkit.getDefaultToolkit().getScreenSize().height;}

}
