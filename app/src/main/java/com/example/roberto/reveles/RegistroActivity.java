package com.example.roberto.reveles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.roberto.reveles.Model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class RegistroActivity extends AppCompatActivity {
    private EditText edtCorreoR;
    private EditText edtContrasenaR, edtConfContrasenaR;
    private EditText edtNombreR;
    private Button btnRegistroR;
    private ImageButton ibtnImagenR;
    private com.rey.material.widget.CheckBox cbMostrar, cbMostrar2;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tabla_usuario;

    private final int Image_request = 1;
    private StorageReference storageReference;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edtCorreoR = (EditText) findViewById(R.id.edtCorreoR);
        edtContrasenaR = (EditText) findViewById(R.id.edtContrasenaR);
        edtConfContrasenaR = (EditText) findViewById(R.id.edtConfContrasenaR);
        edtNombreR = (EditText) findViewById(R.id.edtNombreR);
        btnRegistroR = (Button) findViewById(R.id.btnRegistroR);
        ibtnImagenR = (ImageButton) findViewById(R.id.ibtnImagenR);
        cbMostrar = (com.rey.material.widget.CheckBox) findViewById(R.id.cbMostrar);
        cbMostrar2 = (com.rey.material.widget.CheckBox) findViewById(R.id.cbMostrar2);

        //Inicializar Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        tabla_usuario = firebaseDatabase.getReference("usuario");
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        storageReference = FirebaseStorage.getInstance().getReference().child("ImagenPerfil");

        btnRegistroR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarRegistro();
            }
        });

        cbMostrar2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!cbMostrar2.isChecked()) {
                    //mostrar password
                    edtConfContrasenaR.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    //ocultar password
                    edtConfContrasenaR.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        cbMostrar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!cbMostrar.isChecked()) {
                    //mostrar password
                    edtContrasenaR.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    //ocultar password
                    edtContrasenaR.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        ibtnImagenR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentimg = new Intent();
                intentimg.setAction(Intent.ACTION_GET_CONTENT);
                intentimg.setType("image/*");
                startActivityForResult(intentimg, Image_request);

            }
        });
    }

    private void iniciarRegistro() {
        final String nombre = edtNombreR.getText().toString();
        final String correo = edtCorreoR.getText().toString();
        final String contrasena = edtContrasenaR.getText().toString();
        final String confcontrasena = edtConfContrasenaR.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(RegistroActivity.this);
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.show();

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(correo) && !TextUtils.isEmpty(contrasena)) {

            if (contrasena.equals(confcontrasena)) {

                if (resultUri != null) {

                    StorageReference filepath = storageReference.child(resultUri.getLastPathSegment());

                    filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            firebaseAuth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        tabla_usuario.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                //Registrar usuario
                                                String downloadUri = taskSnapshot.getDownloadUrl().toString();
                                                String user_id = firebaseAuth.getCurrentUser().getUid();
                                                DatabaseReference currentUserDB = tabla_usuario.child(user_id);

                                                Usuario usuario = new Usuario(
                                                        edtNombreR.getText().toString(),
                                                        edtCorreoR.getText().toString(),
                                                        edtContrasenaR.getText().toString(),
                                                        downloadUri
                                                );
                                                currentUserDB.setValue(usuario);
                                                entrarLogin();
                                                progressDialog.dismiss();
                                                Toast.makeText(RegistroActivity.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    } else {//task
                                        progressDialog.dismiss();
                                        Toast.makeText(RegistroActivity.this, "Usuario ya existe", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegistroActivity.this, "Selecciona una imagen", Toast.LENGTH_SHORT).show();
                }

            } else { //equals
                progressDialog.dismiss();
                Toast.makeText(RegistroActivity.this, "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
            }
        } else {
            progressDialog.dismiss();
            Toast.makeText(RegistroActivity.this, "¡Ups falta rellenar campos!", Toast.LENGTH_SHORT).show();
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
        Intent loginIntent = new Intent(RegistroActivity.this, MainActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_request && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                ibtnImagenR.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
