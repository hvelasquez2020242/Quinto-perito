/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hugovelasquez.bean;

/**
 *
 * @author Hugo Daniel Velasquez Patzan
 * Codigo Tecnico IN5BV 
 * @date 9 jul. 2021
 * @time 21:45:26
 */
public class Cargos {
    
    private int id; 
    private String nombre; 

    public Cargos() {
    }

    public Cargos(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return id + " | " + nombre;

    }
    
    
    
}
