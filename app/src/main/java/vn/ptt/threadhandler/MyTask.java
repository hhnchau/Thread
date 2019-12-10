package vn.ptt.threadhandler;

import android.os.Handler;

public class MyTask {
    private Runnable runnable;
    private Handler handler;
    private int schedule;
    private boolean loop;

    public interface TaskCallback {
        void task();
    }

    public MyTask(int schedule, final TaskCallback callback) {
        this.schedule = schedule;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.task();
                    if (loop) start();
                }
            }
        };
    }

    public void start() {
        handler.postDelayed(runnable, schedule);
    }

    public void loop() {
        loop = true;
        start();
    }

    public void stop() {
        loop = false;
        handler.removeCallbacks(runnable);
    }
}