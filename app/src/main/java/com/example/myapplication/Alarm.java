package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Vector;

public class Alarm extends AppCompatActivity {

    Button addAlarm;
    public static Vector<AlarmInfo> listeAlarmes=new Vector<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        alarmFragment bottomFragment = (alarmFragment) this.getSupportFragmentManager().findFragmentById(R.id.bottom_fragment);
        bottomFragment.showText();
        addAlarm=findViewById(R.id.addAlarm);
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClick(v);
            }
        });
    }
    public void myClick(View v){
        Intent intent =new Intent(this,AddAlarm.class);
        this.startActivity(intent);
    }

}
