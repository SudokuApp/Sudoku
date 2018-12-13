package c.b.a.sudokuapp.fragments;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ButtonFragmentTest {

    private ButtonFragment BG;

    @Before
    public void init(){
        BG = new ButtonFragment();
    }

    @After
    public void destroy(){
        BG = null;
    }

    @Test
    public void testInput(){
        BG.setInput("5");

        assertEquals("5", BG.getInput());
    }

}