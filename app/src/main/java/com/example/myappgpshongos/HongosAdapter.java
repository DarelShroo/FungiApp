package com.example.myappgpshongos;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HongosAdapter extends RecyclerView.Adapter<HongosAdapter.ViewHolder> implements View.OnClickListener {
    private List<Hongo> mData;
    private LayoutInflater mInflater;
    private Context context;
    private View.OnClickListener listener;

    public HongosAdapter(List<Hongo> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    public HongosAdapter() {

    }

    @Override
    public int getItemCount(){
        return mData.size();
    }

    @Override
    public HongosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.list_element_hongo, null);
        view.setOnClickListener(this);
        return new HongosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HongosAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<Hongo> items){
        mData = items;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class  ViewHolder extends  RecyclerView.ViewHolder {
         public ImageView iconImage, iconImage2;
        TextView nombrecientifico, nombrecomun;
        Bitmap bmp;
        public ViewHolder(View itemView){
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImageView);
            iconImage2 = itemView.findViewById(R.id.iconImageView2);
            nombrecientifico = itemView.findViewById(R.id.nombrecientificoTextView);
            nombrecomun = itemView.findViewById(R.id.nombrecomunTextView);
        }
        void bindData(final Hongo item){
            bmp = StringToBitMap(item.getImagen());
            iconImage2.setImageBitmap(bmp);
            if(item.comestible.equals("true")){
                iconImage.setImageResource(R.drawable.ic_comestible);
            }else {
                iconImage.setImageResource(R.drawable.ic_toxica);
            }
            nombrecientifico.setText(item.getNombrecientifico());
            nombrecomun.setText(item.getNombrecomun());
        }
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
