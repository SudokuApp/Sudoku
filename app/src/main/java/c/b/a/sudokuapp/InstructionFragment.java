package c.b.a.sudokuapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static c.b.a.sudokuapp.MenuFragment.currUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstructionFragment extends Fragment {

    public InstructionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instructions, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();

        Activity a = getActivity();
        TextView userTxt = a.findViewById(R.id.diff_user);
        userTxt.setText(getString(R.string.welcome_user) + splitUserEmail(currUser.getEmail()));
    }

    private String splitUserEmail(String email) {
        String[] emailArr = email.split("@");
        return emailArr[0];
    }


}
