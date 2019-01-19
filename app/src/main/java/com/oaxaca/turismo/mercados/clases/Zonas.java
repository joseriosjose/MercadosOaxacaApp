package com.oaxaca.turismo.mercados.clases;


import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Zonas {//implements Comparable<Zonas> {
    private String nombre;
    private ArrayList<Mercado> mercados;

    public Zonas(String nombre, ArrayList<Mercado> mercados){
        this.mercados= mercados;
        this.nombre = nombre;
    }
    public Zonas(String nombre){
        this.nombre = nombre;
    }

    public ArrayList<Mercado> getMercados() {
        return mercados;
    }

    public void setMercados(ArrayList<Mercado> mercados) {
        this.mercados= mercados;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString(){
        String t = nombre + "\n";
        for(int i=0; i<mercados.size(); i++){
            t += "\n" +mercados.get(i).toString();
        }
        return t;
    }
}