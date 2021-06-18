package com.example.dev;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Model> recordList;

    public RecordListAdapter(Context context, int layout, ArrayList<Model> recordList) {
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class viewHolder{
        ImageView imageView;
        TextView txtAd, txtAdres, txtKonum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row=null;
        viewHolder holder=new viewHolder();

        if(row==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(layout, null);
            holder.txtAd= row.findViewById(R.id.txtAd);
            holder.txtAdres= row.findViewById(R.id.txtAdres);
            holder.txtKonum= row.findViewById(R.id.txtKonum);
            holder.imageView= row.findViewById(R.id.imgIcon);
            row.setTag(holder);
        }
        else{
            holder=(viewHolder)row.getTag();
        }

        Model model= recordList.get(position);
        holder.txtAd.setText(model.getAd());
        holder.txtAdres.setText(model.getAdres());
        holder.txtKonum.setText(model.getKonum());

        byte[] recordImage=model.getImage();
        Bitmap bitmap= BitmapFactory.decodeByteArray(recordImage,0,recordImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
