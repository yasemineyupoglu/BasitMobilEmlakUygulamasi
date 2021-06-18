package com.example.dev;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class VeritabaniEv extends SQLiteOpenHelper {

    VeritabaniEv(Context context,
                 String name,
                 SQLiteDatabase.CursorFactory factory,
                 int version){
        super(context, name, factory, version);
    }

    public void queryData(String sorgu){
        SQLiteDatabase databasem= getWritableDatabase();
        databasem.execSQL(sorgu);
    }

    //veri ekleme yeri
    public void insertData(String ad, String adres, String konum, byte[] image){
        SQLiteDatabase databasem= getWritableDatabase();
        String sorgu= "INSERT INTO kayit VALUES(NULL, ?, ?, ?, ?)"; //veri ekleme sorgusu
        SQLiteStatement statement1=databasem.compileStatement(sorgu);
        statement1.clearBindings();

        statement1.bindString(1,ad);
        statement1.bindString(2,adres);
        statement1.bindString(3,konum);
        statement1.bindBlob(4,image);

        statement1.executeInsert();

    }

    //güncelleme kısmı
    public void updateData(String ad, String adres, String konum, byte[] image, int id){
        SQLiteDatabase databasem= getWritableDatabase();
        String sorgu= "UPDATE kayit SET ad=?, adres=?, konum=?, image=? WHERE id=? ";  //güncelleme sorgusu
        SQLiteStatement statement1=databasem.compileStatement(sorgu);

        statement1.bindString(1,ad);
        statement1.bindString(2,adres);
        statement1.bindString(3,konum);
        statement1.bindBlob(4,image);
        statement1.bindDouble(5, (double)id);
        statement1.execute();
        databasem.close();
    }

    //silme kısmı
    public void deleteData(int id){
        SQLiteDatabase databasem= getWritableDatabase();
        String sorgu= "DELETE FROM kayit WHERE id=?";  //silme sorgusu
        SQLiteStatement statement1=databasem.compileStatement(sorgu);
        statement1.clearBindings();
        statement1.bindDouble(1, (double)id);

        statement1.execute();
        databasem.close();
    }

    public Cursor getData(String sorgu){
        SQLiteDatabase databasem= getReadableDatabase();
        return databasem.rawQuery(sorgu,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
