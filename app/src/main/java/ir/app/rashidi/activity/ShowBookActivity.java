package ir.app.rashidi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ir.app.rashidi.R;
import ir.app.rashidi.data.local.DbHelper;
import ir.app.rashidi.entity.Book;

public class ShowBookActivity extends AppCompatActivity {
    private Book book;
    private DbHelper dbHelper;

    private ImageView imageView;
    private TextView textView;
    private RatingBar ratingBar;
    private Button sendSms;
    private Button call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);

        Toolbar toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.bookShowImage);
        textView = findViewById(R.id.bookShowTitle);
        ratingBar = findViewById(R.id.Rating_Bar);
        sendSms = findViewById(R.id.sendSms);
        call = findViewById(R.id.Call);
        dbHelper = new DbHelper(this);


        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> finish());
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            book = (Book) bundle.get("book");

            textView.setText(book.getName());
            Picasso.get().load(book.getImage()).into(imageView);

            ratingBar.setRating(book.getScore());
        }


        ratingBar.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            try {
                float score = ratingBar.getRating();
                dbHelper.setScoreBook(book.getId(), String.valueOf(score));

                Toast.makeText(this, "باموفقیت ثبت شد.", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "خطا در ثبت امتیاز", Toast.LENGTH_SHORT).show();
            }
        });

        sendSms.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= 23){
                int result = ContextCompat.checkSelfPermission(ShowBookActivity.this,
                        Manifest.permission.SEND_SMS);
                if (result == PackageManager.PERMISSION_GRANTED){
                    sendSms();
                }else{
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS,},0);
                }
            }else {
                sendSms();
            }
        });

        call.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= 23){
                int result = ContextCompat.checkSelfPermission(ShowBookActivity.this,
                        Manifest.permission.CALL_PHONE);
                if (result == PackageManager.PERMISSION_GRANTED){
                    callPhone();
                }else{
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
                }
            }else {
                callPhone();
            }
        });
    }

    private void sendSms(){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(book.getNasherTell(), null, "send sms", null, null);
    }

    private void callPhone(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + book.getNasherTell()));
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                sendSms();
            }
        }else if(requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                callPhone();
            }
        }
    }
}