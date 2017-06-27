package com.example.hp_06.foregroundmediaplayer;

/**
 * Created by HP-06 on 3/11/2016.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class ForegroundService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private static final String LOG_TAG = "ForegroundService";
    public static boolean IS_SERVICE_RUNNING = false;
    String title;
    String ArtWorkUrl;
private static MediaPlayer player;
    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        initMusicPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         title =intent.getStringExtra(Constants.ACTION.Title);
        String StreamUrl =intent.getStringExtra(Constants.ACTION.StreamUrl);
         ArtWorkUrl =intent.getStringExtra(Constants.ACTION.ArtWorkUrl);
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Start Foreground Intent ");
            player.reset();
            try{
                player.setDataSource( StreamUrl + "?client_id=e8cbc9cd5537a4b122a342278dd857a7" );
            }
            catch(Exception e){
                Log.e("MUSIC SERVICE", "Error setting data source", e);
            }
            player.prepareAsync();


            showNotification();


            Toast.makeText(this, "Service Started!", Toast.LENGTH_SHORT).show();


        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            Log.i(LOG_TAG, "Clicked Play");
            togglePlayPause();
        } else if (intent.getAction().equals(Constants.ACTION.PAUSE_ACTION)) {
                Log.i(LOG_TAG, "Clicked Previous");
            togglePlayPause();

        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION ) ){
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
           // ForegroundService.IS_SERVICE_RUNNING = false;
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra(Constants.ACTION.Title,title);
        notificationIntent.putExtra(Constants.ACTION.ArtWorkUrl, ArtWorkUrl);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent,0);



        Intent playIntent = new Intent(this, ForegroundService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);
        Intent pauseIntent = new Intent(this, ForegroundService.class);
        pauseIntent.setAction(Constants.ACTION.PAUSE_ACTION);
        PendingIntent ppauseIntent = PendingIntent.getService(this, 0,
                pauseIntent, 0);


        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);

        Notification notification = (new NotificationCompat.Builder(this)
                .setContentTitle("Player")
                .setTicker("MediaPlayer")
                .setContentText(title)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_play, "Play",
                        pplayIntent)
                .addAction(android.R.drawable.ic_media_pause, "Pause",
                        ppauseIntent)
                .build());
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);

    }
    public void initMusicPlayer(){
        //set player properties

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

    }



    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case if services are bound (Bound Services).
        return null;
    }
    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
    public static int togglePlayPause() {
        if (player.isPlaying()) {
            player.pause();
            return 1;

        } else {
            player.start();
            return 0;
        }
    }
}
