package com.example.dev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.icu.lang.UCharacterEnums;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText username, password, email, telefon;
    Button kayit, iptal;
    Veritabani veritabani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        telefon=findViewById(R.id.telefon);
        kayit=findViewById(R.id.kayit);
        iptal=findViewById(R.id.iptal);

        veritabani=new Veritabani(this);

        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameValue= username.getText().toString();
                String passwordValue= password.getText().toString();
                String emailValue= email.getText().toString();
                String telefonValue= telefon.getText().toString();

                if(usernameValue.length()>1){
                    ContentValues contentValues=new ContentValues();
                    contentValues.put("username", usernameValue);
                    contentValues.put("password", passwordValue);
                    contentValues.put("email", emailValue);
                    contentValues.put("telefon", telefonValue);

                    veritabani.insertUser(contentValues);
                    Toast.makeText(RegisterActivity.this, "Kullanıcı Kayıt Oldu", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Değerleri Girin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}