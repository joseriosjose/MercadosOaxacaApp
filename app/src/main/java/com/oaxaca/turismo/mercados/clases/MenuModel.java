package com.oaxaca.turismo.mercados.clases;

public class MenuModel {

    public String menuName, url;
    public boolean hasChildren, isGroup;
    public int posi;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, String url,int posi) {

        this.menuName = menuName;
        this.url = url;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
        this.posi=posi;
    }

    public int getPos(){
        return posi;
    }
    public String getMenuName(){return menuName;}
}
