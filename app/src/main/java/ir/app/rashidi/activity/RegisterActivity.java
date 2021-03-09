package ir.app.rashidi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ir.app.rashidi.R;
import ir.app.rashidi.data.local.DbHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText Name,LastName,Email,passwordToggle ,passwordToggleComfird ;
    Button cancle ,Register ;
    SharedPreferences Preferences;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name = findViewById(R.id.edtName);
        LastName = findViewById(R.id.edtFamily);
        Email = findViewById(R.id.edtEmail);
        passwordToggle=findViewById(R.id.passwordToggle);
        passwordToggleComfird=findViewById(R.id.passwordToggleComfird);
        cancle = findViewById(R.id.cancle);
        Register = findViewById(R.id.Register);
        Preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        dbHelper = new DbHelper(this);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Name.getText().toString();
                String family = LastName.getText().toString();
                String email = Email.getText().toString();
                String password = passwordToggle.getText().toString();
                String confirmPass = passwordToggleComfird.getText().toString();


                if (name.length() > 0
                        && family.length() > 0
                        && email.length() > 0
                        && password.length() > 0
                        && confirmPass.length() > 0 ) {
                    if (password.equals(confirmPass)){
                        if (dbHelper.insertUser(name,family,email,password)){
                            SharedPreferences.Editor editor = Preferences.edit();
                            editor.putBoolean("rememberMe",true);
                            editor.apply();
                            Toast.makeText(RegisterActivity.this, "ثبت نام با موفقیت انجام شد", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(RegisterActivity.this, "در ثبت نام خطایی رخ داده است", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "رمز عبور با تکرار آن برابر نیست", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "پر کردن فیلد ضروری است!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}