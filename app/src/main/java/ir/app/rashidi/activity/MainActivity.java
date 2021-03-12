package ir.app.rashidi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import ir.app.rashidi.R;
import ir.app.rashidi.adapter.BookListAdapter;
import ir.app.rashidi.data.local.DbHelper;
import ir.app.rashidi.entity.Book;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

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
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.bookList);
        radioGroup = findViewById(R.id.radioGroup);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        dbHelper = new DbHelper(this);
        books = dbHelper.getAllBook();
        adapter = new BookListAdapter(this, books);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radioBtnGride) {
                layoutManager.setSpanCount(2);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
            } else if (i == R.id.radioBtnList) {
                layoutManager.setSpanCount(1);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
            }
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            Intent intent = null;
            switch (menuItem.getItemId()) {
                case R.id.home:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    intent = new Intent(MainActivity.this,ShowListBookFroDownloadActivity.class);
                    startActivity(intent);
                    break;
                case R.id.setting:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    intent = new Intent(MainActivity.this,SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.call:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    intent = new Intent(MainActivity.this,ContactActivity.class);
                    startActivity(intent);
                    break;
                case R.id.about:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    intent = new Intent(MainActivity.this,AboutActivity.class);
                    startActivity(intent);
                    break;
                case R.id.logout:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("rememberMe",false);
                    editor.apply();
                    Toast.makeText(this, "با موفقیت خارج شدید", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(this,LoginActivity.class));
                    finishAffinity();
                    break;
                default:
            }
            return true;
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
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
        getMenuInflater().inflate(R.menu.search_menu, menu);

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
    public void onBackPressed() {
        super.onBackPressed();
    }
}