package com.haikuwind;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.haikuwind.dialogs.FinishListener;
import com.haikuwind.dialogs.ProgressTask;
import com.haikuwind.feed.HaikuWindData;
import com.haikuwind.feed.HttpRequest;

public class RegisterActivity extends Activity {

    @SuppressWarnings("unused")
    private static String TAG = HaikuWind.class.getSimpleName();

    private static final int ERROR_TRY_AGAIN_REGISTER = 0;
    private static final int SUGGEST_NETWORK_SETTINGS = 4;
    
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashscreen);
        progressDialog = new ProgressDialog(this);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        NetworkInfo info = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            showDialog(SUGGEST_NETWORK_SETTINGS);
        } else if(!HaikuWindData.getInstance().isRegistered()) {
            registerUser();
        } else {
            nextScreen();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
 
        if(progressDialog !=null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (id) {
        case (ERROR_TRY_AGAIN_REGISTER):
            builder.setMessage(R.string.oops)
                    .setCancelable(false)
                    .setNegativeButton(R.string.cancel,
                            new FinishListener(this))
                    .setPositiveButton(R.string.try_again,
                            new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    registerUser();
                                }
                            });
            break;

        case SUGGEST_NETWORK_SETTINGS:
            builder.setTitle(R.string.connection_failed)
                    .setMessage(R.string.suggest_network_settings)
                    .setCancelable(false)
                    .setNegativeButton(R.string.cancel,
                            new FinishListener(this))
                    .setPositiveButton(R.string.accept,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    startActivity(new Intent(
                                            Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            });
            break;
        default:
            return super.onCreateDialog(id);
        }
                
        return builder.create();
    }
    
    private void registerUser() {
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
       new RegisterTask(progressDialog).start();
    }
    
    private void nextScreen() {
        Intent i = new Intent(this, HaikuWind.class);
        startActivityForResult(i, 0);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    private class RegisterTask extends ProgressTask {
        public RegisterTask(ProgressDialog progressDialog) {
            super(progressDialog);
        }


        @Override
        protected void handleError() {
            Log.d(TAG, "unable to register user");
            // TODO how to show the same dialog that is open now?
            onCreateDialog(ERROR_TRY_AGAIN_REGISTER).show();
        }

        @Override
        protected void handleSuccess() {
            View v = findViewById(R.id.splashscreen);
            Log.d(TAG, "user registered");
            HaikuWindData.getInstance().setRegistered(true);
            nextScreen();
        }

        @Override
        protected void execute() throws Exception {
            Log.d(TAG, "registering user");
            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String userId = tManager.getDeviceId();

            HttpRequest.newUser(userId);

        }
    }

}
