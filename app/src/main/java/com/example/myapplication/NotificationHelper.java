package com.example.myapplication;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID="chanelID";
    public static final String channelName="channel";
    public static String title;
    private String stringUri;
    private NotificationManager manager;
    private Bitmap bitmapImage=null;
    NotificationCompat.BigPictureStyle style;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel(){
        NotificationChannel channel=new NotificationChannel(channelID,channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManeger().createNotificationChannel(channel);
    }
    public NotificationManager getManeger(){
        if(manager==null){
            manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
    public NotificationCompat.Builder getChannelNotification(String title, String stringUri) throws IOException {
        long[] v = {500,1000};

        this.title=title;
        this.stringUri=stringUri;


        Intent activityIntent = new Intent(this, Medicament.class);
        activityIntent.putExtra("nomMed",(String) this.title);
        activityIntent.putExtra("url",(String) this.stringUri);

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);
        return new NotificationCompat.Builder(getApplicationContext(),channelID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("MEDICAMENT")
                .setContentText("c'est le temps de prendre :"+this.title)
                .setVibrate(v)
                .setSound( RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE), AudioManager.STREAM_MUSIC)
                .setContentIntent(contentIntent);
    }
}
