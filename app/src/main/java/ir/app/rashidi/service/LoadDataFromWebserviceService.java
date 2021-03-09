package ir.app.rashidi.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ir.app.rashidi.MyApplication;
import ir.app.rashidi.activity.MainActivity;
import ir.app.rashidi.activity.SplashActivity;
import ir.app.rashidi.data.local.DbHelper;
import ir.app.rashidi.data.remote.RetrofitInstance;
import ir.app.rashidi.data.remote.Webservice;
import ir.app.rashidi.entity.Book;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadDataFromWebserviceService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("juyfhf","jyfgjhfgjhfgvjh");
        getAllBook();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getAllBook(){
        DbHelper dbHelper = new DbHelper(MyApplication.getContext());
        Webservice webservice = RetrofitInstance.getInstance().getWebservice();
        Call<List<Book>> call = webservice.getAllBook();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NotNull Call<List<Book>> call, @NotNull Response<List<Book>> response) {
                Log.i("Response =>",response.toString());
                if (response.isSuccessful()){
                    for (Book book : response.body()){
                        dbHelper.insertBook(book);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Book>> call, @NotNull Throwable t) {
                Log.i("Error =>",t.getMessage());
            }
        });
    }
}
