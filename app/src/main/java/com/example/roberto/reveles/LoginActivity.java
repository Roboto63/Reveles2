package com.example.roberto.reveles;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        usernameEditText = ( EditText) findViewById(R.id.registrousername);

        passwordEditText   = ( EditText) findViewById(R.id.registropassword);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    if(user.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Correo no verificado", Toast.LENGTH_SHORT).show();
                    }


                }

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


            }
        };

    }



    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (authStateListener != null)
            firebaseAuth.removeAuthStateListener(authStateListener);
    }

    public void login(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Error", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent2);
                }
            }
        });


    }
    public void registrar(View view) {
        Intent intent2 =new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(intent2);
    }

    public void entrar(View view) {
        Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent2);
    }
}

