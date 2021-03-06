package com.example.myapplication;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
public class LocalisationManager extends AppCompatActivity {
    private Button myLocalisation;
    private Button hisLocalisation;
    private static final int REQUEST_LOCATION = 11;
    LocationManager locationManager;
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
        if(MainActivity.linkedId==null){
            noLinkedError();
            return;
        }
        FirebaseDatabase.getInstance().getReference().child("Users").child(MainActivity.linkedId)
                .child("Localisation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    cord=dataSnapshot.getValue(Coordonee.class);
                    sendLocalisation(cord);
                    return;
                } else {
                    return;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void noLinkedError() {
        new AlertDialog.Builder(this)
                .setTitle("Votre compte n'est pas lié avec aucun utilisateur")
                .setMessage("Voulez vous ajouter un compte?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        startActivity(new Intent(LocalisationManager.this, LinkAccountActivity.class));
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
                        Intent intent =new Intent(LocalisationManager.this,MapsActivity.class);
                        intent.putExtra("longitude",c.getLongitude());
                        intent.putExtra("lattitude",c.getLatitude());
                        startActivity(intent);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void sendLocalisation(Coordonee c){
        Intent intent =new Intent(this,MapsActivity.class);
        intent.putExtra("longitude",c.getLongitude());
        intent.putExtra("lattitude",c.getLatitude());
        intent.putExtra("dateLocalisation",c.getDateLocalisation());
        startActivity(intent);
    }
    public void getMyPosition(View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
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
            cord.setDateLocalisation(Calendar.getInstance().getTime().toString());
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
                Toast.makeText(this, "Activer GPS", Toast.LENGTH_SHORT).show();
            }
            if(cord.getLongitude()!=null && cord.getLatitude() !=null){
                Toast.makeText(this, "Ta position actuelle est" + "\n" + "Lattitude = " + cord.getLatitude()
                        + "\n" + "Longitude = " + cord.getLongitude(), Toast.LENGTH_SHORT).show();
                sendLocalisation(cord);
            }else{

            }
        }
    }
    protected void buildAlertMessageNoGps() {
        Toast.makeText(this, "activer gps", Toast.LENGTH_SHORT).show();
    }
}