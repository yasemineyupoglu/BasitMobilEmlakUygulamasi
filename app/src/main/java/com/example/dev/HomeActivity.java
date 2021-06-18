package com.example.dev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.BitmapCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class HomeActivity extends AppCompatActivity {

    EditText EditEvAdi, EditEvAdres, EditEvKonum;
    Button BtnKaydet, BtnListele;
    ImageView imageView;

    final int REQUEST_CODE_GALLERY=999;
    public static VeritabaniEv veritabaniEv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Yeni Kayıt");


        EditEvAdi= findViewById(R.id.EditEvAdi);
        EditEvAdres= findViewById(R.id.EditEvAdres);
        EditEvKonum= findViewById(R.id.EditEvKonum);
        BtnKaydet= findViewById(R.id.BtnKaydet);
        BtnListele= findViewById(R.id.BtnListele);
        imageView= findViewById(R.id.imageView);

        //veritabanı oluşturma
        veritabaniEv= new VeritabaniEv(this,"databasem.db", null,1);
        //tablo oluşturma
        veritabaniEv.queryData( "CREATE TABLE IF NOT EXISTS kayit(id INTEGER PRIMARY KEY AUTOINCREMENT, ad TEXT, adres TEXT, konum TEXT, image BLOB)");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityCompat.requestPermissions(
                        HomeActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY
                );
            }
        });

        BtnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    veritabaniEv.insertData(
                            EditEvAdi.getText().toString().trim(),
                            EditEvAdres.getText().toString().trim(),
                            EditEvKonum.getText().toString().trim(),
                            imageViewToByte(imageView)
                    );
                    Toast.makeText(HomeActivity.this, "Başarılı bir şekilde eklendi", Toast.LENGTH_LONG).show();
                    //görünümleri sıfırlama
                    EditEvAdi.setText("");
                    EditEvAdres.setText("");
                    EditEvKonum.setText("");
                    imageView.setImageResource(R.drawable.addphoto);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        BtnListele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, RecordListActivity.class));

            }
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap=((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE_GALLERY){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            }
            else{
                Toast.makeText(this, "Fotoğrafa Erişim İzni Yok..", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE_GALLERY && resultCode==RESULT_OK){
            Uri imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)  //resim klavuz çizgilerini etkinleştirdi
            .setAspectRatio(1,1) //görüntü kare olacak
            .start(this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                Uri resultUri= result.getUri();
                //galeriden seçilen resmi ayarla
                imageView.setImageURI(resultUri);
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error=result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}