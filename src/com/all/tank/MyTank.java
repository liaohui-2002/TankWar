package com.all.tank;

import com.all.Util.MyUtil;

import java.awt.*;

/**
 * @auther Mr.Liao
 * @date 2022/3/29 16:01
 */
//drawImage 填入的x、y是图形的左上角坐标
/**
 * 玩家坦克
 */
public class MyTank extends Tank{
    //坦克的图片数组
    private static Image[] tankImg;

    static {
        tankImg = new Image[4];
        tankImg[0] = MyUtil.createImage("res/p1tankU.gif");
        tankImg[1] = MyUtil.createImage("res/p1tankD.gif");
        tankImg[2] = MyUtil.createImage("res/p1tankL.gif");
        tankImg[3] = MyUtil.createImage("res/p1tankR.gif");
    }
    public MyTank(int x, int y, int dir) {
        super(x, y, dir);
    }

    @Override
    public void drawImgTank(Graphics g) {
        g.drawImage(tankImg[getDir()], getX() - RADIUS, getY() - RADIUS, null);
    }
}
