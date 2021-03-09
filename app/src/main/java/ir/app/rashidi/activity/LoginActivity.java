package ir.app.rashidi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import ir.app.rashidi.R;
import ir.app.rashidi.data.local.DbHelper;

public class LoginActivity extends AppCompatActivity {
    EditText emailEdt,passwordEdt;
    Button login, Register;
    CheckBox rememberMe;
    SharedPreferences Preferences;
    DbHelper dbHelper;

    boolean remeber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdt = findViewById(R.id.edtEmail);
        login = findViewById(R.id.login);
        passwordEdt = findViewById(R.id.password);
        rememberMe = findViewById(R.id.RememberMe);
        Register = findViewById(R.id.Register);

        Preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        dbHelper = new DbHelper(this);

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                remeber = b;
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEdt.getText().toString().trim();
                String password = passwordEdt.getText().toString().trim();

                if (dbHelper.getUser(email,password) != null){
                    SharedPreferences.Editor editor = Preferences.edit();
                    editor.putBoolean("rememberMe",remeber);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "نام کاربری یا رمز عبور اشتباه است", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}