package com.oaxaca.turismo.mercados.activitys;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.oaxaca.turismo.mercados.MainActivity;
import com.oaxaca.turismo.mercados.R;
import com.oaxaca.turismo.mercados.clases.Mercado;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.DataFormatException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.behindthemath.justifiedtextview.JustifiedTextView;

public class Head extends Fragment {
    private TextView name,address,horario,horaIndica;
    private CircleImageView logo;
    private Typeface font;
    private TextView history;
    private Mercado mercado;
    private ProgressDialog pDialog;

    private String imageProfile;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.head, container, false);
        name        = (TextView)view.findViewById(R.id.nameMarket);
        history    = (TextView) view.findViewById(R.id.history);
        address     = (TextView)view.findViewById(R.id.address);
        logo= (CircleImageView) view.findViewById(R.id.logo);
        horario = (TextView) view.findViewById(R.id.hora);
        horaIndica = (TextView) view.findViewById(R.id.horaIndcador);
        refresh();

        imageProfile = MainActivity.getBase_url()+mercado.getImag();
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), profile.class);
                i.putExtra("url", MainActivity.getBase_url()+mercado.getImag());
                i.putExtra("name", mercado.getNombre()+"");
                startActivity(i);
            }
        });
        return view;
    }

    public void setMercado(Mercado m){
        this.mercado = m;
    }

    public void refresh() {
        name.setText(mercado.getNombre() + "");
        history.setText(mercado.getHistoria() + "");
        String acum = "";
        if(mercado.getDireccion()!=null && !mercado.getDireccion().equals("null")) {
            String[] dire = mercado.getDireccion().split(",");
            acum = "Calle " + dire[0] + ", Num." + dire[1] + " ,Col." + dire[2] + " ,CP." + dire[3];

        }
        address.setText(acum);
        horario.setText(mercado.getHoraA() + " - " +mercado.getHoraC());
        Glide.with(getContext()).load(MainActivity.getBase_url() + mercado.getImag()).into(logo);

        if((mercado.getHoraA()+"").contains(":") && (mercado.getHoraC()+"").contains(":")){
            String [] horaIni = mercado.getHoraA().split(":");
            String [] horaCie = mercado.getHoraC().split(":");

            int horaInicio = Integer.parseInt(horaIni[0]);
            int horaCierre = Integer.parseInt(horaCie[0]);

            //if(horaCierre<12) horaCierre += 12;

            Calendar calendario = new GregorianCalendar();
            int horaActual =calendario.get(Calendar.HOUR_OF_DAY);

            mercado.setHoraC(horaCierre+":"+horaCie[1]);


            if(horaActual >= horaInicio && horaActual< horaCierre){
                horaIndica.setText("Abierto");
                horaIndica.setTextColor(Color.parseColor("#01B51F"));
                horario.setText(mercado.getHoraA() + " - " +mercado.getHoraC());
            }else{
                horaIndica.setText("Cerrado");
                horaIndica.setTextColor(Color.parseColor("#ED0000"));
                horario.setText(mercado.getHoraA() + " - " +mercado.getHoraC());
            }
        }
    }
}

