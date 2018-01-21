package com.example.roberto.reveles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText usuariosEditText;
    private DatabaseReference iDatabase;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        usernameEditText = ( EditText) findViewById(R.id.registrousername);
        passwordEditText   = ( EditText) findViewById(R.id.registropassword);
        usuariosEditText = (EditText) findViewById(R.id.nombreusuario);
        mprogress= new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    Toast.makeText(RegistroActivity.this, "usuario creado", Toast.LENGTH_LONG).show();
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
        final String usuario = usuariosEditText.getText().toString();
        final String username = usernameEditText.getText().toString();
        final String password= passwordEditText.getText().toString();
        if (!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            //mprogress.setMessage("llenar todas las casillas");
            //mprogress.show();

            firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {




                    }else {
                        firebaseAuth.signInWithEmailAndPassword(username,password);
                        DatabaseReference iDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
                        DatabaseReference currentUserDB = iDatabase.child(firebaseAuth.getCurrentUser().getUid());
                        currentUserDB.child("NombreUsuario").setValue(usuario);
                        currentUserDB.child("Correo").setValue(username);
                        currentUserDB.child("password").setValue(password);
                        String user_id = firebaseAuth.getCurrentUser().getUid();
                        Intent intent2 = new Intent(RegistroActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        user.sendEmailVerification();
                    }


                }
            });
        }
    }
}
