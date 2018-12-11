package c.b.a.sudokuapp;

//A class class with a name and a score, used only by ScoreHandler
public class ScorePair{
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
