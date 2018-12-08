package c.b.a.sudokuapp.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import c.b.a.sudokuapp.R;

import static android.widget.Toast.makeText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ButtonGroup extends Fragment {

    private int gridID;
    private OnFragmentInteractionListener listener;

    public ButtonGroup() { }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    public int [] getGrid(){
        return new int[]{R.id.t0, R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7, R.id.t8};
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_button_group, container, false);
        View view = inflater.inflate(R.layout.fragment_button_group, container, false);
        int gridBoxes[] = getGrid();
        for(int id : gridBoxes){
            TextView textView = view.findViewById(id);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onFragmentInteraction(gridID, Integer.parseInt(view.getTag().toString()), view);
                }
            });
        }
        return view;
    }

    public void setGridID(int gridID){

        this.gridID = gridID;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }
        else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int row, int cell, View view);
    }

}