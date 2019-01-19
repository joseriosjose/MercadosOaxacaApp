package com.oaxaca.turismo.mercados.clases;

public class Local {
    private int id;
    private String nombre;
    private String imageUrl;
    private String slogan;
    private String historia;
    private String productos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreGiro() {
        return nombreGiro;
    }

    public void setNombreGiro(String nombreGiro) {
        this.nombreGiro = nombreGiro;
    }

    private String nombreGiro;
    private String[] contacto;


    public Local(int id, String nombre,String nombreGiro, String imageUrl,String historia, String tags){
        this.nombre = nombre;
        this.imageUrl = imageUrl;
        this.id = id;
        this.nombreGiro = nombreGiro;
        this.historia = historia;
        this.productos = tags.replace(",","\n");
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    public String[] getContacto() {
        return contacto;
    }

    public void setContacto(String[] contacto) {
        this.contacto = contacto;
    }

    @Override
    public String toString(){
        return "-----------"+nombre +" "+ id +" "+ nombreGiro;
    }
}
