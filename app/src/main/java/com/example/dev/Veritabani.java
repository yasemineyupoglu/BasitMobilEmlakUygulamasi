package com.example.dev;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Veritabani extends SQLiteOpenHelper {

    static String name="database";
    static int version=1;

    String createTableUser= "CREATE TABLE if not exists \"user\" (\n" +
            "\t\"id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t\"username\"\tINTEGER,\n" +
            "\t\"password\"\tTEXT,\n" +
            "\t\"email\"\tTEXT,\n" +
            "\t\"telefon\"\tTEXT\n" +
            ")";

    public Veritabani(@Nullable Context context) {
        super(context, name, null, version);
        getWritableDatabase().execSQL(createTableUser);
    }

    public void insertUser(ContentValues contentValues){
        getWritableDatabase().insert("user","", contentValues);
    }

    public boolean isLoginValid(String username, String password){
        String sql= "Select count(*) from user where username='"+username+"' and password='"+password+"'";
        SQLiteStatement statement=getReadableDatabase().compileStatement(sql);
        long l= statement.simpleQueryForLong();
        statement.close();

        if(l==1){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
