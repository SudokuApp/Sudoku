package c.b.a.sudokuapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static c.b.a.sudokuapp.MenuFragment.currUser;
import static c.b.a.sudokuapp.MenuFragment.easyScores;
import static c.b.a.sudokuapp.MenuFragment.mediumScores;
import static c.b.a.sudokuapp.MenuFragment.hardScores;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {


    private LinearLayout easyListView;
    private LinearLayout mediumListView;
    private LinearLayout hardListView;
    private FirebaseAuth firebaseAuth;
    private TextView userTxt;

    private Activity a;

    public ScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();

        userTxt.setText(getString(R.string.welcome_user) + splitUserEmail(currUser.getEmail()));
    }

    private String splitUserEmail(String email) {
        String[] emailArr = email.split("@");
        return emailArr[0];
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setVariables();

        // Inflate into lists
        inflateIntoLists(easyListView, easyScores);
        inflateIntoLists(mediumListView, mediumScores);
        inflateIntoLists(hardListView, hardScores);

    }

    private void setVariables() {
        a = getActivity();
        userTxt = a.findViewById(R.id.score_user);
        easyListView = a.findViewById(R.id.global_easy_list);
        mediumListView = a.findViewById(R.id.global_medium_list);
        hardListView = a.findViewById(R.id.global_hard_list);
        firebaseAuth = FirebaseAuth.getInstance();
    }


    private void inflateIntoLists(LinearLayout list, List<ScorePair> scores){

        Collections.sort(scores, ScorePair.ScoreComparator);

        for(ScorePair s : scores){
            View view = View.inflate(a, R.layout.layout_scorepair, null);
            TextView name = view.findViewById(R.id.scorepair_name);
            TextView time = view.findViewById(R.id.scorepair_time);
            name.setText(s.getName());
            time.setText(DateUtils.formatElapsedTime(s.getScore()));
            list.addView(view);
        }
    }
}
