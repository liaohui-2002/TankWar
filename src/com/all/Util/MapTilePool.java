package com.all.Util;

import com.all.map.MapTile;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther Mr.Liao
 * @date 2022/3/30 16:02
 */
public class MapTilePool {
    public static final int DEFAULT_POOL_SIZE = 50;
    public static final int POOL_MAX_SIZE = 70;
    private static List<MapTile> pool = new ArrayList<>();

    static {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new MapTile());
        }
    }

    /**
     * @return
     */
    public static MapTile get(){
        MapTile mapTile = null;
        if(pool.size() == 0) {//池塘被掏空
            mapTile  = new MapTile();
        }
        else {
            mapTile = pool.remove(0);
        }
//        System.out.println("从对象池中获取了一个对象，剩余："+pool.size());
        return mapTile;
    }

    public static void theReturn(MapTile mapTile){
        if(pool.size() == POOL_MAX_SIZE){//池塘满了 不归还直接不要
            return;
        }else {
            pool.add(mapTile);
//            System.out.println("对象池中归还了一颗子弹，当前池中子弹数目："+pool.size());
        }
    }
}
