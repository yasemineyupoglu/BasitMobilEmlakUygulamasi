package com.example.dev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button giris, kayit;
    Veritabani veritabani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        giris=findViewById(R.id.giris);
        kayit=findViewById(R.id.kayit);
        veritabani=new Veritabani(this);

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameValue= username.getText().toString();
                String passwordValue= password.getText().toString();

                if(veritabani.isLoginValid(usernameValue,passwordValue)){
                    Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Giriş Başarılı!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Geçersiz Kullanıcı adı veya Şifre", Toast.LENGTH_SHORT).show();
                }
            }
        });

        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}