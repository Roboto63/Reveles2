package com.example.roberto.reveles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PrincipalActivity extends AppCompatActivity {
    /*RecyclerView rvImages;
    DatabaseReference databaseReferenceList;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        /*databaseReferenceList = FirebaseDatabase.getInstance().getReference().child("Postimages");

        rvImages = (RecyclerView)findViewById(R.id.rvImagesList);
        rvImages.setHasFixedSize(true);
        rvImages.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent sesionusua1 = new Intent(PrincipalActivity.this, PostActivity.class);
                startActivity(sesionusua1);
                finish();
            }
        });*/
    }
    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<uppostimages,uppostimagesHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<uppostimages, uppostimagesHolder>(
               uppostimages.class,
                R.layout.uppostimages,
                uppostimagesHolder.class,
                databaseReferenceList


        ) {
            @Override
            protected void populateViewHolder(uppostimagesHolder viewHolder, uppostimages model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());

            }
        };
        rvImages.setAdapter(firebaseRecyclerAdapter);
    }
    //Recyclerview
    public static class uppostimagesHolder extends RecyclerView.ViewHolder{

        View view;

        public uppostimagesHolder(View itemView) {
            super(itemView);

            view = itemView;
        }

        public void setTitle(String title){
            TextView image_tile = (TextView) view.findViewById(R.id.tvUserTitle);
            image_tile.setText(title);
        }
        public void setDesc(String Desc){
            TextView image_desc = (TextView) view.findViewById(R.id.tvUserDesc);
            image_desc.setText(Desc);
        }

        public void  setImage(Context context, String image){
            ImageView imageView=(ImageView) view.findViewById(R.id.UserImage);



        }
    }*/
}
