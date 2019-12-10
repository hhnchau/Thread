package vn.ptt.threadhandler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorActivity extends AppCompatActivity {
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private static final int KEEP_ALIVE_TIME = 1000;

    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS;

    private int count = 0;
    private int count1 = 0;
    private int count2 = 0;
    private int v = 0;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    count++;
                    String msg = count < 1000 ? "working " : "done ";
                    updateStatus("SingleThreadExecutor: " + msg + count);
                }
            });

        }
    };

    private Runnable mRunnable1 = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    count1++;
                    String msg = count1 < 1000 ? "working " : "done ";
                    updateStatus1("ThreadPoolExecutor: "+msg + count1);
                }
            });

        }
    };

    private Runnable mRunnable2 = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    count2++;
                    String msg = count2 < 1000 ? "working " : "done ";
                    updateStatus2("Thread: "+msg + count2);
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_pool_executor);
    }

    public void thread(){
        new Thread(mRunnable2).start();
    }

    public void buttonClickSingleThread(View view) {
        count = 0;
        Executor mSingleThreadExecutor = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 1000; i++) {
            mSingleThreadExecutor.execute(mRunnable); //Tao ra 1 thread thuc thi 1000 runnable
        }
    }

    public void buttonClickThreadPool(View view) {
        count1 = 0;
        ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES + 5,   // Initial pool size
                NUMBER_OF_CORES + 8,   // Max pool size
                KEEP_ALIVE_TIME,       // Time idle thread waits before terminating
                KEEP_ALIVE_TIME_UNIT,  // Sets the Time Unit for KEEP_ALIVE_TIME
                new LinkedBlockingDeque<Runnable>());  // Work Queue

        for (int i = 0; i < 1000; i++) {
            mThreadPoolExecutor.execute(mRunnable1);
        }
    }

    public void buttonThread(View view) {
        count2 = 0;
        for (int i = 0; i < 1000; i++) {
            thread(); // => Tao ra 1000 thread khac nhau
        }
    }

    private void updateStatus(String msg) {
        Log.d("TAG", msg);
        ((TextView) findViewById(R.id.text)).setText(msg);
    }

    private void updateStatus1(String msg) {
        Log.d("TAG", msg);
        ((TextView) findViewById(R.id.text1)).setText(msg);
    }

    private void updateStatus2(String msg) {
        Log.d("TAG", msg);
        ((TextView) findViewById(R.id.text2)).setText(msg);
    }

    public void clickMe(View view) {
        if (view instanceof Button) {
            ((Button) view).setText((v + ""));
            v++;
        }
    }

}
