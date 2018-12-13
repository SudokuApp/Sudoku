package c.b.a.sudokuapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private User user;

    @Before
    public void init(){
        user = new User("test@test.is");
    }

    @After
    public void cleanUp(){
        user = null;
    }

    @Test
    public void EmptyConstructer(){
        User testuser = new User();
    }

    @Test
    public void getCurrentGame() {
        assertEquals(user.getCurrentGame(), "");
    }

    @Test
    public void getUserSolution() {
        assertEquals(user.getUserSolution(), "");
    }

    @Test
    public void setUserSolution() {
        user.setUserSolution("testSolution");
        assertEquals(user.getUserSolution(), "testSolution");
    }

    @Test
    public void getSolution() {
        assertEquals(user.getSolution(), "");
    }

    @Test
    public void getCurrentTime() {
        assertEquals(user.getCurrentTime(), 0);
    }

    @Test
    public void getEasyHighScores() {
        assertEquals(user.getEasyHighScores(), Integer.MAX_VALUE);
    }

    @Test
    public void getMediumHighScores() {
        assertEquals(user.getMediumHighScores(), Integer.MAX_VALUE);

    }

    @Test
    public void getHardHighScores() {
        assertEquals(user.getHardHighScores(), Integer.MAX_VALUE);

    }

    @Test
    public void getEmail() {
        assertEquals(user.getEmail(), "test@test.is");
    }

    @Test
    public void setCurrentTime() {
        user.setCurrentTime(69);
        assertEquals(user.getCurrentTime(), 69);
    }

    @Test
    public void setEasyHighScores() {
        user.setEasyHighScores(69);
        assertEquals(user.getEasyHighScores(), 69);
    }

    @Test
    public void setMediumHighScores() {
        user.setMediumHighScores(69);
        assertEquals(user.getMediumHighScores(), 69);
    }

    @Test
    public void setHardHighScores() {
        user.setHardHighScores(69);
        assertEquals(user.getHardHighScores(), 69);
    }

    @Test
    public void getDiff() {
        assertEquals(user.getDiff(), "");
    }
}