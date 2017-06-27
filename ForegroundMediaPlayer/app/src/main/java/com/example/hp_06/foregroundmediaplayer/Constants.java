package com.example.hp_06.foregroundmediaplayer;

/**
 * Created by HP-06 on 3/11/2016.
 */
public class Constants {


    public interface ACTION {
        public static String MAIN_ACTION = "com.marothiatechs.foregroundservice.action.main";
        public static String CANCEL_ACTION = "com.marothiatechs.foregroundservice.action.cancel";
        public static String PAUSE_ACTION = "com.marothiatechs.foregroundservice.action.pause";
        public static String PLAY_ACTION = "com.marothiatechs.foregroundservice.action.play";
        public static String NEXT_ACTION = "com.marothiatechs.foregroundservice.action.next";
        public static String STARTFOREGROUND_ACTION = "com.marothiatechs.foregroundservice.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.marothiatechs.foregroundservice.action.stopforeground";
        public static String Title ="TrackTitle";
        public static String StreamUrl ="StreamUrl";
        public static String ArtWorkUrl ="ArtWorkUrl";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
