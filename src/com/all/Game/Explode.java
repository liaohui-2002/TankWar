package com.all.Game;

import com.all.Util.MyUtil;

import java.awt.*;

/**
 * @auther Mr.Liao
 * @date 2022/3/29 17:23
 */
public class Explode {
    public static final int EXPLODE_FRAME_COUNT = 12;
    //导入资源
    private static Image[] img;
    //爆炸效果的宽度和高度
    private static int explodeWidth;
    private static int explodeHeight;

    static {
        img = new Image[EXPLODE_FRAME_COUNT/2];
        for (int i = 0; i < img.length; i++) {
//            img[i] = Toolkit.getDefaultToolkit().createImage();
            img[i] = MyUtil.createImage("res/blast"+i+".gif");
        }
    }

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        index = 0;
    }

    public Explode() {
        index = 0;
    }

    //    爆炸效果的属性
    private int x,y;
    //当前播放帧的下标
    private int index;

    private boolean visible = true;
    public void draw(Graphics g){
        if (index>= EXPLODE_FRAME_COUNT){
            visible = false;
            index =0;
        }
        if(explodeHeight<=0){
            explodeWidth = img[0].getWidth(null);
            explodeHeight = img[0].getHeight(null);
        }
        if(!visible) return;
        g.drawImage(img[index/2],x-explodeWidth/2,y-explodeHeight/2,null);
        index++;

    }

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
