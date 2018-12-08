package c.b.a.sudokuapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static c.b.a.sudokuapp.LoginActivity.acco;
import static c.b.a.sudokuapp.LoginActivity.account;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {

    // Views
    private TextView userTxt;
    private TextView logout;
    private Button newGame;
    private Button resume;

    // Authentication variables
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    // Current activity
    private Activity a;

    private FragmentManager fragmentManager;

    // Variables for Facebook sign-in / sign-out
    private boolean isLoggedIn;
    private AccessToken accessToken;

    // Variables for Google sign-in / sign-out
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    public MenuFragment() {
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

        return inflater.inflate(R.layout.fragment_menu, container, false);
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

    @Override
    public void onStart() {
        super.onStart();

        // If user is not logged in, he/she is taken to the login screen
        if(firebaseAuth.getCurrentUser() == null && !isLoggedIn && acco == null) {
            a.finish();
            a.startActivity(new Intent(a, LoginActivity.class));
        }

        // Say welcome to the user, using their email // TODO breyta ef við höfum eð annað en email
        if(isLoggedIn) {
            userTxt.setText(getString(R.string.welcome_user)); // TODO þarf að finna hvernig email eða nafn frá facebook
        }
        else if(acco != null) {
            userTxt.setText(getString(R.string.welcome_user) + firebaseUser.getEmail());
        }
        else {
            userTxt.setText(getString(R.string.welcome_user) + firebaseUser.getEmail());
        }
    }

    /**
     * Set click listeners on buttons
     */
    private void setClickListeners() {
        logout.setOnClickListener(this);
        newGame.setOnClickListener(this);
        resume.setOnClickListener(this);
    }

    /**
     * Set instance variables
     */
    private void setVariables() {
        a = getActivity();
        userTxt = a.findViewById(R.id.user);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        logout = a.findViewById(R.id.logout);
        newGame = a.findViewById(R.id.new_game_btn);
        resume = a.findViewById(R.id.resume_btn);

        accessToken = AccessToken.getCurrentAccessToken();
        // Check if user is logged in via facebook
        isLoggedIn = accessToken != null && !accessToken.isExpired();

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
     * Called when the "New game" button is clicked
     * Fragment is replaced with the Difficulty fragment, which lets the user
     * choose difficulty level of the game
     */
    private void newGame() {
        fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.main_frag, new DifficultyFragment()).commit();
        fragmentManager.executePendingTransactions();
    }

    /**
     * Called when the log out button is clicked.
     * Takes user back to the login screen
     */
    private void logout() {
        if(isLoggedIn) {
            LoginManager.getInstance().logOut();
            firebaseAuth.signOut();
        } else if(acco != null) {
            firebaseAuth.signOut();
            mGoogleSignInClient.signOut();
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
        else if(v == newGame) {
            newGame();
        }
        else if(v == resume) {
            startActivity(new Intent(a, GameActivity.class));
        }

    }
}
