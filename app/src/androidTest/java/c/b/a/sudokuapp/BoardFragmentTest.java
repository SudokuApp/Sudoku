package c.b.a.sudokuapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardFragmentTest {

    private BoardFragment board;
    String str = "007000800000000000600100000213056700040000003798003040050624970862070300974005612";
    int [][] arr = {{0,0,7,0,0,0,8,0,0},
                    {0,0,0,0,0,0,0,0,0},
                    {6,0,0,1,0,0,0,0,0},
                    {2,1,3,0,5,6,7,0,0},
                    {0,4,0,0,0,0,0,0,3},
                    {7,9,8,0,0,3,0,4,0},
                    {0,5,0,6,2,4,9,7,0},
                    {8,6,2,0,7,0,3,0,0},
                    {9,7,4,0,0,5,6,1,2}};

    @Before
    public void init(){
        board = new BoardFragment();
    }

    @After
    public void destroy(){
        board = null;
    }

    @Test
    public void intToStringTest(){
        assertEquals(arr, board.stringToInt(str));
    }

    @Test
    public void stringToIntTest(){
        assertEquals(str, board.intToString(arr));
    }

}