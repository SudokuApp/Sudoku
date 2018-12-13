package c.b.a.sudokuapp.services;

import java.util.ArrayList;
import java.util.List;

import c.b.a.sudokuapp.entities.ScorePair;

import static c.b.a.sudokuapp.fragments.MenuFragment.fireBaseHandler;

// A class that handles the scores (time)
public class ScoreHandler {

    private String diff;

    //class needs the difficulty and a reference to the database
    public ScoreHandler(String diff) {

        this.diff = diff;

    }

    //Add the score to a list and remove the highest time
    public List<ScorePair> compareToGlobal(int newScore, List<ScorePair> globalScores){

        if(globalScores == null){
            globalScores = new ArrayList<>();
        }

        //add score t list
        globalScores.add(new ScorePair(Logic.splitUserEmail(fireBaseHandler.getUserEmail()), newScore));

        //if list is longer than 5, delete the highest time
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
        return globalScores;
    }

    //check if this new score is better than the users old one for this difficulty
    // and save it to the database
    public void compareToPrivate(int newScore){

        List<ScorePair> temp;

        switch (diff) {
            case "easy":
                if (newScore < fireBaseHandler.getUserEasyHighScore()) {

                    fireBaseHandler.setUserEasyHighScore(newScore);
                }
                temp = compareToGlobal(newScore, fireBaseHandler.easyScores);
                fireBaseHandler.setEasyLeaderBoards(temp);
                break;
            case "medium":
                if (newScore < fireBaseHandler.getUserMediumHighScore()) {

                    fireBaseHandler.setUserMediumHighScore(newScore);

                }
                temp = compareToGlobal(newScore, fireBaseHandler.mediumScores);
                fireBaseHandler.setMediumLeaderBoards(temp);
                break;
            case "hard":
                if (newScore < fireBaseHandler.getUserHardHighScore()) {

                    fireBaseHandler.setUserHardHighScore(newScore);

                }
                temp = compareToGlobal(newScore, fireBaseHandler.hardScores);
                fireBaseHandler.setHardLeaderBoards(temp);
                break;
        }
    }
}
