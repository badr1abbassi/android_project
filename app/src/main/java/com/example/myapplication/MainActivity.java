package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    Button localisation,camera,appel,sante,taches,aider;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitude,longitude;
    private static final int REQUEST_CALL = 1;
    private static final String number = "+212625733640";

    private FusedLocationProviderClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localisation=findViewById(R.id.buttonLocalisation);
        sante=findViewById(R.id.buttonHealth);
        camera=findViewById(R.id.buttonCamera);
        appel = findViewById(R.id.buttonAppel);
        taches = findViewById(R.id.buttonTaches);

        sante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClick(v);
            }
        });

        localisation.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myClick(v);
        }
    });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClick(v);
            }
        });

        appel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClick(v);
            }
        });

        taches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClick(v);
            }
        });


    }

    private	void	myClick(View	v) {
            switch (v.getId()){
                case R.id.buttonLocalisation:
                    getPosition(v);
                    break;
                case R.id.buttonHealth:
                    getAlarm(v);
                    break;
                case R.id.buttonCamera:
                    getScanner(v);
                    break;
                case R.id.buttonAppel:
                   getAppel(v);
                    break;
                case R.id.buttonTaches:
                    getTache(v);
                    break;
                default:
                    Toast.makeText(this, v.getId()+"", Toast.LENGTH_SHORT).show();
                    break;
            }
    }

    public void getPosition2(View v){
        getPermissionLocalisation();
        //if(ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Toast.makeText(MainActivity.this, "localisaton:" + location, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "localisaton: NULL", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        /*}else{
            Toast.makeText(MainActivity.this, "localisaton: permission", Toast.LENGTH_SHORT).show();
            getPermissionLocalisation();
        }*/
    }
    public void getPermissionLocalisation(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
    }



    public void getPosition(View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }
    public  void getAlarm(View v){
        Intent intent =new Intent(this,Alarm.class);
        this.startActivity(intent);

    }
    public  void getScanner(View v){
        Intent intent =new Intent(this,Scanner.class);
        this.startActivity(intent);

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);



            } else {
                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();

            }
            if(longitude!=null && lattitude !=null){
                Toast.makeText(this, "Your current location is" + "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude, Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(this,MapsActivity.class);
                intent.putExtra("longitude",longitude);
                intent.putExtra("lattitude",lattitude);
                startActivity(intent);
            }else{
                Toast.makeText(this, "makayn walo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        Toast.makeText(this, "activer gps", Toast.LENGTH_SHORT).show();
    }

    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.dec) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,Authentification.class));
        }
        else if (id == R.id.profil) {
            startActivity(new Intent(this, ProfilActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAppel(View v){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }
    public void getTache(View v){
        Intent intent =new Intent(this, TacheActivity.class);
        this.startActivity(intent);
    }

}
