package com.all.tank;

import com.all.Game.GameFrame;
import com.all.Game.LevelInfo;
import com.all.Util.Constant;
import com.all.Util.MyUtil;
import com.all.Util.EnemyTanksPool;

import java.awt.*;

/**
 * @auther Mr.Liao
 * @date 2022/3/29 16:03
 */
/**
 * 敌人坦克类
 */
public class EnemyTank extends Tank{
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_GREEN= 1;
    private int type = TYPE_NORMAL;
    private static Image[] normalEnemyImg;
    private static Image[] greenEnemyImg;
    //记录ai开始时间
    private long aiTime;

    static {
        normalEnemyImg = new Image[4];
        normalEnemyImg[0] = MyUtil.createImage("res/enemy1U.gif");
        normalEnemyImg[1] = MyUtil.createImage("res/enemy1D.gif");
        normalEnemyImg[2] = MyUtil.createImage("res/enemy1L.gif");
        normalEnemyImg[3] = MyUtil.createImage("res/enemy1R.gif");
        greenEnemyImg = new Image[4];
        greenEnemyImg[0] = MyUtil.createImage("res/enemy2U.gif");
        greenEnemyImg[1] = MyUtil.createImage("res/enemy2D.gif");
        greenEnemyImg[2] = MyUtil.createImage("res/enemy2L.gif");
        greenEnemyImg[3] = MyUtil.createImage("res/enemy2R.gif");
    }

    private EnemyTank(int x, int y, int dir) {
        super(x, y, dir);
        //敌人一旦创建就开始记时
        aiTime = System.currentTimeMillis();
        type =MyUtil.getRandomNumber(0,2);
    }

    public EnemyTank() {
        aiTime = System.currentTimeMillis();
        type =MyUtil.getRandomNumber(0,2);
    }

    @Override
    public void drawImgTank(Graphics g) {
        AI();

        g.drawImage(type == TYPE_NORMAL ? normalEnemyImg[getDir()]:
                greenEnemyImg[getDir()],getX() - RADIUS, getY() - RADIUS, null);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //创建敌人坦克
    public static Tank createEnemy() {
        int x = MyUtil.getRandomNumber(0, 2) == 0 ? RADIUS : Constant.FRAME_WIDTH - 2*RADIUS;
        int y = GameFrame.titleBarH + RADIUS;
        int dir = DIR_DOWN;
        EnemyTank enemy = (EnemyTank)EnemyTanksPool.get();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setDir(dir);
        enemy.setEnemy(true);
        enemy.setState(STATE_MOVE);
        //根据游戏难度设置敌人血量
        int maxHp = Tank.DEFAULT_HP*LevelInfo.getInstance().getLevelType();
        enemy.setHp(maxHp);
        enemy.setMaxHp(maxHp);
        //通过关卡中信息中的敌人信息设置敌人类型
        int enemyType = LevelInfo.getInstance().getRandomEnemyType();
        enemy.setType(enemyType);
        return enemy;
    }

    /**
     * 敌人的AI
     */
    private void AI(){
        if(System.currentTimeMillis() - aiTime>Constant.ENEMY_AI_INTERVAL){
            //间隔3s 随机一个状态
            setDir(MyUtil.getRandomNumber(DIR_UP,DIR_RIGHT+1));
            setState(MyUtil.getRandomNumber(0,2) == 0?STATE_STAND:STATE_MOVE);
            aiTime = System.currentTimeMillis();
        }
        //比较小的概率去开火
        if(Math.random()<Constant.ENEMY_FIRE_PERCENT){
            fire();
        }
    }
}
