package com.all.tank;

import com.all.Game.Bullet;
import com.all.Game.Explode;
import com.all.Game.GameFrame;
import com.all.Util.*;
import com.all.map.Gift;
import com.all.map.MapTile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther Mr.Liao
 * @date 2022/3/27 21:09
 * 坦克类
 */
public abstract class Tank {

    //四个方向
    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;
    //半径
    public static final int RADIUS = 30;
    //默认速度 ,每帧跑4像素
    public static final int DEFAULT_SPEED = 4;

    //坦克的状态
    public static final int STATE_STAND = 0;
    public static final int STATE_MOVE = 1;
    public static final int STATE_DIE = 2;

    //坦克的生命值
    public static final int DEFAULT_HP = 100;
    public static int maxHp = DEFAULT_HP;

    private int x, y;

    private int hp = DEFAULT_HP;//血量
    private int atk;//攻击力
    public static final int ATK_MAX = 25;
    public static final int ATK_MIN = 15;
    private int speed = DEFAULT_SPEED;
    private int dir;
    private int state = STATE_STAND;


    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }

    private Color color;
    private boolean isEnemy = false;

    private BloodBar bar = new BloodBar();
    // 炮弹
    private List<Bullet> bullets = new ArrayList();

    //使用容器保存当前坦克身上的爆炸效果
    private List<Explode> explodes = new ArrayList<>();

    public Tank(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;

        initTank();
    }

    public Tank() {
        initTank();
    }

    public void initTank() {
        color = MyUtil.getRandomColor();
        atk = MyUtil.getRandomNumber(ATK_MIN, ATK_MAX);
    }


    /**
     * 坦克的绘制
     *
     * @param g
     */
    public void draw(Graphics g) {
        logic();
        drawImgTank(g);
        drawBullets(g);
        bar.draw(g);
    }


    /**
     * 使用图片方式绘制坦克
     *
     * @param g
     */
    public abstract void drawImgTank(Graphics g);
    /**
     * 使用系统方式绘制坦克
     *
     * @param g
     */
    private void drawTank(Graphics g) {
        g.setColor(color);

        //绘制坦克的圆形
        g.fillOval(x - RADIUS, y - RADIUS, RADIUS << 1, RADIUS << 1);
        int endX = x, endY = y;

        switch (dir) {
            case DIR_UP:
                endY = y - RADIUS * 2;
                g.drawLine(x - 1, y, endX - 1, endY);
                g.drawLine(x + 1, y, endX + 1, endY);
                break;
            case DIR_DOWN:
                endY = y + RADIUS * 2;
                g.drawLine(x - 1, y, endX - 1, endY);
                g.drawLine(x + 1, y, endX + 1, endY);
                break;
            case DIR_LEFT:
                endX = x - RADIUS * 2;
                g.drawLine(x, y - 1, endX, endY - 1);
                g.drawLine(x, y + 1, endX, endY + 1);
                break;
            case DIR_RIGHT:
                endX = x + RADIUS * 2;
                g.drawLine(x, y - 1, endX, endY - 1);
                g.drawLine(x, y + 1, endX, endY + 1);
                break;
        }
        g.drawLine(x, y, endX, endY);
    }


    //坦克的逻辑处理
    private void logic() {
        switch (state) {
            case STATE_STAND:
                break;
            case STATE_MOVE:
                move();
                break;
            case STATE_DIE:
                break;
        }
    }

    //坦克的移动功能
    private int oldX = -1, oldY = -1;

    private void move() {
        oldX = x;
        oldY = y;
        switch (dir) {
            case DIR_UP:
                y -= speed;
                if (y < RADIUS + GameFrame.titleBarH) {
                    y = RADIUS + GameFrame.titleBarH;
                }
                break;
            case DIR_DOWN:
                y += speed;
                if (y > Constant.FRAME_HEIGHT - RADIUS) {
                    y = Constant.FRAME_HEIGHT - RADIUS;
                }
                break;
            case DIR_LEFT:
                x -= speed;
                if (x < RADIUS) {
                    x = RADIUS;
                }
                break;
            case DIR_RIGHT:
                x += speed;
                if (x > Constant.FRAME_WIDTH - RADIUS) {
                    x = Constant.FRAME_WIDTH - RADIUS;
                }
                break;
        }
    }

    public static int getDirUp() {
        return DIR_UP;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List getBullets() {
        return bullets;
    }

    public void setBullets(List bullets) {
        this.bullets = bullets;
    }


    //（上次）开火时间
    private long fireTime;
    //子弹发射最小时候间隔 0.5s
    public static final int FIRE_INTERVAL = 300;

    /**
     * 坦克开火
     * 创建了一个子弹对象属性信息，子弹对象的属性信息通过坦克的信息获得
     * 然后将创建的子弹添加到坦克的弹夹
     */
    public void fire() {
        if (System.currentTimeMillis() - fireTime > FIRE_INTERVAL) {
            int bulletX = x;
            int bulletY = y;
            switch (dir) {
                case DIR_UP:
                    bulletY -= RADIUS;
//                if(this.getClass().equals(EnemyTank))
//                bulletX = x + RADIUS / 2 + 5;
                    break;
                case DIR_DOWN:
                    bulletY += RADIUS;
//                bulletX = x + RADIUS / 2 + 5;
                    break;
                case DIR_LEFT:
                    bulletX -= RADIUS;
//                bulletY = y + RADIUS / 2 + 5;
                    break;
                case DIR_RIGHT:
                    bulletX += RADIUS;
//                bulletY = y + RADIUS / 2 + 5;
                    break;
            }
            Bullet bullet = BulletsPool.get();
            bullet.setAtk(atk);
            bullet.setDir(dir);

            bullet.setX(bulletX);
            bullet.setY(bulletY);
            bullet.setColor(color);

            bullet.setVisible(true);
//        Bullet bullet = new Bullet(bulletX, bulletY, dir, atk, color);
            bullets.add(bullet);
            //发射子弹后记录发射时间
            fireTime = System.currentTimeMillis();
            //开火音乐
            MusicUtil.playFire();
        }
//        System.out.println("发射一颗子弹，子弹数目："+(BulletsPool.DEFAULT_POOL_SIZE-bullets.size()));
    }

    /**
     * 将当前坦克发射的子弹绘制
     *
     * @param g
     */
    private void drawBullets(Graphics g) {
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        //遍历子弹 把不可见的子弹移除并归还
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (!bullet.isVisible()) {
                Bullet remove = bullets.remove(i);
                //还原到对象池中
                BulletsPool.theReturn(remove);
                i--;
//                System.out.println("归还一颗子弹，子弹数目："+(BulletsPool.DEFAULT_POOL_SIZE-bullets.size()));
            }
        }
    }

    /**
     * 坦克销毁时候处理坦克所有子弹
     */
    public void bulletsReturn() {
        for (Bullet bullet : bullets) {
            BulletsPool.theReturn(bullet);
        }
        bullets.clear();
    }

    //坦克和子弹碰撞的方法
    public void collideBullets(List<Bullet> bullets) {
        //遍历所有的子弹，依次和当前坦克进行碰撞检测
        for (Bullet bullet : bullets) {
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            //子弹和坦克撞上了
            if (MyUtil.isCollide(x, y, RADIUS, bulletX, bulletY)) {
                //子弹消失
                bullet.setVisible(false);
                //坦克受伤
                hurt(bullet);
                //添加爆炸效果
                addExplode(bulletX, bulletY);
            }
        }
    }

    //添加爆炸效果
    private void addExplode(int x, int y) {
        Explode explode = ExplodesPool.get();
        explode.setX(x);
        explode.setY(y);
        explode.setVisible(true);
        MusicUtil.playBomb();
        explodes.add(explode);

    }

    //坦克受到伤害
    private void hurt(Bullet bullet) {
        int atk = bullet.getAtk();
        hp -= atk;
        if (hp < 0) {
            hp = 0;
            die();
        }
    }


    //坦克死亡 TODO
    private void die() {
        if (isEnemy) {
            GameFrame.killEnemyCount++;
            //敌人坦克被消灭 还回对象池
            EnemyTanksPool.theReturn(this);
            //判断是否通过本关
            if (GameFrame.isCrossLevel()) {
                //判断是否通关
                if (GameFrame.isLastLevel()) {
                    //通关
                    GameFrame.setGameState(Constant.STATE_WIN);
                } else {
                    //TODO 进入下一关
                    GameFrame.startCrossLevel();
//                   GameFrame.nextLevel();
                }
            }
        } else {
            delaySecondsToOver(1000);
            //game over
            GameFrame.setGameState(Constant.STATE_LOST);

        }
    }

    /**
     * 判断当前坦克是否死亡
     *
     * @return
     */
    public boolean isDie() {
        return hp <= 0;
    }

    /**
     * 绘制当前坦克上的所有的爆炸效果
     *
     * @param g
     */
    public void drawExplodes(Graphics g) {
        for (Explode explode : explodes) {
            explode.draw(g);
        }
        //将不可见的爆炸效果删除 还回对象池
        for (int i = 0; i < explodes.size(); i++) {
            if (!explodes.get(i).isVisible()) {
                Explode remove = explodes.remove(i);
                ExplodesPool.theReturn(remove);
                i--;
            }
        }
    }

    public abstract void enhance();

  /*  public void enhance() {
        this.hp = maxHp;
        this.atk =  ATK_MAX;
        this.speed = DEFAULT_SPEED*2;
    }*/

    //坦克血条
    class BloodBar {
        public static final int BAR_LENGTH = 2 * RADIUS;
        public static final int BAR_HEIGHT = 5;

        public void draw(Graphics g) {
            //底色
            g.setColor(Color.YELLOW);
            g.fillRect(x - RADIUS, y - RADIUS - BAR_HEIGHT * 2, BAR_LENGTH, BAR_HEIGHT);
            //红色血量
            g.setColor(Color.red);
            g.fillRect(x - RADIUS, y - RADIUS - BAR_HEIGHT * 2, hp * BAR_LENGTH / maxHp, BAR_HEIGHT);
            //蓝色边框
            g.setColor(Color.WHITE);
            g.drawRect(x - RADIUS, y - RADIUS - BAR_HEIGHT * 2, BAR_LENGTH, BAR_HEIGHT);
        }
    }

    //坦克的子弹与地图块的碰撞
    public void bulletCollideMapTile(List<MapTile> tiles) {
//        for (MapTile tile : tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (tile.isCollideBullet(bullets)) {
                //添加爆炸效果
                addExplode(tile.getX() + MapTile.radius, tile.getY() + MapTile.radius);
                //打到水泥块
                if (tile.getType() == MapTile.TYPE_HARD)
                    continue;
                //设置地图块销毁 TODO
                tile.setVisible(false);
//                tiles.remove(tile);
                //归还对象池
                MapTilePool.theReturn(tile);
                if (tile.isHouse()) {
                    //当老巢被击毁之后一秒钟切换到游戏结束的画面
                    delaySecondsToOver(1000);
//                    GameFrame.setGameState(Constant.STATE_LOST);
                }
            }
        }
    }

    /**
     * 延迟若干毫秒切换到游戏结束画面
     *
     * @param millisSecond
     */
    private void delaySecondsToOver(int millisSecond) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GameFrame.setGameState(Constant.STATE_LOST);
            }
        }.start();
    }

    /**
     * 一个地图块和当前坦克的碰撞方法
     * 从tile中提取出八个点，只要有一个点与坦克有碰撞，可以判定为发生碰撞
     * 从左上角开始顺时针判断每个点
     *
     * @param tiles
     * @return
     */
    public boolean isCollideTile(List<MapTile> tiles) {
//        for (MapTile tile : tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            MapTile tile = tiles.get(i);
            if (tile.isVisible() && tile.getType() == MapTile.TYPE_COVER) continue;
            int tileX = tile.getX();
            int tileY = tile.getY();
            boolean collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            //碰上了就直接返回，否则判断下一个点
            if (collide) {
                return true;
            }

            //点2；
            tileX += MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }

            //点3 右上角点；
            tileX += MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }

            //点4 右中；
