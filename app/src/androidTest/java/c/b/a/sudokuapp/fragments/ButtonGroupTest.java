package c.b.a.sudokuapp.fragments;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ButtonGroupTest {

    private ButtonGroup BG;

    @Before
    public void init(){
        BG = new ButtonGroup();
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