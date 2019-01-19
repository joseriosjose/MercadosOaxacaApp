package com.oaxaca.turismo.mercados;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.oaxaca.turismo.mercados.conexion.Peticiones;

import java.util.ArrayList;
import java.util.List;
import static com.oaxaca.turismo.mercados.MainActivity.base_url;
import static com.oaxaca.turismo.mercados.MainActivity.llave;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;
    private static Context estees ;
    public static boolean bandera=true;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        estees=container.getContext();
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }


        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(CardItem item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        titleTextView.setText(item.getTitle());
        final ImageButton button = (ImageButton) view.findViewById(R.id.ver);

        String im = item.getUrlimagen();

        if(im!= null)
            Glide.with(estees).asBitmap().load(im).into(button);

        final int nnumeroid = item.getIdmercado();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(bandera){
                    bandera=false;
                    hacerprti(nnumeroid);
                }
            }
        });



    }

    public static void hacerprti(final int ihm){
        final ProgressDialog progress =new ProgressDialog(estees);
        progress.setMessage("Descargando Informacion");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {

                Peticiones peticion2 = new Peticiones(estees,base_url+"Mercado/mercadoById/"+llave+"/"+ihm);
                Peticiones peticion3 = new Peticiones(estees,base_url+"Mercado/imgFromMercado/"+llave+"/"+ihm);
                Peticiones peticion4 = new Peticiones(estees,base_url+"Mercado/localesDelMercado/"+llave+"/"+ihm);

                while (!peticion2.getBanderita() ||
                        !peticion3.getBanderita() ||
                        !peticion4.getBanderita()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Toast.makeText(estees,e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
                principal.infomer=peticion2.getJSON();
                principal.galeri=peticion3.getJSON();
                principal.giros = peticion4.getJSON();

                Intent intent = null;
                if(principal.lista!=null && principal.infomer!=null && principal.galeri!=null && principal.giros!=null)
                {
                    bandera=true;
                    progress.cancel();
                    intent = new Intent(estees, principal.class);
                    estees.startActivity(intent);

                }
            }
        });
        hilo.start();
    }

}