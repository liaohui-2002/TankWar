package com.all.map;
import com.all.Game.Bullet;
import com.all.Util.BulletsPool;
import com.all.Util.MyUtil;
import java.awt.*;
import java.util.List;

/**
 * 地图元素快
 * @auther Mr.Liao
 * @date 2022/3/30 15:51
 */
public class MapTile {

    public static int tileW  =60;
    public  static  int radius = tileW>>1;
    private String name;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HOUSE = 1;
    public static final int TYPE_COVER = 2;
    public static final int TYPE_HARD = 3;
    public static final int TYPE_WATER = 4;

    private int type = TYPE_NORMAL;
    //资源导入
    private static Image[] tileImg;
    static {
        tileImg= new Image[5];
        tileImg[TYPE_NORMAL] = MyUtil.createImage("res/walls.gif");
        tileImg[TYPE_HOUSE] = MyUtil.createImage("res/house.gif");
        tileImg[TYPE_COVER] = MyUtil.createImage("res/cover.gif");
        tileImg[TYPE_HARD] = MyUtil.createImage("res/hard.gif");
        tileImg[TYPE_WATER] = MyUtil.createImage("res/water.gif");
        if(tileW <= 0){
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //xy分别是图片资源左上角的坐标
    private int x,y;
    private boolean visible = true;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public MapTile(int x, int y) {
        this.x = x;
        this.y = y;
        if(tileW <= 0){
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
        }
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

    public MapTile() {
    }

    public void draw(Graphics g){
        if(!visible) return;
        if(tileW <= 0){
            tileW = tileImg[TYPE_NORMAL].getWidth(null);
        }
        g.drawImage(tileImg[type],x,y,null);
        //绘制块上的名字
        if(name!=null){
            g.setColor(Color.WHITE);
            g.drawString(name,x,y);
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 地图块和若干子弹是否有碰撞
     * @param bullets
     * @return
     */
    public boolean isCollideBullet(List<Bullet> bullets){
        if(!visible || type == TYPE_COVER || type == TYPE_WATER)return false;
        for (Bullet bullet : bullets) {
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();

            boolean collide = MyUtil.isCollide(x + radius, y + radius, radius, bulletX, bulletY);
            if(collide){
                //子弹销毁
                bullet.setVisible(false);
                BulletsPool.theReturn(bullet);
                return true;
            }
        }
        return false;
    }
    //判断当前地图块是不是老巢
    public boolean isHouse(){
        return type == TYPE_HOUSE;
    }
}
