package c.b.a.sudokuapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static c.b.a.sudokuapp.LoginActivity.account;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {

    // Views
    private TextView userTxt;
    private TextView logout;
    private TextView userEasy;
    private TextView userMedium;
    private TextView userHard;
    private Button newGame;
    private Button resume;
    private Button leaderBoards;

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

    public static User currUser;
    public static List<ScorePair> easyScores;
    public static List<ScorePair> mediumScores;
    public static List<ScorePair> hardScores;


    private DatabaseReference ref;
    private DatabaseReference userRef;
    private DatabaseReference refMakeNewUser;
    private FirebaseDatabase mDatabase;



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


        userRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                  if(!dataSnapshot.exists()) {
                      String email = firebaseAuth.getCurrentUser().getProviderData().get(1).getEmail();
                      writeNewUser(email);
                  }

                  currUser = dataSnapshot.getValue(User.class);

                  if(currUser != null) {
                      if(currUser.getCurrentGame().equals("")) {
                          resume.setEnabled(false);
                      }
                      getHighScore();
                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) { }
          });

        // Set click listeners
        setClickListeners();

        // Show top high scores for each difficulty
        getLeaderboards();
    }

    @Override
    public void onStart() {
        super.onStart();

        // If user is not logged in, he/she is taken to the login screen
        if(firebaseAuth.getCurrentUser() == null && !isLoggedIn && account == null) {
            a.finish();
            a.startActivity(new Intent(a, LoginActivity.class));
        }

        welcomeUser();
    }

    @SuppressLint("SetTextI18n")
    private void welcomeUser() {
        String emailRegular = firebaseAuth.getCurrentUser().getEmail();
        String emailGoogleFB = firebaseAuth.getCurrentUser().getProviderData().get(1).getEmail();

        if(firebaseAuth.getCurrentUser() != null) {
            userTxt.setText(getString(R.string.welcome_user) + splitUserEmail(emailRegular));
        } else {
            userTxt.setText(getString(R.string.welcome_user) + splitUserEmail(emailGoogleFB));
        }
    }

    private String splitUserEmail(String email) {
        String[] emailArr = email.split("@");
        return emailArr[0];
    }

    /**
     * Set click listeners on buttons
     */
    private void setClickListeners() {
        logout.setOnClickListener(this);
        newGame.setOnClickListener(this);
        resume.setOnClickListener(this);
        leaderBoards.setOnClickListener(this);
    }

    /**
     * Set instance variables
     */
    private void setVariables() {
        a = getActivity();
        userTxt = a.findViewById(R.id.user);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        refMakeNewUser = FirebaseDatabase.getInstance().getReference();

        logout = a.findViewById(R.id.logout);
        newGame = a.findViewById(R.id.new_game_btn);
        resume = a.findViewById(R.id.resume_btn);
        leaderBoards = a.findViewById(R.id.leaderboards_btn);
        userEasy = a.findViewById(R.id.user_highscore_easy);
        userMedium = a.findViewById(R.id.user_highscore_medium);
        userHard = a.findViewById(R.id.user_highscore_hard);

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

        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference("users");
        userRef = ref.child(firebaseAuth.getUid());
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
     * Shows 5 highest scores for each difficulty
     * TODO bæta við global
     */
    private void goToLeaderBoards() {
        fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.main_frag, new ScoreFragment()).commit();
        fragmentManager.executePendingTransactions();
    }

    @SuppressLint("SetTextI18n")
    private void getHighScore() {

        if(currUser.getEasyHighScores() != Integer.MAX_VALUE) {
            userEasy.setText("Your high score for easy puzzles:\n" + DateUtils.formatElapsedTime(currUser.getEasyHighScores()));
        }
        if(currUser.getMediumHighScores() != Integer.MAX_VALUE) {
            userMedium.setText("Your high score for medium puzzles:\n" + DateUtils.formatElapsedTime(currUser.getMediumHighScores()));
        }
        if(currUser.getHardHighScores() != Integer.MAX_VALUE) {
            userHard.setText("Your high score for hard puzzles:\n" + DateUtils.formatElapsedTime(currUser.getHardHighScores()));
        }
    }

    private void getLeaderboards(){
        DatabaseReference scoreref = mDatabase.getReference("leaderBoards");
        DatabaseReference easyScoresRef = scoreref.child("easy");
        DatabaseReference mediumScoresRef = scoreref.child("medium");
        DatabaseReference hardScoresRef = scoreref.child("hard");

        easyScoresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                easyScores = new ArrayList<>();

                //read all the highest scores for this difficulty
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    easyScores.add(ds.getValue(ScorePair.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mediumScoresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mediumScores = new ArrayList<>();

                //read all the highest scores for this difficulty
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    mediumScores.add(ds.getValue(ScorePair.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        hardScoresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hardScores = new ArrayList<>();

                //read all the highest scores for this difficulty
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    hardScores.add(ds.getValue(ScorePair.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Called when the log out button is clicked.
     * Takes user back to the login screen
     */
    private void logout() {
        if(isLoggedIn) {
            LoginManager.getInstance().logOut();
            firebaseAuth.signOut();
        } else if(account != null) {
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
            Intent intent = new Intent(a, GameActivity.class);
            intent.putExtra("DIFF", currUser.getDiff());
            startActivity(intent);
        }
        else if(v == leaderBoards) {
            goToLeaderBoards();
        }

    }

    private void writeNewUser(String email) {

        User user = new User(email);

        refMakeNewUser.child("users").child(firebaseAuth.getUid()).setValue(user);
    }
}
