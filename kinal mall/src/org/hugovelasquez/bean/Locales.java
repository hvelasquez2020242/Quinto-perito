/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hugovelasquez.bean;
import java.math.BigDecimal;


/**
 *
 * @author Hugo Daniel Velasquez Patzan
 * Codigo Tecnico IN5BV 
 * @date 9 jun. 2021
 * @time 13:35:03
 */
public class Locales {

private int id;    
private BigDecimal saldoFavor;
private BigDecimal saldoContra;
private int mesesPendientes;
private boolean disponibilidad;
private BigDecimal valorLocal;
private BigDecimal valorAdministracion; 

    public Locales() {
    }

    public Locales(int id, BigDecimal saldoFavor, BigDecimal saldoContra, int mesesPendientes, boolean disponibilidad, BigDecimal valorLocal, BigDecimal valorAdministracion) {
        this.id = id;
        this.saldoFavor = saldoFavor;
        this.saldoContra = saldoContra;
        this.mesesPendientes = mesesPendientes;
        this.disponibilidad = disponibilidad;
        this.valorLocal = valorLocal;
        this.valorAdministracion = valorAdministracion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getSaldoFavor() {
        return saldoFavor;
    }

    public void setSaldoFavor(BigDecimal saldoFavor) {
        this.saldoFavor = saldoFavor;
    }

    public BigDecimal getSaldoContra() {
        return saldoContra;
    }

    public void setSaldoContra(BigDecimal saldoContra) {
        this.saldoContra = saldoContra;
    }

    public int getMesesPendientes() {
        return mesesPendientes;
    }

    public void setMesesPendientes(int mesesPendientes) {
        this.mesesPendientes = mesesPendientes;
    }

    public boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public BigDecimal getValorLocal() {
        return valorLocal;
    }

    public void setValorLocal(BigDecimal valorLocal) {
        this.valorLocal = valorLocal;
    }

    public BigDecimal getValorAdministracion() {
        return valorAdministracion;
    }

    public void setValorAdministracion(BigDecimal valorAdministracion) {
        this.valorAdministracion = valorAdministracion;
    }

   
    

    
    @Override
    public String toString() {
        String strDisponibilidad = "";
        if(disponibilidad) {
            strDisponibilidad = "Disponible"; 
        }else {
            strDisponibilidad = "No Disponible";
        }
        return id + " | " + strDisponibilidad;
    }

    

        
    

    
}
	