package com.example.roberto.reveles;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Roberto on 10/01/2018.
 */

public class tab5 extends Fragment {
    ImageButton salir;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View myview = inflater.inflate(R.layout.tab5, container, false);

        TextView rutas = (TextView) myview.findViewById(R.id.person_name92);

        rutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myview.getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });


        return myview;

    }


}
