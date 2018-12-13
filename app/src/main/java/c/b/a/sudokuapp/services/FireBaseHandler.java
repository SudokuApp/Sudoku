package c.b.a.sudokuapp.services;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import c.b.a.sudokuapp.entities.ScorePair;
import c.b.a.sudokuapp.entities.User;

public class FireBaseHandler {

    public FirebaseDatabase FBdatabase;
    public DatabaseReference leaderBoardsRef, easyLeaderBoards, mediumLeaderBoards, hardLeaderBoards, userRef;
    public List<ScorePair> easyScores;
    public List<ScorePair> mediumScores;
    public List<ScorePair> hardScores;
    public User currUser;


    //Initialize the almighty FireBaseHandler
    public FireBaseHandler(String userId){

        FBdatabase = FirebaseDatabase.getInstance();

        //A reference to the root of all users
        DatabaseReference allUsersRef = FBdatabase.getReference("users");

        //A reference to this specific user
        userRef = allUsersRef.child(userId);

        //references to the leaderboards
        leaderBoardsRef = FBdatabase.getReference("leaderBoards");
        easyLeaderBoards = leaderBoardsRef.child("easy");
        mediumLeaderBoards = leaderBoardsRef.child("medium");
        hardLeaderBoards = leaderBoardsRef.child("hard");

        getLeaderboards();
    }

    //read from the leaderboards into lists
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


    //write a new user to the database
    public void writeNewUser(String email) {

        User user = new User(email);
        userRef.setValue(user);
    }

    //save various things to the users database
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


    //save various leaderboards
    public void setEasyLeaderBoards(List<ScorePair> list){
        easyScores = list;
        easyLeaderBoards.setValue(list);
    }
    public void setMediumLeaderBoards(List<ScorePair> list){
        mediumScores = list;
        mediumLeaderBoards.setValue(list);
    }
    public void setHardLeaderBoards(List<ScorePair> list){
        hardScores = list;
        hardLeaderBoards.setValue(list);
    }

    // git some
    public String getUserEmail(){
        return currUser.getEmail();
    }
    public int getUserEasyHighScore(){
        return currUser.getEasyHighScores();
    }
    public int getUserMediumHighScore(){
        return currUser.getMediumHighScores();
    }
    public int getUserHardHighScore(){
        return currUser.getHardHighScores();
    }
}
