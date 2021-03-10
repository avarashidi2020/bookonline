package ir.app.rashidi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.app.rashidi.R;
import ir.app.rashidi.adapter.BookListAdapter;
import ir.app.rashidi.data.local.DbHelper;
import ir.app.rashidi.entity.Book;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;
    RecyclerView recyclerView;
    BookListAdapter adapter ;
    DbHelper dbHelper;
    List<Book> books = new ArrayList<>();
    RadioGroup radioGroup;
    GridLayoutManager layoutManager;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.bookList);
        radioGroup = findViewById(R.id.radioGroup);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        dbHelper = new DbHelper(this);
        books = dbHelper.getAllBook();
        adapter = new BookListAdapter(this,books);
         layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radioBtnGride){
                layoutManager.setSpanCount(2);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
            }else if (i == R.id.radioBtnList){
                layoutManager.setSpanCount(1);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void searchBooks(String text){
        if (dbHelper.searchBook(text).size() > 0){
            adapter.setBooks(dbHelper.searchBook(text));
        }else {
            Toast.makeText(this, "کتابی یافت نشد", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("rememberMe",false);
            editor.apply();
            Toast.makeText(this, "با موفقیت خارج شدید", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}