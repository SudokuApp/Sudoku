package c.b.a.sudokuapp;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static c.b.a.sudokuapp.MenuFragment.currUser;

//A class class with a name and a score, used only by ScoreHandler
class ScorePair{
    private String name;
    private int score;
    ScorePair(){
        this.name = "";
        this.score = Integer.MAX_VALUE;
    }

    ScorePair(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int getScore() {
        return score;
    }
}

// A class that compares a users scores to their best and the global best
class ScoreHandler {

    private List<ScorePair> globalScores;
    private DatabaseReference globalScoresRef;
    private String diff;

    ScoreHandler(FirebaseDatabase mDatabase, String diff) {

        this.diff = diff;
        DatabaseReference ref = mDatabase.getReference("leaderBoards");
        this.globalScoresRef = ref.child(diff);
        this.globalScores = new ArrayList<>();

        globalScoresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //read all the highest scores for this difficulty
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    globalScores.add(ds.getValue(ScorePair.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //add this score to the leaderboards and the remove the highest score if there are more than 5.
    private void compareToGlobal(int newScore){

        globalScores.add(new ScorePair(currUser.getEmail(), newScore));
        if(globalScores.size() > 5){

            ScorePair highestGlobal = new ScorePair();

            for(ScorePair s : globalScores){
                if(highestGlobal.getScore() > s.getScore()){
                    highestGlobal = s;
                }
            }
            globalScores.remove(highestGlobal);
        }
        globalScoresRef.setValue(globalScores);
    }

    //check if this new score is better than the users old one for this difficulty
    void compareToPrivate(int newScore){
        if (diff.equals("easy") && newScore < currUser.getEasyHighScores()){
            currUser.setEasyHighScores(newScore);
        }
        else if (diff.equals("medium") && newScore < currUser.getMediumHighScores()){
            currUser.setMediumHighScores(newScore);
        }
        else if (diff.equals("hard") && newScore < currUser.getHardHighScores()) {
            currUser.setHardHighScores(newScore);
        }
        compareToGlobal(newScore);
    }
}
