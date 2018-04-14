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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InisioSesionActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int SIGN_IN_GOOGLE_CODE = 1;
    private EditText LoginEmailText;
    private EditText LoginPasswordText;
    private Button LoginBtn;

    private FirebaseAuth firebaseAuth;
    private Button LoginRegBtn;
    private ProgressDialog loginProgress;
    private DatabaseReference databaseReference;
    private SignInButton btnSignInGoogle;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inisiosesion);

        LoginEmailText = (EditText) findViewById(R.id.edtEmail);
        LoginPasswordText = (EditText) findViewById(R.id.edtPassword);
        LoginBtn = (Button) findViewById(R.id.btnLogin);
        LoginRegBtn = (Button) findViewById(R.id.btnRegistro);
        btnSignInGoogle = (SignInButton) findViewById(R.id.googleSignin);

        firebaseAuth = FirebaseAuth.getInstance();

        loginProgress = new ProgressDialog(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        LoginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entrarRegistro();
            }
        });

        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(i, SIGN_IN_GOOGLE_CODE);

            }
        });
    }

    private void Login() {

        String email = LoginEmailText.getText().toString().trim();
        String password = LoginPasswordText.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            loginProgress.setMessage("Iniciando sesión...");


            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        loginProgress.show();
                        entrarMain();
                        loginProgress.dismiss();
                    } else {
                        Toast.makeText(InisioSesionActivity.this, "¡ups datos incorrectos!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(InisioSesionActivity.this, "¡ups falta rellenar cadros!", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInGoogleFirebase(GoogleSignInResult googleSignInResult) {
        if (googleSignInResult.isSuccess()) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInResult.getSignInAccount().getIdToken(), null);
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(InisioSesionActivity.this, "Google Authentication Success", Toast.LENGTH_LONG).show();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        String name = user.getDisplayName();
                        String email = user.getEmail();
                        String user_id = firebaseAuth.getCurrentUser().getUid();
                        DatabaseReference iDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
                        DatabaseReference currentUserDB = iDatabase.child(user_id);
                        currentUserDB.child("Correo").setValue(email);
                        currentUserDB.child("NombreUsuario").setValue(name);

                        entrarMain();
                    } else {
                        Toast.makeText(InisioSesionActivity.this, "Google Authentication  Unsuccess", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(InisioSesionActivity.this, "Google Sign In Authentication  Unsuccess", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            entrarMain();
        }
    }

    public void entrarMain() {
        Intent entrar = new Intent(InisioSesionActivity.this, PrincipalActivity.class);
        startActivity(entrar);
        finish();
    }

    public void entrarRegistro() {
        Intent intent2 = new Intent(InisioSesionActivity.this, RegistroActivity.class);
        startActivity(intent2);
        finish();
    }

    public void mapa(View view) {
        Intent intent6 = new Intent(InisioSesionActivity.this, MapsActivity.class);
        startActivity(intent6);
    }

    public void recuperar(View view) {
        Intent intent7 = new Intent(InisioSesionActivity.this, RecuperarContraActivity.class);
        startActivity(intent7);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_GOOGLE_CODE) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            signInGoogleFirebase(googleSignInResult);
        } else {
            //callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}


