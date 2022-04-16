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
    public static final int TANK_LOW  =1;
    public static final int TANK_UP  =2;
    //坦克的图片数组
    private static Image[] tankImg1;
    private static Image[] tankImg2;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private int level;

    static {
        tankImg1 = new Image[4];
        tankImg2 = new Image[4];
        tankImg1[0] = MyUtil.createImage("res/p1tankU.gif");
        tankImg1[1] = MyUtil.createImage("res/p1tankD.gif");
        tankImg1[2] = MyUtil.createImage("res/p1tankL.gif");
        tankImg1[3] = MyUtil.createImage("res/p1tankR.gif");

        tankImg2[0] = MyUtil.createImage("res/newp1U.gif");
        tankImg2[1] = MyUtil.createImage("res/newp1D.gif");
        tankImg2[2] = MyUtil.createImage("res/newp1L.gif");
        tankImg2[3] = MyUtil.createImage("res/newp1R.gif");
    }
    public MyTank(int x, int y, int dir) {
        super(x, y, dir);
        this.level= TANK_LOW;
    }

    @Override
    public void drawImgTank(Graphics g) {
        if (this.level == TANK_LOW) {
            g.drawImage(tankImg1[getDir()], getX() - RADIUS, getY() - RADIUS, null);
        }
        else if(this.level == TANK_UP){
            g.drawImage(tankImg2[getDir()], getX() - RADIUS, getY() - RADIUS, null);
        }
    }
    @Override
    public void enhance(){
        this.level = TANK_UP;
        this.setHp(maxHp);
        this.setAtk(ATK_MAX*2);
        this.setSpeed(DEFAULT_SPEED*2);
    }
}
