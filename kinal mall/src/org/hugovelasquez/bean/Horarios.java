/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hugovelasquez.bean;

import java.time.Year;
import java.util.Date;
import java.sql.Time;

/**
 *
 * @author Hugo Daniel Velasquez Patzan
 * Codigo Tecnico IN5BV 
 * @date 2 jul. 2021
 * @time 21:47:27
 */
public class Horarios {
    private int id;
    private Time horarioEntrada;
    private Time horarioSalida;
    private Boolean lunes; 
    private Boolean martes; 
    private Boolean miercoles; 
    private Boolean jueves; 
    private Boolean viernes;

    public Horarios() {
    }

    public Horarios(int id, Time horarioEntrada, Time horarioSalida, Boolean lunes, Boolean martes, Boolean miercoles, Boolean jueves, Boolean viernes) {
        this.id = id;
        this.horarioEntrada = horarioEntrada;
        this.horarioSalida = horarioSalida;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
    }

    

    public Horarios(Boolean lunes) {
        this.lunes = lunes;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Time getHorarioEntrada() {
        return horarioEntrada;
    }

    public void setHorarioEntrada(Time horarioEntrada) {
        this.horarioEntrada = horarioEntrada;
    }

    public Time getHorarioSalida() {
        return horarioSalida;
    }

    public void setHorarioSalida(Time horarioSalida) {
        this.horarioSalida = horarioSalida;
    }

    
    public Boolean getLunes() {
        return lunes;
    }

    public void setLunes(Boolean lunes) {
        this.lunes = lunes;
    }

    public Boolean getMartes() {
        return martes;
    }

    public void setMartes(Boolean martes) {
        this.martes = martes;
    }

    public Boolean getMiercoles() {
        return miercoles;
    }

    public void setMiercoles(Boolean miercoles) {
        this.miercoles = miercoles;
    }

    public Boolean getJueves() {
        return jueves;
    }

    public void setJueves(Boolean jueves) {
        this.jueves = jueves;
    }

    public Boolean getViernes() {
        return viernes;
    }

    public void setViernes(Boolean viernes) {
        this.viernes = viernes;
    }
    
   @Override
    public String toString() {
        return id + " | " + "hora de entrada: "+ horarioEntrada + " | " + " hora de salida: " + horarioSalida;
    }
  
    
    
}
