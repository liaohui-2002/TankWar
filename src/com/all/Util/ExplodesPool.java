package com.all.Util;

import com.all.Game.Explode;

import java.util.ArrayList;
import java.util.List;

/**
 * 爆炸效果对象池
 * @auther Mr.Liao
 * @date 2022/3/29 19:57
 */
public class ExplodesPool {

    public static final int DEFAULT_POOL_SIZE = 10;
    public static final int POOL_MAX_SIZE = 20;
    //保存所有子弹的容器
    private static List<Explode> pool = new ArrayList<>();

    //在类加载的时候创建200个子弹添加到容器中
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Explode());
        }
    }

    /**
     * 从子弹池中获取炸弹对象
     * @return
     */
    public static Explode get(){
        Explode explode = null;
        if(pool.size() == 0) {
            explode  = new Explode();
        }
        else {
            explode = pool.remove(0);
        }
//        System.out.println("从对象池中获取了一个对象，剩余："+pool.size());
        return explode;
    }

    //爆炸销毁的时候 归还到池子里来
    public static void theReturn(Explode explode){
        if(pool.size() == POOL_MAX_SIZE){//池塘满了 不归还直接不要
            return;
        }else {
            pool.add(explode);

        }
    }
}
