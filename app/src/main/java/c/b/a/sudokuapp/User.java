package c.b.a.sudokuapp;

public class User {
    private String email;
    private String userName;
    private int[][] currentGame;
    private int[][] solution;
    private String[] easyHighScores;
    private String[] mediumHighScores;
    private String[] hardHighScores;
    //total games? number of cancelled games?

    public User(String email, String userName) {
        this.email = email;
        this.userName = userName;
        this.easyHighScores = new String[5];
        this.mediumHighScores = new String[5];
        this.hardHighScores = new String[5];
    }
    public User(String email){
        this.email = email;
        this.userName = "user";
        this.easyHighScores = new String[5];
        this.mediumHighScores = new String[5];
        this.hardHighScores = new String[5];
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int[][] getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(int[][] currentGame) {
        this.currentGame = currentGame;
    }

    public int[][] getSolution() {
        return solution;
    }

    public void setSolution(int[][] solution) {
        this.solution = solution;
    }

    public String[] getEasyHighScores() {
        return easyHighScores;
    }

    public void setEasyHighScores(String[] easyHighScores) {
        this.easyHighScores = easyHighScores;
    }

    public String[] getMediumHighScores() {
        return mediumHighScores;
    }

    public void setMediumHighScores(String[] mediumHighScores) {
        this.mediumHighScores = mediumHighScores;
    }

    public String[] getHardHighScores() {
        return hardHighScores;
    }

    public void setHardHighScores(String[] hardHighScores) {
        this.hardHighScores = hardHighScores;
    }
}