package com.haikuwind.dialogs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public abstract class ProgressTask extends Thread {
    private final static int SUCCESS = 0;
    private final static int ERROR = 1;

    private Handler handler = new Handler() {
        
        @Override
        public void handleMessage(Message msg) {
            if(SUCCESS==msg.what) {
                handleSuccess();
            } else {
                handleError();
            }
            if(progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };
    
    private final ProgressDialog progressDialog;

    public ProgressTask(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }


    public void run() {
        Looper.prepare();

        try {
            execute();
            handler.sendEmptyMessage(SUCCESS);
        } catch (Exception e) {
            handler.sendEmptyMessage(ERROR);
        }
     }


    protected abstract void handleError();

    protected abstract void handleSuccess();

    protected abstract void execute() throws Exception;
}