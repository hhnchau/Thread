package vn.ptt.threadhandler;

import android.os.Looper;
import android.os.Message;

public class MyThread extends Thread{

    private int cnt;
    private boolean running;
    MainActivity.MyHandler mainHandler;

    public MyThread(MainActivity.MyHandler mainHandler) {
        super();
        this.mainHandler = mainHandler;
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    public void msgToThread(String msgin){
        String msgout = new StringBuilder(msgin).reverse().toString();
        mainHandler.sendMessage(
                Message.obtain(mainHandler,
                        MainActivity.MyHandler.UPDATE_MSG, msgout));
    }

    @Override
    public void run() {
        cnt = 0;
        running = true;

        String prompt;
        if(Looper.myLooper() == Looper.getMainLooper()){
            prompt = "myThread run in UI Thread";
        }else{
            prompt = "myThread run in NOT UI Thread";
        }
        mainHandler.sendMessage(
                Message.obtain(mainHandler,
                        MainActivity.MyHandler.UPDATE_MSG, prompt));

        while (running){
            try {
                Thread.sleep(1000);
                mainHandler.sendMessage(
                        Message.obtain(mainHandler,
                                MainActivity.MyHandler.UPDATE_CNT, cnt));

                cnt++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
