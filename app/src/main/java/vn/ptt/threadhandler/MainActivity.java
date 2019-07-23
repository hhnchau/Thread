package vn.ptt.threadhandler;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btnStart, btnStop, btnSend;
    EditText editTextMsgToSend;
    TextView textViewCntReceived, textViewMsgReceived;

    MyThread myThread;
    MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button)findViewById(R.id.startthread);
        btnStop = (Button)findViewById(R.id.stopthread);
        btnSend = (Button)findViewById(R.id.send);
        editTextMsgToSend = (EditText)findViewById(R.id.msgtosend);
        textViewCntReceived = (TextView)findViewById(R.id.cntreceived);
        textViewMsgReceived = (TextView)findViewById(R.id.msgreceived);

        myHandler = new MyHandler(this);

        btnStart.setOnClickListener(btnStartOnClickListener);
        btnStop.setOnClickListener(btnStopOnClickListener);
        btnSend.setOnClickListener(btnSendOnClickListener);
    }

    View.OnClickListener btnStartOnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(myThread != null){
                        myThread.setRunning(false);
                    }
                    myThread = new MyThread(myHandler);
                    myThread.start();
                }
            };

    View.OnClickListener btnStopOnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(myThread != null){
                        myThread.setRunning(false);
                        myThread = null;
                    }
                }
            };

    View.OnClickListener btnSendOnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(myThread != null){
                        myThread.msgToThread(editTextMsgToSend.getText().toString());
                    }
                }
            };


    private void updateCnt(int cnt){
        textViewCntReceived.setText(String.valueOf(cnt));
    }

    private void updateMsg(String msg){
        textViewMsgReceived.setText(msg);
    }

    public static class MyHandler extends Handler {

        public static final int UPDATE_CNT = 0;
        public static final int UPDATE_MSG = 1;
        private MainActivity parent;

        public MyHandler(MainActivity parent) {
            super();
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case UPDATE_CNT:
                    int c = (int)msg.obj;
                    parent.updateCnt(c);
                    break;
                case UPDATE_MSG:
                    String m = (String)msg.obj;
                    parent.updateMsg(m);
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
}
