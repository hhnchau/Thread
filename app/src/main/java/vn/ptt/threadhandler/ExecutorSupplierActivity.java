package vn.ptt.threadhandler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ExecutorSupplierActivity extends AppCompatActivity {
    private int click;
    private int count;
    private int count1;
    private int count2;
    private int count3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executor_supplier);
    }

    public void forBackgroundTasks(View view) {
        count = 0;
        for (int i = 0; i < 1000; i++) {
            Log.d("TAG", "1. forBackgroundTasks execute " + i);
            DefaultExecutorSupplier.getInstance().forBackgroundTasks().execute(runnableBackgroundTasks);
        }
    }

    public void forLightWeightBackgroundTasks(View view) {
        count1 = 0;
        for (int i = 0; i < 1000; i++) {
            Log.d("TAG", "2. forLightWeightBackgroundTasks execute " + i);
            DefaultExecutorSupplier.getInstance().forLightWeightBackgroundTask().execute(runnableLightWeightBackgroundTasks);
        }
    }

    public void forMainThreadTasks(View view) {
        count2 = 0;
        for (int i = 0; i < 1000; i++) {
            Log.d("TAG", "3. forMainThreadTasks execute " + i);
            DefaultExecutorSupplier.getInstance().forMainThreadTask().execute(runnableMainThreadTasks);
        }
    }

    public void forPriorityBackgroundTask(View view) {
        count3 = 0;
        for (int i = 0; i < 1000; i++) {
            Log.d("TAG", "4. forPriorityBackgroundTask execute " + i);
            if (i % 2 == 0)
                DefaultExecutorSupplier.getInstance().forPriorityBackgroundTasks().submit(runnablePriorityHighBackgroundTasks);
            else
                DefaultExecutorSupplier.getInstance().forPriorityBackgroundTasks().submit(runnablePriorityLowBackgroundTasks);
        }
    }

    public void clickMe(View view) {
        if (view instanceof Button) {
            ((Button) view).setText((click + ""));
            click++;
        }
    }

    private Runnable runnableBackgroundTasks = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    count++;
                    String msg = count < 1000 ? "working " : "done ";
                    updateStatus("1. forBackgroundTasks: " + msg + count);
                }
            });

        }
    };

    private Runnable runnableLightWeightBackgroundTasks = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    count1++;
                    String msg = count1 < 1000 ? "working " : "done ";
                    updateStatus1("2. forLightWeightBackgroundTasks: " + msg + count1);
                }
            });

        }
    };

    private Runnable runnableMainThreadTasks = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    count2++;
                    String msg = count2 < 1000 ? "working " : "done ";
                    updateStatus2("3. forMainThreadTasks: " + msg + count2);
                }
            });

        }
    };


    private DefaultExecutorSupplier.PriorityRunnable runnablePriorityHighBackgroundTasks = new DefaultExecutorSupplier.PriorityRunnable(DefaultExecutorSupplier.Priority.HIGH) {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    count3++;
                    String msg = count3 < 1000 ? "working " : "done ";
                    updateStatus3("4. forPriorityBackgroundTask: HIGH: " + msg + count3);
                }
            });

        }
    };

    private DefaultExecutorSupplier.PriorityRunnable runnablePriorityLowBackgroundTasks = new DefaultExecutorSupplier.PriorityRunnable(DefaultExecutorSupplier.Priority.LOW) {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    count3++;
                    String msg = count3 < 1000 ? "working " : "done ";
                    updateStatus3("4. forPriorityBackgroundTask: LOW: " + msg + count3);
                }
            });

        }
    };


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

    private void updateStatus3(String msg) {
        Log.d("TAG", msg);
        ((TextView) findViewById(R.id.text3)).setText(msg);
    }
}
