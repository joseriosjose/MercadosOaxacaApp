package com.oaxaca.turismo.mercados.servicios;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.oaxaca.turismo.mercados.MainActivity;
import com.oaxaca.turismo.mercados.clases.NotificationUtils;
import com.oaxaca.turismo.mercados.clases.NotificationVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;


public class GoogleService extends Service implements LocationListener{

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude,longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 40000;
    public static JSONObject listam,urlimg;

    Location location2 = new Location("localizacion 2");


    public GoogleService( ) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(),5,notify_interval);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    private void fn_getlocation(){
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable){

        }else {
           boolean bann=false;
            if (isNetworkEnable){
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location!=null){

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                         bann=true;
                        // /fn_update(location);
                    }
                }

            }
            if (isGPSEnable){
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location!=null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        bann=true;
                        //fn_update(location);
                    }
                }
            }

            if(bann){
                fn_update(location);
            }


        }

    }

    private class TimerTaskToGetLocation extends TimerTask{
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }

    private void fn_update(Location location){
        revisar();
    }

    JSONObject lm, li;

    public void crear(){
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("listamercados.txt")));

            String texto = fin.readLine();
            //Toast.makeText(getApplicationContext(),texto,Toast.LENGTH_SHORT).show();
            lm = new JSONObject(texto);
            BufferedReader fin2 =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("listaimagenes.txt")));

            String texto2 = fin2.readLine();
            //Toast.makeText(getApplicationContext(),texto2,Toast.LENGTH_SHORT).show();
            li = new JSONObject(texto2);
            /*fin =new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("listaimagenes.txt")));

            texto = fin.readLine();
            li = new JSONObject(texto);*/
            fin2.close();
            fin.close();

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),"NEL NO HAY ARCHIVOS",Toast.LENGTH_SHORT).show();
        }
    }


    private void revisar() {

        JSONObject objJson = listam;
        JSONObject objJson2 = urlimg;
        try{
            String urlimg="";
            JSONArray listaJson = objJson.optJSONArray("mercados");
            for(int i=0; i< listaJson.length(); i++) {
                JSONObject obj_dato = listaJson.getJSONObject(i);
                final int id_m = obj_dato.getInt("idMercado");
                String nombre = obj_dato.getString("nombre");
                double lati = obj_dato.getDouble("latitud");
                double longi = obj_dato.getDouble("longitud");
                JSONArray listaJson2 = objJson2.optJSONArray("imagenes");
                location2.setLatitude(lati);  //latitud
                location2.setLongitude(longi);

                for(int j=0;j<listaJson2.length();j++){
                    urlimg="";
                    JSONObject obj_dato2 = listaJson2.getJSONObject(j);
                    int id_me = obj_dato2.getInt("idMercado");
                    if(id_m==id_me){
                        urlimg = MainActivity.base_url+obj_dato2.getString("imagen");
                        break;
                    }
                }
                final String url=urlimg;
                final String nnn=nombre;
                final int iddd=id_m;
                double distance = location.distanceTo(location2);
                if(distance<4210 && distance>10){

                    Thread hilo = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            NotificationVO notificationVO = new NotificationVO();
                            notificationVO.setTitle(nnn);
                            notificationVO.setMessage("Descubre Oaxaca");
                            notificationVO.setIconUrl(url);
                            notificationVO.setAction("activity");
                            notificationVO.setActionDestination("SecondActivity-"+iddd);
                            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

                            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                            notificationUtils.createNotification(notificationVO,resultIntent,getApplicationContext());
                            notificationUtils.playNotificationSound();

                        }
                    });
                    hilo.start();

                }else{
                    //Toast.makeText(getApplicationContext(),"lejos"+nombre,Toast.LENGTH_SHORT).show();
                }
            }

        }catch (Exception ex){
           // Toast.makeText(this,"sigo vivo",Toast.LENGTH_LONG).show();
        }
    }


}