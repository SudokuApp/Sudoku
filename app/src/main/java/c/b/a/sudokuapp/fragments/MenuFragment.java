package c.b.a.sudokuapp.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.text.format.DateUtils;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.util.Objects;

import c.b.a.sudokuapp.services.FireBaseHandler;
import c.b.a.sudokuapp.GameActivity;
import c.b.a.sudokuapp.LoginActivity;
import c.b.a.sudokuapp.R;
import c.b.a.sudokuapp.entities.User;

import static c.b.a.sudokuapp.services.Logic.splitUserEmail;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {

    // Views
    private TextView userTxt, logout, userEasyView, userMediumView, userHardView;;
    private Button newGame, resume, leaderBoards, instructions;

    // Authentication variables
    private FirebaseAuth firebaseAuth;


    // Current activity
    private Activity a;

    private FragmentManager fragmentManager;

    // Variables for Google sign-in / sign-out
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    public static FireBaseHandler fireBaseHandler;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set variables
        setVariables();

        // Retrieve data from the database
        retrieveData();

        // Set click listeners
        setClickListeners();
    }

    @Override
    public void onStart() {
        super.onStart();

        // If user is not logged in, he/she is taken to the login screen
        if(firebaseAuth.getCurrentUser() == null) {
            a.finish();
            a.startActivity(new Intent(a, LoginActivity.class));
        }

        // Welcome the user with his/her email
        welcomeUser();
    }


    public void retrieveData(){
        fireBaseHandler.userRef.addValueEventListener(new ValueEventListener() {
            @Override
            // Called with a snapshot of the data at this location. Called each time that data changes
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // If user does not exist in the database, one is created with user's email
                if(!dataSnapshot.exists()) {
                    // Get users email through the Firebase authentication
                    fireBaseHandler.writeNewUser(Objects.requireNonNull(firebaseAuth.getCurrentUser())
                            .getProviderData().get(1).getEmail());
                }

                //get the data for this user and save it locally
                fireBaseHandler.currUser = dataSnapshot.getValue(User.class);

                if(fireBaseHandler.currUser != null) {
                    if(fireBaseHandler.currUser.getCurrentGame().equals("")) {
                        resume.setEnabled(false);     // User is not able to press the resume button unless there is a game to resume
                    }
                    // Get the user's high score
                    printHighScore();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    //print the users highest score for each difficulty
    @SuppressLint("SetTextI18n")
    private void printHighScore() {

        if(fireBaseHandler.currUser.getEasyHighScores() != Integer.MAX_VALUE) {
            userEasyView.setText("Your high score for easy puzzles:\n" + DateUtils.formatElapsedTime(fireBaseHandler.currUser.getEasyHighScores()));
        }
        if(fireBaseHandler.currUser.getMediumHighScores() != Integer.MAX_VALUE) {
            userMediumView.setText("Your high score for medium puzzles:\n" + DateUtils.formatElapsedTime(fireBaseHandler.currUser.getMediumHighScores()));
        }
        if(fireBaseHandler.currUser.getHardHighScores() != Integer.MAX_VALUE) {
            userHardView.setText("Your high score for hard puzzles:\n" + DateUtils.formatElapsedTime(fireBaseHandler.currUser.getHardHighScores()));
        }
    }

    /**
     * Gets the user's email
     * Calls a method to split the email
     * Welcomes the user with his/her splitted email
     */
    @SuppressLint("SetTextI18n")
    private void welcomeUser() {
        String emailRegular = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        String emailGoogleFB = firebaseAuth.getCurrentUser().getProviderData().get(1).getEmail();

        if(firebaseAuth.getCurrentUser() != null) {
            assert emailRegular != null;
            userTxt.setText(getString(R.string.welcome_user) + splitUserEmail(emailRegular));
        } else {
            assert emailGoogleFB != null;
            userTxt.setText(getString(R.string.welcome_user) + splitUserEmail(emailGoogleFB));
        }
    }


    /**
     * Set click listeners on buttons
     */
    private void setClickListeners() {
        logout.setOnClickListener(this);
        newGame.setOnClickListener(this);
        resume.setOnClickListener(this);
        leaderBoards.setOnClickListener(this);
        instructions.setOnClickListener(this);
    }

    /**
     * Set instance variables
     */
    private void setVariables() {
        // Current activity
        a = getActivity();
        userTxt = a.findViewById(R.id.user);

        // Instance of the Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Buttons
        logout = a.findViewById(R.id.logout);
        newGame = a.findViewById(R.id.new_game_btn);
        resume = a.findViewById(R.id.resume_btn);
        instructions = a.findViewById(R.id.instructions);
        leaderBoards = a.findViewById(R.id.leaderboards_btn);

        // Views for user's high score
        userEasyView = a.findViewById(R.id.user_highscore_easy);
        userMediumView = a.findViewById(R.id.user_highscore_medium);
        userHardView = a.findViewById(R.id.user_highscore_hard);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(a, gso);

        //the almighty FireBaseHandler, hallowed be thy name.
        fireBaseHandler = new FireBaseHandler(firebaseAuth.getUid());
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
     * Called when the "High score" button is clicked
     * Shows 5 highest scores of all users for each difficulty
     */

    private void goToLeaderBoards() {
        fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.main_frag, new ScoreFragment()).commit();
        fragmentManager.executePendingTransactions();
    }


    /**
     * Called when the How to play button is clicked
     * Takes user to the instruction page
     */
    private void goToInstuctions() {
        fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.main_frag, new InstructionFragment()).commit();
        fragmentManager.executePendingTransactions();
    }

    /**
     * Called when the log out button is clicked.
     * Takes user back to the login screen
     */
    private void logout() {
        mGoogleSignInClient.signOut();
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();

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
        // Changes the screen so that the user can choose difficulty for the game
        else if(v == newGame) {
            newGame();
        }
        // Resume an existing game
        else if(v == resume) {
            Intent intent = new Intent(a, GameActivity.class);
            intent.putExtra("DIFF", fireBaseHandler.currUser.getDiff());
            startActivity(intent);
        }
        // See top 5 leaders in each difficulty
        else if(v == leaderBoards) {
            goToLeaderBoards();
        }
        // Learn how to play the game
        else if(v == instructions) {
            goToInstuctions();
        }

    }
}
