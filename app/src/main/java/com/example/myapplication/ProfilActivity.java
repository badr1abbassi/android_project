package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        loadUser();

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
                loadUser();
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

    public void loadUser() {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User user = dataSnapshot.getValue(User.class);
                Ename.getEditText().setText(user.getName());
                Eemail.getEditText().setText(user.getEmail());
                Ephone.getEditText().setText(user.getPhone());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("erreur", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(userListener);
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
                                                User cUser = new User(email, phone, name);
                                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(cUser).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                            Log.d("echec", "Error auth failed");
                        }

                    }
                });
    }

}
