package com.oaxaca.turismo.mercados.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oaxaca.turismo.mercados.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String url = getIntent().getStringExtra("url");
        String name = getIntent().getStringExtra("name");


        ImageView m = (ImageView) findViewById(R.id.image_profile);
        TextView t = (TextView) findViewById(R.id.nombre_profile);

        t.setText(name);
        RelativeLayout r = (RelativeLayout) findViewById(R.id.back_button_profile);

        Glide.with(getApplication()).load(url).into(m);

        PhotoViewAttacher p = new PhotoViewAttacher(m);

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
