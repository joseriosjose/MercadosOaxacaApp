package com.oaxaca.turismo.mercados;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.oaxaca.turismo.mercados.clases.GoogleService;
import com.oaxaca.turismo.mercados.clases.Mercado;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity {
    static JSONObject listam,urlimg;
    static ArrayList<Mercado> mercad = new ArrayList<Mercado>();
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    SharedPreferences mPref;
    SharedPreferences.Editor medit;
    Double latitude,longitude;
    Geocoder geocoder;
    Location location2 = new Location("localizacion 2");

    Location location1 = new Location("localizacion 1");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mCardAdapter = new CardPagerAdapter();
        crearCartasMercados();


        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
        mCardShadowTransformer.enableScaling(true);
        geocoder = new Geocoder(this, Locale.getDefault());
        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        medit = mPref.edit();


        Button button = (Button) findViewById(R.id.btntutorial);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),tutorialActivity.class);
                startActivity(i);

            }
        });

        fn_permission();
         activar();


    }

    private void activar(){
        if (boolean_permission) {

            if (mPref.getString("service", "").matches("")) {
                medit.putString("service", "service").commit();

                Intent intent = new Intent(getApplicationContext(), GoogleService.class);
                startService(intent);

            } else {
                Toast.makeText(getApplicationContext(), "el servicio ya esta corriendo", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Toast.makeText(getApplicationContext(), "Por favor habilita el gps", Toast.LENGTH_SHORT).show();

        }
    }
    private void crearCartasMercados() {
        JSONObject objJson = listam;
        JSONObject objJson2 = urlimg;
        try{
            String urlimg="";
            JSONArray listaJson = objJson.optJSONArray("mercados");
            for(int i=0; i< listaJson.length(); i++) {
                JSONObject obj_dato = listaJson.getJSONObject(i);
                int id_m = obj_dato.getInt("idMercado");
                String nombre = obj_dato.getString("nombre");
                double lati = obj_dato.getDouble("latitud");
                double longi = obj_dato.getDouble("longitud");
                JSONArray listaJson2 = objJson2.optJSONArray("imagenes");
                for(int j=0;j<listaJson2.length();j++){
                    urlimg="";
                    JSONObject obj_dato2 = listaJson2.getJSONObject(j);
                    int id_me = obj_dato2.getInt("idMercado");
                    if(id_m==id_me){
                        urlimg =MainActivity.base_url+obj_dato2.getString("imagen");
                        break;
                    }
                }
                mCardAdapter.addCardItem(new CardItem(nombre,id_m,urlimg,lati,longi));
            }

        }catch (Exception ex){
            Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MenuActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))) {


            } else {
                ActivityCompat.requestPermissions(MenuActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;
            Toast.makeText(getApplicationContext(), "el servicio ya esta", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Please danos permisisos", Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            latitude = Double.valueOf(intent.getStringExtra("latutide"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);

               /* tv_area.setText(addresses.get(0).getAdminArea());
                tv_locality.setText(stateName);
                tv_address.setText(countryName);
            */


            } catch (IOException e1) {
                e1.printStackTrace();
            }


            //tv_latitude.setText(latitude+"");
            //tv_longitude.setText(longitude+"");
            location1.setLatitude(latitude);
            location1.setLongitude(longitude);
            double distance = location1.distanceTo(location2);
            /*if(distance<80 && distance>10){
                cerquita.setText(distance+" estas cerca");
            }else{
                cerquita.setText(distance+" estas lejos");
            }
            tv_address.getText();*/


        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(GoogleService.str_receiver));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}
