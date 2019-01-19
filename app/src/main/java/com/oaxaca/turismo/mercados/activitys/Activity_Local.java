package com.oaxaca.turismo.mercados.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.oaxaca.turismo.mercados.R;
import com.oaxaca.turismo.mercados.adapter.RecyclerViewAdapter;
import com.oaxaca.turismo.mercados.clases.Categoria;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Local extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    static Categoria categoria;


    public static int c=0;

    private ExpandableTextView expTv1;
    private ExpandableTextView expTv2;
    private CircleImageView circleImageView;
    private TextView catego;
    private TextView nombre, nombreMercado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        circleImageView = (CircleImageView) findViewById(R.id.logo);
        expTv1 = (ExpandableTextView) findViewById(R.id.historia);
        expTv2 = (ExpandableTextView) findViewById(R.id.productos);
        catego = (TextView) findViewById(R.id.categoria);
        nombre = (TextView) findViewById(R.id.nombreLocal);
        nombreMercado = (TextView) findViewById(R.id.mercadoNombre);

        String nombre = getIntent().getExtras().getString("nombre")+"";
        nombreMercado.setText(nombre);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, categoria);
        recyclerView.setAdapter(adapter);




        RelativeLayout backLa = (RelativeLayout) findViewById(R.id.backButton);
        backLa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        refresh();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(), profile.class);
                i.putExtra("url",categoria.getLocales().get(c).getImageUrl());
                i.putExtra("name", categoria.getLocales().get(c).getNombre()+"");
                startActivity(i);
            }
        });

    }



    public void refresh() {
        String img =categoria.getLocales().get(c).getImageUrl();
        if(img.contains("null") || img == null){
            circleImageView.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(this).asBitmap().load(img).into(circleImageView);
        }
        expTv1.setText(categoria.getLocales().get(c).getHistoria() + "");
        expTv2.setText(categoria.getLocales().get(c).getProductos() + "");
        nombre.setText(categoria.getLocales().get(c).getNombre()+"");
        catego.setText(categoria.getNombre()+"");

    }

}
