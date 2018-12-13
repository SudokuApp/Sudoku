package c.b.a.sudokuapp;

import android.app.Activity;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsAnything;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static c.b.a.sudokuapp.MenuFragment.fireBaseHandler;
import static org.junit.Assert.*;

public class ScoreHandlerTest {

    private MenuFragment mf;
    private ScoreHandler easySH;
    private List<ScorePair> list;

    @Before
    public void setUP(){

        mf = Mockito.mock(MenuFragment.class);
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

        List temp = null;
        temp = easySH.compareToGlobal(7, temp);
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