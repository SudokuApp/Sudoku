package c.b.a.sudokuapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class DifficultyFragment extends Fragment implements View.OnClickListener {

    // Views
    private TextView userTxt;
    private TextView logout;
    private Button easy;
    private Button medium;
    private Button hard;

    // Authentication variables
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    // Current activity
    private Activity a;

    // Variables for Facebook sign-in / sign-out
    private boolean isLoggedIn;
    private AccessToken accessToken;

    // Variables for Google sign-in / sign-out
    private boolean isSignedIn;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount account;
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

        // If user is not logged in, he/she is taken to the login screen
        if(firebaseAuth.getCurrentUser() == null && !isLoggedIn) {
            a.startActivity(new Intent(a, LoginActivity.class));
            a.finish();
        }

        // Set click listeners
        setClickListeners();

        // Say welcome to the user
        if(isLoggedIn) {
            userTxt.setText(getString(R.string.welcome_user)); // TODO þarf að finna hvernig email eða nafn frá facebook
        }
        else {
           // userTxt.setText(getString(R.string.welcome_user) + firebaseUser.getEmail());
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
        a = getActivity();
        userTxt = a.findViewById(R.id.diff_user);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        logout = a.findViewById(R.id.diff_logout);
        easy = a.findViewById(R.id.easy);
        medium = a.findViewById(R.id.medium);
        hard = a.findViewById(R.id.hard);

        accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();

        account = GoogleSignIn.getLastSignedInAccount(a);
        // Check if user is logged in via Google
        isSignedIn = account != null && !account.isExpired();
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(a, gso);
    }


    // TODO, commenta betur!
    /**
     * Called when any of the difficulty buttons has been clicked.
     *
     * @param diff
     */

    /*
    private void startGame(String diff){
        a.finish();
        Intent intent = new Intent(a, GameActivity.class);
        intent.putExtra("DIFF", diff);
        startActivity(intent);
    }*/

    /**
     * Called when the log out button is clicked.
     * Takes user back to the login screen
     */
    private void logout() {
        if(isLoggedIn) {
            LoginManager.getInstance().logOut();
            firebaseAuth.signOut();
        } else if(isSignedIn) {
            firebaseAuth.signOut();
            //mGoogleSignInClient.signOut();
        } else if(firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
        }
        a.finish();
        a.startActivity(new Intent(a, LoginActivity.class));
    }

    /**
     * Called when a view has been clicked
     * @param v
     */
    @Override
    public void onClick(View v) {

        if(v == logout) {
            logout();
        }
        /*
        if(v == easy) {
            startGame("easy");
        }
        if(v == medium) {
            startGame("medium");
        }
        if(v == hard) {
            startGame("hard");
        }
        */
    }
}