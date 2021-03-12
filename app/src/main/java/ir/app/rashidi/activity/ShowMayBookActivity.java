package ir.app.rashidi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import ir.app.rashidi.R;

public class ShowMayBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_may_book);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String storage = extras.getString("storage");
            String name = extras.getString("name");
            int size = extras.getInt("size");

        }
    }

}