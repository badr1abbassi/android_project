package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.ArrayList;
import java.util.List;

public class Authentification extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText editTextemail , editTextmdp;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    Spinner spinner ;
    int choix;

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
        spinner = findViewById(R.id.spinRole);
        // Create an ArrayAdapter using the string array and a default spinner layout
       ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,R.array.role,android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
       spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
       spinner.setAdapter(spinAdapter);
       //specify the interface implementation
       spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        choix = position;
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        String error = " Choisir un role";
        Toast.makeText(getApplicationContext(),error, Toast.LENGTH_SHORT).show();
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
            editTextmdp.setError("Vous devez entrer votre mot de passe");
            editTextmdp.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    //si spinner est le pere
                    if (choix == 0) {
                        Intent intent = new Intent(Authentification.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    //si spinner est le fil => se diriger vers avctivite fils
                    else {
                        Intent intent = new Intent(Authentification.this, FilsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

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
