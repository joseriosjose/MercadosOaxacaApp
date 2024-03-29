package com.oaxaca.turismo.mercados.activitys;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.oaxaca.turismo.mercados.MainActivity;
import com.oaxaca.turismo.mercados.R;
import com.oaxaca.turismo.mercados.clases.Mercado;
import java.util.Calendar;
import java.util.GregorianCalendar;
import de.hdodenhof.circleimageview.CircleImageView;

public class Head extends Fragment {
    private TextView name,address,horario,horaIndica;
    private CircleImageView logo;
    private TextView history;
    private Mercado mercado;

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

            acum = getString(R.string.Calle) + " "+ dire[0] +  ", "+
                    getString(R.string.Numero) +" "+ dire[1] + ", " +
                    getString(R.string.Colonia) + " "+ dire[2] + " ,"+
                    getString(R.string.CodigoPostal)+ " "+dire[3];

        }
        address.setText(acum);
        horario.setText(mercado.getHoraA() + " - " +mercado.getHoraC());
        Glide.with(getContext()).load(MainActivity.getBase_url() + mercado.getImag()).into(logo);

        if((mercado.getHoraA()+"").contains(":") && (mercado.getHoraC()+"").contains(":")){
            String [] horaIni = mercado.getHoraA().split(":");
            String [] horaCie = mercado.getHoraC().split(":");
            int horaInicio = Integer.parseInt(horaIni[0]);
            int horaCierre = Integer.parseInt(horaCie[0]);
            Calendar calendario = new GregorianCalendar();
            int horaActual =calendario.get(Calendar.HOUR_OF_DAY);
            mercado.setHoraC(horaCierre+":"+horaCie[1]);
            if(horaActual >= horaInicio && horaActual< horaCierre){
                horaIndica.setText(getString(R.string.abierto_label));
                horaIndica.setTextColor(Color.parseColor("#01B51F"));
                horario.setText(mercado.getHoraA() + " - " +mercado.getHoraC());
            }else{
                horaIndica.setText(getString(R.string.cerrado_label));
                horaIndica.setTextColor(Color.parseColor("#ED0000"));
                horario.setText(mercado.getHoraA() + " - " +mercado.getHoraC());
            }
        }
    }
}
