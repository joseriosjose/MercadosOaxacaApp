package com.oaxaca.turismo.mercados.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.oaxaca.turismo.mercados.MainActivity;
import com.oaxaca.turismo.mercados.R;
import com.oaxaca.turismo.mercados.activitys.Activity_Local;
import com.oaxaca.turismo.mercados.activitys.profile;
import com.oaxaca.turismo.mercados.clases.Categoria;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 2/12/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private Categoria categoria;
    private Context mContext;
    ExpandableTextView hist;
    ExpandableTextView prod;
    CircleImageView logo;
    private TextView catego;
    private TextView nombre;


    public RecyclerViewAdapter(Context context, Categoria categoria) {
        mContext = context;
        this.categoria = categoria;
        hist = (ExpandableTextView) ((Activity) mContext).findViewById(R.id.historia);
        prod = (ExpandableTextView) ((Activity) mContext).findViewById(R.id.productos);
        logo = (CircleImageView) ((Activity) mContext).findViewById(R.id.logo);
        catego = (TextView) ((Activity) mContext).findViewById(R.id.categoria);
        nombre = (TextView) ((Activity) mContext).findViewById(R.id.nombreLocal);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String img = categoria.getLocales().get(position).getImageUrl();
        if(img.contains("null") || img == null){

            holder.image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).asBitmap().load(img).into(holder.image);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openLocal(position);

                }catch (Exception e){
                    Log.d(TAG, e.toString());
                }
            }
        });
    }
    public void openLocal(int position){
        hist.setText(categoria.getLocales().get(position).getHistoria()+"");
        prod.setText(categoria.getLocales().get(position).getProductos()+"");
        final String img = categoria.getLocales().get(position).getImageUrl();
        final String nomb = categoria.getLocales().get(position).getNombre()+"";
        nombre.setText(nomb);
        catego.setText(categoria.getNombre()+"");
        if(img.contains("null") || img == null){
            logo.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mContext).asBitmap().load(img).into(logo);
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, profile.class);
                    i.putExtra("url", img);
                    i.putExtra("name", nomb);
                    mContext.startActivity(i);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return categoria.getLocales().size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.image_view);
        }
    }
}
