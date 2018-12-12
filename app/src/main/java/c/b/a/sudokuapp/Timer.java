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
    private TextView timeTaken;

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

    //get time in mm:ss or hh:mm:ss if you suck
    String getTimeReadable(){
        return DateUtils.formatElapsedTime((timeTotal));
    }

    //get time in seconds
    public int getTime(){
        return timeTotal;
    }

    // starts a thread that counts the seconds, starting from 'start'
    void startTimeThread(final int start){
        //if, for some reason, this thread already exists, stop it.
        if(t != null){
            stopThread();
        }
        t = new Thread() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                while (!isInterrupted()) {
                    //if the timer is paused, check every 0.1 second if it's still paused
                    if(isPaused){
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // get the time elapsed + the starting value and have the handler post it to
                    // the UI thread
                    else{
                        timeTotal = (int) SystemClock.currentThreadTimeMillis() / 1000 + start;
                        handler.handleMessage(new Message());
                    }
                }
            }
        };
        t.start();
    }

    //apparently, you can no longer just stop a thread. So this function interrupts the therad and
    // sets it's priority to the lowest possible
    void stopThread(){
        if(t != null){
            while(!t.isInterrupted()){
                t.interrupt();
            }
            t.setPriority(Thread.MIN_PRIORITY);
            t = null;
        }
    }
}
