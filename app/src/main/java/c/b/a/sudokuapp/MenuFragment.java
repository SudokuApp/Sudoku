package c.b.a.sudokuapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Activity a;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean isLoggedIn;
    AccessToken accessToken;

    // Views
    private TextView userTxt;
    private TextView logout;
    private Button newGame;
    private Button resume;

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

        // If user is not logged in, he/she is taken to the login screen
        if(firebaseAuth.getCurrentUser() == null && !isLoggedIn) {
            a.finish();
            a.startActivity(new Intent(a, LoginActivity.class));
        }

        // Set click listeners
        setClickListeners();

        // Say welcome to the user
        if(isLoggedIn) {
            userTxt.setText(getString(R.string.welcome_user)); // TODO þarf að finna hvernig email eða nafn frá facebook
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
        isLoggedIn = accessToken != null && !accessToken.isExpired();
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
        if(firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
        } else if(isLoggedIn) {
            LoginManager.getInstance().logOut();
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
            //startActivity(new Intent(a, GameActivity.class));
        }

    }
}
