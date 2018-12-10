package c.b.a.sudokuapp.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import c.b.a.sudokuapp.R;

import static android.widget.Toast.makeText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ButtonGroup extends Fragment {


    private Activity a;
    private Button[] buttons;
    private String input = "";
    private TextView status;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;


    public ButtonGroup() { }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_button_group, container, false);
        return inflater.inflate(R.layout.fragment_button_group, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        a = getActivity();
        sharedPref = a.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        linkButtons();
    }

    private void linkButtons(){
        buttons = new Button[]{a.findViewById(R.id.btn1), a.findViewById(R.id.btn2),a.findViewById(R.id.btn3),
                a.findViewById(R.id.btn4),a.findViewById(R.id.btn5),a.findViewById(R.id.btn6),a.findViewById(R.id.btn7),
                a.findViewById(R.id.btn8),a.findViewById(R.id.btn9),a.findViewById(R.id.btnMark)};

        for(Button b : buttons){
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    input = v.getTag().toString();
                    setOperator(input);

                }
            });
        }
        status = a.findViewById(R.id.opField);
    }


    @SuppressLint("SetTextI18n")
    public void setOperator(String in){
        status.setText("Operation: "+in);
        editor.putString(getString(R.string.input), input).commit();
    }

}