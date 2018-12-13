package c.b.a.sudokuapp.entities;

import java.util.Comparator;


//A class class with a name and a score, used for the leaderboards
public class ScorePair implements Comparable<ScorePair>{

    private String name;
    private int score;

    // init the score as the highest value possible (the worst possible)
    public ScorePair(){
        this.name = "";
        this.score = Integer.MAX_VALUE;
    }

    public ScorePair(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }


    // Method to compare 2 ScorePairs based on code from
    // https://www.mkyong.com/java/java-object-sorting-example-comparable-and-comparator/

    @Override
    public int compareTo(ScorePair o) {
        return this.getScore() - o.getScore();
    }

    public static Comparator<ScorePair> ScoreComparator
            = new Comparator<ScorePair>() {

        public int compare(ScorePair pair1, ScorePair pair2) {
            return pair1.compareTo(pair2);
        }
    };
}
