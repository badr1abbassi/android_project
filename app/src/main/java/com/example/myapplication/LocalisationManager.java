package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocalisationManager extends AppCompatActivity {

    private Button myLocalisation;
    private Button hisLocalisation;
    private static final int REQUEST_LOCATION = 11;

    LocationManager locationManager;
    String lattitude,longitude;
    Coordonee cord=new Coordonee();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation_manager);
        myLocalisation=findViewById(R.id.myLocal);
        hisLocalisation=findViewById(R.id.hisLoacl);
        myLocalisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyPosition(v);
            }
        });
        hisLocalisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHisPosition(v);
            }
        });
    }

    private void getHisPosition(View v) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(MainActivity.linkedId)
                .child("Localisation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                        cord=dataSnapshot.getValue(Coordonee.class);
                        System.out.println("hhhhhhhhhhhhhhhhhhhhhh"+cord.getLatitude());
                        sendLocalisation(cord);
                    return;
                } else {
                    System.out.println("pbbbbbbbbbbbbbbbbbb"+cord.getLatitude());
                    return;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void sendLocalisation(Coordonee c){
        Intent intent =new Intent(this,MapsActivity.class);
        intent.putExtra("longitude",c.getLongitude());
        intent.putExtra("lattitude",c.getLatitude());
        startActivity(intent);
    }

    public void getMyPosition(View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }
    private void getLocation() {
        cord=new Coordonee();
        if (ActivityCompat.checkSelfPermission(LocalisationManager.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (LocalisationManager.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LocalisationManager.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                cord.setLatitude(String.valueOf(latti));
                cord.setLongitude(String.valueOf(longi));

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                cord.setLatitude(String.valueOf(latti));
                cord.setLongitude(String.valueOf(longi));


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                cord.setLatitude(String.valueOf(latti));
                cord.setLongitude(String.valueOf(longi));
            } else {
                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();
            }
            if(cord.getLongitude()!=null && cord.getLatitude() !=null){
                Toast.makeText(this, "Your current location is" + "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude, Toast.LENGTH_SHORT).show();
                sendLocalisation(cord);
            }else{
                Toast.makeText(this, "makayn walo", Toast.LENGTH_SHORT).show();
            }
        }
    }
    protected void buildAlertMessageNoGps() {
        Toast.makeText(this, "activer gps", Toast.LENGTH_SHORT).show();
    }
    
    
    
}
