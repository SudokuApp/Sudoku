package c.b.a.sudokuapp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.widget.TextView;

import java.lang.ref.WeakReference;


public class Timer {
    private Thread t;
    private static TimeHandler handler;
    private boolean isPaused;
    private int timeTotal;
    private TextView timeTaken;

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

        void printTime(){
            timerReference.get().printTime();
        }
    }


    Timer(TextView timeTaken){
        this.timeTaken = timeTaken;
        isPaused = false;
        handler = new TimeHandler(this);
    }

    private void printTime(){
        timeTaken.setText(getTimeReadable());
    }

    void pauseTimer(){
        this.isPaused = true;
    }

    void resumeTimer(){
        this.isPaused = false;
    }

    String getTimeReadable(){
        return DateUtils.formatElapsedTime((timeTotal));
    }

    public int getTime(){
        return timeTotal;
    }

    void startTimeThread(final int start){
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
                        handler.handleMessage(new Message());
                    }
                }
            }
        };
        t.start();
    }

    void stopThread(){
        if(t != null){
            while(!t.isInterrupted()){
                t.interrupt();
            }
            t = null;
        }
    }
}
