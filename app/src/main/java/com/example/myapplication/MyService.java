package com.example.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

/**
 * Created by Tutlane on 02-08-2017.
 */

public class MyService extends Service implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    public static MediaPlayer player;
    private static final String channelID="chanelID";
    @Override
    public IBinder onBind(Intent intent) {
        return  null;
    }

    @Override
    public void onCreate(){
        //Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        player = MediaPlayer.create(this, R.raw.alerte);

        // This will play the ringtone continuously until we stop the service.
        player.setLooping(false);
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stopping the player when service is destroyed
        //Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            if(y>15){
                player.start();
                //System.out.println("x="+x+"y="+y+"z="+z);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.appel.performClick();
                    }
                }, 5000);

              /*  NotificationHelper notificationHelper=new NotificationHelper(this);
                NotificationCompat.Builder nb= null;
                try {
                    nb = notificationHelper.getAlerteNotification();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                notificationHelper.getManeger().notify(2,nb.build());*/
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}