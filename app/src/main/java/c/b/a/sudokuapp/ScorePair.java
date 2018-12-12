package c.b.a.sudokuapp;

import java.util.Comparator;

//A class class with a name and a score, used only by ScoreHandler
public class ScorePair implements Comparable<ScorePair>{
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

    void setScore(int score){
        this.score = score;
    }


    // Method to compare 2 ScorePairs based on code from
    // https://www.mkyong.com/java/java-object-sorting-example-comparable-and-comparator/

    @Override
    public int compareTo(ScorePair o) {
        return this.getScore() - o.getScore();
    }

    static Comparator<ScorePair> ScoreComparator
            = new Comparator<ScorePair>() {

        public int compare(ScorePair pair1, ScorePair pair2) {
            return pair1.compareTo(pair2);
        }
    };
}
