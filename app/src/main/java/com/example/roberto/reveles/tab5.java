package com.example.roberto.reveles;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Roberto on 10/01/2018.
 */

public class tab5 extends Fragment {
    ImageButton salir;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myview=inflater.inflate (R.layout.tab5, container,false);
        return myview;
    }

}
