package com.example.roberto.reveles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private EditText edititle, edipost;
    private Button bttpost;
    private ImageView ivpost;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog regProgress;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    Uri imageUri;

    private static  final int Image_request = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        // firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Postimages");
        storageReference  = FirebaseStorage.getInstance().getReference();
//elementos a usar para el post
        edititle = (EditText)findViewById(R.id.edititle);
        edipost = (EditText)findViewById(R.id.edipost);
        bttpost = (Button) findViewById(R.id.bttpost);
        ivpost = (ImageView) findViewById(R.id.ivpost);

//uso del manejo de la imagen
        ivpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentimg = new Intent();
                intentimg.setAction(Intent.ACTION_GET_CONTENT);
                intentimg.setType("image/*");
                startActivityForResult(intentimg,Image_request);

            }
        });
//metodo bton para ppublicar post
        bttpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sttartPost();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_request && resultCode == RESULT_OK) {
            imageUri = data.getData();
            ivpost.setImageURI(imageUri);
        }
    }

    private void sttartPost() {
        final String title,desc;

        title = edititle.getText().toString().trim();
        desc = edipost.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && imageUri != null) {
            //regProgress.setMessage("Registrando...");
            //regProgress.show();
            StorageReference filepath  = storageReference.child("Postimages").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
                    DatabaseReference new_Post = databaseReference.push();
                    String user_id = firebaseAuth.getCurrentUser().getUid();

                    new_Post.child(user_id).child("TituloPost").setValue(title);
                    new_Post.child(user_id).child("DescripcionPost").setValue(desc);
                    new_Post.child(user_id).child("ImagenPost").setValue(downloadUri);

                    Intent Intent= new Intent(PostActivity.this, MainActivity.class);
                    Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(Intent);
                    finish();



                }
            });
        }else
            Toast.makeText(PostActivity.this,"cehcar texto",Toast.LENGTH_SHORT).show();



    }
}
