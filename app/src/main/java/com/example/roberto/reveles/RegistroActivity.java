package com.example.roberto.reveles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
    private EditText RegEmailText;
    private EditText RegPasswordText;
    private EditText RegNameText;
    private Button RegistroBtn;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog regProgress;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        firebaseAuth = FirebaseAuth.getInstance();

        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        regProgress = new ProgressDialog(this);

        RegEmailText = (EditText) findViewById(R.id.edtCorreoR);
        RegPasswordText = (EditText) findViewById(R.id.edtContrasenaR);
        RegNameText = (EditText) findViewById(R.id.edtNombreR);
        RegistroBtn = (Button) findViewById(R.id.btnRegistroR);

        RegistroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarRegistro();
            }
        });
    }

    private void iniciarRegistro() {
        final String nombre = RegNameText.getText().toString();
        final String correo = RegEmailText.getText().toString();
        final String contrasena = RegPasswordText.getText().toString();

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(correo) && !TextUtils.isEmpty(contrasena)) {

            regProgress.setMessage("Registrando...");
            regProgress.show();

            firebaseAuth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String user_id = firebaseAuth.getCurrentUser().getUid();
                        DatabaseReference iDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
                        DatabaseReference currentUserDB = iDatabase.child(user_id);

                        currentUserDB.child("Correo").setValue(correo);
                        currentUserDB.child("NombreUsuario").setValue(nombre);
                        currentUserDB.child("password").setValue(contrasena);

                        regProgress.dismiss();

                        entrarLogin();
                    }
                }
            });
        } else {
            Toast.makeText(RegistroActivity.this, "¡ups falta rellenar cadros!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            entrarLogin();
        }
    }

    private void entrarLogin() {
        Intent loginIntent = new Intent(RegistroActivity.this, InisioSesionActivity.class);
        startActivity(loginIntent);
        finish();
    }

}
