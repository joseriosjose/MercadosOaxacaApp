package com.oaxaca.turismo.mercados;

public class CardItem {

    private String mTitleResource;
    private int idmercado;
    private String urlimagen;
    private double las=0,los=0;
    public CardItem(String title,int id_mercado,String url,double la,double lo) {
        mTitleResource = title;
        idmercado=id_mercado;
        urlimagen=url;
        las=la;
        los=lo;
    }

    public String getTitle() {
        return mTitleResource;
    }
    public int getIdmercado() {
        return idmercado;
    }

    public String getUrlimagen() {
        return urlimagen;
    }

    public double getLa() {
        return las;
    }

    public double getLo() {
        return los;
    }
}