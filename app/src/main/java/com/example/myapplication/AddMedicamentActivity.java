package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

public class AddMedicamentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static  final int ALARME_CODE = 250;
    private TimePicker alarmTimePicker;
    private EditText alarmTextView;
    TextView date;
    Calendar c;
    //date + time
    private int minute=0;
    private int hour=0;
    private int dayOfMonth=0;
    private int month=0;
    private int year=0;


    MedicamentInfos medicamentInfos;
    private String alarmeDate,alarmeTime;
    private boolean repeat;
    private String message;
    private String status;
    private DialogFragment datePicker;
    String cameraPermission[];
    String storagePermission[];
    Uri image_uri;
    ImageButton image;
    Uri resultUri;
    Date madate;
    public static  final int CAMERA_REQUEST_CODE1 = 201;
    public static  final int STORAGE_REQUEST_CODE1 = 401;
    public static  final int IMAGE_PICK_GALLERY_CODE1 = 1003;
    public static  final int IMAGE_PICK_CAMERA_CODE1 = 1002;
    Date madate;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        image=findViewById(R.id.imageIv2);
        madate=new Date();
        cameraPermission = new String [] {Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageImportDialog();
            }
        });
        c=Calendar.getInstance();
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView=findViewById(R.id.alarmText);
        date=findViewById(R.id.dateMessage);
        Button date=findViewById(R.id.date);
        requestAlarmePermissions();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        final Button enregistrer=findViewById(R.id.EnregistrerAlarme);
        enregistrer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                setAlarm(v);
            }
        });
        mStorageReference=FirebaseStorage.getInstance().getReference("medicaments").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final  Button annuler=findViewById(R.id.annulerAlarme);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        ActivityCompat.requestPermissions(this,cameraPermission, ScannerActivity.CAMERA_REQUEST_CODE);
    }
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermissions(){
        ActivityCompat.requestPermissions(this,storagePermission, ScannerActivity.STORAGE_REQUEST_CODE);
    }
    private void requestAlarmePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SET_ALARM) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Perm check:SET_ALARM", "Permission Denied");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SET_ALARM}, 222);
            }
        } else {
            Log.d("Perm check:SET_ALARM", "Permission Exists");
        }
    }
    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image To text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, ScannerActivity.IMAGE_PICK_CAMERA_CODE);
    }
    private void pickGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, ScannerActivity.IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE1:
                if(grantResults.length >0) {
                    boolean cameraAccepted = grantResults [0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted) {
                        pickCamera();
                    }
                    else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE1:
                if(grantResults.length >0) {
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted) {
                        pickGallery();
                    }
                    else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE1) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON).start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE1) {
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON).start(this);
            }
            if(requestCode ==ALARME_CODE){
                Toast.makeText(getApplicationContext(),"votre medicament est enregistrée", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //image.setImageURI(resultUri);

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setAlarm(View v){
        message=alarmTextView.getText().toString();
        medicamentInfos =new MedicamentInfos();
        medicamentInfos.setRepeat(repeat);
        medicamentInfos.setMessage(message);
        medicamentInfos.setStatus("on");

        this.hour=alarmTimePicker.getCurrentHour();
        this.minute=alarmTimePicker.getCurrentMinute();

        madate.setHour(this.hour);
        madate.setMinute(this.minute);
        medicamentInfos.setDate(madate);

        if(TextUtils.isEmpty(alarmTextView.getText())){
            alarmTextView.setError( "medicament obligatoire !" );
        }else {
            medicamentInfos.setId(ListeMedicamentActivity.listeAlarmes.size()+1);
            uploadImage(medicamentInfos.getId());
            Intent intent = new Intent(AddMedicamentActivity.this, ListeMedicamentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }
    }
    private String getImageExtension(Uri uri){
        String extension;
        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        return extension;
    }
    private void uploadImage(int id) {
        if(resultUri !=null){
            String path=id+"."+getImageExtension(resultUri);
            StorageReference imageRef=mStorageReference.child(path);

            imageRef.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddMedicamentActivity.this,"upload successful",Toast.LENGTH_SHORT).show();
                    Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            medicamentInfos.setImageUrl(uri.toString());
                            saveAlarm(medicamentInfos);
                            try {
                                startAlarm(medicamentInfos);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(AddMedicamentActivity.this,"image ajoutée",Toast.LENGTH_SHORT).show();

                        }
                    });
                    //for progressBar
                    /* Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(0);
                        }
                    },500);*/
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddMedicamentActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.d("erreur Image",e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //ajouter progressbar
                    /*
                    double progress=(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int)progress);
                    */
                }
            });
        }else{
            Toast.makeText(this,"aucune image selectionée",Toast.LENGTH_SHORT).show();
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
        this.year=year;
        this.month=month;
        this.month++;
        this.dayOfMonth=dayOfMonth;
        madate.setYear(this.year);
        madate.setMonth(this.month);
        madate.setDayOfMonth(this.dayOfMonth);
        date.setText(this.dayOfMonth+"/"+this.month+"/"+this.year);
    }

    public void startAlarm(MedicamentInfos medicamentInfos) throws ParseException {
        AlarmManager alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent =new Intent(this,AlertReceiver.class);
        intent.putExtra("title", medicamentInfos.getMessage());
        intent.putExtra("uriImage", medicamentInfos.getImageUrl());
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this, medicamentInfos.getId(),intent,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(medicamentInfos.getDate().getMonth()!=0 && medicamentInfos.getDate().getYear()!=0) {
                Long diff = medicamentInfos.getCalendar().getTimeInMillis();
                System.out.println("getTime(): " + medicamentInfos.getCalendar().getTime().toString());
                Log.d("diff Calandar", diff + "");
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, diff, pendingIntent);
            }else{
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        medicamentInfos.getTime().getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
            //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,diff,3600000,pendingIntent);
            Toast.makeText(this, "Alarme"+ medicamentInfos.getId()+"ON", Toast.LENGTH_LONG).show(); //Generate a toast only if you want
        }
    }
    public void cancelAlarm(int id){
        AlarmManager alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent =new Intent(this,AlertReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,id,intent,0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarme OFF", Toast.LENGTH_LONG).show(); //Generate a toast only if you want
    }

    public void saveAlarm(MedicamentInfos medicamentInfos){
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("alarmes").child("alarm"+ medicamentInfos.getId())
                .setValue(medicamentInfos).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddMedicamentActivity.this, ListeMedicamentActivity.class);
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