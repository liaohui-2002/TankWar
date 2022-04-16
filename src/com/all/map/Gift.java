package com.all.map;

import com.all.Util.Constant;
import com.all.Util.MyUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @auther Mr.Liao
 * @date 2022/4/11 18:08
 */
public class Gift {
    public static int giftW = 40;
    public static int radius = giftW>>1;

    //导入图片
    public static Image star;
     static {
         star =  MyUtil.createImage("res/star.gif");
     }
     private int x,y;//图片左上角右上角坐标
     private boolean visible = true;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Gift() {
        x = MyUtil.getRandomNumber(0, Constant.FRAME_WIDTH-giftW);
        y = MyUtil.getRandomNumber(0,Constant.FRAME_HEIGHT-giftW);
    }

    public Gift(int x, int y) {
        this.x = x;
        this.y = y;
        if(giftW<=0){
            giftW = star.getWidth(null);
        }
    }

    public void draw(Graphics g){
        if(!visible) return;
        if(giftW<=0){
            giftW = star.getWidth(null);
        }
        //System.out.println("调用了一次礼物绘画");
        g.drawImage(star,x,y,null);
    }
}
