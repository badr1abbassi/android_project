package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LinkAccount extends AppCompatActivity {
    TextInputLayout userId;
    TextView tvLinked;
    Button add, btnBack;
    String identif;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_account);
        userId = findViewById(R.id.userId);
        add = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        tvLinked = findViewById(R.id.tvLinked);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkAccount();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alreadyLinked();
            }
        });
        ref = FirebaseDatabase.getInstance().getReference();
        alreadyLinked();


    }

    public void linkAccount() {
        if (add.getText().equals("Changer")) {
            userId.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
            btnBack.setVisibility(View.VISIBLE);
            userId.setHint("Identifiant");
            userId.getEditText().setText("");
            tvLinked.setText(R.string.addAcount);

            add.setText("Ajouter");
            return;
        }
        identif = userId.getEditText().getText().toString().trim();
        if (identif.isEmpty()) {
            userId.setError("Vous devez entrer un identifiant");
            userId.requestFocus();
            return;
        }
        ref.child("Users").child(identif).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (identif.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        userId.setError("Cet identifiant appartient à votre compte vous devez choisir un autre identifiant");
                        userId.requestFocus();
                        return;
                    }

                    FirebaseDatabase.getInstance().getReference("Users").child(identif).child("linked")
                            .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("linked")
                            .setValue(identif).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                } else {
                    userId.setError("Cet identifiant n'appartient à aucun utilisateur");
                    userId.requestFocus();
                    return;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void alreadyLinked() {
        btnBack.setVisibility(View.GONE);
        userId.setError(null);

        if (MainActivity.linkedId != null) {
            tvLinked.setText("Vous avez déja liée avec ");
            userId.getEditText().setText(MainActivity.linkedUser.getName());
            add.setText("Changer");
            return;
        }


    }

}
