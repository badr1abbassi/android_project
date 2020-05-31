package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Alarm extends AppCompatActivity {

    ImageButton addAlarm;
    alarmFragment bottomFragment;
    public static List<AlarmInfo> listeAlarmes;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        listeAlarmes=new ArrayList<>();
        bottomFragment = (alarmFragment) this.getSupportFragmentManager().findFragmentById(R.id.bottom_fragment);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("alarmes");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    AlarmInfo alarmInfo=postSnapshot.getValue(AlarmInfo.class);
                    listeAlarmes.add(alarmInfo);
                   // startAlarm(alarmInfo);
                }
                bottomFragment.showAlarmes(listeAlarmes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Alarm.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

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
   /*public void startAlarm(AlarmInfo alarmInfo){
        AlarmManager alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent =new Intent(this,AlertReceiver.class);
        intent.putExtra("title",alarmInfo.getMessage());
        intent.putExtra("uriImage",alarmInfo.getImageUrl());
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,alarmInfo.getId(),intent,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmInfo.getCalendar().getTimeInMillis(),pendingIntent);
            Toast.makeText(this, "Alarme "+alarmInfo.getId()+" ON", Toast.LENGTH_LONG).show(); //Generate a toast only if you want
        }
    }*/
}
