package c.b.a.sudokuapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.junit.Test;

import static org.junit.Assert.*;

public class LogicTest {

    Logic logic = new Logic();

    @Test
    public void countEmptyCellsTest() {
        int[][] arr1 = { {2, 0, 0, 0, 0, 0, 0, 3, 0},
                         {0, 0, 0, 0, 0, 7, 5, 0, 0},
                         {0, 6, 9, 0, 0, 0, 0, 0, 7},
                         {3, 1, 2, 4, 0, 6, 0, 0, 8},
                         {4, 0, 0, 0, 0, 9, 0, 0, 3},
                         {7, 9, 8, 0, 0, 0, 0, 0, 6},
                         {6, 0, 1, 8, 0, 0, 9, 0, 5},
                         {8, 0, 0, 0, 0, 0, 0, 6, 2},
                         {9, 0, 3, 0, 2, 5, 8, 1, 4} };
        assertEquals(46, logic.countEmptyCells(arr1));

        int[][] arr2 = { {0, 0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 0, 0, 0, 0} };

        assertEquals(81, logic.countEmptyCells(arr2));

        int[][] arr3 = { {7, 2, 8, 3, 6, 5, 4, 1, 9},
                         {1, 6, 3, 4, 9, 7, 2, 8, 5},
                         {4, 5, 9, 1, 2, 8, 3, 6, 7},
                         {2, 1, 4, 5, 3, 6, 7, 9, 8},
                         {3, 7, 5, 2, 8, 9, 1, 4, 6},
                         {8, 9, 6, 7, 1, 4, 5, 3, 2},
                         {9, 3, 1, 6, 5, 2, 8, 7, 4},
                         {6, 4, 2, 8, 7, 1, 9, 5, 3},
                         {5, 8, 7, 9, 4, 3, 6, 2, 1} };

        assertEquals(0, logic.countEmptyCells(arr3));
    }

    @Test
    public void stringToIntTest() {
        int[][] arr = { {1, 0, 0, 0, 7, 0, 0, 3, 0},
                {0, 0, 0, 0, 0, 7, 5, 0, 0},
                {0, 6, 9, 0, 0, 0, 0, 8, 7},
                {3, 1, 2, 4, 0, 6, 0, 0, 8},
                {4, 0, 0, 0, 0, 9, 0, 0, 3},
                {7, 9, 8, 0, 0, 0, 0, 0, 6},
                {6, 0, 1, 8, 3, 0, 9, 0, 5},
                {8, 0, 0, 0, 0, 0, 0, 6, 2},
                {9, 0, 3, 0, 2, 5, 8, 1, 4} };

        String str = "100070030000007500069000087312406008400009003798000006601830905800000062903025814";

        assertArrayEquals(arr, logic.stringToInt(str));
    }

    @Test
    public void intToStringTest() {
        int[][] arr = { {1, 0, 0, 0, 7, 0, 0, 3, 0},
                {0, 0, 0, 0, 0, 7, 5, 0, 0},
                {0, 6, 9, 0, 0, 0, 0, 8, 7},
                {3, 1, 2, 4, 0, 6, 0, 0, 8},
                {4, 0, 0, 0, 0, 9, 0, 0, 3},
                {7, 9, 8, 0, 0, 0, 0, 0, 6},
                {6, 0, 1, 8, 3, 0, 9, 0, 5},
                {8, 0, 0, 0, 0, 0, 0, 6, 2},
                {9, 0, 3, 0, 2, 5, 8, 1, 4} };

        String str = "100070030000007500069000087312406008400009003798000006601830905800000062903025814";

        assertEquals(str, logic.intToString(arr));
    }

    @Test
    public void splitEmailTest() {

        String str = "testing@testing.is";
        assertEquals("testing", logic.splitUserEmail(str));
    }

    @Test
    public void convertBoardToStringTest() {
        int[][] arr = { {2, 0, 0, 0, 0, 0, 0, 3, 0},
                        {0, 0, 0, 0, 0, 7, 5, 0, 0},
                        {0, 6, 9, 0, 0, 0, 0, 0, 7},
                        {3, 1, 2, 4, 0, 6, 0, 0, 8},
                        {4, 0, 0, 0, 0, 9, 0, 0, 3},
                        {7, 9, 8, 0, 0, 0, 0, 0, 6},
                        {6, 0, 1, 8, 0, 0, 9, 0, 5},
                        {8, 0, 0, 0, 0, 0, 0, 6, 2},
                        {9, 0, 3, 0, 2, 5, 8, 1, 4} };
        assertEquals("[[2,0,0,0,0,0,0,3,0]," +
                                "[0,0,0,0,0,7,5,0,0]," +
                                "[0,6,9,0,0,0,0,0,7]," +
                                "[3,1,2,4,0,6,0,0,8]," +
                                "[4,0,0,0,0,9,0,0,3]," +
                                "[7,9,8,0,0,0,0,0,6]," +
                                "[6,0,1,8,0,0,9,0,5]," +
                                "[8,0,0,0,0,0,0,6,2]," +
                                "[9,0,3,0,2,5,8,1,4]]", logic.convertBoardToString(arr));
    }

    @Test
    public void createEmptyBoardTest() {
        assertArrayEquals(new int[9][9], logic.createEmptyBoard());
    }

}