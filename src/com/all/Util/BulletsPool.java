package com.all.Util;

import com.all.Game.Bullet;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Mr.Liao
 * @date 2022/3/28 12:57
 */
//创建一个子弹池 避免了反复创建和销毁对象
public class BulletsPool {
    public static final int DEFAULT_POOL_SIZE = 200;
    public static final int POOL_MAX_SIZE = 300;
    //保存所有子弹的容器
    private static List<Bullet> pool = new ArrayList<>();
    //在类加载的时候创建200个子弹添加到容器中
    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
                pool.add(new Bullet());
        }
    }

    /**
     * 从子弹池中获取子弹对象
     * @return
     */
    public static Bullet get(){
        Bullet bullet = null;
        if(pool.size() == 0) {//池塘被掏空
           bullet  = new Bullet();
        }
        else {//池塘中还有子弹就拿走第一颗
            bullet = pool.remove(0);
        }
        return bullet;
    }

    //子弹销毁的时候 归还到池子里来
    public static void theReturn(Bullet bullet){
        if(pool.size() == POOL_MAX_SIZE){//池塘满了 不归还直接不要
            return;
        }else {
            pool.add(bullet);
        }
    }
}
