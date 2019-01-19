package com.oaxaca.turismo.mercados.clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Categoria implements Serializable{
    private String nombre;
    private ArrayList<Local> locales;

    public Categoria(String nombre, ArrayList<Local> locales){
        this.locales = locales;
        this.nombre = nombre;
    }
    public Categoria(String nombre){
        this.nombre = nombre;
    }

    public ArrayList<Local> getLocales() {
        return locales;
    }

    public void setLocales(ArrayList<Local> locales) {
        this.locales = locales;
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
        for(int i=0; i<locales.size(); i++){
            t += "\n" +locales.get(i).toString();
        }
        return t;
    }
}
