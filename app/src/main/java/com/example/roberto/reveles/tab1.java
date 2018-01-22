package com.example.roberto.reveles;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Roberto on 10/01/2018.
 */


public class tab1 extends Fragment {


    public tab1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tab1, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_viewUno);
        rv.setHasFixedSize(true);
        MyAdapterUno adapter = new MyAdapterUno(new String[]{"Example One", "Example Two", "Example Three", "Example Four", "Example Five", "Example Six", "Example Seven", "Example Seven", "Example Seven", "Example Seven", "Example Seven", "Example Seven", "Example Seven", "Example Seven", "Example Seven", "Example Seven", "Example Seven"});
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }


}
