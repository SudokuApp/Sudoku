package c.b.a.sudokuapp;

import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import c.b.a.sudokuapp.services.Timer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TimerTest {

    private Timer timer;
    private TextView view;

    @Before
    public void init(){
        view = Mockito.mock(TextView.class);
        timer = new Timer(view);

        Mockito.doNothing().when(view).setText(isA(String.class));
    }

    @After
    public void cleanUp(){
        timer.stopThread();
        timer = null;
        view = null;
    }

    @Test
    public void pauseTimer(){
        timer.pauseTimer();
        assertTrue(timer.isPaused());
    }

    @Test
    public void resumeTimer() {
        timer.resumeTimer();
        assertFalse(timer.isPaused());
    }

    @Test
    public void getTime() {
        timer.startTimeThread(35);
        timer.addMinute();
        assertEquals(timer.getTime(), 95);
    }

    @Test
    public void startTimeThread() {
        assertEquals(timer.getTime(), 0);

        timer.addMinute();
        assertEquals(timer.getTime(), 60);
        assertFalse(timer.isAlive());

        timer.startTimeThread(40);
        assertTrue(timer.isAlive());
        assertEquals(timer.getTime(), 100);

    }

    @Test
    public void addMinute() {
        timer.addMinute();
        assertEquals(timer.getPunishment(), 60);
    }

    @Test
    public void getPunishment(){
        timer.addMinute();
        assertEquals(timer.getPunishment(), 60);
    }

    @Test
    public void stopThread() {
        timer.startTimeThread(0);
        timer.stopThread();
        assertFalse(timer.isAlive());
    }

    @Test
    public void isAlive(){
        timer.startTimeThread(0);
        assertTrue(timer.isAlive());
        timer.stopThread();
        assertFalse(timer.isAlive());
    }
}