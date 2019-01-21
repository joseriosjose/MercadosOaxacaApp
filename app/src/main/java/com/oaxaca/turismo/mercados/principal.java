package com.oaxaca.turismo.mercados;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.oaxaca.turismo.mercados.activitys.Gallery;
import com.oaxaca.turismo.mercados.activitys.Head;
import com.oaxaca.turismo.mercados.activitys.Lista;
import com.oaxaca.turismo.mercados.activitys.Mapa;
import com.oaxaca.turismo.mercados.adapter.Adaptador_ListaExpandible;
import com.oaxaca.turismo.mercados.clases.Categoria;
import com.oaxaca.turismo.mercados.clases.Local;
import com.oaxaca.turismo.mercados.clases.MenuModel;
import com.oaxaca.turismo.mercados.clases.Mercado;
import com.oaxaca.turismo.mercados.clases.Zonas;
import com.oaxaca.turismo.mercados.conexion.Peticiones;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variables ocupadas;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    Adaptador_ListaExpandible expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    static JSONObject lista;
    static JSONObject infomer;
    static JSONObject galeri;
    static JSONObject giros;
    static double lo=-96.727260,la=17.057874;
    static String seleccionado;
    int selecc;
    Head comi;
    Gallery comi2;
    Lista comi3;
    Context mContext;
    Peticiones peticion2,peticion3, peticion4;
    int i=0;
    boolean bandera = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        expandableListView = findViewById(R.id.expandableListView);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        obtenerlosmercadosparaelmenu();
        botonesflotantes();
        botonMapa();
        mContext = getApplicationContext();
        comi = new Head() ;
        comi2 = new Gallery();
        comi3 = new Lista();

    }


    public void botonesflotantes(){

        FloatingActionButton nuevo= (FloatingActionButton) findViewById(R.id.fab);
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String labelLocation = "VISITA OAXACA: "+seleccionado;
                    String uri = "geo:<" + la+ ">,<" + lo+ ">?q=<" +la + ">,<" + lo+ ">(" + labelLocation + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), R.string.instala_nave,Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void botonMapa(){
        FloatingActionButton nuevo= (FloatingActionButton) findViewById(R.id.map);
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent mapita = new Intent(principal.this,Mapa.class);
                    startActivity(mapita);
                    // Toast.makeText(getApplicationContext(),"Mapa del mercado.",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),R.string.fallo_mapa,Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //esto era para poner los tres puntitos
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar el elemento de la barra de acción hace clic aquí. La barra de acción
        // Manejar automáticamente los clics en el botón Inicio / Arriba, durante tanto tiempo.
        // a medida que especifique una actividad principal en AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(),"Accion Configuracion",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Manejar los elementos de la vista de navegación aquí.
        // int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //metodo que genera la barra de navegacion segun las arreglos
    private void prepareMenuData() {
        MenuModel childModel;
        for(int i=0; i<mercadostotal.size(); i++) {
            List<MenuModel> childModelsList = new ArrayList<>();
            MenuModel menuModel = new MenuModel(mercadostotal.get(i).getNombre(), true, true, "", i); //Menu of Java Tutorials
            headerList.add(menuModel);
            for (int j = 0; j < mercadostotal.get(i).getMercados().size(); j++) {
                childModel = new MenuModel(mercadostotal.get(i).getMercados().get(j).getNombre(), false,
                        false,"", i);
                childModelsList.add(childModel);
            }
            if (menuModel.hasChildren) {
                Log.d("API123", "here");
                childList.put(menuModel, childModelsList);
            }
        }
    }

    //metodo que controla el funcionamiento de la barra de navegacion
    private void populateExpandableList(){
        expandableListAdapter = new Adaptador_ListaExpandible(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                        onBackPressed();
                    } }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, final int childPosition, long id) {
                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    seleccionado=model.getMenuName();
                    for(int i=0; i<mercadostotal.size(); i++) {
                        for (int j = 0; j < mercadostotal.get(i).getMercados().size(); j++) {
                            if(mercadostotal.get(i).getMercados().get(j).getNombre().equals(model.getMenuName())){
                                la=mercadostotal.get(i).getMercados().get(j).getLatitud();
                                lo=mercadostotal.get(i).getMercados().get(j).getLongitud();
                                selecc=mercadostotal.get(i).getMercados().get(j).getId_mercado();
                            }
                        }
                    }
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    onBackPressed();
                    /**
                     * MANDAR A LLAMAR METODO**/
                    peticion2 = new Peticiones(getApplicationContext(),
                            MainActivity.getBase_url()+"Mercado/mercadoById/"+MainActivity.getLlave()+"/"+selecc);
                    peticion3 = new Peticiones(getApplicationContext(),
                            MainActivity.getBase_url()+"Mercado/imgFromMercado/"+MainActivity.getLlave()+"/"+selecc);
                    peticion4 = new Peticiones(getApplicationContext(),
                            MainActivity.getBase_url()+"Mercado/localesDelMercado/"+MainActivity.getLlave()+"/"+selecc);

                    bandera = true;
                    final ProgressDialog progress =new ProgressDialog(principal.this);
                    progress.setMessage("Descargando Informacion");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setCancelable(false);
                    progress.show();
                    new Thread() {
                        public void run() {
                            while (bandera) {
                                try {
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {

                                            if(peticion2.getBanderita() && peticion3.getBanderita() && peticion4.getBanderita()) {
                                                principal.infomer = peticion2.getJSON();
                                                principal.galeri = peticion3.getJSON();
                                                principal.giros = peticion4.getJSON();
                                                Mercado m  = getMercado();
                                                comi.setMercado(m);
                                                comi.refresh();

                                                comi2.setArrayImages(getGaleria());
                                                comi2.refresh();

                                                comi3.setArrayCategoria(getCategorias(),m.getNombre());
                                                comi3.refresh();
                                                progress.cancel();
                                                bandera = false;
                                            }
                                        }
                                    });
                                    Thread.sleep(300);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                i++;

                            }
                        }
                    }.start();
                    return true;
                }
                return false;
            }
        });

    }



    private ArrayList<Zonas> mercadostotal;

    private void obtenerlosmercadosparaelmenu(){
        mercadostotal = new ArrayList<Zonas>();
        String zonaactua="";
        ArrayList<Mercado> mercadostt = new ArrayList<Mercado>();
        JSONObject objJson = lista;
        try{
            JSONArray listaJson = objJson.optJSONArray("mercados");
            for(int i=0; i< listaJson.length(); i++) {
                JSONObject obj_dato = listaJson.getJSONObject(i);
                int id_m = obj_dato.getInt("idMercado");
                String nombre = obj_dato.getString("nombre");
                String zona = obj_dato.getString("zona");
                switch (zona){
                    case "C": zona="Zona Centro"; break;
                    case "CH": zona="Zona Centro Historico";break;
                    case "S": zona="Zona Sur";break;
                    case "O": zona="Zona Oriente";break;
                    case "N": zona="Zona Norte";break;
                    case "P": zona="Zona Poniente";break;
                }

                double latitudm = obj_dato.getDouble("latitud");
                double longitudm = obj_dato.getDouble("longitud");
                Mercado temp = new Mercado(id_m, nombre, zona, latitudm, longitudm);

                mercadostt.add(temp);

            }
            while(mercadostt.size()>0){
                ArrayList<Mercado> t = new ArrayList<>();
                zonaactua = mercadostt.get(0).getZona();
                Mercado x = mercadostt.get(0);
                t.add(x);
                mercadostt.remove(0);
                for(int j=0; j<mercadostt.size(); j++){
                    if(zonaactua.equalsIgnoreCase(mercadostt.get(j).getZona())){
                        Mercado x2 = mercadostt.get(j);
                        t.add(x2);
                        mercadostt.remove(j);
                        j--;
                    }
                }
                Collections.sort(t, new Comparator<Mercado>() {
                    public int compare(Mercado obj1, Mercado obj2) {
                        return obj1.getNombre().compareTo(obj2.getNombre());
                    }
                });
                mercadostotal.add(new Zonas(zonaactua,t));
            }
        }catch (Exception ex){
            // Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
        }
        Collections.sort(mercadostotal, new Comparator<Zonas>() {
            public int compare(Zonas obj1, Zonas obj2) {
                return obj1.getNombre().compareTo(obj2.getNombre());
            }
        });
        //Collections.sort(mercadostotal);
        if(objJson!=null){
            prepareMenuData();
            populateExpandableList();
        }else{
            //Toast.makeText(this,"es nulo",Toast.LENGTH_LONG).show();
        }
    }

    public Mercado getMercado(){
        Mercado m = new Mercado();
        try{
            JSONArray listaJson = infomer.optJSONArray("mercado");
            JSONObject obj_dato = listaJson.getJSONObject(0);
            m = new Mercado(obj_dato.getString("nombre"),
                    obj_dato.getString("historia"),
                    obj_dato.getString("direccion"),
                    obj_dato.getString("horaA"),
                    obj_dato.getString("horaC"),
                    obj_dato.getString("imagen"));
        }catch (Exception ex){
        }
        return m;
    }

    public ArrayList<String> getGaleria(){
        ArrayList<String> galeria = new ArrayList<>();
        try{
            JSONArray listaJson = galeri.optJSONArray("imagenes");
            for(int i=0; i<listaJson.length(); i++) {
                JSONObject obj_dato = listaJson.getJSONObject(i);
                galeria.add(MainActivity.getBase_url()+obj_dato.getString("imagen"));
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return galeria;
    }

    public ArrayList<Categoria> getCategorias(){
        ArrayList<Categoria> cate = new ArrayList<>();
        ArrayList<Local> l = new ArrayList<>();
        try {
            String categoriaActual = "";
            JSONArray listaJson = giros.optJSONArray("locales");
            for(int i=0; i<listaJson.length(); i++){
                JSONObject obj_dato = listaJson.getJSONObject(i);
                Local local = new Local(Integer.parseInt(obj_dato.getString("idLocal")),
                        obj_dato.getString("nombre"),
                        obj_dato.getString("nombreGiro"),
                        MainActivity.getBase_url()+obj_dato.getString("imagen"),
                        obj_dato.getString("historia"),
                        obj_dato.getString("tags"));
                l.add(local);
            }

            while(l.size()>0){
                ArrayList<Local> t = new ArrayList<>();
                categoriaActual = l.get(0).getNombreGiro();
                Local x = l.get(0);
                t.add(x);
                l.remove(0);
                for(int j=0; j<l.size(); j++){
                    if(categoriaActual.equalsIgnoreCase(l.get(j).getNombreGiro())){
                        Local x2 = l.get(j);
                        t.add(x2);
                        l.remove(j);
                        j--;
                    }
                }
                cate.add(new Categoria(categoriaActual,t));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return cate;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Mercado m = getMercado();
            switch (position) {
                case 0:
                    comi.setMercado(m);
                    return comi;
                case 1:
                    comi2.setArrayImages(getGaleria());
                    return comi2;
                case 2:
                    comi3.setArrayCategoria(getCategorias(),m.getNombre());
                    return comi3;
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

}