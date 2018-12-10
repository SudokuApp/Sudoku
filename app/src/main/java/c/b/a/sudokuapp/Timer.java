package c.b.a.sudokuapp;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.widget.TextView;

public class Timer {
    public Thread t;
    private boolean isPaused;
    public int timeTotal;

    public Timer(){
        isPaused = false;
    }
    public void pauseTimer(){
        this.isPaused = true;
    }

    public void resumeTimer(){
        this.isPaused = false;
    }

    public boolean isAlive(){
        return t.isAlive();
    }

    public String getTimeReadable(){
        return DateUtils.formatElapsedTime((timeTotal));
    }

    public int getTime(){
        return timeTotal;
    }

    public void startTimeThread(final int start, final TextView timeTaken){
        if(t != null){
            stopThread();
        } 
        t = new Thread() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                while (!isInterrupted()) {
                    if(isPaused){
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        timeTotal = (int) SystemClock.currentThreadTimeMillis() / 1000 + start;
                        timeTaken.setText(DateUtils.formatElapsedTime(timeTotal));
                    }
                }
            }
        };
        t.start();
    }

    public void stopThread(){
        if(t != null){
            while(!t.isInterrupted()){
                t.interrupt();
            }
            t = null;
        }
    }
}
