package com.oaxaca.turismo.mercados.activitys;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.oaxaca.turismo.mercados.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Mapa extends AppCompatActivity {

    private PhotoViewAttacher photoViewAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        ImageView mapita = (ImageView) findViewById(R.id.mapita);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapa_mercado_20noviembre, opts);
        mapita.setImageBitmap (bitmap);
        photoViewAttacher = new PhotoViewAttacher(mapita);

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // altura y anchura de la imagen
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calcule el mayor valor de inSampleSize que es una potencia de 2 y mantiene ambos
            // Altura y anchura mayores a la altura y anchura solicitadas.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
