package com.example.roberto.reveles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InisioSesionActivity extends AppCompatActivity {

    Button apertura;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    //private RadioButton rbsesion;
    //private boolean isactivateRB;
    //private static final String STRING_PREFERENCES = "com.example.roberto.reveles";
    //private static final String PREFERENCE_ESTADO_RB ="estado.radio.button.sesion";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inisiosesion);
        //if (obtenerEstadoRB()){
        //Intent mantener = new Intent(LoginActivity.this,MainActivity.class);
        //startActivity(mantener);
        //finish();
        //}

        usernameEditText = (EditText) findViewById(R.id.registrousername);

        passwordEditText = (EditText) findViewById(R.id.registropassword);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent mantener = new Intent(InisioSesionActivity.this, MainActivity.class);
                    startActivity(mantener);
                    finish();

                }
            }
        };

        //rbsesion = (RadioButton)findViewById(R.id.RBSesion);
        //isactivateRB = rbsesion.isChecked();//desactivado el radio button
        //rbsesion.setOnClickListener(new View.OnClickListener() {
        //se activa el button
        //  @Override
        //public void onClick(View v) {
        //if(isactivateRB){
        //  rbsesion.setChecked(false);
        //}
        //isactivateRB =rbsesion.isChecked();
        //}
        //});
    }

    //public void guardarEstadoRB(){
    //  SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
    //preferences.edit().putBoolean(PREFERENCE_ESTADO_RB,rbsesion.isChecked()).apply();
    //}
    //public boolean obtenerEstadoRB(){
    //  SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
    //return  preferences.getBoolean(PREFERENCE_ESTADO_RB, false);
    //}
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void login(View view) {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (!username.isEmpty() && !password.isEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //guardarEstadoRB();
                    if (!task.isSuccessful()) {
                        Toast.makeText(InisioSesionActivity.this, "¡ups datos incorrectos!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(InisioSesionActivity.this, "¡ups falta rellenar cadros!", Toast.LENGTH_SHORT).show();
        }
    }

    public void registrar(View view) {
        Intent intent2 = new Intent(InisioSesionActivity.this, RegistroActivity.class);
        startActivity(intent2);
    }

    public void entrar(View view) {
        Intent en = new Intent(InisioSesionActivity.this, MainActivity.class);
        startActivity(en);
        finish();
    }
}


