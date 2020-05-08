 package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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
     String cameraPermission[];
     String storagePermission[];
     Uri image_uri;
     ImageView image;
     Uri resultUri;
     public static AddAlarm instance() {
         return inst;
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
       image=findViewById(R.id.imageIv2);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageImportDialog();
            }
        });

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
     private void showImageImportDialog() {
         String[] items = {" Camera", " Gallery"};
         AlertDialog.Builder dialog = new AlertDialog.Builder(this);
         dialog.setTitle("Choisir une image");
         dialog.setItems(items, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 if(which == 0) {
                     if (!checkCameraPermission()) {
                         requestCameraPermissions();
                     }
                     else {
                         pickCamera();
                     }
                 }
                 if( which == 1) {
                     if (!checkStoragePermission()) {
                         requestStoragePermissions();
                     }
                     else {
                         pickGallery();
                     }

                 }
             }
         });
         dialog.create().show();
     }
     private boolean checkCameraPermission(){
         boolean result = ContextCompat.checkSelfPermission(this,
                 Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
         boolean result1 = ContextCompat.checkSelfPermission(this,
                 Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
         return result && result1;
     }
     private void requestCameraPermissions() {
         ActivityCompat.requestPermissions(this,cameraPermission, Scanner.CAMERA_REQUEST_CODE);
     }
     private boolean checkStoragePermission() {
         return ContextCompat.checkSelfPermission(this,
                 Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
     }

     private void requestStoragePermissions(){
         ActivityCompat.requestPermissions(this,storagePermission, Scanner.STORAGE_REQUEST_CODE);
     }

     private void pickCamera() {
         ContentValues values = new ContentValues();
         values.put(MediaStore.Images.Media.TITLE,"NewPic");
         values.put(MediaStore.Images.Media.DESCRIPTION,"Image To text");
         image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
         Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
         startActivityForResult(cameraIntent, Scanner.IMAGE_PICK_CAMERA_CODE);
     }
     private void pickGallery(){
         Intent intent = new Intent(Intent.ACTION_PICK);
         intent.setType("image/*");
         startActivityForResult(intent, Scanner.IMAGE_PICK_GALLERY_CODE);
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (resultCode == RESULT_OK) {
             if (requestCode == Scanner.IMAGE_PICK_GALLERY_CODE) {
                 CropImage.activity(data.getData())
                         .setGuidelines(CropImageView.Guidelines.ON).start(this);
             }
             if (requestCode == Scanner.IMAGE_PICK_CAMERA_CODE) {
                 CropImage.activity(image_uri)
                         .setGuidelines(CropImageView.Guidelines.ON).start(this);
             }
         }
         if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
             CropImage.ActivityResult result = CropImage.getActivityResult(data);
             if (resultCode == RESULT_OK) {
                 resultUri = result.getUri();
                 image.setImageURI(resultUri);

             }
             else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                 Exception error = result.getError();
                 Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
             }
         }
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
         alarmInfo.setImage(resultUri);
        if(TextUtils.isEmpty(alarmTextView.getText())){
            alarmTextView.setError( "medicament obligatoire !" );
        }else {
            Alarm.listeAlarmes.add(alarmInfo);
            alarmInfo.setId(++ALARMEID);
            startAlarm(alarmInfo);
            Intent intent = new Intent(AddAlarm.this, Alarm.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);

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
        intent.putExtra("title",alarmInfo.getMessage());
         intent.putExtra("uriImage",alarmInfo.getImage().toString());
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

     public void saveAlarm(AlarmInfo alarmInfo){
         FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("alarm"+alarmInfo.getId())
                 .setValue(alarmInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful()) {
                     Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(AddAlarm.this, Alarm.class);
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     startActivity(intent);

                 }
                 else {
                     Toast.makeText(getApplicationContext(),"Probleme", Toast.LENGTH_SHORT).show();
                 }
             }
         });
     }
 }
