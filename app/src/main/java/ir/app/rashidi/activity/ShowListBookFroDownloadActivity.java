package ir.app.rashidi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ir.app.rashidi.R;
import ir.app.rashidi.adapter.BookListAdapter;
import ir.app.rashidi.adapter.BookListShowDownloadAdapter;
import ir.app.rashidi.data.local.DbHelper;
import ir.app.rashidi.entity.Book;
import ir.app.rashidi.network.BookDownload;

public class ShowListBookFroDownloadActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BookListShowDownloadAdapter adapter ;
    DbHelper dbHelper;
    List<Book> books = new ArrayList<>();

    private Book book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_book_fro_download);

        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.bookList);
        setSupportActionBar(toolbar);
        dbHelper = new DbHelper(this);
        books = dbHelper.getAllBook();
        adapter = new BookListShowDownloadAdapter(this,books);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        adapter.setSetOnClick((view, book) -> {
            this.book = book;
            if (Build.VERSION.SDK_INT >= 23){
                int result = ContextCompat.checkSelfPermission(ShowListBookFroDownloadActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (result == PackageManager.PERMISSION_GRANTED){
                    new BookDownload(ShowListBookFroDownloadActivity.this).execute(book.getFileUrl());
                }else{
                    ActivityCompat.requestPermissions(ShowListBookFroDownloadActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }
            }else {
                new BookDownload(ShowListBookFroDownloadActivity.this).execute(book.getFileUrl());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                new BookDownload(ShowListBookFroDownloadActivity.this).execute(book.getFileUrl());
            }
        }
    }
}