package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordActivity extends AppCompatActivity {
    TextInputLayout Emdp, Eoldmdp;
    Button btnSave, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Emdp = findViewById(R.id.ENewmdp);
        Eoldmdp = findViewById(R.id.Emdp);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass();
            }
        });

    }


    public void changePass() {
        if(!isConnected()){
            noConnectionError();
            return;
        }
        final String mdp = Emdp.getEditText().getText().toString().trim();
        final String oldmdp = Eoldmdp.getEditText().getText().toString().trim();


        if (mdp.isEmpty() || mdp.length() < 6) {
            Emdp.setError("Vous devez entrer au moin 6 caractères");
            Emdp.requestFocus();
            return;
        }


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldmdp);

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(mdp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ChangePasswordActivity.this, "Success,votre mot de passe est mis à jour", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        finish();
                                    } else {
                                        Toast.makeText(ChangePasswordActivity.this, "Erreur, le mot de passe n'est pas mis à jour", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Eoldmdp.setError("Mot de passe incorrecte");
                            Eoldmdp.requestFocus();
                        }
                    }
                });

    }
    public void noConnectionError() {
        new AlertDialog.Builder(this)
                .setTitle("Problème de connexion")
                .setMessage("vérifiez votre connexion et réessayez")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(R.drawable.wrong_password)
                .show();
    }
    public boolean  isConnected() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }

}
