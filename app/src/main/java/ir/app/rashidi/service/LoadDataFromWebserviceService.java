package ir.app.rashidi.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

import ir.app.rashidi.MyApplication;
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
        getAllBook();
        return null;
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
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()){
                    for (Book book : response.body()){
                        dbHelper.insertBook(book);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.i("Error =>",t.getMessage());
            }
        });
    }
}
