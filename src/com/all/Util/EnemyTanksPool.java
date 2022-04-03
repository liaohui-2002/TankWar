package com.all.Util;

import com.all.tank.EnemyTank;
import com.all.tank.Tank;

import java.util.ArrayList;
import java.util.List;

/**
 * 敌人坦克对象池
 * @auther Mr.Liao
 * @date 2022/3/29 20:43
 */
public class EnemyTanksPool {
    public static final int DEFAULT_POOL_SIZE = 20;
    public static final int POOL_MAX_SIZE = 20;
    //保存所有子弹的容器
    private static List<Tank> pool = new ArrayList<>();

    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new EnemyTank());
        }
    }

    /**
     * 从子弹池中获取子弹对象
     * @return
     */
    public static Tank get(){
        Tank tank = null;
        if(pool.size() == 0) {//池塘被掏空
            tank  = new EnemyTank();
        }
        else {//池塘中还有子弹就拿走第一颗
            tank = pool.remove(0);
        }
        System.out.println("从对象池中获取了一个对象，剩余："+pool.size());
        return tank;
    }

    //子弹销毁的时候 归还到池子里来
    public static void theReturn(Tank tank){
        if(pool.size() == POOL_MAX_SIZE){//池塘满了 不归还直接不要
            return;
        }else {
            pool.add(tank);
            System.out.println("对象池中归还了一颗子弹，当前池中子弹数目："+pool.size());
        }
    }
}
