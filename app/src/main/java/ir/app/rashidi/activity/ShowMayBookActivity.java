package ir.app.rashidi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ir.app.rashidi.R;

public class ShowMayBookActivity extends AppCompatActivity {
    private TextView txtStorage,txtName,txtSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_may_book);
        txtStorage = findViewById(R.id.storage);
        txtName = findViewById(R.id.Name);
        txtSize = findViewById(R.id.size);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String storage = extras.getString("storage");
            String name = extras.getString("name");
            long size = extras.getLong("size");

            txtStorage.setText(storage);
            txtName.setText(name);
            txtSize.setText(String.valueOf(size) + "MB");

        }
    }

}