package c.b.a.sudokuapp;

import com.google.gson.JsonArray;

public class User {
    private String currentGame;
    private String solution;
    private int currentTime;
    private int easyHighScores;
    private int mediumHighScores;
    private int hardHighScores;
    private int totalCompleted;
    private String email;

    //total games? number of cancelled games?

    public User() {

    }

    public User(String email) {
        this.currentGame = "";
        this.solution = "";
        this.currentTime = 0;
        this.easyHighScores = 0;
        this.mediumHighScores = 0;
        this.hardHighScores = 0;
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

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public void setEasyHighScores(int easyHighScores) {
        this.easyHighScores = easyHighScores;
    }

    public void setMediumHighScores(int mediumHighScores) {
        this.mediumHighScores = mediumHighScores;
    }

    public void setHardHighScores(int hardHighScores) {
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

    public int getCurrentTime() {
        return currentTime;
    }

    public int getEasyHighScores() {
        return easyHighScores;
    }

    public int getMediumHighScores() {
        return mediumHighScores;
    }

    public int getHardHighScores() {
        return hardHighScores;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }
}