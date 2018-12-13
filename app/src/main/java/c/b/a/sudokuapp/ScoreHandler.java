package c.b.a.sudokuapp;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static c.b.a.sudokuapp.MenuFragment.easyScores;
import static c.b.a.sudokuapp.MenuFragment.mediumScores;
import static c.b.a.sudokuapp.MenuFragment.hardScores;

import java.util.ArrayList;
import java.util.List;

import static c.b.a.sudokuapp.MenuFragment.currUser;

// A class that handles the scores (time)
class ScoreHandler {

    private String diff;
    private FirebaseDatabase mDatabase;

    //class needs the difficulty and a reference to the database
    ScoreHandler(String diff, FirebaseDatabase mDatabase) {

        this.diff = diff;
        this.mDatabase = mDatabase;

    }

    //add this score to the leaderboards and the remove the highest score if there are more than 5.
    private void compareToGlobal(int newScore, List<ScorePair> globalScores, DatabaseReference ref){

        if(globalScores == null){
            globalScores = new ArrayList<>();
        }
        globalScores.add(new ScorePair(Logic.splitUserEmail(currUser.getEmail()), newScore));

        if(globalScores.size() > 5){

            ScorePair highestGlobal = new ScorePair();
            highestGlobal.setScore(0);

            for(ScorePair s : globalScores){
                if(highestGlobal.getScore() < s.getScore()){
                    highestGlobal = s;
                }
            }
            globalScores.remove(highestGlobal);
        }
        ref.setValue(globalScores);
    }

    //check if this new score is better than the users old one for this difficulty
    void compareToPrivate(int newScore, DatabaseReference userRef){

        DatabaseReference scoreref = mDatabase.getReference("leaderBoards");
        boolean highscore = false;

        switch (diff) {
            case "easy":
                if (newScore < currUser.getEasyHighScores()) {
                    highscore = true;

                    currUser.setEasyHighScores(newScore);
                }
                sendToCompareToGlobal(newScore, easyScores, scoreref);
                break;
            case "medium":
                if (newScore < currUser.getMediumHighScores()) {
                    highscore = true;

                    currUser.setMediumHighScores(newScore);
                }
                sendToCompareToGlobal(newScore, mediumScores, scoreref);
                break;
            case "hard":
                if (newScore < currUser.getHardHighScores()) {
                    highscore = true;

                    currUser.setHardHighScores(newScore);
                }
                sendToCompareToGlobal(newScore, hardScores, scoreref);
                break;
        }
        if(highscore){
            saveUserScore(newScore, userRef);
        }
    }

    private void saveUserScore(int newScore, DatabaseReference ref){
        ref.child(diff + "HighScores").setValue(newScore);
    }

    private void sendToCompareToGlobal(int newScore, List<ScorePair> list, DatabaseReference ref){
        DatabaseReference scoreref = ref.child(diff);
        compareToGlobal(newScore, list, scoreref);
    }
}
