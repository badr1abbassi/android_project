package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    Button addAlarm;
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

}
