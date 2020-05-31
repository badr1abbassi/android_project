package com.example.myapplication;


import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;



public class LocalisationService extends IntentService {
    private static final int REQUEST_LOCATION = 12;
    LocationManager locationManager;
    private Coordonee coordonee;

    public LocalisationService() {
        super("LocalisationService");
    }


    @Override
    protected void onHandleIntent(Intent workIntent) {
        while (true) {
            getMyPosition();
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                System.out.println("salina mn 6 sec");
            }

        }
    }

    public void getMyPosition() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            sendLocation();
        }
    }

    private void sendLocation() {
        Coordonee c = new Coordonee();
        if (ActivityCompat.checkSelfPermission(LocalisationService.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (LocalisationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                c.setLatitude(String.valueOf(latti));
                c.setLongitude(String.valueOf(longi));

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                c.setLatitude(String.valueOf(latti));
                c.setLongitude(String.valueOf(longi));


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                c.setLatitude(String.valueOf(latti));
                c.setLongitude(String.valueOf(longi));
            } else {
                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();
            }
            if (c.getLongitude() != null && c.getLatitude() != null) {
                saveLocalisation(c);
            } else {
                Toast.makeText(this, "impossible d'obtenir votre localisation", Toast.LENGTH_SHORT).show();
            }
        }

    }
    protected void buildAlertMessageNoGps() {
        Toast.makeText(this, "activer gps", Toast.LENGTH_SHORT).show();
    }


    public void saveLocalisation(Coordonee c){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Localisation")
                    .setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                    }
                }
        });
        }
    }
}