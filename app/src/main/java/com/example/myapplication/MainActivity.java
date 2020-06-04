package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    public static User Cuser, linkedUser;
    DatabaseReference ref;

    Button localisation, camera, appel, sante, taches, aider;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitude, longitude;
    private static final int REQUEST_CALL = 1;
    private static String number;
    public static String linkedId;

    private Intent ServLocalIntent;
    private FusedLocationProviderClient client;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String phone = "phoneKey";
    public static final String lId = "idKey";
    public static final String lName = "lnameKey";
    public static final String lEmail = "lemailKey";
    public static final String lphone = "lphoneKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localisation = findViewById(R.id.buttonLocalisation);
        sante = findViewById(R.id.buttonHealth);
        camera = findViewById(R.id.buttonCamera);
        appel = findViewById(R.id.buttonAppel);
        taches = findViewById(R.id.buttonTaches);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        ref = FirebaseDatabase.getInstance().getReference();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (isConnected()) {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                finish();
                startActivity(new Intent(this, AuthentificationActivity.class));
            } else {
                loadUser();
                getLinked();
                sharedpreferences = getSharedPreferences(mypreference,
                        Context.MODE_PRIVATE);
            }

        } else {
            sharedpreferences = getSharedPreferences(mypreference,
                    Context.MODE_PRIVATE);
            if (sharedpreferences.contains(Name)) {
                Cuser = new User(sharedpreferences.getString(Email, ""),sharedpreferences.getString(phone, ""),sharedpreferences.getString(Name, ""));
                linkedUser = new User(sharedpreferences.getString(lEmail, ""),sharedpreferences.getString(lphone, ""),sharedpreferences.getString(lName, ""));
                linkedId = sharedpreferences.getString(lId,"");
            }
            else
                noConnectionError();

        }

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

        navigationView.setNavigationItemSelectedListener(this);
        getPermissionLocalisation();

        ServLocalIntent=new Intent(this,LocalisationService.class);
        startService(ServLocalIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServLocalIntent = new Intent(this, LocalisationService.class);
        startService(ServLocalIntent);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(Cuser!=null && linkedUser!=null) {
            editor.putString(Name, Cuser.getName());
            editor.putString(Email, Cuser.getEmail());
            editor.putString(phone, Cuser.getPhone());
            editor.putString(lName, linkedUser.getName());
            editor.putString(lEmail, linkedUser.getEmail());
            editor.putString(lphone, linkedUser.getPhone());
            editor.putString(lId, linkedId);
            editor.commit(); // commit changes
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, AuthentificationActivity.class));
        }


    }

    public boolean  isConnected() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }

    private void myClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLocalisation:
                getLocalisation(v);
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
                Toast.makeText(this, v.getId() + "", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getLocalisation(View v) {
        Intent intent = new Intent(this, LocalisationManager.class);
        this.startActivity(intent);
    }

    public void getAlarm(View v) {
        if (isConnected()) {
            Intent intent = new Intent(this, ListeMedicamentActivity.class);
            this.startActivity(intent);
        } else {
            noConnectionError();
        }

    }

    public void getScanner(View v) {
        Intent intent = new Intent(this, ScannerActivity.class);
        this.startActivity(intent);

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, AuthentificationActivity.class));
        } else if (id == R.id.profil) {
            if (Cuser!=null) {
                startActivity(new Intent(this, ProfilActivity.class));
            } else {
                noConnectionError();
            }
        } else if (id == R.id.password) {
            if (isConnected()) {
                startActivity(new Intent(this, ChangePasswordActivity.class));
            } else {
                noConnectionError();
            }
        } else if (id == R.id.link) {
            if (linkedUser!=null) {
                startActivity(new Intent(this, LinkAccountActivity.class));
            } else {
                noConnectionError();
            }
        } else if (id == R.id.share) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String ident = FirebaseAuth.getInstance().getCurrentUser().getUid();
            myIntent.putExtra(Intent.EXTRA_TEXT, ident);
            startActivity(Intent.createChooser(myIntent, "Partager votre ID"));
        }else if (id == R.id.settings) {
            Intent intent=new Intent(this, Parametres.class);
            startActivity(intent);
        }


        return false;
    }

    public void getAppel(View v) {
        if (linkedUser.getPhone()!=null) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                if (linkedId != null) {
                    String dial = "tel:" + linkedUser.getPhone();
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                } else {
                    noLinkedError();
                }
            }
        } else {
            noConnectionError();
        }

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
                        startActivity(new Intent(MainActivity.this, LinkAccountActivity.class));

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void noConnectionError() {
        new AlertDialog.Builder(this)
                .setTitle("Problème de connexion")
                .setMessage("vérifiez votre connexion et réessayez")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void getTache(View v) {
        Intent intent = new Intent(this, TacheActivity.class);
        this.startActivity(intent);
    }

    public void getPermissionLocalisation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 13);

        }
    }

    public void loadUser() {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UIs
                Cuser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("erreur", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(userListener);
    }

    public void getLinked() {

        ref.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("linked").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    linkedId = dataSnapshot.getValue().toString();
                    getLinkedUser();
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

    public void getLinkedUser() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(linkedId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    linkedUser = dataSnapshot.getValue(User.class);
                    System.out.println("aah"+linkedUser.getName());
                    return;
                } else {
                    System.out.println("kaaaaa");
                    return;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
