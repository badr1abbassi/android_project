package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MedicamentActivity extends AppCompatActivity {

    private String url;
    private String nomMedic;
    private ImageView imageView;
    private LinearLayout ll;
    private TextView textView;
    private Button btn_annuler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament);
        imageView=findViewById(R.id.imageView6);
        textView=(TextView) findViewById(R.id.nomMedic);
        btn_annuler=findViewById(R.id.btn_annuler);
        if(getIntent().getSerializableExtra("url")!=null){
            url=(String) getIntent().getSerializableExtra("url");
            /*Picasso.get().load(url).placeholder(R.drawable.health)// Place holder image from drawable folder
                    .error(R.drawable.x).centerCrop().fit().into(imageView);*/
            Picasso picasso = new Picasso.Builder(this)
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            Log.d("picasso","exception"+exception.getMessage());
                            Log.d("picasso url",url);
                        }
                    })
                    .build();
            picasso.load(NotificationHelper.stringUri)
                    .fit()
                    .into(imageView);
        }
            nomMedic=(String) NotificationHelper.title;
            textView.setText(nomMedic);
            btn_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
