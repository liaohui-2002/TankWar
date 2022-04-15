package com.all.Game;


import com.all.Util.Constant;
import com.all.Util.MusicUtil;
import com.all.Util.MyUtil;
import com.all.map.GameMap;
import com.all.map.Gift;
import com.all.map.MapTile;
import com.all.tank.EnemyTank;
import com.all.tank.MyTank;
import com.all.tank.Tank;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


import static com.all.Util.Constant.*;

/**
 * @auther Mr.Liao
 * @date 2022/3/27 17:56
 */
//继承jdk定义好的类 Frame窗口类
 public class GameFrame extends Frame implements Runnable {
    //    第一次使用的时候加载
    private Image overImg = null;
    //定义一张和屏幕大小一致的图片
    private BufferedImage bufImg = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
    //游戏状态
    public static int gameState;

    //菜单指针
    private static int menuIndex;
    //定义坦克对象
    private static Tank myTank;

    //标题栏目的高度
    public static int titleBarH;

    //敌人的坦克容器
    private static List<Tank> enemies = new ArrayList<>();

    //用来记录产生了本关卡产生了多少个敌人
    private static int bornEnemyCount;

    public static int killEnemyCount;

    //定义地图相关内容
    private static GameMap gameMap = new GameMap();;

    //开始过关动画
    public static int flashTime;
    public static final int RECT_WIDTH=40;
    public static final int RECT_COUNT = FRAME_WIDTH/RECT_WIDTH +1;
    public static boolean isOpen = false;
    public static void startCrossLevel(){
        gameState = STATE_CROSS;
        flashTime = 0;
        isOpen = false;
    }
    /*
    游戏主窗口
     */
    public GameFrame() throws HeadlessException {
        initFrame();

        initEventListener();
        //启动用于刷新窗口的线程
        new Thread(this).start();
    }

    /**
     * 进入下一关的方法
     */
    public static void nextLevel() {
        startGame(LevelInfo.getInstance().getLevel()+1);
    }

    /**
     * 初始化游戏相关
     */
    private void initGame() {
        gameState = STATE_MENU;
    }

    /*
    实现属性初始化
     */
    private void initFrame() {
        setTitle(GAME_TITLE);//设置窗口标题
        //设置窗口可见
        setVisible(true);
        //设置窗口大小
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        //设置窗口左上角坐标
        setLocation(FRAME_X, FRAME_Y);
        //设置窗口大小不可变
        setResizable(false);
        //求出标题栏的高度
        titleBarH = getInsets().top;
        /*repaint();*/
    }


    /**
     * @param g1 系统画笔
     * @Description 是Frame类的方法  负责所有绘制内容
     * 所有需要在屏幕中显示的内容 都需要在该方法内调用 该方法不能主动调用
     * 通过调用repaint();去回调该方法
     */
    @Override
    public void update(Graphics g1) {
        //得到图片画笔
        Graphics g = bufImg.getGraphics();
        g.setFont(GAME_FONT);
        switch (gameState) {
            case STATE_MENU:
                drawMenu(g);
                break;
            case STATE_HELP:
                drawHelp(g);
                break;
            case STATE_ABOUT:
                drawAbout(g);
                break;
            case STATE_RUN:
                drawRun(g);
                break;
            case STATE_LOST:
                drawLost(g,"过关失败！");
                break;
            case STATE_WIN:
                drawWin(g);
                break;
            case STATE_CROSS:
                drawCross(g);
                break;
        }

        //使用系统画笔将图片绘制到FRAME上面
        g1.drawImage(bufImg, 0, 0, null);
    }

    /**
     * 绘制过关动画
     * @param g
     */
    public void drawCross(Graphics g){
        gameMap.drawBK(g);
        myTank.draw(g);
        gameMap.drawCover(g);
        g.setColor(Color.BLACK);
        //关闭百叶窗效果
        if(!isOpen){
            for (int i = 0; i < RECT_COUNT; i++) {
             g.fillRect(i*RECT_WIDTH,0,flashTime,FRAME_HEIGHT);
            }
            //所有叶片都关闭了
            if(flashTime++ - RECT_WIDTH>5){
                isOpen = true;
                //初始化下一个地图
                gameMap.initMap(LevelInfo.getInstance().getLevel()+1);
            }
        }
        else {
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i*RECT_WIDTH,0,flashTime,FRAME_HEIGHT);
            }
            if(flashTime-- == 0){
                startGame(LevelInfo.getInstance().getLevel());
            }
        }
    }
    /**
     * 绘制游戏结束的画面
     *
     * @param g
     */
    private void drawLost(Graphics g,String str) {
        //保证只加载一次
        if (overImg == null) {
            overImg = MyUtil.createImage("res/over.gif");
        }
        int width = overImg.getWidth(null);
        int height = overImg.getHeight(null);

        g.drawImage(overImg, FRAME_WIDTH - width >> 1, FRAME_HEIGHT - height >> 1, null);

        //添加键位提示信息
        g.setColor(Color.RED);
        g.drawString(OVER_STR0, 30, FRAME_HEIGHT - 40);
        g.drawString(OVER_STR1, FRAME_WIDTH - 250, FRAME_HEIGHT - 40);
        g.drawString(str,FRAME_WIDTH/2 -30,GameFrame.titleBarH+ MapTile.tileW);
    }

    //游戏运行状态的绘制
    private void drawRun(Graphics g) {
        //绘制黑色背景
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        //绘制地图的碰撞层
        gameMap.drawBK(g);
        drawEnemy(g);
        myTank.draw(g);
        gameMap.drawGift(g);


        //绘制地图的遮挡层
        gameMap.drawCover(g);
        drawExplodes(g);
        //子弹和坦克的碰撞方法
        bulletCollideTank();

        //子弹和所有地图块的碰撞
        bulletAngTankCollideMapTile();

    }

    //绘制所有的敌人 如果已经死亡 从容器中移除
    private void drawEnemy(Graphics g) {
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if (enemy.isDie()) {
                enemies.remove(i);
                i--;
                continue;
            }
            enemy.draw(g);
        }
        //System.out.println("敌人数量：" + enemies.size());

    }

    private Image helpImg;
    private Image aboutImg;
    private void drawAbout(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        if(aboutImg == null){
            aboutImg = MyUtil.createImage("res/about.png");
        }
        int width = aboutImg.getWidth(null);
        int height = aboutImg.getHeight(null);
        int x = FRAME_WIDTH - width >>1;
        int y = FRAME_HEIGHT - height>>1;
        g.drawImage(aboutImg,x,y,null);
        g.setColor(Color.WHITE);
        g.drawString("任意键继续",10,FRAME_HEIGHT-10);
    }

    private void drawHelp(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        if(helpImg == null){
            helpImg = MyUtil.createImage("res/help.png");
        }
        int width = helpImg.getWidth(null);
        int height = helpImg.getHeight(null);
        int x = FRAME_WIDTH - width >>1;
        int y = FRAME_HEIGHT - height>>1;
        g.drawImage(helpImg,x,y,null);
        g.setColor(Color.WHITE);
        g.drawString("任意键继续",10,FRAME_HEIGHT-10);
    }

    /**
     * 绘制菜单状态下内容
     *
     * @param g 画笔对象，由系统提供
     */
    private void drawMenu(Graphics g) {
        //绘制黑色背景
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        final int STR_WIDTH = 76;
        int x = FRAME_WIDTH - STR_WIDTH >> 1;
        int y = FRAME_HEIGHT / 3;
        final int DIS = 50;//行间距
        g.setColor(Color.WHITE);
        for (int i = 0; i < MENUS.length; i++) {
            if (i == menuIndex) {//将选中的菜单项颜色设置为红色
                g.setColor(Color.RED);
            } else {
                //其他的为白色
                g.setColor(Color.WHITE);
            }
            g.drawString(MENUS[i], x, y + DIS * i);
        }
    }
    private void drawWin(Graphics g) {
        drawLost(g,"游戏通关！");
    }

    /*
   处理事件的方法
    */
    private void initEventListener() {
        //给当前窗口添加监听器 （注册监听事件）
        addWindowListener(new WindowAdapter() {
            //点击关闭按钮会被执行的方法
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);//传入
            }
        });
        /**
         * 添加按键监听事件
         */
        addKeyListener(new KeyAdapter() {
            //按键按下的时候被回调的函数
            @Override
            public void keyPressed(KeyEvent e) {//e保存了你按下了哪个按键
                //被按下键的键值
                int keyCode = e.getKeyCode();
                //不同的游戏状态给出不同的处理办法
                switch (gameState) {
                    case STATE_MENU:
                        keyPressedEventMenu(keyCode);
                        break;
                    case STATE_HELP:
                        keyPressedEventHelp(keyCode);
                        break;
                    case STATE_ABOUT:
                        keyPressedEventAbout(keyCode);
                        break;
                    case STATE_RUN:
                        keyPressedEventRun(keyCode);
                        break;
                    case STATE_LOST:
                        keyPressedEventOver(keyCode);
                        break;
                    case STATE_WIN:
                        keyPressedEventWin(keyCode);
                        break;

                }
            }


            //按键松开的时候回调的内容
            @Override
            public void keyReleased(KeyEvent e) {
                //被按下键的键值
                int keyCode = e.getKeyCode();
                //不同的游戏状态给出不同的处理办法
                if (gameState == STATE_RUN) {
                    keyReleasedEventRun(keyCode);
                }
            }
        });
    }

    /**
     * 游戏通关的按键处理
     * @param keyCode
     */
    private void keyPressedEventWin(int keyCode) {
        keyPressedEventOver(keyCode);
    }

    /**
     * 按键松开的时候游戏中的处理方法
     *
     * @param keyCode
     */
    private void keyReleasedEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setState(Tank.STATE_STAND);
                break;
        }
    }

    //游戏结束状态的按键处理 TODO
    private void keyPressedEventOver(int keyCode) {
        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if (keyCode == KeyEvent.VK_ENTER) {
            setGameState(STATE_MENU);
            resetGame();
            //游戏需要重置 初始化

        }
    }

    // 重置游戏状态
    private void resetGame() {
        killEnemyCount = 0;

        menuIndex = 0;
        //先让自己坦克的子弹还回对象池
        myTank.bulletsReturn();
        //销毁坦克
        myTank = null;
        for (Tank enemy : enemies) {
            enemy.bulletsReturn();
        }
        //清空敌人
        enemies.clear();
        //清空地图资源

        gameMap = null;
    }

    /**
     * 游戏运行中的按键处理
     *
     * @param keyCode
     */
    private void keyPressedEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                myTank.setDir(Tank.DIR_UP);
                //除了改变方向 还要设置坦克状态为move状态
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                myTank.setDir(Tank.DIR_DOWN);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                myTank.setDir(Tank.DIR_LEFT);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setDir(Tank.DIR_RIGHT);
                myTank.setState(Tank.STATE_MOVE);
                break;
            case KeyEvent.VK_SPACE:
                myTank.fire();
                break;
        }
    }

    private void keyPressedEventAbout(int keyCode) {
        setGameState(STATE_MENU);
    }

    private void keyPressedEventHelp(int keyCode) {
        setGameState(STATE_MENU);
    }

    //菜单状态下按键的处理
    private void keyPressedEventMenu(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:

                if (--menuIndex < 0) {
                    menuIndex = MENUS.length - 1;
                }
                repaint();
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:

                if (++menuIndex > MENUS.length - 1) {
                    menuIndex = 0;
                }
                repaint();//窗口重绘制
                break;
            case KeyEvent.VK_ENTER:
                switch (menuIndex){
                    case 0:
                        startGame(1);
                        break;
                    case 1:
                        //选择关卡界面
                        break;
                    case 2:
                        setGameState(STATE_HELP);
                        break;
                    case 3:
                        setGameState(STATE_ABOUT);
                        break;
                    case 4:
                        System.exit(0);
                        break;
                }
                break;
        }
    }

    /**
     * 开始新游戏的方法
     * 并加载关卡信息
     */
    private static void startGame(int level) {
        enemies.clear();
        if(gameMap == null){
            gameMap = new GameMap();
        }
        gameMap.initMap(level);
        MusicUtil.playStart();
        killEnemyCount = 0;
        bornEnemyCount = 0;

        gameState = STATE_RUN;
        //创建坦克对象，敌人的坦克
        myTank = new MyTank(FRAME_WIDTH / 3, FRAME_HEIGHT - Tank.RADIUS, Tank.DIR_UP);

        //使用一个单独的线程用于生成敌人的坦克
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (LevelInfo.getInstance().getEnemyCount() > bornEnemyCount &&
                            enemies.size() < ENEMY_MAX_COUNT) {
                        Tank enemy = EnemyTank.createEnemy();
                        enemies.add(enemy);
                        bornEnemyCount++;

                    }
                    try {
                        Thread.sleep(ENEMY_BORN_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //只有在run状态才创建敌人
                    if (gameState != STATE_RUN) {
                        break;
                    }
                }
            }
        }.start();

        //使用另一個綫程產生礼物
        new Thread() {
//            Graphics g = Gift.star.getGraphics();
            @Override
            public void run() {
                while (true) {
                    gameMap.getGift().setVisible(true);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ( !gameMap.getGift().isVisible()) {
                        break;
                    }
                }
            }
        }.start();
    }

    /**
     * 此方法拿来控制刷新率
     */
    @Override
    public void run() {
        while (true) {
            //此处设置33ms刷新一次
            repaint();
            try {
                Thread.sleep(REPAINT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void bulletCollideTank() {
        //玩家坦克的子弹与敌人坦克碰撞的判断
        for (Tank enemy : enemies) {
            enemy.collideBullets(myTank.getBullets());
        }
        //敌人坦克的子弹和玩家坦克碰撞的判断
        for (Tank enemy : enemies) {
            myTank.collideBullets(enemy.getBullets());
        }
    }

    //和地图块的碰撞
    private void bulletAngTankCollideMapTile() {
        myTank.bulletCollideMapTile(gameMap.getTiles());

        for (Tank enemy : enemies) {
            enemy.bulletCollideMapTile(gameMap.getTiles());
        }
        //玩家坦克和地图块的碰撞
        if (myTank.isCollideTile(gameMap.getTiles())) {
            myTank.back();
        }
        //敌人的坦克和敌人的碰撞
        for (Tank enemy : enemies) {
            if (enemy.isCollideTile(gameMap.getTiles())) {
                enemy.back();
            }
        }
        //玩家捡到强化材料
        if(myTank.isCollideGift(gameMap.getGift())){
            //玩家升级  TODO
            playerUp();

        }
        //将所有不可见的地图块移除
        gameMap.clearDestroyedTile();
    }

    private void playerUp() {
        myTank.enhance();
    }

    //所有坦克上的爆炸效果
    private void drawExplodes(Graphics g) {
        for (Tank enemy : enemies) {
            enemy.drawExplodes(g);
        }
        myTank.drawExplodes(g);
    }

    //获得游戏状态
    public static void setGameState(int gameState) {
        GameFrame.gameState = gameState;
    }

    //修改游戏状态
    public static int getGameState() {
        return gameState;
    }

    /**
     * 判断游戏是否最后一关
     */
    public static boolean isLastLevel() {
        int currentLevel = LevelInfo.getInstance().getLevel();
        int levelCount = GameInfo.getLevelCount();
        return currentLevel == levelCount;

        //resetGame();

    }

    /**
     * 判断是否过关
     *
     * @return
     */
    public static boolean isCrossLevel() {
        return killEnemyCount == LevelInfo.getInstance().getEnemyCount();
    }
}