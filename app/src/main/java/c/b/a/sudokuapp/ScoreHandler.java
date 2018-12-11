package c.b.a.sudokuapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static c.b.a.sudokuapp.MenuFragment.easyScores;
import static c.b.a.sudokuapp.MenuFragment.mediumScores;
import static c.b.a.sudokuapp.MenuFragment.hardScores;

import java.util.ArrayList;
import java.util.List;

import static c.b.a.sudokuapp.MenuFragment.currUser;

//A class class with a name and a score, used only by ScoreHandler
class ScoreHandler {

    private String diff;
    private FirebaseDatabase mDatabase;

    ScoreHandler(String diff, FirebaseDatabase mDatabase) {

        this.diff = diff;
        this.mDatabase = mDatabase;

    }

    //add this score to the leaderboards and the remove the highest score if there are more than 5.
    private void compareToGlobal(int newScore, List<ScorePair> globalScores, DatabaseReference ref){
        if(globalScores == null){
            globalScores = new ArrayList<>();
        }
        globalScores.add(new ScorePair(splitUserEmail(currUser.getEmail()), newScore));
        if(globalScores.size() > 5){

            ScorePair highestGlobal = new ScorePair();

            for(ScorePair s : globalScores){
                if(highestGlobal.getScore() > s.getScore()){
                    highestGlobal = s;
                }
            }
            globalScores.remove(highestGlobal);
        }
        ref.setValue(globalScores);
    }

    private String splitUserEmail(String email) {
        String[] emailArr = email.split("@");
        return emailArr[0];
    }

    //check if this new score is better than the users old one for this difficulty
    void compareToPrivate(int newScore, DatabaseReference userRef){

        DatabaseReference scoreref = mDatabase.getReference("leaderBoards");

        if (diff.equals("easy")){
            if (newScore < currUser.getEasyHighScores()){
                currUser.setEasyHighScores(newScore);
                userRef.child("easyHighScores").setValue(newScore);
            }
            compareToGlobal(newScore, easyScores, scoreref.child("easy"));
        }
        else if (diff.equals("medium")){
            if (newScore < currUser.getMediumHighScores()){
                currUser.setMediumHighScores(newScore);
                userRef.child("mediumHighScores").setValue(newScore);
            }
            compareToGlobal(newScore, mediumScores, scoreref.child("medium"));
        }
        else if (diff.equals("hard")) {
            if (newScore < currUser.getHardHighScores()){
                currUser.setHardHighScores(newScore);
                userRef.child("hardHighScores").setValue(newScore);
            }
            compareToGlobal(newScore, hardScores, scoreref.child("hard"));
        }
    }
}
