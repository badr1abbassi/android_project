package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Authentification extends AppCompatActivity implements View.OnClickListener {

    EditText editTextemail , editTextmdp;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        findViewById(R.id.textViewAuth).setOnClickListener(this);
        findViewById(R.id.btnAuth).setOnClickListener(this);
        findViewById(R.id.progressBar);
        editTextmdp = findViewById(R.id.mdp);
        editTextemail = findViewById(R.id.email);
        mAuth=FirebaseAuth.getInstance();
    }

    private void authentification() {
        String email = editTextemail.getText().toString().trim();
        String mdp = editTextmdp.getText().toString().trim();
        if(email.isEmpty()){
            editTextemail.setError("Vous devez entrer votre email");
            editTextemail.requestFocus();
            return;
        }
        if(mdp.isEmpty()){
            editTextmdp.setError("Vous devez entrerle mot de passe");
            editTextmdp.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Intent intent = new Intent(Authentification.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewAuth:
                startActivity(new Intent(this,Inscription.class));
                break;
            case R.id.btnAuth:
                authentification();
                break;
        }
    }
}
