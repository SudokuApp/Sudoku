package c.b.a.sudokuapp;

public class User {
    private String currentGame;
    private String userSolution;
    private String solution;
    private int currentTime;
    private int easyHighScores;
    private int mediumHighScores;
    private int hardHighScores;
    private String email;
    private String diff;

    //total games? number of cancelled games?

    public User() {

    }

    public User(String email) {
        this.currentGame = "";
        this.solution = "";
        this.userSolution = "";
        this.currentTime = 0;
        this.easyHighScores = Integer.MAX_VALUE;
        this.mediumHighScores = Integer.MAX_VALUE;
        this.hardHighScores = Integer.MAX_VALUE;
        this.email = email;
        this.diff = "";

    }

    public String getCurrentGame() {
        return currentGame;
    }

    public String getUserSolution() {
        return userSolution;
    }

    public void setUserSolution(String userSolution) {
        this.userSolution = userSolution;
    }

    public String getSolution() {
        return solution;
    }

    public int getCurrentTime() { return currentTime; }

    public int getEasyHighScores() {
        return easyHighScores;
    }

    public int getMediumHighScores() {
        return mediumHighScores;
    }

    public int getHardHighScores() {
        return hardHighScores;
    }

    public String getEmail() {
        return email;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public void setEasyHighScores(int easyHighScores) {
        this.easyHighScores = easyHighScores;
    }

    public void setMediumHighScores(int mediumHighScores) { this.mediumHighScores = mediumHighScores; }

    public void setHardHighScores(int hardHighScores) {
        this.hardHighScores = hardHighScores;
    }

    public String getDiff() {
        return diff;
    }
}