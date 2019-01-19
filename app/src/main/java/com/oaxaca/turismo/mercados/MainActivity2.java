package com.oaxaca.turismo.mercados;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.oaxaca.turismo.mercados.MenuActivity;
import com.oaxaca.turismo.mercados.conexion.Peticiones;
import com.oaxaca.turismo.mercados.principal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.oaxaca.turismo.mercados.MainActivity.base_url;
import static com.oaxaca.turismo.mercados.MainActivity.llave;


public class MainActivity2 extends AppCompatActivity {
    static String base_url = "http://hernandezislasadrian.000webhostapp.com/";
    static String llave="r5da3dfd0dssw4hfohu9fdgrv14";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String ihm =getIntent().getExtras().getString("IdM");
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {

                Peticiones peticion = new Peticiones(getApplicationContext(),base_url+"Mercado/mercados/"+llave);
                Peticiones peticion2 = new Peticiones(getApplicationContext(),base_url+"Mercado/mercadoById/"+llave+"/"+ihm);
                Peticiones peticion3 = new Peticiones(getApplicationContext(),base_url+"Mercado/imgFromMercado/"+llave+"/"+ihm);
                Peticiones peticion4 = new Peticiones(getApplicationContext(),base_url+"Mercado/localesDelMercado/"+llave+"/"+ihm);

                while (!peticion.getBanderita() || !peticion2.getBanderita() ||
                        !peticion3.getBanderita() ||
                        !peticion4.getBanderita() ){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
                principal.lista=peticion.getJSON();
                principal.infomer=peticion2.getJSON();
                principal.galeri=peticion3.getJSON();
                principal.giros = peticion4.getJSON();

                Intent intent = null;
                if(principal.lista!=null && principal.infomer!=null && principal.galeri!=null && principal.giros!=null)
                {
                    // progress.setMessage("Informacion Lista");    //esto ya esta full
                    intent = new Intent(getApplicationContext(), principal.class);
                    try{
                        JSONArray listaJson = principal.lista.optJSONArray("mercados");
                        for(int i=0; i< listaJson.length(); i++) {
                            JSONObject obj_dato = listaJson.getJSONObject(i);
                            int id_m = obj_dato.getInt("idMercado");
                            String nombre =obj_dato.getString("nombre");
                            if(id_m == Integer.parseInt(ihm)){
                                principal.la= obj_dato.getDouble("latitud");
                                principal.lo= obj_dato.getDouble("longitud");
                                principal.seleccionado=nombre;
                            }
                        }
                    }catch (Exception e){}

                    startActivity(intent);
                    finish();
                }
                else{

                    TimerTask timerTask = new TimerTask() {
                        int cont = 3;
                        @Override
                        public void run() {
                            if(cont > 0){
                                System.out.println("Cerrando en..."+ cont);
                                cont--;
                            }
                            else if(cont == 0)
                                finish();
                        }
                    };
                    Timer t = new Timer();
                    t.schedule(timerTask,1000,1000);
                }

            }
        });
        hilo.start();
        super.onCreate(savedInstanceState);
    }
    public static String getBase_url(){
        return base_url;
    }

    public static String getLlave(){
        return llave;
    }
}