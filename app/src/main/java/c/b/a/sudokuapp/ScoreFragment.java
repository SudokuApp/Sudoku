package c.b.a.sudokuapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static c.b.a.sudokuapp.MenuFragment.currUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {


    private TextView userEasy;
    private TextView userMedium;
    private TextView userHard;

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

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set variables
        setVariables();

        // Show high score
        getHighScore();

    }

    private void setVariables() {
        a = getActivity();
        userEasy = a.findViewById(R.id.user_highscore_easy);
        userMedium = a.findViewById(R.id.user_highscore_medium);
        userHard = a.findViewById(R.id.user_highscore_hard);
    }

    @SuppressLint("SetTextI18n")
    private void getHighScore() {

        if(currUser.getEasyHighScore() != Integer.MAX_VALUE) {
            userEasy.setText("Your high score for easy puzzles: " + DateUtils.formatElapsedTime(currUser.getEasyHighScore()));
        }
        if(currUser.getMediumHighScore() != Integer.MAX_VALUE) {
            userMedium.setText("Your high score for medium puzzles: " + DateUtils.formatElapsedTime(currUser.getMediumHighScore()));
        }
        if(currUser.getHardHighScore() != Integer.MAX_VALUE) {
            userHard.setText("Your high score for hard puzzles: " + DateUtils.formatElapsedTime(currUser.getHardHighScore()));
        }
    }

}
