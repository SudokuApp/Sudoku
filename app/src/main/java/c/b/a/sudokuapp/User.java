package c.b.a.sudokuapp;

import com.google.gson.JsonArray;

public class User {
    private String currentGame;
    private String solution;
    private String currentTime;
    private String easyHighScores;
    private String mediumHighScores;
    private String hardHighScores;
    private int totalCompleted;
    private String email;

    //total games? number of cancelled games?

    public User() {

    }

    public User(String email) {
        this.currentGame = "";
        this.solution = "";
        this.currentTime = "";
        this.easyHighScores = "";
        this.mediumHighScores = "";
        this.hardHighScores = "";
        this.totalCompleted = 0;
        this.email = email;

    }

    public String getEmail() {
        return email;
    }


    public void setCurrentGame(String currentGame) {
        this.currentGame = currentGame;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public void setEasyHighScores(String easyHighScores) {
        this.easyHighScores = easyHighScores;
    }

    public void setMediumHighScores(String mediumHighScores) {
        this.mediumHighScores = mediumHighScores;
    }

    public void setHardHighScores(String hardHighScores) {
        this.hardHighScores = hardHighScores;
    }

    public void setTotalCompleted(int totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public String getCurrentGame() {

        return currentGame;
    }

    public String getSolution() {
        return solution;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getEasyHighScores() {
        return easyHighScores;
    }

    public String getMediumHighScores() {
        return mediumHighScores;
    }

    public String getHardHighScores() {
        return hardHighScores;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }
}