//        tileX += MapTile.radius;
            tileY += MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }

            //点5 右下；
            tileY += MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }

            //点6 下中；
            tileX -= MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }

            //点7 左下；
            tileX -= MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
            //点8  左中点；
            tileY -= MapTile.radius;
            collide = MyUtil.isCollide(x, y, RADIUS, tileX, tileY);
            if (collide) {
                return true;
            }
        }
        return false;
    }
    public boolean isCollideGift(Gift gift){
        if(gift == null){
            gift = new Gift();
        }
        int giftX = gift.getX();
        int giftY = gift.getY();
        boolean collide = MyUtil.isCollide(x, y, RADIUS, giftX, giftY);
        //碰上了就直接返回，否则判断下一个点
        if (collide) {
            return true;
        }

        //点2；
        giftX += Gift.radius;
        collide = MyUtil.isCollide(x, y, RADIUS, giftX, giftY);
        if (collide) {
            return true;
        }

        //点3 右上角点；
        giftX += Gift.radius;
        collide = MyUtil.isCollide(x, y, RADIUS, giftX, giftY);
        if (collide) {
            return true;
        }

        //点4 右中；
//        tileX += MapTile.radius;
        giftY += Gift.radius;
        collide = MyUtil.isCollide(x, y, RADIUS, giftX, giftY);
        if (collide) {
            return true;
        }

        //点5 右下；
        giftY += Gift.radius;
        collide = MyUtil.isCollide(x, y, RADIUS, giftX, giftY);
        if (collide) {
            return true;
        }

        //点6 下中；
        giftX -= Gift.radius;
        collide = MyUtil.isCollide(x, y, RADIUS, giftX, giftY);
        if (collide) {
            return true;
        }

        //点7 左下；
        giftX -= Gift.radius;
        collide = MyUtil.isCollide(x, y, RADIUS, giftX, giftY);
        if (collide) {
            return true;
        }
        //点8  左中点；
        giftY -= Gift.radius;
        collide = MyUtil.isCollide(x, y, RADIUS, giftX, giftY);
        if (collide) {
            return true;
        }
        return false;
    }

    /**
     * 坦克回退的方法
     */
    public void back() {
        x = oldX;
        y = oldY;
     /*   switch (dir){
            case DIR_UP:
                y+=speed;
                break;
            case DIR_DOWN:
                y-=speed;
                break;
            case DIR_LEFT:
                x+=speed;
                break;
            case DIR_RIGHT:
                x-=speed;
                break;
        }*/
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
}
