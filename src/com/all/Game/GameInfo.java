package com.all.Game;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**游戏信息相关类
 * @auther Mr.Liao
 * @date 2022/4/5 10:37
 */
public class GameInfo {
    private static int levelCount;

    static {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("level/gameinfo"));
            levelCount = Integer.parseInt(prop.getProperty("levelCount"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getLevelCount() {
        return levelCount;
    }
}
