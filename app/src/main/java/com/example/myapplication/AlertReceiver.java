package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.IOException;

public class AlertReceiver extends BroadcastReceiver {
    String title;
    String uriString;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle b=intent.getExtras();
        if(b!=null){
            title= (String)b.get("title");
            uriString=(String) b.get("uriImage");
            if(title!=null && uriString!=null) {
            NotificationHelper notificationHelper=new NotificationHelper(context);
                NotificationCompat.Builder nb= null;
                try {
                    nb = notificationHelper.getChannelNotification(title,uriString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                notificationHelper.getManeger().notify(1,nb.build());
            }else{
                Log.d("status","-------------------------> medicament khaaawi");

            }
        }else{
            Log.d("status","-------------------------> madazzzzch l'objet");
        }
    }
}
