package com.example.jose.distancechats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Jose on 17/04/2018.
 */

public class Adapter extends BaseAdapter{
    Context context;
    ArrayList<User>arrayList;

    //MainActivity m_a;

    public Adapter(Context context, ArrayList<User>arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int posicion) {
        return arrayList.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.itemuser,null);
        }
        TextView nombre = (TextView) view.findViewById(R.id.nombreUser);
        TextView distan = (TextView) view.findViewById(R.id.distanciaUser);

        nombre.setText(arrayList.get(posicion).getNombre());

        //obtenemos las coordenadas de los usuarios
        double lati = arrayList.get(posicion).getLatitud();
        double longi = arrayList.get(posicion).getLongitud();

        //le damos formato a los decimales
        DecimalFormat df = new DecimalFormat("#.0");

        //imprimimos
        distan.setText(" "+df.format(arrayList.get(posicion).getDistancia())+" KM");

        return view;
    }
}










