package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfilActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView textView;
    DatabaseReference ref;

    TextInputLayout Ename, Eemail, Ephone, Eoldmdp;
    Button btnSave, btnBack;
    LinearLayout buttons;
    ImageButton modifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        Ename = findViewById(R.id.Ename);
        Eemail = findViewById(R.id.Eemail);
        Ephone = findViewById(R.id.Ephone);
        modifier = findViewById(R.id.modifier);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        buttons = findViewById(R.id.buttons);
        Eoldmdp = findViewById(R.id.Emdp);

        Ename.getEditText().setText(MainActivity.Cuser.getName());
        Eemail.getEditText().setText(MainActivity.Cuser.getEmail());
        Ephone.getEditText().setText(MainActivity.Cuser.getPhone());

        Ename.getEditText().setInputType(InputType.TYPE_NULL);
        Eemail.getEditText().setInputType(InputType.TYPE_NULL);
        Ephone.getEditText().setInputType(InputType.TYPE_NULL);
        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttons.setVisibility(View.VISIBLE);
                Ename.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                Eemail.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                Ephone.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttons.setVisibility(View.GONE);
                Ename.getEditText().setText(MainActivity.Cuser.getName());
                Eemail.getEditText().setText(MainActivity.Cuser.getEmail());
                Ephone.getEditText().setText(MainActivity.Cuser.getPhone());
                Ename.getEditText().setInputType(InputType.TYPE_NULL);
                Eemail.getEditText().setInputType(InputType.TYPE_NULL);
                Ephone.getEditText().setInputType(InputType.TYPE_NULL);

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserData();
            }
        });

    }

    public void addUserData() {

        final String email = Eemail.getEditText().getText().toString().trim();
        final String name = Ename.getEditText().getText().toString().trim();
        final String phone = Ephone.getEditText().getText().toString().trim();
        final String oldmdp = Eoldmdp.getEditText().getText().toString().trim();


        if (email.isEmpty()) {
            Eemail.setError("Vous devez entrer votre email");
            Eemail.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            Ephone.setError("Vous devez entrer votre numéro de téléphone");
            Ephone.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            Ename.setError("Vous devez entrer votre identifiant");
            Ename.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Eemail.setError("Vous devez entrer un email valide");
            Eemail.requestFocus();
            return;
        }
        if(!isConnected()){
            noConnectionError();
            return;
        }
        if(oldmdp.isEmpty()){
            Eoldmdp.setError("Vous devez entrer votre mot de passe");
            Eoldmdp.requestFocus();
            return;
        }


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldmdp);
        // Current Login Credentials \\
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updateEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("succes", "email updated.");
                                                User user = new User(email, phone, name);
                                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }
                                                });
                                            } else {
                                                System.out.println("laaaaaaa email " + task.getException().getMessage());
                                            }
                                        }
                                    });
                        } else {
                            Eoldmdp.setError("Mot de passe incorrect");
                            Eoldmdp.requestFocus();
                            return;
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
                .setIcon(android.R.drawable.ic_dialog_alert)
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
