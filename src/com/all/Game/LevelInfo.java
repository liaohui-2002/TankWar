package com.all.Game;

/**
 * @auther Mr.Liao
 * @date 2022/4/3 22:06
 */

import com.all.Util.MyUtil;

/**
 * 管理当前关卡的的信息
 */
public class LevelInfo {
    private LevelInfo(){

    }
    //定义静态的本类类型的变量
    private static LevelInfo instance;
    //懒汉模式
    public static LevelInfo getInstance(){
        if(instance == null){
            instance = new LevelInfo();
        }
        return instance;
    }
    //关卡编号
    private int level;
    //关卡敌人数量
    private int enemyCount;
    //通关时间限制 -1表示不限时
    private int crossTime = -1;
    //敌人类型信息
    private int[] enemyType;

    public int getLevelType() {
        return levelType<=0?1:levelType;
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
    }

    //游戏难度
    private int levelType;

    public int[] getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(int[] enemyType) {
        this.enemyType = enemyType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public int getCrossTime() {
        return crossTime;
    }

    public void setCrossTime(int crossTime) {
        this.crossTime = crossTime;
    }

    //获得敌人类型数组中的随机一个元素
    public int getRandomEnemyType(){
        int index =  MyUtil.getRandomNumber(0,enemyType.length);
        return enemyType[index];
    }
}
