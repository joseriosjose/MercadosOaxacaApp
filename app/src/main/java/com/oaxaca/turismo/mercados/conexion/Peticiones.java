package com.oaxaca.turismo.mercados.conexion;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.oaxaca.turismo.mercados.R;

import org.json.JSONObject;

public class Peticiones {
    public boolean banderita=false;
    public  Context contx;
    public boolean veces=true;
    public Peticiones (Context contexto,String pet){
        contx=contexto;
        getPetitions(pet);
    }
public  JSONObject loquellega;

    private void getPetitions(String urll){

        String url = urll;
        JsonObjectRequest objetojson = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    loquellega =response;
                    banderita=true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(contx, R.string.error_conexion,Toast.LENGTH_LONG).show();
                    }
                });
        VolleySingleton.getInstanciaVolley(contx).addToRequestQueue(objetojson);
        objetojson.setRetryPolicy(new DefaultRetryPolicy(400000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    public JSONObject getJSON(){
        return loquellega;
    }

    public boolean getBanderita() {
        return banderita;
    }

}
