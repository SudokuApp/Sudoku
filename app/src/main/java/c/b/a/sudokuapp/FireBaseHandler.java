package c.b.a.sudokuapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static c.b.a.sudokuapp.MenuFragment.fireBaseHandler;

public class FireBaseHandler {

    public FirebaseDatabase FBdatabase;
    public DatabaseReference leaderBoardsRef, easyLeaderBoards, mediumLeaderBoards, hardLeaderBoards, userRef;
    private TextView  userEasyView, userMediumView, userHardView;
    public static List<ScorePair> easyScores;
    public static List<ScorePair> mediumScores;
    public static List<ScorePair> hardScores;
    public User currUser;

    public FireBaseHandler(Activity a, String userId){
        FBdatabase = FirebaseDatabase.getInstance();
        DatabaseReference allUsersRef = FBdatabase.getReference("users");
        userRef = allUsersRef.child(userId);

        leaderBoardsRef = FBdatabase.getReference("leaderBoards");
        easyLeaderBoards = leaderBoardsRef.child("easy");
        mediumLeaderBoards = leaderBoardsRef.child("medium");
        hardLeaderBoards = leaderBoardsRef.child("hard");


        userEasyView = a.findViewById(R.id.user_highscore_easy);
        userMediumView = a.findViewById(R.id.user_highscore_medium);
        userHardView = a.findViewById(R.id.user_highscore_hard);



        getLeaderboards();
    }

    private void getLeaderboards(){

        easyLeaderBoards.addValueEventListener(new ValueEventListener() {
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
        mediumLeaderBoards.addValueEventListener(new ValueEventListener() {
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
        hardLeaderBoards.addValueEventListener(new ValueEventListener() {
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

    public void writeNewUser(String email) {

        User user = new User(email);
        userRef.setValue(user);
    }

    public void retrieveData(final String email, final Button resume){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            // Called with a snapshot of the data at this location. Called each time that data changes
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // If user does not exist in the database, one is created with user's email
                if(!dataSnapshot.exists()) {
                    // Get users email through the Firebase authentication
                    writeNewUser(email);
                }

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

    public void setUserCurrentGame(String val){
        userRef.child("currentGame").setValue(val);
    }

    public void setUserSolution(String val){
        userRef.child("solution").setValue(val);
    }

    public void setUserDiff(String val){
        userRef.child("diff").setValue(val);
    }

    public void setUserUserSolution(String val){
        currUser.setUserSolution(val);
        userRef.child("userSolution").setValue(val);
    }

    public void resetUserGame(String board, String diff){
        setUserCurrentGame("");
        setUserSolution("");
        setUserUserSolution(board);
        setUserDiff(diff);
    }

    public void setUserEasyHighScore(int val){
        currUser.setEasyHighScores(val);
        saveUserEasyHighScore();
    }

    public void saveUserEasyHighScore(){
        userRef.child("easyHighScores").setValue(currUser.getEasyHighScores());
    }

    public void setUserMediumHighScore(int val){
        currUser.setMediumHighScores(val);
        saveUserMediumHighScore();
    }

    public void saveUserMediumHighScore(){
        userRef.child("mediumHighScores").setValue(currUser.getMediumHighScores());
    }

    public void setUserHardHighScore(int val){
        currUser.setHardHighScores(val);
        saveUserHardHighScore();
    }

    public void saveUserHardHighScore(){
        userRef.child("hardHighScores").setValue(currUser.getHardHighScores());
    }

    public void setUserCurrentTime(int val){
        currUser.setCurrentTime(val);
        saveUserCurrentTime();
    }

    public void saveUserCurrentTime(){
        userRef.child("currentTime").setValue(currUser.getCurrentTime());
    }


    public void saveAllTimes(){
        saveUserEasyHighScore();
        saveUserMediumHighScore();
        saveUserHardHighScore();
        saveUserCurrentTime();
    }

}
