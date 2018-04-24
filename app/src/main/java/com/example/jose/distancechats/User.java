package com.example.jose.distancechats;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jose on 17/04/2018.
 */

class User {

    int id;
    String nombre;
    double latitud;
    double longitud;
    double distancia;


    public User(JSONObject objetoJSON) throws JSONException {

        this.nombre =  objetoJSON.getString("nombre").toString();
        this.latitud = Double.parseDouble(objetoJSON.getString("latitud").toString());
        this.longitud = Double.parseDouble(objetoJSON.getString("longitud").toString());
    }

    public void User(int id, String nombre,double latitud,double longitud){
        this.id = id;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
