package com.example.roberto.reveles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        usernameEditText = ( EditText) findViewById(R.id.registrousername);

        passwordEditText   = ( EditText) findViewById(R.id.registropassword);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    Toast.makeText(RegistroActivity.this, "usuario creado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(RegistroActivity.this, "usuario", Toast.LENGTH_LONG).show();
                }

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

    public void registrado(View view) {
        String username = usernameEditText.getText().toString();
        String password= passwordEditText.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()){
                    Toast.makeText(RegistroActivity.this, "Error de conexion", Toast.LENGTH_LONG).show();

                }else{
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    user.sendEmailVerification();
                }
            }
        });
    }
}
