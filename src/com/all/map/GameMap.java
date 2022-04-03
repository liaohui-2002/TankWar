package com.all.map;

import com.all.Game.GameFrame;
import com.all.Util.Constant;
import com.all.Util.MapTilePool;
import com.all.tank.Tank;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏地图类
 *
 * @auther Mr.Liao
 * @date 2022/3/30 15:59
 */
public class GameMap {

    public static final int MAP_X = Tank.RADIUS * 3;
    public static final int MAP_Y = Tank.RADIUS * 3 + GameFrame.titleBarH;
    public static final int MAP_WIDTH = Constant.FRAME_WIDTH - Tank.RADIUS * 6;
    public static final int MAP_HEIGHT = Constant.FRAME_HEIGHT - Tank.RADIUS * 8 - GameFrame.titleBarH;

    private int height;

    //地图元素容器
    private List<MapTile> tiles = new ArrayList<>();

    //大本营
    private TankHouse house;

    public GameMap() {
        initMap();
    }

    public List<MapTile> getTiles() {
        return tiles;
    }

    public void setTiles(List<MapTile> tiles) {
        this.tiles = tiles;
    }

    /**
     * 初始化地图元素快
     */
    private void initMap() {

       /* final int COUNT = 30;
        for (int i = 0; i < COUNT; i++) {
            MapTile tile = MapTilePool.get();

            int x = MyUtil.getRandomNumber(MAP_X, MAP_X + MAP_WIDTH - MapTile.tileW);
            int y = MyUtil.getRandomNumber(MAP_Y, MAP_Y + MAP_HEIGHT - MapTile.tileW);
            //新生成的块和已经存在的块有重叠
            if (isCollide(tiles, x, y)) {
                i--;
                continue;
            }
            tile.setX(x);
            tile.setY(y);
            tiles.add(tile);
        }*/

        //三行的地图
        addRow(MAP_X,MAP_X+MAP_WIDTH,MAP_Y,MapTile.TYPE_NORMAL,0);
        addRow(MAP_X,MAP_X+MAP_WIDTH,MAP_Y+MapTile.tileW*2,MapTile.TYPE_COVER,0);
        addRow(MAP_X,MAP_X+MAP_WIDTH,MAP_Y+MapTile.tileW*4,MapTile.TYPE_HARD,MapTile.tileW+MapTile.tileW/2);

        //初始化大本营
        house = new TankHouse();
        addHouse();
    }

    /**
     * 将老巢的所有元素块添加到地图容器中
     */
    private void addHouse() {
        tiles.addAll(house.getTiles());
    }

    /**
     * 只对没有遮挡效果的块进行绘制
     * @param g
     */
    public void drawBK(Graphics g) {
        for (MapTile tile : tiles) {
            if(tile.getType()!= MapTile.TYPE_COVER){
            tile.draw(g);}
        }
        house.draw(g);
    }

    /**
     * 只绘制有遮挡效果的块
     * @param g
     */
    public void drawCover(Graphics g) {
        for (MapTile tile : tiles) {
            if(tile.getType() == MapTile.TYPE_COVER){
                tile.draw(g);}
        }
        house.draw(g);
    }

    /**
     * 判断某一个点确定的块是否和容器中所有的方块位置有重叠
     *
     * @param tiles
     * @param x
     * @param y
     * @return 有重叠返回true 没有重叠返回false
     */
    private boolean isCollide(List<MapTile> tiles, int x, int y) {
        for (MapTile tile : tiles) {
            int tileX = tile.getX();
            int tileY = tile.getY();
            if (Math.abs(tileX - x) < MapTile.tileW && Math.abs(tileY - y) < MapTile.tileW) {
                return true;
            }

        }
        return false;
    }

    /**
     * 将所有不可见的块回收
     */
    public void clearDestroyedTile() {
        for (int i = 0; i < tiles.size(); i++) {
            MapTile mapTile = tiles.get(i);
            if (!mapTile.isVisible()) {
                tiles.remove(i);
            }
        }
    }

    /**
     * 往地图快容器中添加一行指定类型的地图快
     *
     * @param type   地图块的类型
     * @param DIS    地图块中心点之间的间隔 间隔是块宽度 表示连续 大于块的宽度表示不连续
     * @param startX 横坐标起始
     * @param startY 纵坐标起始位置
     * @param endX   横坐标结束位置
     */
    public void addRow(int startX, int endX, int startY, int type, final int DIS) {
        int count = 0;
        count = (endX - startX) / (MapTile.tileW+DIS);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(startX + i * (MapTile.tileW+DIS));
            tile.setY(startY);
            tiles.add(tile);
        }
    }
}
