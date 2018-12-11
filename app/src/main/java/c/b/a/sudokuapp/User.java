package c.b.a.sudokuapp;

import com.google.gson.JsonArray;

public class User {
    private String currentGame;
    private String userSolution;
    private String solution;
    private int currentTime;
    private int easyHighScore;
    private int mediumHighScore;
    private int hardHighScore;
    private int totalCompleted;
    private String email;

    //total games? number of cancelled games?

    public User() {

    }

    public User(String email) {
        this.currentGame = "";
        this.solution = "";
        this.userSolution = "";
        this.currentTime = 0;
        this.easyHighScore = Integer.MAX_VALUE;
        this.mediumHighScore = Integer.MAX_VALUE;
        this.hardHighScore = Integer.MAX_VALUE;
        this.totalCompleted = 0;
        this.email = email;

    }

    public String getCurrentGame() {
        return currentGame;
    }

    public String getUserSolution() {
        return userSolution;
    }

    public String getSolution() {
        return solution;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public int getEasyHighScore() {
        return easyHighScore;
    }

    public int getMediumHighScore() {
        return mediumHighScore;
    }

    public int getHardHighScore() {
        return hardHighScore;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public String getEmail() {
        return email;
    }

    public void setCurrentGame(String currentGame) {
        this.currentGame = currentGame;
    }

    public void setUserSolution(String userSolution) {
        this.userSolution = userSolution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public void setEasyHighScores(int easyHighScores) {
        this.easyHighScore = easyHighScores;
    }

    public void setMediumHighScores(int mediumHighScores) {
        this.mediumHighScore = mediumHighScores;
    }

    public void setHardHighScores(int hardHighScores) {
        this.hardHighScore = hardHighScores;
    }

    public void setTotalCompleted(int totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}