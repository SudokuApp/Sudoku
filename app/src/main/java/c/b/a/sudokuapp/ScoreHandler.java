package c.b.a.sudokuapp;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import static c.b.a.sudokuapp.MenuFragment.fireBaseHandler;

// A class that handles the scores (time)
class ScoreHandler {

    private String diff;

    //class needs the difficulty and a reference to the database
    ScoreHandler(String diff) {

        this.diff = diff;

    }

    //add this score to the leaderboards and the remove the highest score if there are more than 5.
    private void compareToGlobal(int newScore, List<ScorePair> globalScores){

        if(globalScores == null){
            globalScores = new ArrayList<>();
        }
        globalScores.add(new ScorePair(Logic.splitUserEmail(fireBaseHandler.currUser.getEmail()), newScore));

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
        //ref.setValue(globalScores);
    }

    //check if this new score is better than the users old one for this difficulty
    void compareToPrivate(int newScore){


        switch (diff) {
            case "easy":
                if (newScore < fireBaseHandler.currUser.getEasyHighScores()) {

                    fireBaseHandler.setUserEasyHighScore(newScore);
                }
                break;
            case "medium":
                if (newScore < fireBaseHandler.currUser.getMediumHighScores()) {

                    fireBaseHandler.setUserMediumHighScore(newScore);
                }
                break;
            case "hard":
                if (newScore < fireBaseHandler.currUser.getHardHighScores()) {

                    fireBaseHandler.setUserHardHighScore(newScore);
                }
                break;
        }
    }
}
