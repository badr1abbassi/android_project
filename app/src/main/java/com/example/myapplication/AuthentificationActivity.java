package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthentificationActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout editTextemail, editTextmdp;
    ProgressBar progressBar;
    public User user;
    private FirebaseAuth mAuth;
    String Erreur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        findViewById(R.id.textViewAuth).setOnClickListener(this);
        findViewById(R.id.btnAuth).setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        editTextmdp = findViewById(R.id.mdp);
        editTextemail = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
    }


    private void authentification() {
        final String email = editTextemail.getEditText().getText().toString().trim();
        final String mdp = editTextmdp.getEditText().getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        if (email.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            editTextemail.setError("Vous devez entrer votre email");
            editTextemail.requestFocus();
            return;
        }
        if (mdp.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            editTextmdp.setError("Vous devez entrer le mot de passe");
            editTextmdp.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    MainActivity.linkedUser = null;
                    MainActivity.linkedId = null;
                    finish();
                    Intent intent = new Intent(AuthentificationActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(AuthentificationActivity.this).setTitle("Problème d'authentification")
                            .setPositiveButton(android.R.string.yes, null);
                    if (task.getException().getMessage().contains("network")) {
                       Erreur= "Erreur de connexion\nVerifier votre connexion avant de réessayer";
                       alert.setIcon(R.drawable.no_network);
                    } else if (task.getException().getMessage().contains("password")) {
                        Erreur = "Le mot de passe est incorrecte";
                        alert.setIcon(R.drawable.wrong_password);
                    } else if (task.getException().getMessage().contains("record")) {
                        Erreur ="Cet email n'appartient à aucun utilisateur";
                        alert.setIcon(R.drawable.auth);
                    } else if (task.getException().getMessage().contains("email")) {
                       Erreur = "Vous devez entrer un format d'email valide ";
                        alert.setIcon(R.drawable.auth);
                    } else {
                        Erreur = task.getException().getMessage();
                    }

                            alert.setMessage(Erreur).show();
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewAuth:
                finish();
                startActivity(new Intent(AuthentificationActivity.this, InscriptionActivity.class));
                break;
            case R.id.btnAuth:
                authentification();
                break;
        }
    }

}
