package com.oaxaca.turismo.mercados.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.oaxaca.turismo.mercados.R;
import com.oaxaca.turismo.mercados.adapter.AdaptadorListaExpandible;
import com.oaxaca.turismo.mercados.clases.Categoria;
import com.oaxaca.turismo.mercados.clases.MenuModel;
import com.oaxaca.turismo.mercados.clases.Mercado;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lista extends Fragment  {
    ArrayList<Categoria> cate;
    String nombre;
    AdaptadorListaExpandible expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);


        expandableListView = (ExpandableListView) view.findViewById(R.id.recy);
        expandableListAdapter = new AdaptadorListaExpandible(getContext(), headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent,
                                        View v, int groupPosition,
                                        int childPosition, long id) {
                Activity_Local.categoria = cate.get(groupPosition);
                Activity_Local.c = childPosition;
                Intent intento = new Intent(getContext(),Activity_Local.class);
                intento.putExtra("nombre",nombre);
                startActivity(intento);
                return false;
            }
        });
        refresh();
        return view;
    }

    public void setArrayCategoria(ArrayList<Categoria> arrayCategoria, String nombre){
        if(cate!= null) cate.clear();
        this.cate = arrayCategoria;
        this.nombre = nombre;
    }

    public void refresh(){
        clear();
        MenuModel childModel;
        for(int i=0; i<cate.size(); i++) {
            List<MenuModel> childModelsList = new ArrayList<>();
            MenuModel menuModel = new MenuModel(cate.get(i).getNombre(), true, true, "", i); //Menu of Java Tutorials
            headerList.add(menuModel);
            for (int j = 0; j < cate.get(i).getLocales().size(); j++) {
                childModel = new MenuModel(cate.get(i).getLocales().get(j).getNombre(), false,
                        false, cate.get(i).getLocales().get(j).getImageUrl() , i);
                childModelsList.add(childModel);
            }
            if (menuModel.hasChildren) {
                Log.d("API123", "here");
                childList.put(menuModel, childModelsList);
            }

        }
        if(expandableListAdapter!= null)
            expandableListAdapter.notifyDataSetChanged();
    }

    public void clear(){
        if(headerList!=null)    headerList.clear();
        if(childList!= null)    childList.clear();
    }

}