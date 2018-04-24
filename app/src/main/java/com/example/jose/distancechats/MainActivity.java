package com.example.jose.distancechats;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView lista;
    private Adapter adapter;

    public Button btnCal;
    public TextView distanciaText;
    public TextView distTEXT;
    private Spinner spTipo;
    private ArrayAdapter<CharSequence>adapterSP;
    private final static String[] tipo = { "KM", "M"};
    private int setDistancia = 10;

    //json simulado
    String JSONdataUser = "{'usuarios':[ {'nombre': 'Fabiola', 'latitud': '27.494143','longitud': '-109.972277'}, { 'nombre': 'Brittani', 'latitud': '27.482551', 'longitud': '-109.970293' },{ 'nombre': 'Issa', 'latitud': '27.493684','longitud': '-109.945828'}, { 'nombre': 'Alexia','latitud': '27.493841', 'longitud': '-109.940187'}]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spTipo = (Spinner) findViewById(R.id.spinner);
        btnCal = (Button) findViewById(R.id.buttonCalcular);
        distanciaText = (TextView) findViewById(R.id.distanciaKM);
        lista = (ListView) findViewById(R.id.listViewUser);
        distTEXT = (TextView) findViewById(R.id.distTEXT);
        distTEXT.setText("Disponibles a "+setDistancia + " KM");


        //adapter para el spinner
        adapterSP = ArrayAdapter.createFromResource(this, R.array.spinner,android.R.layout.simple_spinner_item);
        adapterSP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapterSP);


        //lista de usuarios
        final ArrayList<User> lista_user = new ArrayList<>();

        JSONObject object = null;

        try {
            //creamos un objeto con el JSON recibido
            object = new JSONObject(JSONdataUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //se crea un JSONArray con el objeto de usuario
        JSONArray json_array = object.optJSONArray("usuarios");

        for (int i = 0; i < json_array.length();i++){
            try {
                //se agrega a la lista de usuarios todos los objetos obtenidos del tipo user
                lista_user.add(new User(json_array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //manda llamar a mostrar usuario con la lista
        mostrarUsuarios(lista_user,setDistancia);

        //cuando cambias el valor del rango de la distancia
         btnCal.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) throws NumberFormatException {
                 String val = distanciaText.getText().toString();
                 if (!val.isEmpty()){
                     int valDistancia = Integer.parseInt(val);
                     distTEXT.setText("Disponibles a "+valDistancia +" KM");
                     mostrarUsuarios(lista_user,valDistancia);
                 }

             }
         });

      //  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        //} else {
          //  locationStart();
        //}

    }

    /**
     * Este metodo muestra a todos los usuarios recibidos y los muestra en pantalla
     * @param lista_user
     * @param setDist
     */
    public void mostrarUsuarios(ArrayList<User> lista_user, int setDist){

        //se crea otro arraylist
        ArrayList<User> newUsuarios = new ArrayList<>();

        //se reccorre array con todos los usuarios
        for (User listaNew: lista_user){

            //obtenemos las coordenadas de los usuarios
            double lati = listaNew.getLatitud();
            double longi = listaNew.getLongitud();

            //calculamos la distancia entre otro usuario con la nuestra
            double calDistancia = calcularDistancia(getLatitude(),getLongitude(),lati,longi);

            //si la distancia es menor a la establecida, los agrega a la nueva lista de usuarios para mostrarlos
            if (calDistancia < setDist){
                //establece el atributo distancia del usuario, esto cada vez que se realize esta accion, osea que
                //cambia constantemente con cada peticion, nadie se va enterar de todas maneras
                listaNew.setDistancia(calDistancia);
                newUsuarios.add(listaNew);
            }
        }

        adapter = new Adapter(this,newUsuarios);
        lista = (ListView) findViewById(R.id.listViewUser);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                User usuario = (User) adapter.getItem(posicion);
            }
        });
    }

    /**
     * Metodo que regresa la Latitud actual del usuario
     * @return Regresa Latitud
     */
    public double getLatitude() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        return location.getLatitude();
    }

    /**
     * Metodo que regresa la Longitud actual del usuario
     * @return Regresa Longitud
     */
    public double getLongitude() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        return location.getLongitude();

    }

    /**
     * Metodo que calcula la distancia entre tu y otro usuario
     * @param latitud_uno
     * @param longitud_uno
     * @param latitud_dos
     * @param longitud_dos
     * @return Regresa Distancia
     */
    public double calcularDistancia(double latitud_uno, double longitud_uno,double latitud_dos, double longitud_dos){

        double calDistancia;
        Location crntLocation=new Location("crntlocation");

        crntLocation.setLatitude(latitud_uno);
        crntLocation.setLongitude(longitud_uno);

        Location newLocation=new Location("newlocation");
        newLocation.setLatitude(latitud_dos);
        newLocation.setLongitude(longitud_dos);

        //float distance = crntLocation.distanceTo(newLocation);  in meters
        calDistancia =crntLocation.distanceTo(newLocation)/1000; // in km  / 1000

        return calDistancia;

    }

}
