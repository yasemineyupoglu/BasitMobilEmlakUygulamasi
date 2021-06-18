package com.example.dev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RecordListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Model> list;
    RecordListAdapter adapter = null;
    ImageView imageViewIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Kayıt Listesi");

        listView=findViewById(R.id.listView);
        list=new ArrayList<>();
        adapter=new RecordListAdapter(this, R.layout.row, list);
        listView.setAdapter(adapter);

        Cursor cursor=HomeActivity.veritabaniEv.getData("SELECT * FROM kayit");
        list.clear();
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String ad=cursor.getString(1);
            String adres=cursor.getString(2);
            String konum=cursor.getString(3);
            byte[] image= cursor.getBlob(4);
            list.add(new Model(id, ad, adres,konum,image));
        }
        adapter.notifyDataSetChanged();
        if(list.size()==0){
            Toast.makeText(this, "Kayıt bulunamadı...", Toast.LENGTH_SHORT).show();
        }
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int i, long id) {
                CharSequence[] items={"Güncelle", "Sil"};
                AlertDialog.Builder dialog= new AlertDialog.Builder(RecordListActivity.this);
                dialog.setTitle("Bir eylem seçiniz");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            //güncelleme
                            Cursor c=HomeActivity.veritabaniEv.getData("SELECT id FROM kayit");
                            ArrayList<Integer> arrID= new ArrayList<Integer>();
                            while(c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                             showDialogGuncel(RecordListActivity.this, arrID.get(i));
                        }
                        if(which==1){
                            //silme
                            Cursor c=HomeActivity.veritabaniEv.getData("SELECT id FROM kayit");
                            ArrayList<Integer> arrID= new ArrayList<Integer>();
                            while(c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }

                            showDialogSil(arrID.get(i));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

    }

    private void showDialogSil(final int idRecord) {
        AlertDialog.Builder dialogdelete = new AlertDialog.Builder(RecordListActivity.this);
        dialogdelete.setTitle("UYARI");
        dialogdelete.setMessage("Silmek istediğinize emin misiniz??");
        dialogdelete.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    HomeActivity.veritabaniEv.deleteData(idRecord);
                    Toast.makeText(RecordListActivity.this, "SİLİNDİ..", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.e("HATA",e.getMessage());
                }
                updateRecordList();
            }
        });

        dialogdelete.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialogdelete.show();
    }

    private void showDialogGuncel(final Activity activity, final int position){
        final Dialog dialog=new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("GÜNCELLE");

        imageViewIcon=dialog.findViewById(R.id.imageViewRecord);
        final EditText editAd=dialog.findViewById(R.id.editAd);
        final EditText editAdres=dialog.findViewById(R.id.editAdres);
        final EditText editKonum=dialog.findViewById(R.id.editKonum);
        Button btnGuncel=dialog.findViewById(R.id.btnGuncel);


        Cursor cursor=HomeActivity.veritabaniEv.getData("SELECT * FROM kayit WHERE id="+position);
        list.clear();
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String ad=cursor.getString(1);
            editAd.setText(ad);
            String adres=cursor.getString(2);
            editAdres.setText(adres);
            String konum=cursor.getString(3);
            editKonum.setText(konum);
            byte[] image= cursor.getBlob(4);

            imageViewIcon.setImageBitmap(BitmapFactory.decodeByteArray(image,0, image.length));
            list.add(new Model(id, ad, adres,konum,image));
        }

        //diyaloğu kurduğum alan
        int width=(int)(activity.getResources().getDisplayMetrics().widthPixels*0.95);
        //yüksekliği ayarla
        int height=(int)(activity.getResources().getDisplayMetrics().heightPixels*0.7);
        dialog.getWindow().setLayout(width,height);
        dialog.show();

        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        RecordListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnGuncel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    HomeActivity.veritabaniEv.updateData(
                            editAd.getText().toString().trim(),
                            editAdres.getText().toString().trim(),
                            editKonum.getText().toString().trim(),
                            HomeActivity.imageViewToByte(imageViewIcon),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "GÜNCELLENDİ..", Toast.LENGTH_SHORT).show();
                }
                catch (Exception error){
                    Log.e("Güncelleme hatası",error.getMessage());
                }
                updateRecordList();

            }
        });
    }

    private void updateRecordList() {
        //veritabanından tüm verileri alacak olan yer
        Cursor cursor=HomeActivity.veritabaniEv.getData("SELECT * FROM kayit");
        list.clear();
        while (cursor.moveToNext()){
            int id= cursor.getInt(0);
            String ad=cursor.getString(1);
            String adres=cursor.getString(2);
            String konum=cursor.getString(3);
            byte[] image=cursor.getBlob(4);

            list.add(new Model(id,ad,adres,konum,image));
        }
        adapter.notifyDataSetChanged();
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
        if(requestCode==888){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 888);
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
        if(requestCode==888 && resultCode==RESULT_OK){
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
                imageViewIcon.setImageURI(resultUri);
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error=result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}