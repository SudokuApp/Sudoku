package c.b.a.sudokuapp;

import org.junit.Test;

import c.b.a.sudokuapp.entities.ScorePair;

import static org.junit.Assert.*;

public class ScorePairTest {


    @Test
    public void getName() {
        ScorePair sp = new ScorePair("john", 123);

        assertEquals(sp.getName(), "john");
        assertNotEquals(sp.getName(), "another name");
    }

    @Test
    public void setName() {
        ScorePair sp = new ScorePair();
        sp.setName("a name");

        assertEquals(sp.getName(), "a name");
        assertNotEquals(sp.getName(), "another name");
    }

    @Test
    public void getScore() {
        ScorePair sp = new ScorePair();
        sp.setScore(1);

        assertEquals(sp.getScore(), 1);
        assertNotEquals(sp.getScore(), 69);
    }

    @Test
    public void setScore() {
        ScorePair sp = new ScorePair();
        sp.setScore(1);

        assertEquals(sp.getScore(), 1);
        assertNotEquals(sp.getScore(), 69);
    }

    @Test
    public void compareTo() {
        ScorePair sp1 = new ScorePair("name1", 1);
        ScorePair sp2 = new ScorePair("name2", 10);

        assertNotEquals(sp1, sp2);
        assertEquals( -9, sp1.ScoreComparator.compare(sp1,sp2));
        assertEquals(sp1.compareTo(sp2), -9);
        assertEquals(sp2.compareTo(sp1), 9);
    }

}