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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Inscription extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout editTextemail , editTextmdp, editTextConfmdp,editTextName,editTextTel;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        editTextmdp = findViewById(R.id.mdp);
        editTextName = findViewById(R.id.name);
        editTextemail = findViewById(R.id.email);
        editTextConfmdp = findViewById(R.id.mdp2);
        editTextTel= findViewById(R.id.tel);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btnInscrire).setOnClickListener(this);
        findViewById(R.id.textViewAuth).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    private void registerUser(){
        final String email = editTextemail.getEditText().getText().toString().trim();
        final String mdp = editTextmdp.getEditText().getText().toString().trim();
        String confmdp = editTextConfmdp.getEditText().getText().toString().trim();
        final String id = editTextName.getEditText().getText().toString().trim();
        final String tel = editTextTel.getEditText().getText().toString().trim();

        if(id.isEmpty()){
            editTextName.setError("Vous devez entrer votre identifiant");
            editTextName.requestFocus();
            return;
        }
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
        if(tel.isEmpty()){
            editTextTel.setError("Vous devez entrer votre numéro de téléphone");
            editTextTel.requestFocus();
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
                            User user = new User(email,tel,id);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Inscription.this, Authentification.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        finish();
                                        startActivity(intent);

                                    }
                                }
                            });


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
                finish();
                startActivity(new Intent(this,Authentification.class));
                break;
        }
    }
}
