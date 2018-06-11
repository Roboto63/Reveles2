package com.example.roberto.reveles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainTwo extends AppCompatActivity {
    RecyclerView rvImages;
    DatabaseReference databaseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        databaseList = FirebaseDatabase.getInstance().getReference().child("Postimages");

        rvImages = (RecyclerView)findViewById(R.id.rvImagesList);
        rvImages.setHasFixedSize(true);
        rvImages.setLayoutManager(new LinearLayoutManager(this));



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent sesionusua1 = new Intent(MainTwo.this, PostActivity.class);
                startActivity(sesionusua1);
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<uppostimages,MainTwo.uppostimagesviewHolder>firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<uppostimages, MainTwo.uppostimagesviewHolder>(
                uppostimages.class, R.layout.uppostimages , MainTwo.uppostimagesviewHolder.class , databaseList
        ) {
            @Override
            protected void populateViewHolder(MainTwo.uppostimagesviewHolder viewHolder, uppostimages model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());

            }
        };
        rvImages.setAdapter(firebaseRecyclerAdapter);
    }

    public void salir(View view) {
        Intent sesionusua1 = new Intent(MainTwo.this, PostActivity.class);
        startActivity(sesionusua1);
        finish();
    }

    //Recyclerview
    public static class uppostimagesviewHolder extends RecyclerView.ViewHolder{

        View view;

        public uppostimagesviewHolder(View itemView) {
            super(itemView);

            view = itemView;
        }

        public void setTitle(String title){
            TextView image_title = (TextView) view.findViewById(R.id.tvUserTitle);
            image_title.setText(title);
        }
        public void setDesc(String desc){
            TextView image_desc = (TextView) view.findViewById(R.id.tvUserDesc);
            image_desc.setText(desc);
        }

        public void  setImage(Context context, String image){
            ImageView imageView=(ImageView) view.findViewById(R.id.UserImage);
            Picasso.with(context).load(image).into(imageView);




        }
    }

}
