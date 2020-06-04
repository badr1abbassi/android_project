package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Parametres extends AppCompatActivity {
    TextView xx,yy,zz;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    Switch swAcc;
    Switch swLoc;
    Intent ServLocalIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        swAcc = (Switch) findViewById(R.id.switch1);
        swLoc = (Switch) findViewById(R.id.switch2);
        ServLocalIntent = new Intent(Parametres.this, LocalisationService.class);
        swAcc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(new Intent(Parametres.this, MyService.class));
                    swAcc.setText("ON");
                } else {
                    // The toggle is disabled
                    stopService(new Intent(Parametres.this, MyService.class));
                    swAcc.setText("OFF");
                }
            }
        });
        swLoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(ServLocalIntent);
                    Toast.makeText(Parametres.this, "Service localisation Started", Toast.LENGTH_LONG).show();
                    swLoc.setText("ON");

                } else {
                    stopService(ServLocalIntent);
                    Toast.makeText(Parametres.this, "Service localisation Stopped", Toast.LENGTH_LONG).show();
                    swLoc.setText("OFF");
                }
            }
        });
    }





}
