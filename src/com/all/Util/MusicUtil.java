package com.all.Util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;


/**
 * @auther Mr.Liao
 * @date 2022/4/4 22:18
 */
public class MusicUtil {
    private static AudioClip start;
    private static AudioClip fire;
    private static AudioClip bomb;
    private static AudioClip add;

    static {
        try {
            start = Applet.newAudioClip(new File("music/start.wav").toURL());
             fire = Applet.newAudioClip(new File("music/fire.wav").toURL());
             bomb = Applet.newAudioClip(new File("music/bomb.wav").toURL());
             add = Applet.newAudioClip(new File(("music/add.wav")).toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void playStart(){
        start.play();
    }
    public static void playFire(){
        fire.play();
    }
    public static void playBomb(){
        bomb.play();
    }
    public static void playAdd(){
        add.play();
    }
}
