package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Parametres extends AppCompatActivity {
    TextView xx, yy, zz;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 11;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    Switch swAcc;
    Switch swLoc;
    Intent ServLocalIntent;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static Boolean locStatus;
    public static Boolean accStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        swAcc = (Switch) findViewById(R.id.switch1);
        swLoc = (Switch) findViewById(R.id.switch2);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        swAcc.setChecked(accStatus);
        swLoc.setChecked(locStatus);
        ServLocalIntent = new Intent(Parametres.this, LocalisationService.class);
        sharedpreferences = getSharedPreferences(MainActivity.mypreference,
                Context.MODE_PRIVATE);

        editor = sharedpreferences.edit();


        if (accStatus) {
            startService(new Intent(Parametres.this, MyService.class));
            swAcc.setText("ON");
        } else {
            stopService(new Intent(Parametres.this, MyService.class));
            swAcc.setText("OFF");
        }
        if (locStatus) {
            startService(ServLocalIntent);
            //Toast.makeText(Parametres.this, "Le service de localisation est commencé", Toast.LENGTH_LONG).show();
            swLoc.setText("ON");

        } else {
            stopService(ServLocalIntent);
           // Toast.makeText(Parametres.this, "service de localisation est arrêté", Toast.LENGTH_LONG).show();
            swLoc.setText("OFF");
        }
        swAcc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(MainActivity.accel, true);
                    editor.commit(); // commit changes
                    startService(new Intent(Parametres.this, MyService.class));
                    swAcc.setText("ON");
                } else {
                    // The toggle is disabled
                    editor.putBoolean(MainActivity.accel, false);
                    editor.commit(); // commit changes
                    stopService(new Intent(Parametres.this, MyService.class));
                    swAcc.setText("OFF");
                }
                accStatus = sharedpreferences.getBoolean(MainActivity.accel, false);
            }
        });
        swLoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(MainActivity.loc, true);
                    editor.commit();
                    startService(ServLocalIntent);
                    swLoc.setText("ON");
                    //Toast.makeText(Parametres.this, "Le service de localisation est commencé", Toast.LENGTH_LONG).show();

                } else {
                    editor.putBoolean(MainActivity.loc, false);
                    editor.commit();
                    stopService(ServLocalIntent);
                    swLoc.setText("OFF");
                    //Toast.makeText(Parametres.this, "service de localisation est arrêté", Toast.LENGTH_LONG).show();

                }
                locStatus = sharedpreferences.getBoolean(MainActivity.loc, false);
            }
        });

    }

}
