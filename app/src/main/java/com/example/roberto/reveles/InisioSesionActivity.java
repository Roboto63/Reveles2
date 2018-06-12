package com.example.roberto.reveles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roberto.reveles.Common.Common;
import com.example.roberto.reveles.Model.Usuario;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class InisioSesionActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int SIGN_IN_GOOGLE_CODE = 1;
    private EditText LoginEmailText;
    private EditText LoginPasswordText;
    private Button LoginBtn;
    private Button LoginRegBtn;
    private com.rey.material.widget.CheckBox cbMostrar3;
    private SignInButton btnSignInGoogle;
    private GoogleApiClient googleApiClient;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tabla_usuario;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inisiosesion);

        LoginEmailText = (EditText) findViewById(R.id.edtEmail);
        LoginPasswordText = (EditText) findViewById(R.id.edtPassword);
        LoginBtn = (Button) findViewById(R.id.btnLogin);
        LoginRegBtn = (Button) findViewById(R.id.btnRegistro);
        cbMostrar3 = (CheckBox) findViewById(R.id.cbMostrar3);
        btnSignInGoogle = (SignInButton) findViewById(R.id.googleSignin);
        btnSignInGoogle.setSize(SignInButton.SIZE_STANDARD);

        //Inicializar Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        tabla_usuario = firebaseDatabase.getReference("usuario");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        cbMostrar3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!cbMostrar3.isChecked()) {
                    //mostrar password
                    LoginPasswordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    //ocultar password
                    LoginPasswordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

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

        String email = LoginEmailText.getText().toString();
        String password = LoginPasswordText.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(InisioSesionActivity.this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        tabla_usuario.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String user_id = firebaseAuth.getCurrentUser().getUid();

                                //guardar email y contraseña
                                //Paper.book().write(Common.EMAIL_KEY, user_id);
                                //Paper.book().write(Common.PASS_KEY, user_id);

                                //Verificar si el usuario existe
                                if (dataSnapshot.child(user_id).exists()) {
                                    //Obtener la informacion del usuario
                                    Usuario usuario = dataSnapshot.child(user_id).getValue(Usuario.class);

                                    Common.currentUser = usuario;
                                    Intent intent = new Intent(InisioSesionActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                    finish();
                                    Toast.makeText(InisioSesionActivity.this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(InisioSesionActivity.this, "¡Ups datos incorrectos!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(InisioSesionActivity.this, "¡ups falta rellenar cadros!", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInGoogleFirebase(GoogleSignInResult googleSignInResult) {
        final ProgressDialog progressDialog = new ProgressDialog(InisioSesionActivity.this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();

        if (googleSignInResult.isSuccess()) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInResult.getSignInAccount().getIdToken(), null);
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        tabla_usuario.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String user_id = firebaseAuth.getCurrentUser().getUid();

                                //guardar email y contraseña
                                Paper.book().write(Common.EMAIL_KEY, user_id);
                                Paper.book().write(Common.PASS_KEY, user_id);

                                //Verificar si el usuario existe
                                if (dataSnapshot.child(user_id).exists()) {
                                    //Obtener la informacion del usuario
                                    Usuario usuario = dataSnapshot.child(user_id).getValue(Usuario.class);

                                    Common.currentUser = usuario;
                                    Intent intent = new Intent(InisioSesionActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                    finish();
                                    Toast.makeText(InisioSesionActivity.this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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
        Intent entrar = new Intent(InisioSesionActivity.this, MainActivity.class);
        startActivity(entrar);
        finish();
    }

    public void entrarRegistro() {
        Intent intent2 = new Intent(InisioSesionActivity.this, RegistroActivity.class);
        startActivity(intent2);
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


