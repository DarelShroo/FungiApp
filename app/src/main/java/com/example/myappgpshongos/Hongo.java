package com.example.myappgpshongos;

import java.util.ArrayList;

public class Hongo {
    public String imagen;
    public String nombrecientifico;
    public String nombrecomun;
    public String descripcion;
    public String confusiones;
    public String comestible;
    public ArrayList paises;
    public String id;

    public Hongo(String nombrecientifico, String nombrecomun, String descripcion, String confusiones, String comestible, ArrayList paises, String imagen, String id) {
        this.nombrecientifico = nombrecientifico;
        this.nombrecomun = nombrecomun;
        this.descripcion = descripcion;
        this.comestible = comestible;
        this.paises = paises;
        this.confusiones = confusiones;
        this.id = id;
        this.imagen = imagen;
    }

    public Hongo() {
    }

    public String getImagen() {
        return imagen;
    }

    public void setImg(String imagen) {
        this.imagen = imagen;
    }

    public String getNombrecientifico() {
        return nombrecientifico;
    }

    public void setNombrecientifico(String nombrecientifico) {
        this.nombrecientifico = nombrecientifico;
    }

    public String getNombrecomun() {
        return nombrecomun;
    }

    public void setNombrecomun(String nombrecomun) {
        this.nombrecomun = nombrecomun;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getConfusiones() {
        return confusiones;
    }

    public void setConfusiones(String confusiones) {
        this.confusiones = confusiones;
    }

    public String getComestible() {
        return comestible;
    }

    public ArrayList getPaises() {
        return paises;
    }

    public void setPaises(ArrayList paises) {
        this.paises = paises;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setComestible(String comestible) {
        this.comestible = comestible;
    }
}
