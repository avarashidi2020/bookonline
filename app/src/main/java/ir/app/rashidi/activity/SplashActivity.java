package ir.app.rashidi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;

import ir.app.rashidi.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        registerNetworkBroadcast();
    }

    protected void registerNetworkBroadcast() {
        registerReceiver(networkChange, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    protected void unRegisterNetwork() {
        try {
            unregisterReceiver(networkChange);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterNetwork();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterNetwork();
    }

    private final BroadcastReceiver networkChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.alart_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

            Button button = dialog.findViewById(R.id.btn);
            button.setOnClickListener(view -> recreate());

            if (isOnline(context)){
                Log.e("main", "connect");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 5000);
            }else{
                Log.e("main", "not connect");
                dialog.show();
            }
        }

        private boolean isOnline(Context context){
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    };
}