package com.example.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by Tutlane on 02-08-2017.
 */

public class MyService extends Service implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private MediaPlayer player;
    private int seuil;
    @Override
    public IBinder onBind(Intent intent) {
        return  null;
    }

    @Override
    public void onCreate(){
        seuil=15;
        Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        player = MediaPlayer.create(this, R.raw.alerte);

        // This will play the ringtone continuously until we stop the service.
        player.setLooping(true);
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stopping the player when service is destroyed
        player.stop();
        seuil=100;
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            if(y>seuil){
                player.start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}