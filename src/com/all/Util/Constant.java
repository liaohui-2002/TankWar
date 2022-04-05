package com.all.Util;

import java.awt.*;

/**
 * @auther Mr.Liao
 * @date 2022/3/27 18:00
 */

/*
游戏常量类
所有游戏中的常量都在该类中维护 方便后期处理
 */
public class Constant {
    public static final  String GAME_TITLE = "坦克大战";

    public  static  final int FRAME_WIDTH= 1260;
    public static  final  int FRAME_HEIGHT = 1080;
    //动态的获得系统屏幕的宽和高
    public static final int SCREEN_W = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_H = Toolkit.getDefaultToolkit().getScreenSize().height;

    //窗口左上角坐标
    public static  final  int FRAME_X = SCREEN_W - FRAME_WIDTH>>1;
    public static  final  int FRAME_Y = SCREEN_H -FRAME_HEIGHT>>1;

    /*************游戏菜单*******************/
    public static final int STATE_MENU = 0;
    public static final int STATE_HELP = 1;
    public static final int STATE_ABOUT = 2;
    public static final int STATE_RUN = 3;
    public static final int STATE_LOST = 4;
    public static final int STATE_WIN = 5;
    public static final int STATE_CROSS = 6;

    public static final String[] MENUS = {
            "开始游戏",
            "继续游戏",
            "游戏帮助",
            "关于游戏",
            "退出游戏",
    };
    public static final String OVER_STR0 = "ESC键退出游戏";
    public static final String OVER_STR1 = "Enter键盘回主菜单";



    /**
     * 字体设置
     */
    public static final Font GAME_FONT = new Font("宋体",Font.BOLD,24);

    public static final  int REPAINT_INTERVAL = 30;

    //敌人坦克最大数量
    public static final int ENEMY_MAX_COUNT = 10;
    public static final int ENEMY_BORN_INTERVAL = 2000;//产生间隔
    public static final int ENEMY_AI_INTERVAL = 3000;//敌人切换状态的间隔
    public static final double ENEMY_FIRE_PERCENT = 0.02;

}
