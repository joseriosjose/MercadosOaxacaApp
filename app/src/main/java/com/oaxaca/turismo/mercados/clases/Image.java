package com.oaxaca.turismo.mercados.clases;


import java.io.Serializable;

public class Image implements Serializable{
    private String small, medium, large;

    public Image() {
    }

    public Image(String name, String small, String medium, String large, String timestamp) {
        this.small = small;
        this.medium = medium;
        this.large = large;
    }


    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

}
