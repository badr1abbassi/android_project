package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Authentification extends AppCompatActivity implements View.OnClickListener {

    EditText editTextemail , editTextmdp;
    ProgressBar progressBar;
    public User user;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        findViewById(R.id.textViewAuth).setOnClickListener(this);
        findViewById(R.id.btnAuth).setOnClickListener(this);
        progressBar= findViewById(R.id.progressBar);
        editTextmdp = findViewById(R.id.mdp);
        editTextemail = findViewById(R.id.email);
        mAuth=FirebaseAuth.getInstance();
    }


    private void authentification() {
        final String email = editTextemail.getText().toString().trim();
        final String mdp = editTextmdp.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        if(email.isEmpty()){
            editTextemail.setError("Vous devez entrer votre email");
            editTextemail.requestFocus();
            return;
        }
        if(mdp.isEmpty()){
            editTextmdp.setError("Vous devez entrer le mot de passe");
            editTextmdp.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(Authentification.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewAuth:
                finish();
                startActivity(new Intent(Authentification.this,Inscription.class));
                break;
            case R.id.btnAuth:
                authentification();
                break;
        }
    }
}
