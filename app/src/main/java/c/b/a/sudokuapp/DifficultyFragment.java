package c.b.a.sudokuapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import static c.b.a.sudokuapp.Logic.splitUserEmail;
import static c.b.a.sudokuapp.MenuFragment.fireBaseHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class DifficultyFragment extends Fragment implements View.OnClickListener {

    // Views
    private TextView userTxt, logout;
    private Button easy, medium, hard;

    // Authentication variables
    private FirebaseAuth firebaseAuth;

    // Current activity
    private Activity a;

    // Variables for Google sign-in / sign-out
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    public DifficultyFragment() {
        // Required empty public constructor
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_difficulty, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set variables
        setVariables();

        // Set click listeners
        setClickListeners();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();

        // If some user is logged in, welcome the user with his/her email
        if(fireBaseHandler.currUser != null) {
            userTxt.setText(getString(R.string.welcome_user) + splitUserEmail(fireBaseHandler.currUser.getEmail()));
        }
    }

    /**
     * Set click listeners on buttons
     */
    private void setClickListeners() {
        logout.setOnClickListener(this);
        easy.setOnClickListener(this);
        medium.setOnClickListener(this);
        hard.setOnClickListener(this);
    }

    /**
     * Set instance variables
     */
    private void setVariables() {
        // Current activity
        a = getActivity();
        userTxt = a.findViewById(R.id.diff_user);

        // Instance of the Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Buttons
        logout = a.findViewById(R.id.diff_logout);
        easy = a.findViewById(R.id.easy);
        medium = a.findViewById(R.id.medium);
        hard = a.findViewById(R.id.hard);

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
     * Called when any of the difficulty buttons has been clicked.
     * Starts the game itself
     * @param diff
     */
    private void startGame(String diff){

        fireBaseHandler.resetUserGame(getString(R.string.initalizeUserSolution), diff);

        a.finish();
        Intent intent = new Intent(a, GameActivity.class);
        intent.putExtra("DIFF", diff);
        startActivity(intent);
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

    /**
     * Called when a view has been clicked
     * @param v the button view
     */
    @Override
    public void onClick(View v) {

        // Log current user out
        if(v == logout) {
            logout();
        }
        // Start an easy game
        if(v == easy) {
            startGame("easy");
        }
        // Starts a medium game
        if(v == medium) {
            startGame("medium");
        }
        // Starts a hard game
        if(v == hard) {
            startGame("hard");
        }
    }
}