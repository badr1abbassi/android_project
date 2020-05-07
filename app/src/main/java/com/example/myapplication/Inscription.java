package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Inscription extends AppCompatActivity implements View.OnClickListener {
    EditText editTextemail , editTextmdp, editTextConfmdp;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        editTextmdp = findViewById(R.id.mdp);
        editTextemail = findViewById(R.id.email);
        editTextConfmdp = findViewById(R.id.mdp2);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btnInscrire).setOnClickListener(this);
        findViewById(R.id.textViewAuth).setOnClickListener(this);
    }

    private void registerUser(){
        String email = editTextemail.getText().toString().trim();
        String mdp = editTextmdp.getText().toString().trim();
        String confmdp = editTextConfmdp.getText().toString().trim();

        if(email.isEmpty()){
            editTextemail.setError("Vous devez entrer votre email");
            editTextemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextemail.setError("Vous devez entrer un email valide");
            editTextemail.requestFocus();
            return;
        }
        if(mdp.isEmpty() || mdp.length()<6){
            editTextmdp.setError("Vous devez entrer au moin 6 caractères");
            editTextmdp.requestFocus();
            return;
        }
        if(!mdp.equals(confmdp)){
            editTextConfmdp.setError("Les mots de passes ne sont pas identiques");
            editTextConfmdp.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, mdp)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Inscription.this, Authentification.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(),"Cet email est déja utilisé", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        // ...
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnInscrire:
                registerUser();
                break;
            case R.id.textViewAuth:
                startActivity(new Intent(this,Authentification.class));
                break;
        }
    }
}
