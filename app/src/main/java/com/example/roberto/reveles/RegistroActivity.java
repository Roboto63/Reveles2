package com.example.roberto.reveles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class RegistroActivity extends AppCompatActivity {
    private EditText RegEmailText;
    private EditText RegPasswordText;
    private EditText RegNameText;
    private Button RegistroBtn;
    private ImageButton ibRegister;
    private final int Image_request = 1;


    private FirebaseAuth firebaseAuth;
    private ProgressDialog regProgress;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        regProgress = new ProgressDialog(this);
        RegEmailText = (EditText) findViewById(R.id.edtCorreoR);
        RegPasswordText = (EditText) findViewById(R.id.edtContrasenaR);
        RegNameText = (EditText) findViewById(R.id.edtNombreR);
        RegistroBtn = (Button) findViewById(R.id.btnRegistroR);
        ibRegister = (ImageButton) findViewById(R.id.iregister);
//Metodo guardar foto de perfil en firabse storage
        storageReference = FirebaseStorage.getInstance().getReference().child("ImagenPerfil");
// Metodo registro en firebase
        RegistroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarRegistro();
            }
        });
        //uso de seleccion de imagen de perfil
        ibRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentimg = new Intent();
                intentimg.setAction(Intent.ACTION_GET_CONTENT);
                intentimg.setType("image/*");
                startActivityForResult(intentimg, Image_request);

            }
        });

    }

    //mtoeodo para el registro en la base de datos del registro de usuarios
    private void iniciarRegistro() {
        final String nombre = RegNameText.getText().toString().trim();
        final String correo = RegEmailText.getText().toString().trim();
        final String contrasena = RegPasswordText.getText().toString().trim();

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(correo) && !TextUtils.isEmpty(contrasena)) {
            //regProgress.setMessage("Registrando...");
            //regProgress.show();
            StorageReference filepath = storageReference.child(resultUri.getLastPathSegment());
            filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    firebaseAuth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String downloadUri = taskSnapshot.getDownloadUrl().toString();
                                String user_id = firebaseAuth.getCurrentUser().getUid();
                                //DatabaseReference iDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
                                DatabaseReference currentUserDB = databaseReference.child(user_id);
                                currentUserDB.child("Correo").setValue(correo);
                                currentUserDB.child("NombreUsuario").setValue(nombre);
                                currentUserDB.child("password").setValue(contrasena);
                                currentUserDB.child("ImagenP").setValue(downloadUri);
                                //regProgress.dismiss();
                                entrarLogin();

                            }
                        }
                    });
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

    //metodo para modificar la imagen de perfil
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
                ibRegister.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
