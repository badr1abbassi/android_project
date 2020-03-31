package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    String medicament;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle b=intent.getExtras();
        if(b!=null){
            medicament=(String) b.get("medicament");
            if(medicament!=null){
        NotificationHelper notificationHelper=new NotificationHelper(context);
        NotificationCompat.Builder nb=notificationHelper.getChannelNotification("Votre medicament",medicament);
        notificationHelper.getManeger().notify(1,nb.build());
            }else{
                Log.d("status","-------------------------> medicament khaaawi");

            }
        }else{
            Log.d("status","-------------------------> madazzzzch l'objet");
        }
    }
}
