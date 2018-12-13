package c.b.a.sudokuapp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import c.b.a.sudokuapp.entities.ScorePair;
import c.b.a.sudokuapp.fragments.MenuFragment;
import c.b.a.sudokuapp.services.FireBaseHandler;
import c.b.a.sudokuapp.services.ScoreHandler;

import static c.b.a.sudokuapp.fragments.MenuFragment.fireBaseHandler;
import static org.junit.Assert.*;

public class ScoreHandlerTest {

    private ScoreHandler easySH;
    private List<ScorePair> list;

    @Before
    public void setUP(){

        fireBaseHandler = Mockito.mock(FireBaseHandler.class);
        easySH = Mockito.spy(new ScoreHandler("easy"));

        Mockito.when(fireBaseHandler.getUserEasyHighScore()).thenReturn(10);
        Mockito.doNothing().when(fireBaseHandler).setEasyLeaderBoards(Collections.<ScorePair>emptyList());
        Mockito.doNothing().when(fireBaseHandler).setUserEasyHighScore(Mockito.isA(Integer.class));

        Mockito.doNothing().when(fireBaseHandler).setMediumLeaderBoards(Collections.<ScorePair>emptyList());
        Mockito.doNothing().when(fireBaseHandler).setHardLeaderBoards(Collections.<ScorePair>emptyList());
        Mockito.when(fireBaseHandler.getUserEmail()).thenReturn("test@test.is");

        list = new ArrayList<>();
        list.add(new ScorePair("1", 10));
        list.add(new ScorePair("2", 20));
        list.add(new ScorePair("3", 30));
        list.add(new ScorePair("4", 40));
        list.add(new ScorePair("5", 50));

    }

    @Test
    public void compareToGlobal() {

        List temp = easySH.compareToGlobal(7, null);
        assertNotNull(temp);

        ScorePair sp = new ScorePair("test", 3);
        list = easySH.compareToGlobal(3, list);
        assertEquals(sp.getScore(), list.get(4).getScore());
        assertEquals(sp.getName(), list.get(4).getName());

        sp.setScore(100);
        list = easySH.compareToGlobal(100, list);
        assertNotEquals(sp.getScore(), list.get(4).getScore());


    }
}