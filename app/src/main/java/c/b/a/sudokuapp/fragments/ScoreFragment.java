package c.b.a.sudokuapp.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;

import c.b.a.sudokuapp.LoginActivity;
import c.b.a.sudokuapp.services.Logic;
import c.b.a.sudokuapp.R;
import c.b.a.sudokuapp.entities.ScorePair;

import static c.b.a.sudokuapp.fragments.MenuFragment.fireBaseHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment implements View.OnClickListener {

    // Layouts for top 5 leaders in each difficulty
    private LinearLayout easyListView, mediumListView, hardListView;
    private TextView userTxt, logout;

    private Activity a;

    // Authentication variables
    private FirebaseAuth firebaseAuth;

    // Variables for Google sign-out
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

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

        setVariables();
        logout.setOnClickListener(this);

        // Inflate into lists
        inflateIntoLists(easyListView, fireBaseHandler.easyScores);
        inflateIntoLists(mediumListView, fireBaseHandler.mediumScores);
        inflateIntoLists(hardListView, fireBaseHandler.hardScores);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();

        // If some user is logged in, welcome the user with his/her email
        if(fireBaseHandler.currUser != null) {
            userTxt.setText(getString(R.string.welcome_user) + Logic.splitUserEmail(fireBaseHandler.currUser.getEmail()));
        }
    }

    /**
     * Set instance variables
     */
    private void setVariables() {
        a = getActivity();
        userTxt = a.findViewById(R.id.score_user);
        logout = a.findViewById(R.id.logout_score);
        easyListView = a.findViewById(R.id.global_easy_list);
        mediumListView = a.findViewById(R.id.global_medium_list);
        hardListView = a.findViewById(R.id.global_hard_list);

        // Instance of the Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(a, gso);
    }

    /**
     * Show 5 highest scores in each difficulty, as well as the name of the user
     * who owns the score
     * @param list
     * @param scores
     */
    private void inflateIntoLists(LinearLayout list, List<ScorePair> scores){

        //sort the lists by score ascending.
        Collections.sort(scores, ScorePair.ScoreComparator);

        //inflate each scorepair into the list
        for(ScorePair s : scores){
            View view = View.inflate(a, R.layout.layout_scorepair, null);
            TextView name = view.findViewById(R.id.scorepair_name);
            TextView time = view.findViewById(R.id.scorepair_time);
            name.setText(s.getName());
            time.setText(DateUtils.formatElapsedTime(s.getScore()));
            list.addView(view);
        }
    }

    /**
     * Called when the log out button is clicked.
     * Takes user back to the login screen
     */
    private void logout() {
        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut();
        firebaseAuth.signOut();

        a.finish();
        a.startActivity(new Intent(a, LoginActivity.class));
    }

    @Override
    public void onClick(View v) {

        if(v == logout) {
            logout();
        }
    }
}
