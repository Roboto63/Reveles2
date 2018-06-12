package com.example.roberto.reveles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilPublicoActivity extends AppCompatActivity {

    private TextView txtNombre;

    public PerfilPublicoActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_publico);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //Obtener la informacion del usuario
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbperfil = FirebaseDatabase.getInstance().getReference().child("usuario");
        DatabaseReference currentUserDB = dbperfil.child(user_id);

        txtNombre = (TextView) findViewById(R.id.txtPerfilNom);

        currentUserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String poner_nombre = dataSnapshot.child("nombre").getValue().toString();

                txtNombre.setText(poner_nombre);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
