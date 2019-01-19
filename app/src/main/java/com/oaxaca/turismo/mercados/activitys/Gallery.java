package com.oaxaca.turismo.mercados.activitys;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oaxaca.turismo.mercados.MainActivity;
import com.oaxaca.turismo.mercados.adapter.GalleryAdapter;
import com.oaxaca.turismo.mercados.clases.Image;

import com.oaxaca.turismo.mercados.R;
import com.oaxaca.turismo.mercados.principal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Gallery extends Fragment {
    private String TAG = principal.class.getSimpleName();
    private static final String endpoint = "http://streekon.com/glide/json/glideimages.json";
    private ArrayList<Image> images;
    private ArrayList<String> im;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    private FragmentActivity context;
    View v;
    private int idMercado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.gallery,container,false);
        context = getActivity();
        //*******galeria
        images = new ArrayList<>();


        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(getActivity());
        mAdapter = new GalleryAdapter(getActivity(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity(),
                recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {
            }

        }));

        refresh();
        return  v;
    }

    public void refresh(){
        images.clear();
        for (int i = 0; i < im.size(); i++) {
            try {
                Image image = new Image();
                image.setSmall(im.get(i));
                image.setMedium(im.get(i));
                image.setLarge(im.get(i));
                images.add(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mAdapter.notifyDataSetChanged();
    };
    public void setArrayImages(ArrayList<String> arrayImages) {
        this.im = arrayImages;
    }
}

