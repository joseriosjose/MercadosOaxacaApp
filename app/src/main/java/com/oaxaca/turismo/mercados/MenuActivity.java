package com.oaxaca.turismo.mercados;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.oaxaca.turismo.mercados.servicios.GoogleService;
import com.oaxaca.turismo.mercados.clases.Mercado;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity {
    static JSONObject listam,urlimg;
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    SharedPreferences mPref;
    SharedPreferences.Editor medit;
    Geocoder geocoder;

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
                Intent intent = new Intent(getApplicationContext(), GoogleService.class);
                startService(intent);
                //Toast.makeText(getApplicationContext(), "el servicio ya esta corriendo", Toast.LENGTH_SHORT).show();
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
            //Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
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
            //Toast.makeText(getApplicationContext(), "el servicio ya esta", Toast.LENGTH_SHORT).show();
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
                  //  Toast.makeText(getApplicationContext(),R.string.peticion, Toast.LENGTH_LONG).show();

                }
            }
        }
    }

}
