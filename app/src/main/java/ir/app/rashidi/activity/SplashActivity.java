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

import com.google.gson.Gson;

import java.util.List;

import ir.app.rashidi.MyApplication;
import ir.app.rashidi.R;
import ir.app.rashidi.data.local.DbHelper;
import ir.app.rashidi.data.remote.RetrofitInstance;
import ir.app.rashidi.data.remote.Webservice;
import ir.app.rashidi.entity.Book;
import ir.app.rashidi.service.LoadDataFromWebserviceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private void getAllBook(){
        DbHelper dbHelper = new DbHelper(MyApplication.getContext());
        Webservice webservice = RetrofitInstance.getInstance().getWebservice();
        Call<List<Book>> call = webservice.getAllBook();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()){
                    if (dbHelper.getAllBook().size() >= response.body().size()){
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, LoadDataFromWebserviceService.class);
                                startService(intent);

                                Intent intent1 = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                        }, 5000);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e("Error =>",t.getMessage());
            }
        });
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
                getAllBook();
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