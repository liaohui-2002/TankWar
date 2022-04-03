package com.all.map;

import com.all.Util.Constant;
import com.all.Util.MyUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 玩家坦克的大本营
 *
 * @auther Mr.Liao
 * @date 2022/3/30 20:48
 */
public class TankHouse {
    //老巢的x，y坐标
    public static final int HOUSE_X = Constant.FRAME_WIDTH - 3 * MapTile.tileW >> 1;
    public static final int HOUSE_Y = Constant.FRAME_HEIGHT - 2 * MapTile.tileW;
    private static Image star;
    private int hp = 1;
    private boolean visible = true;
    //存放家周围的方块
    private  static List<MapTile> tiles =new ArrayList<>();

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }



    public TankHouse() {
        tiles.add(new MapTile(HOUSE_X,HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X,HOUSE_Y+MapTile.tileW));
        tiles.add(new MapTile(HOUSE_X+MapTile.tileW,HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X+MapTile.tileW*2,HOUSE_Y));
        tiles.add(new MapTile(HOUSE_X+MapTile.tileW*2,HOUSE_Y+MapTile.tileW));
       //大本营核心
        tiles.add(new MapTile(HOUSE_X+MapTile.tileW,HOUSE_Y+MapTile.tileW));
        tiles.get(tiles.size()-1).setType(MapTile.TYPE_HOUSE);
    }

    public void draw(Graphics g){
       /* for (MapTile tile : tiles) {
            tile.draw(g);
        }*/
        if(!visible){
            return;
        }

        g.drawImage(star,HOUSE_X+MapTile.tileW+10,HOUSE_Y+MapTile.tileW+10,null);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public static List<MapTile> getTiles() {
        return tiles;
    }

    public void setTiles(List<MapTile> tiles) {
        this.tiles = tiles;
    }
}
