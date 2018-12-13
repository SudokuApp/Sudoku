package c.b.a.sudokuapp.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import c.b.a.sudokuapp.R;



/**
 * A simple {@link Fragment} subclass.
 */
public class ButtonGroup extends Fragment {


    private Activity a;
    private Button[] buttons;
    private Button lastBtn;
    private String input = "";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;


    public ButtonGroup() { }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
                a.findViewById(R.id.btn8),a.findViewById(R.id.btn9),a.findViewById(R.id.btnMark), a.findViewById(R.id.clearBtn)};

        for(Button b : buttons){
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lastBtn!=null){
                        lastBtn.setBackgroundResource(R.drawable.button_default);
                        lastBtn.setTextColor(Color.parseColor("#000000"));
                    }
                    setInput(v.getTag().toString());

                    v.setBackgroundResource(R.drawable.button_focused);
                    setOperator();
                    lastBtn = v.findViewById(v.getId());
                    lastBtn.setTextColor(Color.parseColor("#ffffff"));

                }
            });
        }
    }
    protected void setInput (String in){
        input = in;
    }
    public String getInput(){
        return input;
    }
    //editor.putString(getString(R.string.input), input).commit();
    @SuppressLint("SetTextI18n")
    public void setOperator(){
        editor.putString(getString(R.string.input), getInput()).commit();
    }

}