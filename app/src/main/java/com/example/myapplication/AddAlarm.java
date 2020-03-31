 package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.util.Calendar;

 public class AddAlarm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

     AlarmManager alarmManager;
     private PendingIntent pendingIntent;
     private TimePicker alarmTimePicker;
     private static AddAlarm inst;
     private EditText alarmTextView;
     TextView date;
     Calendar c;

     private String alarmeDate,alarmeTime;
     private boolean repeat;
     private String message;
     private String status;
    public static int ALARMEID=0;


     public static AddAlarm instance() {
         return inst;
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView=findViewById(R.id.alarmText);
        date=findViewById(R.id.dateMessage);
        Button date=findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        final Button enregistrer=findViewById(R.id.EnregistrerAlarme);
        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm(v);
            }
        });
    }
    public void setAlarm(View v){
         message=alarmTextView.getText().toString();
        alarmeTime= alarmTimePicker.getCurrentHour() + ":" + alarmTimePicker.getCurrentMinute();
        AlarmInfo alarmInfo=new AlarmInfo();
        alarmInfo.setRepeat(repeat);
        alarmInfo.setMessage(message);

        alarmInfo.setStatus("on");
        c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,alarmTimePicker.getCurrentHour());
        c.set(Calendar.MINUTE,alarmTimePicker.getCurrentMinute());
        c.set(Calendar.SECOND,0);
        alarmInfo.setCalendar(c);

        if(TextUtils.isEmpty(alarmTextView.getText())){
            alarmTextView.setError( "medicament obligatoire !" );
        }else {
            Alarm.listeAlarmes.add(alarmInfo);
            alarmInfo.setId(++ALARMEID);
            startAlarm(alarmInfo);
            Intent intent = new Intent(this, Alarm.class);
            this.startActivity(intent);
        }
        }

    public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            repeat=true;

        } else {
            repeat=false;
        }
    }
     @Override
     public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDate= DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());
        date.setText(currentDate);
        alarmeDate=currentDate;
     }
     public void startAlarm(AlarmInfo alarmInfo){
        AlarmManager alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent =new Intent(this,AlertReceiver.class);
        intent.putExtra("medicament",alarmInfo.getMessage());
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,alarmInfo.getId(),intent,0);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
             alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmInfo.getCalendar().getTimeInMillis(),pendingIntent);
         }
         Toast.makeText(this, "Alarme ON", Toast.LENGTH_LONG).show(); //Generate a toast only if you want
     }
     public void cancelAlarm(){
         AlarmManager alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
         Intent intent =new Intent(this,AlertReceiver.class);
         PendingIntent pendingIntent=PendingIntent.getBroadcast(this,1,intent,0);
        alarmManager.cancel(pendingIntent);
         Toast.makeText(this, "Alarme OFF", Toast.LENGTH_LONG).show(); //Generate a toast only if you want
     }
 }
