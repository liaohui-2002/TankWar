package com.all.map;

import com.all.Game.GameFrame;
import com.all.Game.LevelInfo;
import com.all.Util.Constant;
import com.all.Util.MapTilePool;
import com.all.Util.MyUtil;

import com.all.tank.Tank;

import java.awt.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.all.Util.Constant.*;

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
    //Gift
    static Gift gift = new Gift();

    public Gift getGift() {
        return gift;
    }

    public void setGift(Gift gift) {
        this.gift = gift;
    }

    //大本营
    private TankHouse house;

    public GameMap() {
    }

    public List<MapTile> getTiles() {
        return tiles;
    }

    public void setTiles(List<MapTile> tiles) {
        this.tiles = tiles;
    }

    /**
     * 初始化地图元素快 level 表示具体第几关
     */
    public void initMap(int level) {
        tiles.clear();
        try {
            loadLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //初始化大本营
        house = new TankHouse();
        addHouse();
    }

    /**
     * 加载关卡信息
     * @param level
     * @throws Exception
     */
    private void loadLevel(int level) throws Exception {
        //获得关卡信息类的唯一实例对象
        LevelInfo levelInfo = LevelInfo.getInstance();
        //设置关卡编号
        levelInfo.setLevel(level);
        Properties prop = new Properties();
        prop.load(new FileInputStream("level/lv_" + level));
        //设置敌人数量
        int enemyCount = Integer.parseInt(prop.getProperty("enemyCount"));
        levelInfo.setEnemyCount(enemyCount);
        String[] enemyType = prop.getProperty("enemyType").split(",");
        //0，1 对敌人类型解析
        int[] type = new int[enemyType.length];
        for (int i = 0; i < type.length; i++) {
            type[i] = Integer.parseInt(enemyType[i]);
        }
        //设置敌人类型
        levelInfo.setEnemyType(type);
        //关卡难度
        String levelType =  prop.getProperty("levelType");
        levelInfo.setLevelType(Integer.parseInt(levelType == null?"1":levelType));

        String methodName = prop.getProperty("method");
        int invokeCount = Integer.parseInt(prop.getProperty("invokeCount"));
        String[] params = new String[invokeCount];
        for (int i = 1; i <= invokeCount; i++) {
            params[i - 1] = prop.getProperty("param" + i);
        }
        invokeMethod(methodName, params);
    }

    private void invokeMethod(String name, String[] params) {
        for (String param : params) {
            String[] split = param.split(",");
            int[] arr = new int[split.length];
            int i;
            for ( i = 0; i < split.length-1; i++) {
                arr[i] = Integer.parseInt(split[i]);
            }
            final int DIS = MapTile.tileW;
            int dis = (int)(Double.parseDouble(split[i])*DIS);
            switch (name) {
                case "addRow":
                    addRow(MAP_X + arr[0] * DIS, MAP_X + MAP_WIDTH - arr[1] * DIS,
                            MAP_Y + arr[2] * DIS, arr[3], dis);
                    break;
                case "addCol":
                    addCol(MAP_Y+arr[0]*DIS,MAP_Y+MAP_HEIGHT-arr[1]*DIS,
                            MAP_X + arr[2]*DIS,arr[3],dis);
                    break;
                case "addRect":
                    addRect(MAP_X+arr[0]*DIS,MAP_X+MAP_WIDTH-arr[1]*DIS,
                            MAP_Y+arr[2]*DIS,MAP_Y+MAP_HEIGHT-arr[3]*DIS,
                            arr[4],dis);
                    break;
            }
        }
    }

    /**
     * 将老巢的所有元素块添加到地图容器中
     */
    private void addHouse() {
        tiles.addAll(house.getTiles());
    }

    /**
     * 只对没有遮挡效果的块进行绘制
     *
     * @param g
     */
    public void drawBK(Graphics g) {
        for (MapTile tile : tiles) {
            if (tile.getType() != MapTile.TYPE_COVER) {
                tile.draw(g);
            }
        }
//        if(gift == null){
//            gift = new Gift();
//        }
//        drawGift(g);
        house.draw(g);
    }

    /**
     * 只绘制有遮挡效果的块
     *
     * @param g
     */
    public void drawCover(Graphics g) {
        for (MapTile tile : tiles) {
            if (tile.getType() == MapTile.TYPE_COVER) {
                tile.draw(g);
            }
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
        count = (endX - startX) / (MapTile.tileW + DIS);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(startX + i * (MapTile.tileW + DIS));
            tile.setY(startY);
            tiles.add(tile);
        }
    }

    /**
     * 往地图容器中添加一列指定类型的地图快
     *
     * @param startY 该列的起始y坐标
     * @param endY   结束y坐标
     * @param X      x坐标
     * @param type   元素类型
     * @param DIS    相邻块的间隔
     */
    public void addCol(int startY, int endY, int X, int type, final int DIS) {
        int count = 0;
        count = (endY - startY) / (MapTile.tileW + DIS);
        for (int i = 0; i < count; i++) {
            MapTile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(X);
            tile.setY(startY + i * (MapTile.tileW + DIS));
            tiles.add(tile);
        }
    }

    /**
     * 对指定的矩形区域添加元素
     *
     * @param startX
     * @param endX
     * @param startY
     * @param endY
     * @param type
     * @param DIS
     */
    public void addRect(int startX, int endX, int startY, int endY, int type, final int DIS) {
        //添加若干行或者若干列即可
        int rows = (endY - startY) / (MapTile.tileW + DIS);
        for (int i = 0; i < rows; i++) {
            addRow(startX, endX, startY + i * (DIS + MapTile.tileW), type, DIS);
        }
    }

    public void drawGift(Graphics g){
        new Thread() {
            @Override
            public void run() {
                while (true) {

                    try {
                        gift.draw(g);
                        Thread.sleep(3000);

                        /*gift.setX(MyUtil.getRandomNumber(0,FRAME_WIDTH-Gift.giftW));
                        gift.setY(MyUtil.getRandomNumber(0,FRAME_HEIGHT-Gift.giftW));*/
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ( !gift.isVisible()) {
                        break;
                    }
                }
            }
        }.start();

    }
}
