package com.example.dell.moviesapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Anca on 1/14/2018.
 */

public class MainActivityPatients   extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView authFailed;
    private Button loginButton;
    private EditText emailEdit,passwordEdit;
    private static final String TAG = "LOGIN_SIGNUP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
//        Intent intent = new Intent(MainActivityPatients.this, HomePatientActivity.class);
//        startActivity(intent);


        loginButton = (Button) findViewById(R.id.loginButtonUser);
//        signupButton = (Button) findViewById(R.id.signupButton);
        emailEdit = (EditText) findViewById(R.id.emailUser);
        passwordEdit = (EditText) findViewById(R.id.passwordUser);
        authFailed = (TextView) findViewById(R.id.authFailedUser);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signIn(emailEdit.getText().toString(), passwordEdit.getText().toString());
            }
        });
//
//        signupButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                signUp(emailEdit.getText().toString(), passwordEdit.getText().toString());
//            }
//        });


    }

    public void signIn(String email, String password){
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                            rootRef.child("admins").child("email").addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String email = dataSnapshot.getValue(String.class);
                                    if(email.equals(mAuth.getCurrentUser().getEmail())  )
                                    {
                                        signedIn(true);
                                    }
                                    else
                                    {
                                        signedIn(false);
                                    }
                                    Log.d(TAG,"AAAAAAAAAAAADMIIIINUL :    " + email);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d(TAG,"CANCELLLEEEED:    ");
                                }
                            });

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivityPatients.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            authFailed.setText("Authentification FAILED !");
                        }
                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            authFailed.setText("Authentification FAILED !");
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    private void signUp(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            signedIn(false);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivityPatients.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            authFailed.setText("Authentification FAILED !");
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    //Normal user - HomeActivity
    public void signedIn(boolean isAdmin){
        Intent intent;
        authFailed.setText("");
        if(isAdmin==true) {
            intent = new Intent(MainActivityPatients.this, HomePatientAdminActivity.class);
        }
        else{
            intent = new Intent(MainActivityPatients.this, HomePatientActivity.class);
        }
        startActivity(intent);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailEdit.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Required.");
            valid = false;
        } else {
            emailEdit.setError(null);
        }

        String password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError("Required.");
            valid = false;
        } else {
            passwordEdit.setError(null);
        }

        return valid;
    }
}
