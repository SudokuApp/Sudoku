package c.b.a.sudokuapp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.widget.TextView;

import java.lang.ref.WeakReference;

//A class that holds the time (score).
public class Timer {
    private Thread t;
    private static TimeHandler handler;
    private boolean isPaused;
    private int timeTotal;
    private int punishment;
    private TextView timeTaken;
    private int start;

    // A handler for the Timer class to interact with the UI thread based on code from
    // https://stackoverflow.com/questions/11407943/this-handler-class-should-be-static-or-leaks-might-occur-incominghandler
    static class TimeHandler extends Handler{
        final WeakReference<Timer> timerReference;

        TimeHandler(Timer timer){
            timerReference = new WeakReference<>(timer);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int status = msg.what;
            if(status == 0){
                printTime();
            }
        }

        // calls the printTime function in the Timer
        void printTime(){
            timerReference.get().printTime();
        }
    }

    //Timer needs to know where to print the time
    Timer(TextView timeTaken){
        this.timeTaken = timeTaken;
        isPaused = false;
        handler = new TimeHandler(this);
        this.punishment = 0;
        this.start = 0;
    }

    //print the time to the UI
    private void printTime(){
        timeTaken.setText(getTimeReadable());
    }

    //pause the timer
    void pauseTimer(){
        this.isPaused = true;
    }

    //resume the timer
    void resumeTimer(){
        this.isPaused = false;
    }

    boolean isPaused(){
        return isPaused;
    }

    int getPunishment(){
        return punishment;
    }

    //get time in mm:ss or hh:mm:ss if you suck
    String getTimeReadable(){
        return DateUtils.formatElapsedTime((getTime()));
    }

    //get time in seconds
    public int getTime(){
        return timeTotal + start + punishment;
    }

    // starts a thread that counts the seconds, starting from 'start'
    void startTimeThread(final int start){

        this.start = start;

        //if, for some reason, this thread already exists, stop it.
        if(isAlive()){
            stopThread();
        }
        t = new Thread() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                while (!isInterrupted()) {

                    // get the time elapsed + the starting value + your punishment and have the
                    // handler post it the UI thread
                    if(!isPaused()){
                        timeTotal = (int) SystemClock.currentThreadTimeMillis() / 1000;
                        handler.handleMessage(new Message());
                    }
                }
            }
        };
        t.start();
    }

    void addMinute(){
        punishment += 60;
    }

    //apparently, you can no longer just stop a thread. So this function interrupts the thread and
    // sets it's priority to the lowest possible
    void stopThread(){
        punishment = 0;
        if(isAlive()){
            while(!t.isInterrupted()){
                t.interrupt();
            }
            t.setPriority(Thread.MIN_PRIORITY);
            t = null;
        }
    }

    boolean isAlive(){
        if(t == null){
            return false;
        }
        else{
            return true;
        }
    }
}
