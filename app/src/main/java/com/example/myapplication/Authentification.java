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
        findViewById(R.id.progressBar);
        editTextmdp = findViewById(R.id.mdp);
        editTextemail = findViewById(R.id.email);
        mAuth=FirebaseAuth.getInstance();

    }

    private void authentification() {
        final String email = editTextemail.getText().toString().trim();
        final String mdp = editTextmdp.getText().toString().trim();
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

                   /*
                    DatabaseReference mFirebaseRef = FirebaseDatabase.getInstance().getReference();


                    Users user = new Users(email, mdp, "06666666");
                    mFirebaseRef.child("ayoub").child("alarme").child("alarme1").setValue(user);


                    Query mQueryRef = mFirebaseRef.child("ayoub").orderByChild("email").equalTo("ayoub@gmail.com");

                    // This type of listener is not one time, and you need to cancel it to stop
                    // receiving updates.
                    mQueryRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                            // This will fire for each matching child node.
                            Users user = snapshot.getValue(Users.class);
                            System.out.println("mmmmmmm");
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });
                    */
                    getData();
                    Intent intent = new Intent(Authentification.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getData(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User user = dataSnapshot.getValue(User.class);
                System.out.println("mmmmmmmmmmmmm"+user.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("erreur", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(postListener);
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
