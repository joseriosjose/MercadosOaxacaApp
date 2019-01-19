package com.oaxaca.turismo.mercados;

import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class tutorialActivity extends TutorialActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String bg = "#FFFFFF";
        addFragment(new Step.Builder()
                .setTitle("Navegación en el Mercado")
                .setContent("1\nAquí encontrarás información general del mercado seleccionado (dirección, telefono, etc).\n\n" +
                        "2\nEl siguiente apartado es la galeria del mercado, encontrarás diversas fotos.\n\n" +
                        "3\nEsta pesta podrás encontrar los locales que se encuentran en ese mercado.")
                .setBackgroundColor(Color.parseColor(bg))
                .setDrawable(R.drawable.tuto_0)
                .setSummary("Bienvenido a este tutorial").build());

        addFragment(new Step.Builder()
                .setTitle("Menu de Selección")
                .setContent("Pulsa el botón para desplegar el menú de zonas")
                .setBackgroundColor(Color.parseColor(bg))
                .setDrawable(R.drawable.tuto_1)
                .setSummary("Continua para saber más").build());

        addFragment(new Step.Builder()
                .setTitle("Despliega la Zona")
                .setContent("Despliega la lista para poder seleccionar tu mercado")
                .setBackgroundColor(Color.parseColor(bg))
                .setDrawable(R.drawable.tuto_2)
                .setSummary("Ya casi terminamos").build());

        addFragment(new Step.Builder()
                .setTitle("Locales por Categoria")
                .setContent("Pulsa el botón para desplegar categoria, podrás observar los locales de la misma")
                .setBackgroundColor(Color.parseColor(bg))
                .setDrawable(R.drawable.tuto_3)
                .setSummary("Eso es todo").build());
    }


    @Override
    public void finishTutorial() {
        finish();
    }

    @Override
    public void currentFragmentPosition(int position) {

    }
}
