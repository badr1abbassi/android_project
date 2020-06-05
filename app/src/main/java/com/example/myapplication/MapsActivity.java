package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;
    String lattitude, longitude;
    String st;
    Button sat;
    CheckBox traffic;
    Spinner style;

    String dateLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ActionBar actionBar = getSupportActionBar();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getIntent().getSerializableExtra("longitude") != null && getIntent().getSerializableExtra("lattitude") != null) {

            if (getIntent().getSerializableExtra("longitude") != null && getIntent().getSerializableExtra("lattitude") != null
                    && getIntent().getSerializableExtra("dateLocalisation") != null
            ) {
                dateLocal = (String) getIntent().getSerializableExtra("dateLocalisation");
                longitude = (String) getIntent().getSerializableExtra("longitude");
                lattitude = (String) getIntent().getSerializableExtra("lattitude");
            }
            actionBar.setSubtitle(dateLocal);

            style = (Spinner) this.findViewById(R.id.style);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.styles, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            style.setAdapter(adapter);
            style.setOnItemSelectedListener(this);
            sat = (Button) this.findViewById(R.id.satellite);
            traffic = (CheckBox) this.findViewById(R.id.traffic);


            sat.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    myClick(v);
                }
            });
            traffic.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    myClick(v);
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }


    private void myClick(View v) {
        switch (v.getId()) {
            case R.id.satellite:
                if (sat.getText().equals("Satellite")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    sat.setText("Terrain");
                    break;
                } else if (sat.getText().equals("Terrain")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    sat.setText("Plan");
                    break;
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    sat.setText("Satellite");
                    break;
                }
            case R.id.traffic:
                if (traffic.isChecked()) {
                    mMap.setTrafficEnabled(true);
                } else {
                    mMap.setTrafficEnabled(false);
                }


        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        if (lattitude != null && longitude != null) {
            LatLng pos = new LatLng(Double.valueOf(lattitude), Double.valueOf(longitude));
            mMap.addMarker(new MarkerOptions().position(pos).title("position actuelle"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 13));
        } else {
            Toast.makeText(this, "erreur lors de chargement des donnees", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        st = parent.getItemAtPosition(position).toString();
        switch (position) {
            case 0:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.standard));
                traffic.setTextColor(Color.parseColor("#000000"));
                CompoundButtonCompat.setButtonTintList(traffic, ColorStateList.valueOf(Color.parseColor("#000000")));
                style.setBackgroundColor(Color.parseColor("#CCFFFFFF"));
                style.setPopupBackgroundResource(R.color.md_grey_100);
                break;
            case 1:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.silver));
                traffic.setTextColor(Color.parseColor("#000000"));
                CompoundButtonCompat.setButtonTintList(traffic, ColorStateList.valueOf(Color.parseColor("#000000")));
                style.setPopupBackgroundResource(R.color.md_grey_300);
                style.setBackgroundColor(Color.parseColor("#CCFFFFFF"));
                break;
            case 2:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.retro));
                traffic.setTextColor(Color.parseColor("#000000"));
                CompoundButtonCompat.setButtonTintList(traffic, ColorStateList.valueOf(Color.parseColor("#000000")));
                style.setBackgroundColor(Color.parseColor("#CCFFFFFF"));
                style.setPopupBackgroundResource(R.color.md_brown_50);
                break;
            case 3:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.dark));
                traffic.setTextColor(Color.parseColor("#FFFFFF"));
                CompoundButtonCompat.setButtonTintList(traffic, ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                style.setBackgroundColor(Color.parseColor("#CCFFFFFF"));
                style.setPopupBackgroundResource(R.color.md_grey_700);
                break;
            case 4:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.night));
                traffic.setTextColor(Color.parseColor("#FFFFFF"));
                CompoundButtonCompat.setButtonTintList(traffic, ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                style.setBackgroundColor(Color.parseColor("#CCFFFFFF"));
                style.setPopupBackgroundResource(R.color.md_blue_grey_400);
                break;
            case 5:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.aubergine));
                traffic.setTextColor(Color.parseColor("#FFFFFF"));
                CompoundButtonCompat.setButtonTintList(traffic, ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                style.setBackgroundColor(Color.parseColor("#CCFFFFFF"));
                style.setPopupBackgroundResource(R.color.md_blue_grey_600);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
