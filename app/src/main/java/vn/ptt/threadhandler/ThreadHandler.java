package vn.ptt.threadhandler;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class ThreadHandler extends AppCompatActivity implements View.OnClickListener {
    Handler mHandler;
    ProgressDialog mProgressBar;
    Button button1, button2, button3;

    int i1, i2,i3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_handler);

         button1 = findViewById(R.id.button1);
         button2 = findViewById(R.id.button2);
         button3 = findViewById(R.id.button3);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                startThread();
                break;
            case R.id.button2:
                i2++;
                button2.setText(i2+"");
                break;
            case R.id.button3:
                button3.setText(i3+"");
                break;
        }
    }

    private void startThread() {
        mHandler=new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    final int currentProgressCount = i;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Update the value background thread to UI thread
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            button1.setText(currentProgressCount+"");
                        }
                    });
                }
            }
        }).start();
    }
}
